# 03-testing.md — Strategia Testowania
> Krasmap — System mapowania POI utrzymywany przez społeczność
> Odpowiedzialność: Kierownik Testowania

---

## 1. Filozofia — Test-Driven Development (TDD)

Każdy element systemu powstaje według cyklu **Red → Green → Refactor**:

```
1. RED    — napisz test, który MUSI się nie powieść (brak implementacji)
2. GREEN  — napisz minimalną implementację, która test przechodzi
3. REFACTOR — popraw kod bez zmiany zachowania (testy muszą nadal przechodzić)
```

**Zasada:** żaden kod produkcyjny nie powstaje bez uprzednio napisanego testu.
Testy są dowodem poprawności i żywą dokumentacją systemu.

---

## 2. Piramida Testów

```
           /\
          /  \
         / 03 \       Testy Architektoniczne (ArchUnit)
        /------\      ~10 testów | czas: <1s
       /  02b   \
      /----------\    Testy Systemowe (MockMvc + Testcontainers)
     /    02a     \   ~30 testów | czas: ~30s
    /--------------\
   /      01        \ Testy Integracyjne (Testcontainers)
  /------------------\ ~40 testów | czas: ~20s
 /        00          \
/----------------------\ Testy Jednostkowe (JUnit 5 + Mockito)
                         ~120 testów | czas: <5s
```

| Poziom | Typ | Narzędzia | Co testujemy |
|---|---|---|---|
| 00 | **Jednostkowe** | JUnit 5, AssertJ, Mockito | Agregaty, Value Objects, logika domenowa |
| 01 | **Integracyjne** | Testcontainers, Spring Boot Test | Adaptery — repozytoria JPA |
| 02a | **Systemowe** | MockMvc, Testcontainers | Pełne ścieżki HTTP → DB |
| 02b | **Akceptacyjne** | MockMvc + Spring Security Test | User Stories end-to-end |
| 03 | **Architektoniczne** | ArchUnit | Naruszenia granic hexagonal |

---

## 3. Narzędzia i Konfiguracja

### 3.1 Zależności Maven (`pom.xml`)

```xml
<!-- JUnit 5 + AssertJ + Mockito (dostarczane przez spring-boot-starter-test) -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>

<!-- Testcontainers — PostgreSQL w testach -->
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>junit-jupiter</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>postgresql</artifactId>
    <scope>test</scope>
</dependency>

<!-- ArchUnit — testy architektury -->
<dependency>
    <groupId>com.tngtech.archunit</groupId>
    <artifactId>archunit-junit5</artifactId>
    <version>1.3.0</version>
    <scope>test</scope>
</dependency>

<!-- Spring Security Test -->
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-test</artifactId>
    <scope>test</scope>
</dependency>
```

### 3.2 Konwencja nazewnictwa testów

```
nazwa metody testowej: should_[oczekiwany rezultat]_when_[warunek]

Przykłady:
  should_create_dwarf_when_valid_data_provided()
  should_throw_exception_when_rating_exceeds_5()
  should_reject_submission_when_reason_is_blank()
  should_return_only_active_dwarfs_when_filtering_by_status()
```

### 3.3 Wspólna konfiguracja Testcontainers

```java
// src/test/java/pl/krasmap/infrastructure/PostgresTestContainer.java

@TestConfiguration
public class PostgresTestContainer {

    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>("postgres:16-alpine")
                .withDatabaseName("krasmap_test")
                .withUsername("test")
                .withPassword("test");
    }
}
```

---

## 4. Testy Jednostkowe (Unit Tests)

**Zasada:** testy domenowe NIE używają Springa. Zero `@SpringBootTest`. Czyste instancje klas Java.

### 4.1 Value Objects — walidacja niezmienników

#### `RatingTest`

```java
// src/test/java/pl/krasmap/interaction/domain/RatingTest.java

class RatingTest {

    @Test
    void should_create_rating_when_value_is_within_range() {
        // given / when
        Rating rating = Rating.of(3);

        // then
        assertThat(rating.getValue()).isEqualTo(3);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    void should_accept_all_valid_rating_values(int value) {
        assertThatNoException().isThrownBy(() -> Rating.of(value));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, 6, 100})
    void should_throw_exception_when_rating_is_out_of_range(int value) {
        assertThatThrownBy(() -> Rating.of(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Rating must be between 1 and 5");
    }
}
```

