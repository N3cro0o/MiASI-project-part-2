import { apiClient } from '../../../shared/api';
import type { Poi } from '../models/Poi';

/**
 * Fetches the list of POIs from the backend.
 * Uses the global apiClient for request configuration.
 */
export const getPois = async (): Promise<Poi[]> => {
  // const response = await apiClient.get<Poi[]>('/api/pois');
  const response = await apiClient.get<Poi[]>('/api/krasnal/get/all');
  return response.data;
};
