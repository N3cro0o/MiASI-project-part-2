import React from 'react';
import { useUsers, useDeleteUser } from '../api/useAdmin';
import { useAuth } from '../../iam/context/AuthContext';

const AdminPanel: React.FC = () => {
  const { data: users, isLoading, isError } = useUsers();
  const { mutate: deleteUser, isPending: isDeleting } = useDeleteUser();
  const { user: currentUser } = useAuth();

  if (isLoading) {
    return <div className="p-5 text-center text-wroclaw-dark/50 animate-pulse">Loading users...</div>;
  }

  if (isError) {
    return <div className="p-5 text-center text-red-600">Failed to load users.</div>;
  }

  const handleDelete = (id: number | string) => {
    if (window.confirm("Are you sure you want to delete this user?")) {
      deleteUser(id);
    }
  };

  return (
    <div className="flex h-full flex-col">
      <header className="px-5 pt-4 pb-4 border-b border-wroclaw-dark/10">
        <h2 className="text-xl font-semibold text-wroclaw-dark">User Management</h2>
      </header>
      <div className="flex-1 overflow-y-auto px-5 py-6">
        <ul className="flex flex-col gap-3">
          {users?.map((u) => {
            const isSelf = currentUser?.login === u.login;
            return (
              <li key={u.id} className="p-4 border border-wroclaw-dark/10 rounded-xl shadow-sm bg-white flex justify-between items-start">
                <div>
                  <p className="font-semibold text-wroclaw-dark">{u.login}</p>
                  <p className="text-xs text-wroclaw-dark/60 mt-0.5">{u.email}</p>
                  <p className="mt-2 text-xs">
                    <span className="inline-block px-2 py-0.5 rounded-md bg-wroclaw-sand/50 border border-wroclaw-dark/10 text-wroclaw-dark font-medium">{u.role}</span>
                  </p>
                </div>
                <button
                  type="button"
                  onClick={() => handleDelete(u.id)}
                  disabled={isSelf || isDeleting}
                  className={`text-xs px-3 py-1.5 rounded-lg font-medium transition-colors ${
                    isSelf
                      ? 'bg-gray-100 text-gray-400 cursor-not-allowed'
                      : 'bg-red-100 text-red-700 hover:bg-red-200'
                  }`}
                >
                  {isSelf ? 'Current' : 'Delete'}
                </button>
              </li>
            );
          })}
        </ul>
        {(!users || users.length === 0) && (
          <p className="text-sm text-center text-wroclaw-dark/50">No users found.</p>
        )}
      </div>
    </div>
  );
};

export default AdminPanel;
