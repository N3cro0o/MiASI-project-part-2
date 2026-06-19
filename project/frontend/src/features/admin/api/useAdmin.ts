import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import apiClient from '../../../shared/api/apiClient';

export interface AdminUser {
  id: number;
  login: string;
  email: string;
  role: string;
  active: boolean;
}

export const useUsers = () => {
  return useQuery({
    queryKey: ['users'],
    queryFn: async (): Promise<AdminUser[]> => {
      const response = await apiClient.get<AdminUser[]>('/api/users');
      return response.data;
    },
  });
};

export const useDeactivateUser = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async (userId: number | string) => {
      const response = await apiClient.delete(`/api/users/${userId}`);
      return response.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['users'] });
    },
  });
};

export const useActivateUser = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async (userId: number | string) => {
      const response = await apiClient.patch(`/api/users/${userId}/activate`);
      return response.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['users'] });
    },
  });
};

export const useChangeUserRole = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async ({ id, newRole }: { id: number | string; newRole: string }) => {
      const response = await apiClient.patch(`/api/users/${id}/role`, { role: newRole });
      return response.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['users'] });
    },
  });
};

export const useCreateUserAsAdmin = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async (userData: Omit<AdminUser, 'id'> & { password?: string }) => {
      const response = await apiClient.post(`/api/users`, userData);
      return response.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['users'] });
    },
  });
};
