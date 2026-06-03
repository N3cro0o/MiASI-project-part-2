-- =============================================================================
-- Wrocławskie Krasnale — PostgreSQL Database Schema
-- Based on: DDD.md (Domain-Driven Design specification)
-- Architecture: Hexagonal Architecture / SOA / EDA
-- =============================================================================
-- IMPORTANT: Each schema corresponds to exactly one Bounded Context.
-- Cross-context references use BIGINT (soft foreign keys) — no declarative
-- FOREIGN KEY constraints exist between schemas. This enforces BC isolation.
-- =============================================================================

-- -----------------------------------------------------------------------------
-- CLEANUP (drop in reverse dependency order)
-- -----------------------------------------------------------------------------
DROP SCHEMA IF EXISTS interaction  CASCADE;
DROP SCHEMA IF EXISTS verification CASCADE;
DROP SCHEMA IF EXISTS poi_catalog  CASCADE;
DROP SCHEMA IF EXISTS iam          CASCADE;

-- -----------------------------------------------------------------------------
-- SCHEMAS — one per Bounded Context
-- -----------------------------------------------------------------------------
CREATE SCHEMA iam;          -- IAM Context
CREATE SCHEMA poi_catalog;  -- POI Catalog Context
CREATE SCHEMA verification; -- Verification Context
CREATE SCHEMA interaction;  -- Interaction Context

-- =============================================================================
-- ENUMS
-- Defined within their owning schema (Bounded Context).
-- =============================================================================

-- IAM Context ─────────────────────────────────────────────────────────────────
CREATE TYPE iam.user_role AS ENUM (
    'GUEST',      -- unauthenticated person; no account or no active session
    'WANDERER',   -- authenticated wanderer; explores the city, adds reviews and submissions
    'EDITOR',     -- authenticated user with content editing privileges
    'ADMIN'       -- full system privileges; manages accounts and roles
);

-- POI Catalog Context ─────────────────────────────────────────────────────────
CREATE TYPE poi_catalog.krasnal_category AS ENUM (
    'MONUMENT',       -- historical monument / zabytek
    'BUILDING',       -- building / budynek
    'KRASNAL_FIGURINE', -- actual krasnal figurine / krasnal
    'FLORA',          -- park, tree, garden / flora
    'PLACE'           -- other place of interest / miejsce
);

CREATE TYPE poi_catalog.krasnal_status AS ENUM (
    'ACTIVE',   -- visible on public map
    'INACTIVE', -- temporarily hidden (e.g. under renovation)
    'ARCHIVED'  -- soft-deleted from public view
);

-- Verification Context ────────────────────────────────────────────────────────
CREATE TYPE verification.submission_status AS ENUM (
    'PENDING',  -- awaiting review by Editor or Admin
    'ACCEPTED', -- approved; KrasnalCreatedEvent fired → Krasnal added to catalog
    'REJECTED'  -- rejected; rejection_reason is mandatory
);

-- =============================================================================
-- UTILITY: updated_at auto-update trigger function (shared)
-- =============================================================================
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- =============================================================================
-- IAM CONTEXT
-- Aggregate Root: User
-- Value Objects:  UserId, Email, HashedPassword
-- =============================================================================

CREATE TABLE iam.users (
    -- Identity (Value Object: UserId)
    id              BIGSERIAL       PRIMARY KEY,

    -- Value Object: Email (RFC-5322, lowercase, unique)
    email           VARCHAR(255)    NOT NULL
                        CONSTRAINT uq_users_email UNIQUE
                        CONSTRAINT ck_users_email_format
                            CHECK (email = LOWER(email) AND email ~* '^[^@\s]+@[^@\s]+\.[^@\s]+$'),

    -- Value Object: HashedPassword (bcrypt hash; never plain-text)
    hashed_password VARCHAR(255)    NOT NULL,

    -- UserRole enum — hierarchy: GUEST < WANDERER < EDITOR < ADMIN
    role            iam.user_role   NOT NULL DEFAULT 'WANDERER',

    -- Soft delete (BR9): setting active=false blocks login without losing
    -- historical data in Verification and Interaction contexts
    active          BOOLEAN         NOT NULL DEFAULT TRUE,

    created_at      TIMESTAMPTZ     NOT NULL DEFAULT NOW()
);

-- Indexes
CREATE INDEX idx_users_email  ON iam.users (email);
CREATE INDEX idx_users_role   ON iam.users (role);
CREATE INDEX idx_users_active ON iam.users (active);

