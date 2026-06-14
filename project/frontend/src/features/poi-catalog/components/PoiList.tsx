import React from 'react';
import PoiCard from './PoiCard';
import { useKrasnals } from '../api/useKrasnals';
import type { Poi } from '../models/Poi';

interface PoiListProps {
  activeCategory: string;
  onPoiSelect: (poi: Poi) => void;
  searchTerm: string;
}

/**
 * Scrollable vertical list of POI cards.
 * Fetches and renders real dwarf data from the backend.
 */
const PoiList: React.FC<PoiListProps> = ({ activeCategory, onPoiSelect, searchTerm }) => {
  const { data: krasnals, isLoading, error } = useKrasnals();

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

  if (!krasnals || krasnals.length === 0) {
    return (
      <div className="flex h-32 items-center justify-center text-sm text-wroclaw-dark/60">
        No POIs found in this area.
      </div>
    );
  }

  // Filter first by category
  const categoryFiltered =
    activeCategory === 'ALL'
      ? krasnals
      : krasnals.filter((poi) => poi.category === activeCategory);

  // Then filter by search term
  const filteredKrasnals = categoryFiltered.filter((poi) =>
    poi.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  if (filteredKrasnals.length === 0) {
    if (searchTerm !== '') {
      return (
        <div className="flex h-32 items-center justify-center text-center text-sm text-wroclaw-dark/60 px-4">
          No dwarfs found matching your search.
        </div>
      );
    }
    return (
      <div className="flex h-32 items-center justify-center text-center text-sm text-wroclaw-dark/60 px-4">
        No POIs found for this category.
      </div>
    );
  }

  // Use type casting to match Poi interface expected by PoiCard if needed
  return (
    <div className="flex flex-col gap-2">
      {filteredKrasnals.map((poi) => (
        <PoiCard key={poi.id} poi={poi as Poi} onClick={onPoiSelect} />
      ))}
    </div>
  );
};

export default PoiList;
