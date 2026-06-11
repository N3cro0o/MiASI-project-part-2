/**
 * Floating avatar button positioned at the top-right corner of the map.
 * Visual shell only — will link to login/profile page via React Router later.
 */
const FloatingAvatar: React.FC = () => {
  return (
    <button
      id="avatar-button"
      type="button"
      className="absolute top-4 right-4 z-[1000] flex h-10 w-10 items-center justify-center rounded-full bg-wroclaw-brick text-white shadow-lg transition-transform hover:scale-110 active:scale-95"
      aria-label="User profile"
    >
      {/* User silhouette icon */}
      <svg
        xmlns="http://www.w3.org/2000/svg"
        className="h-5 w-5"
        fill="none"
        viewBox="0 0 24 24"
        stroke="currentColor"
        strokeWidth={2}
      >
        <path
          strokeLinecap="round"
          strokeLinejoin="round"
          d="M5.121 17.804A9 9 0 0112 15a9 9 0 016.879 2.804M15 11a3 3 0 11-6 0 3 3 0 016 0z"
        />
      </svg>
    </button>
  );
};

export default FloatingAvatar;
