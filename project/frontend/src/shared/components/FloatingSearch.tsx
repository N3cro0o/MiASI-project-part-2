import React from 'react';

interface FloatingSearchProps {
  searchTerm: string;
  onSearchChange: (val: string) => void;
}

/**
 * Floating search bar overlay positioned at the top of the map.
 */
const FloatingSearch: React.FC<FloatingSearchProps> = ({ searchTerm, onSearchChange }) => {
  return (
    <div className="px-5 pt-4 pb-2">
      <div className="flex items-center rounded-full bg-white/90 px-4 py-2 shadow-sm backdrop-blur-sm">
        {/* Search icon */}
        <svg
          xmlns="http://www.w3.org/2000/svg"
          className="mr-2 h-5 w-5 shrink-0 text-gray-400"
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
          strokeWidth={2}
        >
          <path
            strokeLinecap="round"
            strokeLinejoin="round"
            d="M21 21l-4.35-4.35M11 19a8 8 0 100-16 8 8 0 000 16z"
          />
        </svg>

        <input
          id="search-input"
          type="text"
          value={searchTerm}
          onChange={(e) => onSearchChange(e.target.value)}
          placeholder="Search dwarfs & places…"
          className="w-full bg-transparent text-sm text-gray-700 outline-none placeholder:text-gray-400"
        />
      </div>
    </div>
  );
};

export default FloatingSearch;
