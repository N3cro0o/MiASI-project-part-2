import { useMutation } from '@tanstack/react-query';
import { apiClient } from '../../../shared/api';
import { API_ENDPOINTS } from '../../../shared/api/endpoints';
import { useAuth } from '../context/AuthContext';

export const useLogin = () => {
  const { login: saveToken } = useAuth();

  return useMutation({
    mutationFn: async (payload: { loginOrEmail?: string; password?: string }) => {
      // The backend returns the JWT directly as plain text.
      const response = await apiClient.post(API_ENDPOINTS.LOGIN, payload, {
        responseType: 'text',
      });
      return response.data as string;
    },
    onSuccess: (token: string) => {
      // Save token globally on success
      saveToken(token);
    },
  });
};

export const useRegister = () => {
  const { login: saveToken } = useAuth();

  return useMutation({
    mutationFn: async (payload: { login?: string; email?: string; password?: string }) => {
      // The backend returns the JWT directly as plain text.
      const response = await apiClient.post(API_ENDPOINTS.REGISTER, payload, {
        responseType: 'text',
      });
      return response.data as string;
    },
    onSuccess: (token: string) => {
      // Save token globally on success
      saveToken(token);
    },
  });
};
