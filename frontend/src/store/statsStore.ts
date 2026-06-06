import { create } from 'zustand';
import axiosInstance from '../api/axiosInstance';
import type { UserStats } from '../types';

interface StatsState {
  stats: UserStats | null;
  loading: boolean;
  error: string | null;
  fetchStats: () => Promise<void>;
}

export const useStatsStore = create<StatsState>((set) => ({
  stats: null,
  loading: false,
  error: null,

  fetchStats: async () => {
    set({ loading: true, error: null });
    try {
      const response = await axiosInstance.get('/cards/stats');
      set({ stats: response.data, loading: false });
    } catch (err: any) {
      set({ error: err.response?.data?.message || 'Không thể tải thống kê', loading: false });
    }
  },
}));
