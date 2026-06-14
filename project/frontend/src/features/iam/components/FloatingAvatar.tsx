import React from 'react';
import { useAuth } from '../context/AuthContext';

interface FloatingAvatarProps {
  onClick?: () => void;
}

/**
 * Floating avatar button positioned at the top-right corner of the map.
 * Visual shell only — will link to login/profile page via React Router later.
 */
const FloatingAvatar: React.FC<FloatingAvatarProps> = ({ onClick }) => {
  const { isAuthenticated, logout } = useAuth();

  const handleClick = () => {
    if (isAuthenticated) {
      logout();
    } else {
      if (onClick) onClick();
    }
  };

  return (
    <button
      id="avatar-button"
      type="button"
      onClick={handleClick}
      className={`absolute top-4 right-4 z-[1000] flex h-10 w-10 items-center justify-center rounded-full text-white shadow-lg transition-transform hover:scale-110 active:scale-95 ${
        isAuthenticated ? 'bg-green-600' : 'bg-wroclaw-brick'
      }`}
      aria-label={isAuthenticated ? 'Logout' : 'User profile'}
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
      {/* Online indicator dot if authenticated */}
      {isAuthenticated && (
        <span className="absolute bottom-0 right-0 block h-3 w-3 rounded-full bg-green-300 ring-2 ring-white" />
      )}
    </button>
  );
};

export default FloatingAvatar;