#### `CoordinatesTest`

```java
// src/test/java/pl/krasmap/poicatalog/domain/CoordinatesTest.java

class CoordinatesTest {

    @Test
    void should_create_coordinates_when_values_are_in_valid_range() {
        // given / when
        Coordinates coords = Coordinates.of(51.1089, 17.0326);

        // then
        assertThat(coords.getLatitude()).isEqualTo(51.1089);
        assertThat(coords.getLongitude()).isEqualTo(17.0326);
    }

    @Test
    void should_throw_exception_when_latitude_exceeds_90() {
        assertThatThrownBy(() -> Coordinates.of(91.0, 17.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Latitude must be between -90 and 90");
    }

    @Test
    void should_throw_exception_when_longitude_exceeds_180() {
        assertThatThrownBy(() -> Coordinates.of(51.0, 181.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Longitude must be between -180 and 180");
    }

    @Test
    void should_be_equal_when_coordinates_have_same_values() {
        // Value Objects: equality by value, not reference
        assertThat(Coordinates.of(51.1, 17.0))
                .isEqualTo(Coordinates.of(51.1, 17.0));
    }
}
```

#### `EmailTest`

```java
// src/test/java/pl/krasmap/iam/domain/EmailTest.java

class EmailTest {

    @Test
    void should_normalize_email_to_lowercase() {
        Email email = Email.of("User@EXAMPLE.COM");
        assertThat(email.getValue()).isEqualTo("user@example.com");
    }

    @ParameterizedTest
    @ValueSource(strings = {"notanemail", "@nodomain", "nodomain@", ""})
    void should_throw_exception_when_email_format_is_invalid(String value) {
        assertThatThrownBy(() -> Email.of(value))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
```

---

### 4.2 Aggregate Roots — logika domenowa

#### `DwarfTest`

```java
// src/test/java/pl/krasmap/poicatalog/domain/DwarfTest.java

class DwarfTest {

    @Test
    void should_be_active_when_newly_created() {
        // given / when
        Dwarf dwarf = Dwarf.create(
                DwarfName.of("Papa Krasnal"),
                "Test description",
                Coordinates.of(51.1089, 17.0326),
                DwarfCategory.DWARF_FIGURINE
        );

        // then
        assertThat(dwarf.getStatus()).isEqualTo(DwarfStatus.ACTIVE);
        assertThat(dwarf.isVisible()).isTrue();
    }

    @Test
    void should_not_be_visible_when_archived() {
        // given
        Dwarf dwarf = Dwarf.create(
                DwarfName.of("Papa Krasnal"),
                "Test description",
                Coordinates.of(51.1089, 17.0326),
                DwarfCategory.DWARF_FIGURINE
        );

        // when
        dwarf.changeStatus(DwarfStatus.ARCHIVED);

        // then
        assertThat(dwarf.isVisible()).isFalse();
    }

    @Test
    void should_emit_DwarfCreatedEvent_when_created() {
        // given / when
        Dwarf dwarf = Dwarf.create(
                DwarfName.of("Papa Krasnal"),
                "Test description",
                Coordinates.of(51.1089, 17.0326),
                DwarfCategory.DWARF_FIGURINE
        );

        // then
        assertThat(dwarf.getDomainEvents())
                .hasSize(1)
                .first()
                .isInstanceOf(DwarfCreatedEvent.class);
    }
}
```

#### `SubmissionTest`

