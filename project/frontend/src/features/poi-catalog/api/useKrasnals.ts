import { useQuery } from '@tanstack/react-query';
import apiClient from '../../../shared/api/apiClient';
import { API_ENDPOINTS } from '../../../shared/api/endpoints';

export interface Krasnal {
  id: number;
  name: string;
  description: string;
  position: {
    latitude: number;
    longitude: number;
  };
  category: string;
}

/**
 * Hook to fetch all dwarfs from the API.
 */
export const useKrasnals = () => {
  return useQuery({
    queryKey: ['krasnals'],
    queryFn: async (): Promise<Krasnal[]> => {
      const response = await apiClient.get<Krasnal[]>(API_ENDPOINTS.GET_ALL_KRASNALS);
      return response.data;
    },
    staleTime: 5 * 60 * 1000,
  });
};