-- =============================================================================
-- POI CATALOG CONTEXT
-- Aggregate Root: Krasnal
-- Value Objects:  KrasnalId, KrasnalName, Coordinates
-- =============================================================================

CREATE TABLE poi_catalog.krasnals (
    -- Identity (Value Object: KrasnalId)
    id          BIGSERIAL                   PRIMARY KEY,

    -- Value Object: KrasnalName (non-empty, max 255 chars)
    name        VARCHAR(255)                NOT NULL
                    CONSTRAINT ck_krasnals_name_not_empty CHECK (TRIM(name) <> ''),

    -- Textual description of the Krasnal's history and characteristics
    description TEXT,

    -- Value Object: Coordinates — latitude ∈ ⟨-90, 90⟩
    latitude    DOUBLE PRECISION            NOT NULL
                    CONSTRAINT ck_krasnals_latitude CHECK (latitude BETWEEN -90 AND 90),

    -- Value Object: Coordinates — longitude ∈ ⟨-180, 180⟩
    longitude   DOUBLE PRECISION            NOT NULL
                    CONSTRAINT ck_krasnals_longitude CHECK (longitude BETWEEN -180 AND 180),

    -- KrasnalCategory enum (BR1: only ACTIVE krasnals are visible on public map)
    category    poi_catalog.krasnal_category  NOT NULL,

    -- KrasnalStatus enum
    status      poi_catalog.krasnal_status    NOT NULL DEFAULT 'ACTIVE',

    created_at  TIMESTAMPTZ                 NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ                 NOT NULL DEFAULT NOW()
);

-- Auto-update updated_at on every UPDATE
CREATE TRIGGER trg_krasnals_updated_at
    BEFORE UPDATE ON poi_catalog.krasnals
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Indexes
CREATE INDEX idx_krasnals_status   ON poi_catalog.krasnals (status);
CREATE INDEX idx_krasnals_category ON poi_catalog.krasnals (category);

-- Spatial index for map bounding-box queries (US1, US2)
CREATE INDEX idx_krasnals_location ON poi_catalog.krasnals (latitude, longitude);

-- =============================================================================
-- VERIFICATION CONTEXT
-- Aggregate Root: Submission
-- Value Objects:  SubmissionId, SubmissionPayload
-- Soft FKs:       submitted_by_user_id → iam.users.id
--                 reviewed_by_user_id  → iam.users.id
-- =============================================================================

CREATE TABLE verification.submissions (
    -- Identity (Value Object: SubmissionId)
    id                    BIGSERIAL                       PRIMARY KEY,

    -- Soft FK → iam.users.id (IAM Context — no declarative FOREIGN KEY)
    -- Represents the Wanderer who submitted the proposal
    submitted_by_user_id  BIGINT                          NOT NULL,

    -- Value Object: SubmissionPayload stored as JSONB
    -- Expected fields: name, description, latitude, longitude, category
    -- Schema is validated at the application layer (domain invariant)
    payload_json          JSONB                           NOT NULL,

    -- SubmissionStatus enum
    status                verification.submission_status  NOT NULL DEFAULT 'PENDING',

    -- BR5: rejection_reason is mandatory when status = 'REJECTED'
    -- Enforced at application layer; DB-level check below as safety net
    rejection_reason      TEXT,

    -- Soft FK → iam.users.id (Editor or Admin who reviewed)
    reviewed_by_user_id   BIGINT,

    submitted_at          TIMESTAMPTZ                     NOT NULL DEFAULT NOW(),
    reviewed_at           TIMESTAMPTZ,

    -- BR5: if REJECTED, rejection_reason must not be empty
    CONSTRAINT ck_submissions_rejection_reason
        CHECK (
            status <> 'REJECTED'
            OR (rejection_reason IS NOT NULL AND TRIM(rejection_reason) <> '')
        ),

    -- reviewed_at must be set when status is no longer PENDING
    CONSTRAINT ck_submissions_reviewed_at
        CHECK (
            status = 'PENDING'
            OR reviewed_at IS NOT NULL
        )
);

-- Indexes
CREATE INDEX idx_submissions_submitted_by  ON verification.submissions (submitted_by_user_id);
CREATE INDEX idx_submissions_status        ON verification.submissions (status);
CREATE INDEX idx_submissions_submitted_at  ON verification.submissions (submitted_at DESC);

