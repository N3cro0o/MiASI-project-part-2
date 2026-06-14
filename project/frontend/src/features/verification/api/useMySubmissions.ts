import { useQuery } from '@tanstack/react-query';
import apiClient from '../../../shared/api/apiClient';
import { API_ENDPOINTS } from '../../../shared/api/endpoints';

export type SubmissionStatus = 'PENDING' | 'ACCEPTED' | 'REJECTED';

export interface SubmissionReturn {
  id: number;
  userId: number;
  status: SubmissionStatus;
  time: string;
  krasnalName: string;
  krasnalPos: { latitude: number; longitude: number };
}

/**
 * Hook to fetch the current user's submissions.
 */
export const useMySubmissions = (enabled: boolean = true) => {
  return useQuery({
    queryKey: ['my-submissions'],
    enabled,
    queryFn: async (): Promise<SubmissionReturn[]> => {
      const response = await apiClient.get<SubmissionReturn[]>(API_ENDPOINTS.GET_MY_SUBMISSIONS);
      return response.data;
    },
  });
};
