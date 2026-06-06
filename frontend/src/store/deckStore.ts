import { create } from 'zustand';
import axiosInstance from '../api/axiosInstance';
import type { Deck } from '../types';

interface DeckState {
  decks: Deck[];
  loading: boolean;
  error: string | null;
  fetchDecks: () => Promise<void>;
  createDeck: (title: string, description: string) => Promise<void>;
  updateDeck: (id: number, title: string, description: string) => Promise<void>;
  deleteDeck: (id: number) => Promise<void>;
}

export const useDeckStore = create<DeckState>((set, get) => ({
  decks: [],
  loading: false,
  error: null,

  fetchDecks: async () => {
    set({ loading: true, error: null });
    try {
      const response = await axiosInstance.get('/decks/my-decks');
      set({ decks: response.data, loading: false });
    } catch (err: any) {
      set({ error: err.response?.data?.message || 'Không thể tải danh sách bộ thẻ', loading: false });
    }
  },

  createDeck: async (title, description) => {
    try {
      const response = await axiosInstance.post('/decks', { title, description });
      const newDeck = response.data;
      set({ decks: [newDeck, ...get().decks] });
    } catch (err: any) {
      throw new Error(err.response?.data?.message || 'Không thể tạo bộ thẻ');
    }
  },

  updateDeck: async (id, title, description) => {
    try {
      // API Backend hiện tại chưa có update deck trong controller, 
      // nhưng tôi sẽ viết sẵn ở đây theo chuẩn REST.
      const response = await axiosInstance.put(`/decks/${id}`, { title, description });
      set({
        decks: get().decks.map((deck) => (deck.id === id ? response.data : deck)),
      });
    } catch (err: any) {
      throw new Error(err.response?.data?.message || 'Không thể cập nhật bộ thẻ');
    }
  },

  deleteDeck: async (id) => {
    try {
      await axiosInstance.delete(`/decks/${id}`);
      set({ decks: get().decks.filter((deck) => deck.id !== id) });
    } catch (err: any) {
      throw new Error(err.response?.data?.message || 'Không thể xóa bộ thẻ');
    }
  },
}));
