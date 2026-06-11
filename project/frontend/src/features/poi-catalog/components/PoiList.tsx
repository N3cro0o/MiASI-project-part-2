import PoiCard from './PoiCard';
import { usePois } from '../api/usePois';
import type { Poi } from '../models/Poi';

interface PoiListProps {
  activeCategory: string;
  onPoiSelect: (poi: Poi) => void;
}

/**
 * Scrollable vertical list of POI cards.
 * Fetches and renders real POI data from the backend.
 */
const PoiList: React.FC<PoiListProps> = ({ activeCategory, onPoiSelect }) => {
  const { data: pois, isLoading, error } = usePois();

  if (isLoading) {
    return (
      <div className="flex h-32 items-center justify-center text-sm text-wroclaw-dark/60">
        <span className="animate-pulse">Loading POIs...</span>
      </div>
    );
  }

  if (error) {
    return (
      <div className="rounded-xl border border-red-200 bg-red-50 p-4 text-center text-sm text-red-600">
        <p className="font-semibold">Failed to load data</p>
        <p className="mt-1 text-xs opacity-80">{error.message}</p>
      </div>
    );
  }

  if (!pois || pois.length === 0) {
    return (
      <div className="flex h-32 items-center justify-center text-sm text-wroclaw-dark/60">
        No POIs found in this area.
      </div>
    );
  }

  const filteredPois =
    activeCategory === 'ALL'
      ? pois
      : pois.filter((poi) => poi.category === activeCategory);

  if (filteredPois.length === 0) {
    return (
      <div className="flex h-32 items-center justify-center text-sm text-wroclaw-dark/60">
        No POIs found for this category.
      </div>
    );
  }

  return (
    <div className="flex flex-col gap-2">
      {filteredPois.map((poi) => (
        <PoiCard key={poi.id} poi={poi} onClick={onPoiSelect} />
      ))}
    </div>
  );
};

export default PoiList;
