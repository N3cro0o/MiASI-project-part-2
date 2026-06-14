import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import apiClient from '../../../shared/api/apiClient';

export interface UserProfile {
  id: number;
  login: string;
  email: string;
  role: 'GUEST' | 'WANDERER' | 'EDITOR' | 'ADMIN';
}

interface AuthContextValue {
  token: string | null;
  isAuthenticated: boolean;
  user: UserProfile | null;
  isLoadingUser: boolean;
  login: (token: string) => void;
  logout: () => void;
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

/**
 * Checks if a JWT token is expired by decoding the payload.
 */
const isTokenExpired = (token: string | null): boolean => {
  if (!token) return true;
  try {
    const payloadBase64 = token.split('.')[1];
    if (!payloadBase64) return true;
    const decodedJson = atob(payloadBase64);
    const decoded = JSON.parse(decodedJson);
    if (!decoded.exp) return false; // Token has no expiration
    return decoded.exp * 1000 < Date.now();
  } catch (error) {
    console.error('Failed to decode token:', error);
    return true; // Assume expired if it's invalid/unparseable
  }
};

export const AuthProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [token, setToken] = useState<string | null>(() => {
    const storedToken = localStorage.getItem('jwt_token');
    if (isTokenExpired(storedToken)) {
      localStorage.removeItem('jwt_token');
      return null;
    }
    return storedToken;
  });

  const [user, setUser] = useState<UserProfile | null>(null);
  const [isLoadingUser, setIsLoadingUser] = useState<boolean>(true);

  const login = (newToken: string) => {
    localStorage.setItem('jwt_token', newToken);
    setToken(newToken);
  };

  const logout = () => {
    localStorage.removeItem('jwt_token');
    setToken(null);
  };

  useEffect(() => {
    if (!token) {
      setUser(null);
      setIsLoadingUser(false);
      return;
    }

    const checkExpiration = () => {
      if (isTokenExpired(token)) {
        logout();
        alert('Your session has expired. Please log in again.');
      }
    };

    const fetchUser = async () => {
      setIsLoadingUser(true);
      try {
        const response = await apiClient.get<UserProfile>('/api/users/me');
        setUser(response.data);
      } catch (error) {
        console.error('Failed to fetch user', error);
        logout();
      } finally {
        setIsLoadingUser(false);
      }
    };

    fetchUser();
    checkExpiration();

    const handleVisibilityChange = () => {
      if (document.visibilityState === 'visible') {
        checkExpiration();
      }
    };

    const handleFocus = () => {
      checkExpiration();
    };

    // Listeners for window focus and tab visibility
    window.addEventListener('focus', handleFocus);
    document.addEventListener('visibilitychange', handleVisibilityChange);

    // Continuous background check every 60 seconds
    const intervalId = setInterval(checkExpiration, 60000);

    return () => {
      window.removeEventListener('focus', handleFocus);
      document.removeEventListener('visibilitychange', handleVisibilityChange);
      clearInterval(intervalId);
    };
  }, [token]);

  const value: AuthContextValue = {
    token,
    isAuthenticated: !!token,
    user,
    isLoadingUser,
    login,
    logout,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = (): AuthContextValue => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