```java
// src/test/java/pl/krasmap/verification/domain/SubmissionTest.java

class SubmissionTest {

    private static final Long USER_ID = 1L;
    private static final Long REVIEWER_ID = 2L;

    private SubmissionPayload validPayload() {
        return SubmissionPayload.of(
                "Krasnal Testowy", "Opis testowy",
                51.1089, 17.0326, "DWARF_FIGURINE"
        );
    }

    @Test
    void should_have_pending_status_when_submitted() {
        Submission submission = Submission.submit(USER_ID, validPayload());
        assertThat(submission.getStatus()).isEqualTo(SubmissionStatus.PENDING);
    }

    @Test
    void should_change_status_to_accepted_when_approved() {
        // given
        Submission submission = Submission.submit(USER_ID, validPayload());

        // when
        submission.accept(REVIEWER_ID);

        // then
        assertThat(submission.getStatus()).isEqualTo(SubmissionStatus.ACCEPTED);
        assertThat(submission.getReviewedByUserId()).isEqualTo(REVIEWER_ID);
        assertThat(submission.getReviewedAt()).isNotNull();
    }

    @Test
    void should_emit_SubmissionAcceptedEvent_when_accepted() {
        // given
        Submission submission = Submission.submit(USER_ID, validPayload());

        // when
        submission.accept(REVIEWER_ID);

        // then
        assertThat(submission.getDomainEvents())
                .hasSize(2) // SubmissionCreatedEvent + SubmissionAcceptedEvent
                .anyMatch(e -> e instanceof SubmissionAcceptedEvent);
    }

    @Test
    void should_throw_exception_when_rejecting_without_reason() {
        // given
        Submission submission = Submission.submit(USER_ID, validPayload());

        // when / then
        assertThatThrownBy(() -> submission.reject(REVIEWER_ID, ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Rejection reason must not be blank");
    }

    @Test
    void should_throw_exception_when_accepting_already_rejected_submission() {
        // given
        Submission submission = Submission.submit(USER_ID, validPayload());
        submission.reject(REVIEWER_ID, "Invalid location");

        // when / then — BR4: once rejected, cannot be accepted
        assertThatThrownBy(() -> submission.accept(REVIEWER_ID))
                .isInstanceOf(IllegalStateException.class);
    }
}
```

#### `ReviewTest`

```java
// src/test/java/pl/krasmap/interaction/domain/ReviewTest.java

class ReviewTest {

    @Test
    void should_create_review_with_given_rating_and_content() {
        Review review = Review.create(1L, 2L, Rating.of(4),
                CommentContent.of("Świetny krasnal!"));

        assertThat(review.getRating().getValue()).isEqualTo(4);
        assertThat(review.getContent().getValue()).isEqualTo("Świetny krasnal!");
    }

    @Test
    void should_emit_ReviewSubmittedEvent_when_created() {
        Review review = Review.create(1L, 2L, Rating.of(4),
                CommentContent.of("Świetny krasnal!"));

        assertThat(review.getDomainEvents())
                .hasSize(1)
                .first().isInstanceOf(ReviewSubmittedEvent.class);
    }
}
```

---

### 4.3 Application Services — mockowanie portów

```java
// src/test/java/pl/krasmap/poicatalog/application/DwarfServiceImplTest.java

class DwarfServiceImplTest {

    @Mock
    private DwarfRepository dwarfRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private DwarfServiceImpl dwarfService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_save_and_return_dwarf_when_valid_data_provided() {
        // given
        Dwarf expectedDwarf = Dwarf.create(
                DwarfName.of("Papa Krasnal"), "Opis",
                Coordinates.of(51.1089, 17.0326),
                DwarfCategory.DWARF_FIGURINE
        );
        when(dwarfRepository.save(any(Dwarf.class))).thenReturn(expectedDwarf);

        // when
        Dwarf result = dwarfService.createDwarf(
                "Papa Krasnal", "Opis", 51.1089, 17.0326, DwarfCategory.DWARF_FIGURINE
        );

        // then
        assertThat(result.getName().getValue()).isEqualTo("Papa Krasnal");
        verify(dwarfRepository).save(any(Dwarf.class));
        verify(eventPublisher).publishEvent(any(DwarfCreatedEvent.class));
    }

    @Test
    void should_return_only_active_dwarfs_when_filtering_by_status() {
        // given
        List<Dwarf> activeDwarfs = List.of(
                Dwarf.create(DwarfName.of("Krasnal 1"), "Opis",
                        Coordinates.of(51.1, 17.0), DwarfCategory.PLACE)
        );
        when(dwarfRepository.findByStatus(DwarfStatus.ACTIVE))
                .thenReturn(activeDwarfs);

        // when
        List<Dwarf> result = dwarfService.findAllActive();

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(DwarfStatus.ACTIVE);
    }
}
```