-- GIN index for JSONB payload queries (e.g. search by proposed category)
CREATE INDEX idx_submissions_payload_gin   ON verification.submissions USING GIN (payload_json);

-- =============================================================================
-- INTERACTION CONTEXT
-- Aggregate Roots: Review, VisitedEntry
-- Value Objects:   ReviewId, VisitedEntryId, Rating, CommentContent
-- Soft FKs:        krasnal_id      → poi_catalog.krasnals.id
--                  author_user_id / user_id → iam.users.id
-- =============================================================================

-- Review (Comment + Rating as one indivisible aggregate — BR2) ─────────────────
CREATE TABLE interaction.reviews (
    -- Identity (Value Object: ReviewId)
    id              BIGSERIAL       PRIMARY KEY,

    -- Soft FK → poi_catalog.krasnals.id
    krasnal_id        BIGINT          NOT NULL,

    -- Soft FK → iam.users.id
    author_user_id  BIGINT          NOT NULL,

    -- Value Object: Rating — integer ∈ ⟨1, 5⟩ (BR10)
    rating          SMALLINT        NOT NULL
                        CONSTRAINT ck_reviews_rating CHECK (rating BETWEEN 1 AND 5),

    -- Value Object: CommentContent — non-empty, max 2000 chars
    content         VARCHAR(2000)   NOT NULL
                        CONSTRAINT ck_reviews_content_not_empty CHECK (TRIM(content) <> ''),

    created_at      TIMESTAMPTZ     NOT NULL DEFAULT NOW(),

    -- BR2: one Review per (krasnal, author) pair
    CONSTRAINT uq_reviews_krasnal_author UNIQUE (krasnal_id, author_user_id)
);

-- Indexes
CREATE INDEX idx_reviews_krasnal_id       ON interaction.reviews (krasnal_id);
CREATE INDEX idx_reviews_author_user_id ON interaction.reviews (author_user_id);
CREATE INDEX idx_reviews_created_at     ON interaction.reviews (created_at DESC);

-- VisitedEntry ────────────────────────────────────────────────────────────────
CREATE TABLE interaction.visited_entries (
    -- Identity (Value Object: VisitedEntryId)
    id          BIGSERIAL   PRIMARY KEY,

    -- Soft FK → poi_catalog.krasnals.id
    krasnal_id    BIGINT      NOT NULL,

    -- Soft FK → iam.users.id
    user_id     BIGINT      NOT NULL,

    visited_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    -- BR3: one VisitedEntry per (krasnal, user) pair
    CONSTRAINT uq_visited_entries_krasnal_user UNIQUE (krasnal_id, user_id)
);

-- Indexes
CREATE INDEX idx_visited_entries_user_id  ON interaction.visited_entries (user_id);
CREATE INDEX idx_visited_entries_krasnal_id ON interaction.visited_entries (krasnal_id);

-- =============================================================================
-- VIEWS
-- Read-only projections used by the application layer.
-- Views do NOT cross the domain boundary — they are infrastructure read-models.
-- =============================================================================

-- Average rating per Krasnal (US3: visible to Guest)
CREATE VIEW interaction.krasnal_average_ratings AS
SELECT
    krasnal_id,
    COUNT(*)            AS review_count,
    ROUND(AVG(rating), 2) AS average_rating
FROM interaction.reviews
GROUP BY krasnal_id;

-- Active Krasnals visible on public map (BR1: only ACTIVE status)
CREATE VIEW poi_catalog.active_krasnals AS
SELECT
    id,
    name,
    description,
    latitude,
    longitude,
    category,
    created_at
FROM poi_catalog.krasnals
WHERE status = 'ACTIVE';

-- Pending submissions queue for Editors/Admins (US13)
CREATE VIEW verification.pending_submissions AS
SELECT
    id,
    submitted_by_user_id,
    payload_json,
    submitted_at
FROM verification.submissions
WHERE status = 'PENDING'
ORDER BY submitted_at ASC;

-- =============================================================================
-- SEED DATA
-- Minimal bootstrap data required for the application to start.
-- =============================================================================

-- Default Admin account
-- Password: 'admin' hashed with bcrypt (cost=12) — CHANGE IN PRODUCTION
INSERT INTO iam.users (email, hashed_password, role, active)
VALUES (
    'admin@krasmap.pl',
    '$2a$12$placeholder_bcrypt_hash_replace_in_production_env',
    'ADMIN',
    TRUE
);

