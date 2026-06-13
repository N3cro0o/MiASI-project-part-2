import { useQuery } from '@tanstack/react-query';
import axios from 'axios';
import { API_ENDPOINTS } from '../../../shared/api/endpoints';

export type SubmissionStatus = 'PENDING' | 'ACCEPTED' | 'REJECTED';

export interface Submission {
  id: string | number;
  name: string;
  status: SubmissionStatus;
  rejectionReason?: string;
  submittedTime: string;
}

/**
 * Hook to fetch the current user's submissions.
 */
export const useMySubmissions = () => {
  return useQuery({
    queryKey: ['mySubmissions'],
    queryFn: async (): Promise<Submission[]> => {
      const response = await axios.get(API_ENDPOINTS.GET_MY_SUBMISSIONS);
      return response.data;
    },
  });
};
