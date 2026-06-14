import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import apiClient from '../../../shared/api/apiClient';

export interface Review {
  id: number;
  userId: number;
  krasnalId: number;
  rating: number;
  content: string;
  created: string;
}

export interface CreateReviewPayload {
  krasnalId: number;
  rating: number;
  content: string;
}

export const useKrasnalReviews = (krasnalId: number) => {
  return useQuery({
    queryKey: ['reviews', krasnalId],
    queryFn: async (): Promise<Review[]> => {
      const response = await apiClient.get<Review[]>(`/api/reviews/krasnal/${krasnalId}`);
      return response.data;
    },
    enabled: !!krasnalId,
  });
};

export const useCreateReview = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async (payload: CreateReviewPayload) => {
      const response = await apiClient.post<Review>(`/api/krasnals/${payload.krasnalId}/reviews`, {
        rating: payload.rating,
        content: payload.content,
      });
      return response.data;
    },
    onSuccess: (_, variables) => {
      queryClient.invalidateQueries({ queryKey: ['reviews', variables.krasnalId] });
      queryClient.invalidateQueries({ queryKey: ['krasnals'] });
      queryClient.invalidateQueries({ queryKey: ['pois'] }); // Just in case pois is used anywhere
      queryClient.invalidateQueries({ queryKey: ['krasnal', variables.krasnalId] });
    },
  });
};
