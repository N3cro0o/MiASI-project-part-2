/**
 * API Endpoints configuration.
 * This adapter pattern isolates the frontend from backend URL changes.
 * When backend refactors to pure RESTful routes, we only change them here.
 */
export const API_ENDPOINTS = {
    // Submissions (Verification Context)
    CREATE_SUBMISSION: '/api/submission/new',
    GET_USER_SUBMISSIONS: (userId: number) => `/api/submission/get/user/${userId}`,

    // Krasnale (POI Catalog Context)
    GET_ALL_KRASNALS: '/api/krasnal/get/all',
    CREATE_KRASNAL_ADMIN: '/api/krasnal/new', // Fast-track for Admins

    // Reviews (Interaction Context)
    CREATE_REVIEW: '/api/review/add',
    GET_KRASNAL_REVIEWS: (krasnalId: number) => `/api/review/get/krasnal/${krasnalId}`,

    // Auth & Users (IAM Context)
    REGISTER_USER: '/api/user/register',
};