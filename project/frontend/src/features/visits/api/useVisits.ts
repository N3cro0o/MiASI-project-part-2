import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import apiClient from '../../../shared/api/apiClient';

export const useMyVisits = () => {
  return useQuery({
    queryKey: ['my-visits'],
    queryFn: async (): Promise<number[]> => {
      const response = await apiClient.get<number[]>('/api/visits/me');
      return response.data;
    },
  });
};

export const useAddVisit = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: async (krasnalId: number | string) => {
      const response = await apiClient.post(`/api/visits/${krasnalId}`);
      return response.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['my-visits'] });
    },
  });
};

export const useRemoveVisit = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: async (krasnalId: number | string) => {
      const response = await apiClient.delete(`/api/visits/${krasnalId}`);
      return response.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['my-visits'] });
    },
  });
};
