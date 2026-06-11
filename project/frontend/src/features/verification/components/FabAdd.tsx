interface FabAddProps {
  isAddingMode: boolean;
  toggleAddingMode: () => void;
}

/**
 * Floating Action Button (FAB) for proposing a new POI.
 * Positioned at the bottom-right corner of the map.
 * Visual shell only — will open the submission form from the verification context later.
 */
const FabAdd: React.FC<FabAddProps> = ({ isAddingMode, toggleAddingMode }) => {
  return (
    <button
      id="fab-add-poi"
      type="button"
      onClick={toggleAddingMode}
      className={`absolute bottom-6 right-6 z-[1000] flex h-14 w-14 items-center justify-center rounded-full text-white shadow-xl transition-transform hover:scale-110 active:scale-95 ${isAddingMode ? 'bg-red-500' : 'bg-wroclaw-green'
        }`}
      aria-label={isAddingMode ? 'Cancel adding point of interest' : 'Propose a new point of interest'}
    >
      {isAddingMode ? (
        // X icon for cancel
        < svg
          xmlns="http://www.w3.org/2000/svg"
          className="h-7 w-7"
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
          strokeWidth={2.5}
        >
          <path strokeLinecap="round" strokeLinejoin="round" d="M6 18L18 6M6 6l12 12" />
        </svg>
      ) : (
        // Plus icon
        < svg
          xmlns="http://www.w3.org/2000/svg"
          className="h-7 w-7"
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
          strokeWidth={2.5}
        >
          <path strokeLinecap="round" strokeLinejoin="round" d="M12 4v16m8-8H4" />
        </svg >
      )}
    </button >
  );
};

export default FabAdd;
