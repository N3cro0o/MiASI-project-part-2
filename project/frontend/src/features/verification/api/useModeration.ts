import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import apiClient from '../../../shared/api/apiClient';
import { type SubmissionReturn } from './useMySubmissions';

export const usePendingSubmissions = (enabled: boolean = true) => {
  return useQuery({
    queryKey: ['pending-submissions'],
    enabled,
    queryFn: async (): Promise<SubmissionReturn[]> => {
      const response = await apiClient.get<SubmissionReturn[]>('/api/submissions?status=PENDING');
      return response.data;
    },
  });
};

export const useAcceptSubmission = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async (subId: number | string) => {
      const response = await apiClient.post(`/api/submissions/accept/${subId}`);
      return response.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['pending-submissions'] });
      queryClient.invalidateQueries({ queryKey: ['krasnals'] });
    },
  });
};

export const useRejectSubmission = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async ({ subId, reason }: { subId: number | string; reason: string }) => {
      const response = await apiClient.patch(`/api/submissions/reject/${subId}`, { reason });
      return response.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['pending-submissions'] });
    },
  });
};
