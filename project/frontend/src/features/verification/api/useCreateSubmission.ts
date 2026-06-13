import { useMutation } from '@tanstack/react-query';
import axios, { AxiosError } from 'axios';
import { API_ENDPOINTS } from '../../../shared/api/endpoints';

// DTO aligned with the requested changes from backend
interface CreateSubmissionRequest {
    // userId: number; // TODO: Uncomment if Stachu still requires it directly in the body
    name: string;
    category: string;
    description: string;
    latitude: number;
    longitude: number;
}

/**
 * Hook to send a new submission to the backend using Axios.
 */
export const useCreateSubmission = () => {
    return useMutation({
        mutationFn: async (payload: CreateSubmissionRequest) => {
            const response = await axios.post(API_ENDPOINTS.CREATE_SUBMISSION, payload, {
                headers: {
                    'Content-Type': 'application/json',
                    // 'Authorization': `Bearer ${token}` // TODO: Add when IAM Context is ready
                },
            });

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