---

## 5. Testy Integracyjne (Integration Tests)

Testują adaptery wyjściowe (repozytoria JPA) z prawdziwą bazą PostgreSQL w kontenerze Docker.

### 5.1 Konfiguracja bazowa

```java
// src/test/java/pl/krasmap/infrastructure/BaseIntegrationTest.java

@SpringBootTest
@Testcontainers
@Import(PostgresTestContainer.class)
@Transactional
public abstract class BaseIntegrationTest {
    // wspólna konfiguracja dla wszystkich testów integracyjnych
}
```

### 5.2 Testy repozytorium

#### `JpaDwarfRepositoryTest`

```java
// src/test/java/pl/krasmap/poicatalog/infrastructure/JpaDwarfRepositoryTest.java

class JpaDwarfRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private DwarfRepository dwarfRepository;

    @Test
    void should_persist_and_retrieve_dwarf_by_id() {
        // given
        Dwarf dwarf = Dwarf.create(
                DwarfName.of("Papa Krasnal"), "Opis testowy",
                Coordinates.of(51.1089, 17.0326),
                DwarfCategory.DWARF_FIGURINE
        );

        // when
        Dwarf saved = dwarfRepository.save(dwarf);
        Optional<Dwarf> found = dwarfRepository.findById(saved.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getName().getValue()).isEqualTo("Papa Krasnal");
        assertThat(found.get().getStatus()).isEqualTo(DwarfStatus.ACTIVE);
    }

    @Test
    void should_return_only_active_dwarfs_when_filtering_by_status() {
        // given
        Dwarf active = Dwarf.create(DwarfName.of("Aktywny"),
                "Opis", Coordinates.of(51.1, 17.0), DwarfCategory.PLACE);
        Dwarf archived = Dwarf.create(DwarfName.of("Archiwalny"),
                "Opis", Coordinates.of(51.2, 17.1), DwarfCategory.PLACE);
        archived.changeStatus(DwarfStatus.ARCHIVED);

        dwarfRepository.save(active);
        dwarfRepository.save(archived);

        // when
        List<Dwarf> result = dwarfRepository.findByStatus(DwarfStatus.ACTIVE);

        // then
        assertThat(result).extracting(d -> d.getName().getValue())
                .contains("Aktywny")
                .doesNotContain("Archiwalny");
    }
}
```

#### `JpaSubmissionRepositoryTest`

```java
// src/test/java/pl/krasmap/verification/infrastructure/JpaSubmissionRepositoryTest.java

class JpaSubmissionRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Test
    void should_persist_submission_with_jsonb_payload() {
        // given
        SubmissionPayload payload = SubmissionPayload.of(
                "Nowy Krasnal", "Opis", 51.1, 17.0, "DWARF_FIGURINE"
        );
        Submission submission = Submission.submit(1L, payload);

        // when
        Submission saved = submissionRepository.save(submission);

        // then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getPayload().getName()).isEqualTo("Nowy Krasnal");
        assertThat(saved.getStatus()).isEqualTo(SubmissionStatus.PENDING);
    }

    @Test
    void should_return_only_pending_submissions() {
        // given — jeden PENDING, jeden ACCEPTED
        Submission pending = Submission.submit(1L, SubmissionPayload.of(
                "Pending", "Opis", 51.1, 17.0, "PLACE"));
        Submission accepted = Submission.submit(2L, SubmissionPayload.of(
                "Accepted", "Opis", 51.2, 17.1, "MONUMENT"));
        accepted.accept(99L);

        submissionRepository.save(pending);
        submissionRepository.save(accepted);

        // when
        List<Submission> result = submissionRepository
                .findByStatus(SubmissionStatus.PENDING);

        // then
        assertThat(result).allMatch(Submission::isPending);
    }
}
```

