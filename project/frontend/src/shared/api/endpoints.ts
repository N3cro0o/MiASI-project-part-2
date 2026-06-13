/**
 * API Endpoints configuration.
 * This adapter pattern isolates the frontend from backend URL changes.
 * When backend refactors to pure RESTful routes, we only change them here.
 */
export const API_ENDPOINTS = {
  // Auth
  LOGIN: '/api/auth/login', // TODO: Backend should change this to POST for security
  REGISTER: '/api/auth/register',

  // Submissions (Verification Context)
  CREATE_SUBMISSION: '/api/submissions',
  GET_MY_SUBMISSIONS: '/api/users/me/subs',
  GET_PENDING_SUBMISSIONS: '/api/submissions', // Backend should support ?status=PENDING
  ACCEPT_SUBMISSION: (id: number | string) => `/api/submissions/accept/${id}`,
  REJECT_SUBMISSION: (id: number | string) => `/api/submissions/reject/${id}`,

  // Krasnals (Catalog Context)
  GET_ALL_KRASNALS: '/api/krasnals',
  GET_KRASNAL_BY_ID: (id: number | string) => `/api/krasnals/${id}`,

  // Reviews
  CREATE_REVIEW: '/api/reviews',
  GET_KRASNAL_REVIEWS: (id: number | string) => `/api/reviews/krasnal/${id}`,
};