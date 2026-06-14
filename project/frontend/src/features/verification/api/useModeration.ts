import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import apiClient from '../../../shared/api/apiClient';
import { type Submission, type EnrichedSubmission } from './useMySubmissions';

export const usePendingSubmissions = () => {
  return useQuery({
    queryKey: ['pending-submissions'],
    queryFn: async (): Promise<EnrichedSubmission[]> => {
      const response = await apiClient.get<Submission[]>('/api/submissions?status=PENDING');
      return response.data.map((item) => {
        let name = 'Unknown';
        if (item.json) {
          try {
            const parsed = JSON.parse(item.json);
            name = parsed?.name || 'Unknown';
          } catch (error) {
            console.error('Failed to parse submission JSON', error);
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
      const response = await apiClient.patch(`/api/submissions/reject/${subId}`, reason, {
        headers: {
          'Content-Type': 'text/plain',
        },
      });
      return response.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['pending-submissions'] });
    },
  });
};