#### `JpaReviewRepositoryTest`

```java
// src/test/java/pl/krasmap/interaction/infrastructure/JpaReviewRepositoryTest.java

class JpaReviewRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    void should_enforce_unique_review_per_dwarf_and_author() {
        // given
        Review first = Review.create(1L, 1L, Rating.of(5),
                CommentContent.of("Rewelacja!"));
        Review duplicate = Review.create(1L, 1L, Rating.of(3),
                CommentContent.of("Jednak średni..."));

        reviewRepository.save(first);

        // when / then — BR2: unique constraint (dwarf_id, author_user_id)
        assertThatThrownBy(() -> reviewRepository.save(duplicate))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void should_calculate_average_rating_correctly() {
        // given
        reviewRepository.save(Review.create(10L, 1L, Rating.of(4),
                CommentContent.of("Dobry")));
        reviewRepository.save(Review.create(10L, 2L, Rating.of(2),
                CommentContent.of("Słaby")));

        // when
        double avg = reviewRepository.calculateAverageRating(10L);

        // then
        assertThat(avg).isEqualTo(3.0);
    }
}
```

---

## 6. Testy Systemowe / Akceptacyjne

Testują pełne ścieżki HTTP od kontrolera do bazy danych. Mapują bezpośrednio na User Stories.

### 6.1 Konfiguracja bazowa

```java
// src/test/java/pl/krasmap/infrastructure/BaseSystemTest.java

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Import(PostgresTestContainer.class)
@AutoConfigureMockMvc
public abstract class BaseSystemTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;
}
```

### 6.2 US1 — Gość widzi mapę krasnali

```java
// src/test/java/pl/krasmap/system/GuestViewsDwarfsTest.java

class GuestViewsDwarfsTest extends BaseSystemTest {

    @Test
    void US1_guest_can_view_map_with_active_dwarfs() throws Exception {
        mockMvc.perform(get("/map"))
                .andExpect(status().isOk())
                .andExpect(view().name("map/index"));
    }

    @Test
    void US1_map_endpoint_returns_only_active_dwarfs() throws Exception {
        mockMvc.perform(get("/api/dwarfs")
                        .param("status", "ACTIVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].status",
                        everyItem(is("ACTIVE"))));
    }

    @Test
    void US2_guest_can_filter_dwarfs_by_category() throws Exception {
        mockMvc.perform(get("/api/dwarfs")
                        .param("category", "MONUMENT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].category",
                        everyItem(is("MONUMENT"))));
    }
}
```

### 6.3 US10/US14 — Pełny przepływ EDA: Zgłoszenie → Krasnal

```java
// src/test/java/pl/krasmap/system/SubmissionAcceptanceFlowTest.java

class SubmissionAcceptanceFlowTest extends BaseSystemTest {

    @Test
    @WithMockUser(username = "user@test.pl", roles = "USER")
    void US10_user_can_submit_new_dwarf_proposal() throws Exception {
        // given
        String payload = """
            {
                "name": "Krasnal Testowy",
                "description": "Opis testowy",
                "latitude": 51.1089,
                "longitude": 17.0326,
                "category": "DWARF_FIGURINE"
            }
            """;

        // when / then
        mockMvc.perform(post("/submissions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    @WithMockUser(username = "editor@test.pl", roles = "EDITOR")
    void US14_editor_accepts_submission_and_dwarf_appears_on_map() throws Exception {
        // given — utwórz zgłoszenie bezpośrednio w DB
        Long submissionId = createPendingSubmission();

        // when — akceptacja
        mockMvc.perform(post("/submissions/{id}/accept", submissionId))
                .andExpect(status().isOk());

        // then — Krasnal pojawił się na mapie (EDA: SubmissionAccepted → DwarfCreated)
        mockMvc.perform(get("/api/dwarfs").param("status", "ACTIVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name",
                        hasItem("Krasnal Testowy")));
    }

    @Test
    @WithMockUser(username = "editor@test.pl", roles = "EDITOR")
    void US15_editor_rejects_submission_with_reason() throws Exception {
        // given
        Long submissionId = createPendingSubmission();

        // when
        mockMvc.perform(post("/submissions/{id}/reject", submissionId)
                        .param("reason", "Nieprawidłowa lokalizacja"))
                .andExpect(status().isOk());

        // then — status zmieniony, zgłoszenie niewidoczne w kolejce PENDING
        mockMvc.perform(get("/submissions/pending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id",
                        not(hasItem(submissionId.intValue()))));
    }
}
```