-- Default Editor account
INSERT INTO iam.users (email, hashed_password, role, active)
VALUES (
    'editor@krasmap.pl',
    '$2a$12$placeholder_bcrypt_hash_replace_in_production_env',
    'EDITOR',
    TRUE
);

-- Default Wanderer account (for testing)
INSERT INTO iam.users (email, hashed_password, role, active)
VALUES (
    'wanderer@krasmap.pl',
    '$2a$12$placeholder_bcrypt_hash_replace_in_production_env',
    'WANDERER',
    TRUE
);

-- Sample Krasnals in Wrocław (BR1: ACTIVE → visible on map)
INSERT INTO poi_catalog.krasnals (name, description, latitude, longitude, category, status)
VALUES
    (
        'Papa Krasnal',
        'The original Wrocław krasnal, placed in 2001 near Świdnicka Street. Symbol of the Orange Alternative movement.',
        51.1089, 17.0326,
        'KRASNAL_FIGURINE', 'ACTIVE'
    ),
    (
        'Krasnal Skrytek',
        'A sneaky krasnal hiding near the Rynek. One of the most photographed krasnals in the city.',
        51.1100, 17.0319,
        'KRASNAL_FIGURINE', 'ACTIVE'
    ),
    (
        'Ratusz Wrocławski',
        'The Gothic Town Hall of Wrocław, built between the 13th and 15th centuries. A landmark of Lower Silesia.',
        51.1100, 17.0315,
        'MONUMENT', 'ACTIVE'
    ),
    (
        'Ogród Botaniczny UWr',
        'One of the oldest botanical gardens in Poland, established in 1811 by the University of Wrocław.',
        51.1144, 17.0466,
        'FLORA', 'ACTIVE'
    ),
    (
        'Hala Stulecia',
        'UNESCO World Heritage Site. Built in 1913, designed by Max Berg. A masterpiece of reinforced concrete architecture.',
        51.1069, 17.0773,
        'BUILDING', 'ACTIVE'
    );

-- =============================================================================
-- COMMENTS ON TABLES AND COLUMNS
-- =============================================================================

COMMENT ON SCHEMA iam          IS 'IAM Bounded Context — authentication and authorization';
COMMENT ON SCHEMA poi_catalog  IS 'POI Catalog Bounded Context — Krasnal entities and their data';
COMMENT ON SCHEMA verification IS 'Verification Bounded Context — submission queue and review workflow';
COMMENT ON SCHEMA interaction  IS 'Interaction Bounded Context — reviews, ratings, visited entries';

COMMENT ON TABLE iam.users
    IS 'Aggregate Root: User. Stores all system accounts regardless of role.';
COMMENT ON COLUMN iam.users.active
    IS 'Soft delete flag (BR9). FALSE = account blocked, login denied. Historical data preserved.';
COMMENT ON COLUMN iam.users.hashed_password
    IS 'Value Object: HashedPassword. BCrypt hash only — plain-text passwords are NEVER stored.';

COMMENT ON TABLE poi_catalog.krasnals
    IS 'Aggregate Root: Krasnal. Core domain entity representing a real-world POI in Wrocław.';
COMMENT ON COLUMN poi_catalog.krasnals.latitude
    IS 'Value Object: Coordinates.latitude. Valid range: -90 to 90.';
COMMENT ON COLUMN poi_catalog.krasnals.longitude
    IS 'Value Object: Coordinates.longitude. Valid range: -180 to 180.';

COMMENT ON TABLE verification.submissions
    IS 'Aggregate Root: Submission. JSONB payload awaits Editor/Admin review before becoming a Krasnal.';
COMMENT ON COLUMN verification.submissions.payload_json
    IS 'Value Object: SubmissionPayload. Fields: name, description, latitude, longitude, category. Validated at domain layer.';
COMMENT ON COLUMN verification.submissions.submitted_by_user_id
    IS 'Soft FK → iam.users.id. No declarative FK — cross-context reference per BC isolation rules.';

COMMENT ON TABLE interaction.reviews
    IS 'Aggregate Root: Review. Combines Rating (1-5) and CommentContent as one indivisible unit (BR2).';
COMMENT ON TABLE interaction.visited_entries
    IS 'Aggregate Root: VisitedEntry. Private per-user list of visited Krasnals (BR3).';
COMMENT ON COLUMN interaction.reviews.krasnal_id
    IS 'Soft FK → poi_catalog.krasnals.id. No declarative FK — cross-context reference per BC isolation rules.';
