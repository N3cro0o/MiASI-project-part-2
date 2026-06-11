import { useQuery } from '@tanstack/react-query';
import { getPois } from './getPois';
import type { Poi } from '../models/Poi';

/**
 * Custom hook to fetch POIs using React Query.
 * Sets a sensible stale time of 5 minutes.
 */
export const usePois = () => {
  return useQuery<Poi[], Error>({
    queryKey: ['pois'],
    queryFn: getPois,
    staleTime: 5 * 60 * 1000, // 5 minutes
  });
};