### 6.4 US6 — Dodanie Recenzji przez zalogowanego Użytkownika

```java
// src/test/java/pl/krasmap/system/UserAddsReviewTest.java

class UserAddsReviewTest extends BaseSystemTest {

    @Test
    @WithMockUser(username = "user@test.pl", roles = "USER")
    void US6_user_can_add_review_to_dwarf() throws Exception {
        // given
        Long dwarfId = existingActiveDwarfId();
        String reviewPayload = """
            { "rating": 4, "content": "Świetny krasnal!" }
            """;

        // when / then
        mockMvc.perform(post("/dwarfs/{id}/reviews", dwarfId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reviewPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rating").value(4));
    }

    @Test
    @WithMockUser(username = "user@test.pl", roles = "USER")
    void US6_user_cannot_add_second_review_to_same_dwarf() throws Exception {
        // given — pierwsza recenzja już istnieje
        Long dwarfId = existingActiveDwarfId();
        addReview(dwarfId, 4, "Pierwsza recenzja");

        // when / then — BR2
        mockMvc.perform(post("/dwarfs/{id}/reviews", dwarfId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"rating\": 2, \"content\": \"Druga próba\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void US6_guest_cannot_add_review() throws Exception {
        mockMvc.perform(post("/dwarfs/1/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"rating\": 3, \"content\": \"Test\"}"))
                .andExpect(status().isUnauthorized());
    }
}
```

---

## 7. Testy Architektury (ArchUnit)

Automatycznie weryfikują, że granice architektoniczne nie są naruszane w kodzie produkcyjnym.

```java
// src/test/java/pl/krasmap/architecture/HexagonalArchitectureTest.java

@AnalyzeClasses(packages = "pl.krasmap")
class HexagonalArchitectureTest {

    // ─── Reguła 1: Domain nie zależy od Springa ──────────────────────────────
    @ArchTest
    static final ArchRule domain_should_not_depend_on_spring =
        noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat()
            .resideInAPackage("org.springframework..")
            .because("Domain layer must be pure Java — no Spring dependencies (ADR-001)");

    // ─── Reguła 2: Domain nie zależy od JPA / infrastruktury ─────────────────
    @ArchTest
    static final ArchRule domain_should_not_depend_on_jpa =
        noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat()
            .resideInAPackage("jakarta.persistence..")
            .because("Domain layer must not depend on JPA annotations (ADR-001)");

    // ─── Reguła 3: Infrastructure nie jest wołana przez Domain ───────────────
    @ArchTest
    static final ArchRule domain_should_not_depend_on_infrastructure =
        noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat()
            .resideInAPackage("..infrastructure..")
            .because("Domain must not know about infrastructure adapters (ADR-001)");

    // ─── Reguła 4: Konteksty nie wołają się nawzajem bezpośrednio ────────────
    @ArchTest
    static final ArchRule poi_catalog_should_not_depend_on_verification =
        noClasses()
            .that().resideInAPackage("pl.krasmap.poicatalog..")
            .should().dependOnClassesThat()
            .resideInAPackage("pl.krasmap.verification..")
            .because("Bounded Contexts must communicate only via Domain Events (ADR-002)");

    @ArchTest
    static final ArchRule interaction_should_not_depend_on_verification =
        noClasses()
            .that().resideInAPackage("pl.krasmap.interaction..")
            .should().dependOnClassesThat()
            .resideInAPackage("pl.krasmap.verification..")
            .because("Bounded Contexts must communicate only via Domain Events (ADR-002)");

    @ArchTest
    static final ArchRule verification_should_not_depend_on_interaction =
        noClasses()
            .that().resideInAPackage("pl.krasmap.verification..")
            .should().dependOnClassesThat()
            .resideInAPackage("pl.krasmap.interaction..")
            .because("Bounded Contexts must communicate only via Domain Events (ADR-002)");

    // ─── Reguła 5: @Controller tylko w infrastructure ────────────────────────
    @ArchTest
    static final ArchRule controllers_should_reside_in_infrastructure =
        classes()
            .that().areAnnotatedWith(Controller.class)
            .should().resideInAPackage("..infrastructure..")
            .because("Controllers are Primary Adapters — infrastructure layer only (ADR-001)");

    // ─── Reguła 6: @Repository tylko w infrastructure ────────────────────────
    @ArchTest
    static final ArchRule repositories_should_reside_in_infrastructure =
        classes()
            .that().areAnnotatedWith(Repository.class)
            .should().resideInAPackage("..infrastructure..")
            .because("JPA Repositories are Secondary Adapters — infrastructure layer only (ADR-001)");

    // ─── Reguła 7: Porty wyjściowe są interfejsami ───────────────────────────
    @ArchTest
    static final ArchRule repository_ports_should_be_interfaces =
        classes()
            .that().resideInAPackage("..domain..")
            .and().haveSimpleNameEndingWith("Repository")
            .should().beInterfaces()
            .because("Output Ports must be interfaces — Dependency Inversion Principle (ADR-001)");
}
```

