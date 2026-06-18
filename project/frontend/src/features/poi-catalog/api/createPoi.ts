import { apiClient } from '../../../shared/api';
import type { Poi } from '../models/Poi';

export type CreatePoiDto = Omit<Poi, 'id' | 'status' | 'rating' | 'imageUrl' | 'reviewCount'>;

/**
 * Sends a POST request to create a new POI.
 */
export const createPoi = async (data: CreatePoiDto): Promise<Poi> => {
  const response = await apiClient.post<Poi>('/api/krasnal/new', data);
  return response.data;
};
