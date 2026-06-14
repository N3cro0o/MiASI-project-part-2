import { useMutation } from '@tanstack/react-query';
import { AxiosError } from 'axios';
import apiClient from '../../../shared/api/apiClient';
import { API_ENDPOINTS } from '../../../shared/api/endpoints';

// DTO aligned with the requested changes from backend
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

            const response = await apiClient.post(API_ENDPOINTS.CREATE_SUBMISSION, backendPayload);

            return response.data;
        },
        onError: (error: AxiosError<{ message?: string }>) => {
            // Axios automatically throws on 4xx/5xx errors
            const errorMessage = error.response?.data?.message || error.message || 'An unexpected error occurred';
            console.error('Submission failed:', errorMessage);
            // The error will be caught and displayed by the PoiForm component
        },
    });
};