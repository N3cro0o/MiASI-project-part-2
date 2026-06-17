import React from 'react';
import { useAuth } from '../../iam/context/AuthContext';

const UserProfile: React.FC = () => {
  const { logout, user, isLoadingUser } = useAuth();

  if (isLoadingUser) {
    return <div className="p-5 text-center text-wroclaw-dark/50 animate-pulse">Loading profile...</div>;
  }

  return (
    <div className="flex h-full flex-col">
      <header className="px-5 pt-4 pb-4 border-b border-wroclaw-dark/10">
        <h2 className="text-xl font-semibold text-wroclaw-dark">My Profile</h2>
      </header>
      <div className="flex-1 p-5">
        <div className="mb-6">
          <p className="text-sm font-medium text-wroclaw-dark/60">Email</p>
          <p className="font-semibold text-wroclaw-dark">{user?.email || 'N/A'}</p>
        </div>
        <div className="mb-6">
          <p className="text-sm font-medium text-wroclaw-dark/60">Role</p>
          <p className="font-semibold text-wroclaw-dark">{user?.role || 'WANDERER'}</p>
        </div>
        <button
          onClick={logout}
          className="w-full rounded-lg bg-red-600 px-4 py-2 text-white font-medium shadow-sm hover:bg-red-700 transition-colors"
        >
          Logout
        </button>
      </div>
    </div>
  );
};

export default UserProfile;
