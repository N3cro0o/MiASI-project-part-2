/** Props for a single POI card in the list */
export interface PoiCardProps {
  name: string;
  description: string;
  rating: number;
  reviewCount: number;
  imageUrl?: string;
}

/**
 * A single POI entry card.
 * Displays a thumbnail placeholder, name, star rating, and a short description.
 */
const PoiCard: React.FC<PoiCardProps> = ({
  name,
  description,
  rating,
  reviewCount,
  imageUrl,
}) => {
  return (
    <article className="flex gap-3 rounded-xl bg-white/70 p-3 shadow-sm transition-shadow hover:shadow-md">
      {/* Thumbnail */}
      <div className="h-16 w-16 shrink-0 overflow-hidden rounded-lg bg-wroclaw-dark/10">
        {imageUrl ? (
          <img
            src={imageUrl}
            alt={name}
            className="h-full w-full object-cover"
          />
        ) : (
          <div className="flex h-full w-full items-center justify-center text-2xl">
            🗿
          </div>
        )}
      </div>

      {/* Details */}
      <div className="flex min-w-0 flex-1 flex-col justify-center">
        <h3 className="truncate text-sm font-semibold text-wroclaw-dark">
          {name}
        </h3>

        {/* Star rating */}
        <div className="mt-0.5 flex items-center gap-1 text-xs text-wroclaw-dark/60">
          <span className="text-amber-500">⭐</span>
          <span className="font-medium text-wroclaw-dark">{rating.toFixed(1)}</span>
          <span>({reviewCount})</span>
        </div>

        {/* Description snippet */}
        <p className="mt-1 truncate text-xs text-wroclaw-dark/50">
          {description}
        </p>
      </div>
    </article>
  );
};

export default PoiCard;
