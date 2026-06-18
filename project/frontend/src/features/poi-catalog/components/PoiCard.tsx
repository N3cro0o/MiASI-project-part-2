import type { Poi } from '../models/Poi';
import { getCategoryMeta } from '../constants/categories';

/** Props for a single POI card in the list */
export interface PoiCardProps {
  poi: Poi;
  onClick: (poi: Poi) => void;
}

/**
 * A single POI entry card.
 * Displays a thumbnail placeholder, name, star rating, and a short description.
 */
const PoiCard: React.FC<PoiCardProps> = ({ poi, onClick }) => {
  const rating = poi.averageRating ?? poi.rating ?? 0;
  const reviewCount = 0; // Placeholder until backend provides review counts
  const categoryMeta = getCategoryMeta(poi.category);

  return (
    <article 
      onClick={() => onClick(poi)}
      className="flex cursor-pointer gap-3 rounded-xl bg-white/70 p-3 shadow-sm transition-shadow hover:shadow-md"
    >
      {/* Thumbnail */}
      <div className="h-16 w-16 shrink-0 overflow-hidden rounded-lg bg-wroclaw-dark/10">
        {poi.imageUrl ? (
          <img
            src={poi.imageUrl}
            alt={poi.name}
            className="h-full w-full object-cover"
          />
        ) : (
          <div className="flex h-full w-full items-center justify-center text-2xl">
            {categoryMeta.emoji}
          </div>
        )}
      </div>

      {/* Details */}
      <div className="flex min-w-0 flex-1 flex-col justify-center">
        <div className="flex items-start justify-between gap-2">
          <h3 className="truncate text-sm font-semibold text-wroclaw-dark">
            {poi.name}
          </h3>
          <span className="shrink-0 text-xs text-wroclaw-dark/50" title={categoryMeta.label}>
            {categoryMeta.emoji}
          </span>
        </div>

        {/* Star rating and position */}
        <div className="mt-0.5 flex flex-wrap items-center gap-x-2 gap-y-1 text-xs text-wroclaw-dark/60">
          <div className="flex items-center gap-1">
            <span className="text-amber-500">⭐</span>
            <span className="font-medium text-wroclaw-dark">{rating.toFixed(1)}</span>
            <span>({reviewCount})</span>
          </div>
          <div className="text-[10px] text-wroclaw-dark/40">
            {poi.position.latitude.toFixed(4)}, {poi.position.longitude.toFixed(4)}
          </div>
        </div>

        {/* Description snippet */}
        <p className="mt-1 line-clamp-2 text-xs text-wroclaw-dark/50">
          {poi.description}
        </p>
      </div>
    </article>
  );
};

export default PoiCard;
