import type { Poi } from '../models/Poi';
import { getCategoryMeta } from '../constants/categories';
import { useAuth } from '../../iam/context/AuthContext';
import { useKrasnalReviews } from '../../reviews/api/useReviews';
import ReviewForm from '../../reviews/components/ReviewForm';

const safelyFormatDate = (dateString?: string | null) => {
  if (!dateString) return 'Unknown date';
  const date = new Date(dateString);
  if (isNaN(date.getTime())) return 'Invalid date';
  return new Intl.DateTimeFormat('pl-PL', { dateStyle: 'medium', timeStyle: 'short' }).format(date);
};

interface PoiDetailsProps {
  poi: Poi;
  onBack: () => void;
}

/**
 * Visual shell for displaying details of a selected POI.
 * Rendered inside the PoiDrawer when a user clicks on a PoiCard.
 */
const PoiDetails: React.FC<PoiDetailsProps> = ({ poi, onBack }) => {
  const { isAuthenticated } = useAuth();
  const { data: reviews, isLoading: reviewsLoading } = useKrasnalReviews(Number(poi.id));

  const reviewCount = reviews ? reviews.length : 0;
  const averageRating = reviewCount > 0
    ? reviews!.reduce((acc, r) => acc + r.rating, 0) / reviewCount
    : (poi.rating ?? 0);

  const categoryMeta = getCategoryMeta(poi.category);

  return (
    <div className="flex h-full flex-col">
      {/* ── Header with Back Button ─────────────────────────────────────── */}
      <header className="flex items-center gap-3 px-5 pt-4 pb-3">
        <button
          type="button"
          onClick={onBack}
          className="flex h-8 w-8 items-center justify-center rounded-full bg-wroclaw-dark/5 text-wroclaw-dark transition-colors hover:bg-wroclaw-dark/10"
          aria-label="Back to list"
        >
          {/* Arrow left icon */}
          <svg
            xmlns="http://www.w3.org/2000/svg"
            className="h-5 w-5"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
            strokeWidth={2}
          >
            <path strokeLinecap="round" strokeLinejoin="round" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
          </svg>
        </button>
        <span className="text-sm font-medium text-wroclaw-dark/60">
          Back to list
        </span>
      </header>

      {/* ── Details Content (Scrollable) ────────────────────────────────── */}
      <div className="flex-1 overflow-y-auto px-5 pb-6">
        {/* Cover image placeholder */}
        <div className="mb-4 h-48 w-full overflow-hidden rounded-xl bg-wroclaw-dark/10 shadow-sm">
          {poi.imageUrl ? (
            <img
              src={poi.imageUrl}
              alt={poi.name}
              className="h-full w-full object-cover"
            />
          ) : (
            <div className="flex h-full w-full flex-col items-center justify-center text-wroclaw-dark/40">
              <span className="text-5xl">{categoryMeta.emoji}</span>
              <span className="mt-2 text-xs font-medium uppercase tracking-wider">No image</span>
            </div>
          )}
        </div>

        {/* Title and Category */}
        <div className="mb-2 flex items-start justify-between gap-4">
          <h2 className="text-2xl font-bold text-wroclaw-dark">{poi.name}</h2>
          <span className="flex items-center gap-1.5 rounded-full bg-wroclaw-brick/10 px-2.5 py-1 text-xs font-semibold text-wroclaw-brick">
            <span>{categoryMeta.emoji}</span>
            {categoryMeta.label}
          </span>
        </div>

        {/* Rating and Coordinates */}
        <div className="mb-4 flex flex-col gap-1 text-sm text-wroclaw-dark/60">
          <div className="flex items-center gap-1">
            <span className="text-amber-500">⭐</span>
            <span className="font-medium text-wroclaw-dark">{averageRating.toFixed(1)}</span>
            <span>({reviewCount} reviews)</span>
          </div>
          <div className="font-mono text-xs opacity-70">
            {poi.position.latitude.toFixed(6)}, {poi.position.longitude.toFixed(6)}
          </div>
        </div>

        {/* Description */}
        <div className="rounded-xl bg-white/50 p-4 shadow-sm">
          <h3 className="mb-2 text-sm font-semibold text-wroclaw-dark">Description</h3>
          <p className="text-sm leading-relaxed text-wroclaw-dark/80">
            {poi.description}
          </p>
        </div>

        {/* Reviews Section */}
        <div className="mt-6">
          <div className="mb-4 flex items-center justify-between">
            <h3 className="text-lg font-bold text-wroclaw-dark">Reviews</h3>
            <div className="flex items-center gap-1 text-sm text-wroclaw-dark/60">
              <span className="text-amber-500">⭐</span>
              <span className="font-medium text-wroclaw-dark">{averageRating.toFixed(1)}</span>
              <span>({reviewCount} reviews)</span>
            </div>
          </div>

          {reviewsLoading ? (
            <div className="text-sm text-wroclaw-dark/50">Loading reviews...</div>
          ) : reviewCount === 0 ? (
            <div className="text-sm text-wroclaw-dark/50 italic mb-4">No reviews yet. Be the first!</div>
          ) : (
            <div className="flex flex-col gap-3 mb-4">
              {reviews!.map((review) => (
                <div key={review.id} className="rounded-xl bg-white p-3 shadow-sm border border-wroclaw-dark/5">
                  <div className="flex items-center justify-between mb-1">
                    <span className="text-amber-500 text-xs">
                      {'⭐'.repeat(review.rating)}{'☆'.repeat(5 - review.rating)}
                    </span>
                    <span className="text-[10px] text-wroclaw-dark/40">
                      {safelyFormatDate(review.created)}
                    </span>
                  </div>
                  <p className="text-sm text-wroclaw-dark/80">{review.content}</p>
                </div>
              ))}
            </div>
          )}

          {isAuthenticated ? (
            <ReviewForm krasnalId={Number(poi.id)} />
          ) : (
            <div className="mt-4 rounded-lg bg-wroclaw-sand p-4 text-center border border-wroclaw-dark/10">
              <p className="text-sm font-medium text-wroclaw-dark/70">Please log in to leave a review.</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default PoiDetails;
