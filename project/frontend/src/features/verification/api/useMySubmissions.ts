import { useQuery } from '@tanstack/react-query';
import apiClient from '../../../shared/api/apiClient';
import { API_ENDPOINTS } from '../../../shared/api/endpoints';

export type SubmissionStatus = 'PENDING' | 'ACCEPTED' | 'REJECTED';

export interface Submission {
  id: string | number;
  json: string;
  status: SubmissionStatus;
  rejectionReason?: string;
  submittedTime: string;
}

export interface EnrichedSubmission extends Submission {
  krasnalName?: string;
}

/**
 * Hook to fetch the current user's submissions.
 */
export const useMySubmissions = () => {
  return useQuery({
    queryKey: ['my-submissions'],
    queryFn: async (): Promise<EnrichedSubmission[]> => {
      const response = await apiClient.get<Submission[]>(API_ENDPOINTS.GET_MY_SUBMISSIONS);
      return response.data.map((item) => {
        let name = 'Unknown Krasnal';
        if (item.json) {
          try {
            const parsed = JSON.parse(item.json);
            name = parsed?.name || 'Unknown Krasnal';
          } catch (error) {
            console.error('Failed to parse submission JSON for item', item.id);
          }
        }
        return {
          ...item,
          krasnalName: name,
        };
      });
    },
  });
};
