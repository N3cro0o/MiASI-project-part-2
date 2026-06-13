import { apiClient } from '../../../shared/api';
import { API_ENDPOINTS } from '../../../shared/api/endpoints';
import type { Poi } from '../models/Poi';

/**
 * Fetches the list of POIs from the backend.
 * Uses the global apiClient for request configuration.
 */
export const getPois = async (): Promise<Poi[]> => {
  const response = await apiClient.get<Poi[]>(API_ENDPOINTS.GET_ALL_KRASNALS);
  return response.data;
};
