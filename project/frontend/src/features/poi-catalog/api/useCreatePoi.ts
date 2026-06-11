import { useMutation, useQueryClient } from '@tanstack/react-query';
import { createPoi, type CreatePoiDto } from './createPoi';
import type { Poi } from '../models/Poi';

/**
 * Hook to create a new POI.
 * Invalidates the 'pois' query on success to instantly refresh the map and list.
 */
export const useCreatePoi = () => {
  const queryClient = useQueryClient();

  return useMutation<Poi, Error, CreatePoiDto>({
    mutationFn: createPoi,
    onSuccess: () => {
      // Force refetch of all POIs so the new one appears immediately
      queryClient.invalidateQueries({ queryKey: ['pois'] });
    },
  });
};
