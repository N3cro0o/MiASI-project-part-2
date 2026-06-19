import React, { useState } from 'react';
import { useLogin, useRegister } from '../api/useAuthMutations';

export interface AuthModalProps {
  isOpen: boolean;
  onClose: () => void;
}

type AuthView = 'LOGIN' | 'REGISTER';

/**
 * Authentication Modal Component
 * Displays a unified modal for logging in and registering.
 */
const AuthModal: React.FC<AuthModalProps> = ({ isOpen, onClose }) => {
  const [view, setView] = useState<AuthView>('LOGIN');

  // Form states
  const [loginOrEmail, setLoginOrEmail] = useState('');
  const [login, setLogin] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [errorMsg, setErrorMsg] = useState<string | null>(null);

  // Mutations
  const { mutate: loginMutate, isPending: isLoginPending } = useLogin();
  const { mutate: registerMutate, isPending: isRegisterPending } = useRegister();

  if (!isOpen) {
    return null;
  }

  const handleClose = () => {
    // Reset state before closing
    setLoginOrEmail('');
    setLogin('');
    setEmail('');
    setPassword('');
    setErrorMsg(null);
    setView('LOGIN');
    onClose();
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    setErrorMsg(null); // Clear previous errors

    if (view === 'LOGIN') {
      loginMutate(
        { loginOrEmail, password },
        {
          onSuccess: () => {
            handleClose();
          },
          // eslint-disable-next-line @typescript-eslint/no-explicit-any
          onError: (error: any) => {
            console.error("Login failed:", error);
            console.log("Full error object:", error);
            console.log("Response data:", error.response?.data);

            if (!error.response) {
              setErrorMsg("Server is unreachable. Please check if the backend is running.");
              return;
            }

            const rawMsg = error.response?.data?.message;
            const msg = typeof rawMsg === 'string' ? rawMsg.trim() : "";
            
            switch (msg) {
              case "USER_NOT_FOUND": setErrorMsg("User with this login does not exist."); break;
              case "ACCOUNT_BLOCKED": setErrorMsg("This account is blocked."); break;
              case "INVALID_PASSWORD": setErrorMsg("Incorrect password."); break;
              default: setErrorMsg("Login failed: " + (msg || "Unknown error"));
            }
          }
        }
      );
    } else {
      registerMutate(
        { login, email, password },
        {
          onSuccess: () => {
            handleClose();
          },
          onError: () => {
            setErrorMsg('Registration failed. Please check your details and try again.');
          }
        }
      );
    }
  };

  const toggleView = () => {
    setErrorMsg(null);
    setView((prev) => (prev === 'LOGIN' ? 'REGISTER' : 'LOGIN'));
  };

  const isPending = view === 'LOGIN' ? isLoginPending : isRegisterPending;

  return (
    <div className="fixed inset-0 z-[9999] flex items-center justify-center bg-black/50 backdrop-blur-sm">
      <div className="bg-white rounded-2xl shadow-xl w-full max-w-md p-6 relative">
        {/* Close button */}
        <button
          onClick={handleClose}
          className="absolute top-4 right-4 text-gray-400 hover:text-gray-600 transition-colors"
          aria-label="Close"
        >
          <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>

        {/* Header */}
        <div className="mb-6 text-center">
          <h2 className="text-2xl font-bold text-gray-800">
            {view === 'LOGIN' ? 'Sign In' : 'Create Account'}
          </h2>
          <button
            type="button"
            onClick={toggleView}
            className="mt-2 text-sm text-wroclaw-brick hover:underline focus:outline-none"
          >
            {view === 'LOGIN' ? 'Need an account? Register' : 'Already have an account? Sign in'}
          </button>
        </div>

        {/* Form */}
        <form onSubmit={handleSubmit} className="space-y-4">
          {view === 'LOGIN' ? (
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1" htmlFor="loginOrEmail">
                Login or Email
              </label>
              <input
                id="loginOrEmail"
                type="text"
                value={loginOrEmail}
                onChange={(e) => setLoginOrEmail(e.target.value)}
                required
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-wroclaw-brick focus:border-transparent outline-none transition-shadow"
                placeholder="Enter your login or email"
              />
            </div>
          ) : (
            <>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1" htmlFor="login">
                  Login
                </label>
                <input
                  id="login"
                  type="text"
                  value={login}
                  onChange={(e) => setLogin(e.target.value)}
                  required
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-wroclaw-brick focus:border-transparent outline-none transition-shadow"
                  placeholder="Choose a login"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1" htmlFor="email">
                  Email
                </label>
                <input
                  id="email"
                  type="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-wroclaw-brick focus:border-transparent outline-none transition-shadow"
                  placeholder="Enter your email"
                />
              </div>
            </>
          )}

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1" htmlFor="password">
              Password
            </label>
            <input
              id="password"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-wroclaw-brick focus:border-transparent outline-none transition-shadow"
              placeholder="Enter your password"
            />
          </div>

          {/* Error Message */}
          {errorMsg && (
            <div className="p-3 bg-red-50 border border-red-200 text-red-600 text-sm rounded-lg mb-4 text-center font-medium">
              {errorMsg}
            </div>
          )}

          {/* Submit Button */}
          <button
            type="submit"
            disabled={isPending}
            className="w-full py-2.5 px-4 bg-wroclaw-brick hover:bg-wroclaw-brick/90 text-white font-semibold rounded-lg shadow-md transition-colors disabled:opacity-50 disabled:cursor-not-allowed mt-2"
          >
            {isPending ? 'Loading...' : view === 'LOGIN' ? 'Sign In' : 'Register'}
          </button>
        </form>
      </div>
    </div>
  );
};

export default AuthModal;
