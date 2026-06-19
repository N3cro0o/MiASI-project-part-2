import React, { useState } from 'react';
import { useUsers, useDeactivateUser, useActivateUser, useChangeUserRole, useCreateUserAsAdmin } from '../api/useAdmin';
import { useAuth } from '../../iam/context/AuthContext';

const AdminPanel: React.FC = () => {
  const { data: users, isLoading, isError } = useUsers();
  const { mutate: deactivateUser, isPending: isDeactivating } = useDeactivateUser();
  const { mutate: activateUser, isPending: isActivating } = useActivateUser();
  const { mutate: changeRole, isPending: isChangingRole } = useChangeUserRole();
  const { mutate: createUser, isPending: isCreating } = useCreateUserAsAdmin();
  const { user: currentUser } = useAuth();

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [newUser, setNewUser] = useState({ login: '', email: '', password: '', role: 'WANDERER', active: true });
  const [expandedUserId, setExpandedUserId] = useState<number | string | null>(null);

  if (isLoading) {
    return <div className="p-5 text-center text-wroclaw-dark/50 animate-pulse">Loading users...</div>;
  }

  if (isError) {
    return <div className="p-5 text-center text-red-600">Failed to load users.</div>;
  }

  const handleDeactivate = (id: number | string) => {
    if (window.confirm("Are you sure you want to deactivate this user?")) {
      deactivateUser(id);
    }
  };

  const handleActivate = (id: number | string) => {
    if (window.confirm("Are you sure you want to activate this user?")) {
      activateUser(id);
    }
  };

  const handleRoleChange = (id: number | string, newRole: string) => {
    if (window.confirm(`Change role to ${newRole}?`)) {
      changeRole({ id, newRole });
    }
  };

  const handleCreateSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    createUser(newUser, {
      onSuccess: () => {
        setIsModalOpen(false);
        setNewUser({ login: '', email: '', password: '', role: 'WANDERER', active: true });
      }
    });
  };

  const toggleRow = (id: number | string) => {
    setExpandedUserId(prev => prev === id ? null : id);
  };

  return (
    <div className="flex h-full flex-col relative">
      <header className="px-5 pt-4 pb-4 border-b border-wroclaw-dark/10 flex justify-between items-center">
        <h2 className="text-xl font-semibold text-wroclaw-dark">User Management</h2>
        <button 
          onClick={() => setIsModalOpen(true)}
          className="bg-blue-600 text-white px-4 py-2 rounded-md shadow hover:bg-blue-700 transition-colors font-medium"
        >
          Create New User
        </button>
      </header>

      <div className="flex-1 overflow-y-auto px-5 py-6">
        <div className="bg-white rounded-xl shadow-sm border border-wroclaw-dark/10 overflow-hidden overflow-x-auto">
          <table className="w-full text-left border-collapse min-w-[500px]">
            <thead>
              <tr className="bg-gray-50 border-b border-wroclaw-dark/10 text-xs text-wroclaw-dark/60 uppercase tracking-wider">
                <th className="py-3 px-4 font-medium">ID</th>
                <th className="py-3 px-4 font-medium">User</th>
                <th className="py-3 px-4 font-medium">Status</th>
                <th className="py-3 px-4 font-medium text-right">Details</th>
              </tr>
            </thead>
            <tbody>
              {users?.map((u) => {
                const isSelf = currentUser?.login === u.login;
                const isExpanded = expandedUserId === u.id;

                return (
                  <React.Fragment key={u.id}>
                    {/* Main Row */}
                    <tr 
                      onClick={() => toggleRow(u.id)}
                      className="border-b border-wroclaw-dark/5 hover:bg-gray-50 transition-colors cursor-pointer"
                    >
                      <td className="py-3 px-4 text-sm text-wroclaw-dark/60">{u.id}</td>
                      <td className="py-3 px-4">
                        <p className="font-semibold text-wroclaw-dark text-sm">{u.login}</p>
                        <p className="text-xs text-wroclaw-dark/60">{u.email}</p>
                      </td>
                      <td className="py-3 px-4 whitespace-nowrap">
                        <span className={`inline-block px-2 py-0.5 rounded-md text-xs font-medium shrink-0 ${u.active ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'}`}>
                          {u.active ? 'Active' : 'Inactive'}
                        </span>
                      </td>
                      <td className="py-3 px-4 text-right text-wroclaw-dark/40 font-bold text-lg select-none whitespace-nowrap">
                        {isExpanded ? '−' : '+'}
                      </td>
                    </tr>

                    {/* Expanded Row */}
                    {isExpanded && (
                      <tr className="bg-gray-50/50 border-b border-wroclaw-dark/10">
                        <td colSpan={4} className="p-4">
                          <div className="flex items-center gap-6 bg-white p-4 rounded-lg border border-wroclaw-dark/5 shadow-sm">
                            
                            <div className="flex-1">
                              <p className="text-xs text-wroclaw-dark/60 mb-1 uppercase tracking-wider font-semibold">Role Management</p>
                              <select 
                                value={u.role}
                                disabled={isSelf || isChangingRole}
                                onChange={(e) => handleRoleChange(u.id, e.target.value)}
                                className="text-sm bg-gray-50 border border-gray-200 text-wroclaw-dark rounded-md px-3 py-1.5 outline-none focus:border-wroclaw-blue disabled:opacity-50 disabled:cursor-not-allowed"
                              >
                                <option value="GUEST">GUEST</option>
                                <option value="WANDERER">WANDERER</option>
                                <option value="EDITOR">EDITOR</option>
                                <option value="ADMIN">ADMIN</option>
                              </select>
                            </div>

                            <div className="flex-1">
                              <p className="text-xs text-wroclaw-dark/60 mb-1 uppercase tracking-wider font-semibold">Account Actions</p>
                              {u.active ? (
                                <button
                                  type="button"
                                  onClick={() => handleDeactivate(u.id)}
                                  disabled={isSelf || isDeactivating}
                                  className={`text-sm px-4 py-1.5 rounded-lg font-medium transition-colors ${
                                    isSelf ? 'bg-gray-100 text-gray-400 cursor-not-allowed' : 'bg-red-100 text-red-700 hover:bg-red-200'
                                  }`}
                                >
                                  {isSelf ? 'Cannot deactivate self' : 'Deactivate Account'}
                                </button>
                              ) : (
                                <button
                                  type="button"
                                  onClick={() => handleActivate(u.id)}
                                  disabled={isActivating}
                                  className="text-sm px-4 py-1.5 rounded-lg font-medium transition-colors bg-green-100 text-green-700 hover:bg-green-200"
                                >
                                  Activate Account
                                </button>
                              )}
                            </div>
                            
                          </div>
                        </td>
                      </tr>
                    )}
                  </React.Fragment>
                );
              })}
            </tbody>
          </table>
          {(!users || users.length === 0) && (
            <div className="p-8 text-center text-wroclaw-dark/50 text-sm">No users found.</div>
          )}
        </div>
      </div>

      {/* Create User Modal */}
      {isModalOpen && (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
          <div className="bg-white rounded-lg p-8 shadow-2xl w-full max-w-md text-left text-gray-800">
            <h3 className="text-xl font-bold mb-6">Create New User</h3>
            <form onSubmit={handleCreateSubmit} className="flex flex-col">
              
              <label className="block text-sm font-semibold text-gray-700 mb-1">Login</label>
              <input required type="text" value={newUser.login} onChange={e => setNewUser({...newUser, login: e.target.value})} className="w-full border border-gray-300 bg-white text-gray-900 rounded-md p-2 mb-4 focus:ring-2 focus:ring-blue-500 focus:border-transparent" />
              
              <label className="block text-sm font-semibold text-gray-700 mb-1">Email</label>
              <input required type="email" value={newUser.email} onChange={e => setNewUser({...newUser, email: e.target.value})} className="w-full border border-gray-300 bg-white text-gray-900 rounded-md p-2 mb-4 focus:ring-2 focus:ring-blue-500 focus:border-transparent" />
              
              <label className="block text-sm font-semibold text-gray-700 mb-1">Password</label>
              <input required type="password" value={newUser.password} onChange={e => setNewUser({...newUser, password: e.target.value})} className="w-full border border-gray-300 bg-white text-gray-900 rounded-md p-2 mb-4 focus:ring-2 focus:ring-blue-500 focus:border-transparent" />
              
              <label className="block text-sm font-semibold text-gray-700 mb-1">Role</label>
              <select value={newUser.role} onChange={e => setNewUser({...newUser, role: e.target.value})} className="w-full border border-gray-300 bg-white text-gray-900 rounded-md p-2 mb-6 focus:ring-2 focus:ring-blue-500 focus:border-transparent">
                <option value="GUEST">GUEST</option>
                <option value="WANDERER">WANDERER</option>
                <option value="EDITOR">EDITOR</option>
                <option value="ADMIN">ADMIN</option>
              </select>
              
              <div className="flex justify-end gap-3 mt-2">
                <button type="button" onClick={() => setIsModalOpen(false)} className="px-4 py-2 text-gray-600 bg-gray-100 hover:bg-gray-200 rounded-md font-medium transition-colors">
                  Cancel
                </button>
                <button type="submit" disabled={isCreating} className="px-4 py-2 text-white bg-blue-600 hover:bg-blue-700 rounded-md font-medium transition-colors disabled:opacity-50">
                  {isCreating ? 'Creating...' : 'Create User'}
                </button>
              </div>

            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default AdminPanel;
