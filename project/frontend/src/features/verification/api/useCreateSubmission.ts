import { useMutation } from '@tanstack/react-query';
import { AxiosError } from 'axios';
import apiClient from '../../../shared/api/apiClient';
import { API_ENDPOINTS } from '../../../shared/api/endpoints';

interface CreateSubmissionRequest {
    name: string;
    category: string;
    description: string;
    latitude: number;
    longitude: number;
}

/**
 * Hook to send a new submission to the backend using the configured API Client.
 */
export const useCreateSubmission = () => {
    return useMutation({
        mutationFn: async (payload: CreateSubmissionRequest) => {
            // Map the flat frontend payload to the nested backend format (ReviewKrasnal)
            const backendPayload = {
                name: payload.name,
                description: payload.description,
                category: payload.category,
                position: {
                    latitude: payload.latitude,
                    longitude: payload.longitude,
                },
            };

            // Use apiClient - it automatically knows if it should hit localhost:8080 or the production URL
            const response = await apiClient.post(API_ENDPOINTS.CREATE_SUBMISSION, backendPayload, {
                headers: {
                    // TODO: Usunąć dummy-token, gdy apiClient będzie miał wbudowany interceptor do JWT
                    'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJpZCI6NSwiZXhwIjoxNzgxMzc3NjE1fQ.y48oHRLhlTNB7GOC0b8q-y-_CSAe86Bdp1ZJ_cha7uI',
                },
            });

            return response.data;
        },
        onError: (error: AxiosError<{ message?: string }>) => {
            const errorMessage = error.response?.data?.message || error.message || 'An unexpected error occurred';
            console.error('Submission failed:', errorMessage);
        },
    });
};