import axios from 'axios';

const AUTH_TOKEN_KEY = 'jwt_token';

/**
 * Pre-configured Axios instance for all API communication.
 * Base URL is resolved from the VITE_API_URL environment variable.
 */
const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_URL as string,
  headers: {
    'Content-Type': 'application/json',
  },
});

// ── Request Interceptor ─────────────────────────────────────────────────────
// Attaches the Bearer token from localStorage (if present) to every outgoing request.
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem(AUTH_TOKEN_KEY);
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error),
);

// ── Response Interceptor ────────────────────────────────────────────────────
// Handles 401 (clear credentials & redirect to login) and 403 globally.
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error?.response?.status;

    if (status === 401) {
      // Session expired or invalid token — clear stored credentials
      localStorage.removeItem(AUTH_TOKEN_KEY);
      window.location.href = '/login';
    }

    if (status === 403) {
      // Insufficient permissions — log for debugging, let callers handle UI feedback
      console.error('[API] Forbidden: You do not have permission to access this resource.');
    }

    return Promise.reject(error);
  },
);

export default apiClient;