---

## 8. Mapowanie Testów na User Stories

| User Story | Typ testu | Klasa testowa |
|---|---|---|
| US1 — Gość ogląda mapę | Systemowy | `GuestViewsDwarfsTest` |
| US2 — Filtrowanie po kategorii | Systemowy | `GuestViewsDwarfsTest` |
| US3 — Informacje o Krasnalu | Systemowy | `GuestViewsDwarfDetailsTest` |
| US6 — Dodanie Recenzji | Systemowy | `UserAddsReviewTest` |
| US7 — Usunięcie Recenzji | Systemowy | `UserAddsReviewTest` |
| US8 — Lista odwiedzonych | Systemowy | `UserVisitedListTest` |
| US10 — Zgłoszenie Krasnala | Systemowy | `SubmissionAcceptanceFlowTest` |
| US11 — Status zgłoszeń | Systemowy | `SubmissionAcceptanceFlowTest` |
| US14 — Akceptacja Zgłoszenia | Systemowy | `SubmissionAcceptanceFlowTest` |
| US15 — Odrzucenie Zgłoszenia | Systemowy | `SubmissionAcceptanceFlowTest` |
| BR2 — Jeden Review per user | Jednostkowy + Systemowy | `ReviewTest`, `UserAddsReviewTest` |
| BR5 — Powód odrzucenia | Jednostkowy | `SubmissionTest` |
| BR10 — Rating 1–5 | Jednostkowy | `RatingTest` |
| ADR-001 — Hexagonal | Architektoniczny | `HexagonalArchitectureTest` |
| ADR-002 — EDA | Architektoniczny | `HexagonalArchitectureTest` |

---

## 9. Cel Pokrycia Kodu

| Pakiet | Wymagane pokrycie | Uzasadnienie |
|---|---|---|
| `*.domain.*` | **≥ 90%** | Rdzeń logiki biznesowej — najważniejszy |
| `*.application.*` | **≥ 80%** | Orchestracja use-case'ów |
| `*.infrastructure.*` | **≥ 60%** | Testy integracyjne pokrywają adaptery |
| Łącznie | **≥ 75%** | Minimalny próg jakości projektu |

Pokrycie mierzone przez JaCoCo — konfiguracja w `pom.xml`:

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <configuration>
        <excludes>
            <exclude>**/*Entity.class</exclude>
            <exclude>**/*Application.class</exclude>
        </excludes>
    </configuration>
    <executions>
        <execution>
            <goals><goal>prepare-agent</goal></goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>verify</phase>
            <goals><goal>report</goal></goals>
        </execution>
    </executions>
</plugin>
```