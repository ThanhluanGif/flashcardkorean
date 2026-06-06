import { create } from 'zustand';
import axiosInstance from '../api/axiosInstance';
import type { Deck } from '../types';

interface DeckState {
  decks: Deck[];
  publicDecks: Deck[];
  loading: boolean;
  error: string | null;
  totalPages: number;
  totalElements: number;
  publicTotalPages: number;
  fetchDecks: (page?: number, size?: number, keyword?: string) => Promise<void>;
  fetchPublicDecks: (page?: number, size?: number, keyword?: string) => Promise<void>;
  createDeck: (title: string, description: string, isPublic: boolean) => Promise<void>;
  updateDeck: (id: number, title: string, description: string, isPublic: boolean) => Promise<void>;
  deleteDeck: (id: number) => Promise<void>;
  cloneDeck: (id: number) => Promise<void>;
}

export const useDeckStore = create<DeckState>((set, get) => ({
  decks: [],
  publicDecks: [],
  loading: false,
  error: null,
  totalPages: 0,
  totalElements: 0,
  publicTotalPages: 0,

  fetchDecks: async (page = 0, size = 10, keyword = '') => {
    set({ loading: true, error: null });
    try {
      const response = await axiosInstance.get('/decks/my-decks', {
        params: { page, size, keyword }
      });
      set({ 
        decks: response.data.content, 
        totalPages: response.data.totalPages,
        totalElements: response.data.totalElements,
        loading: false 
      });
    } catch (err: any) {
      set({ error: err.response?.data?.message || 'Không thể tải danh sách bộ thẻ', loading: false });
    }
  },

  fetchPublicDecks: async (page = 0, size = 10, keyword = '') => {
    set({ loading: true, error: null });
    try {
      const response = await axiosInstance.get('/decks/public', {
        params: { page, size, keyword }
      });
      set({ 
        publicDecks: response.data.content, 
        publicTotalPages: response.data.totalPages,
        loading: false 
      });
    } catch (err: any) {
      set({ error: err.response?.data?.message || 'Không thể tải danh sách bộ thẻ công khai', loading: false });
    }
  },

  createDeck: async (title, description, isPublic) => {
    try {
      const response = await axiosInstance.post('/decks', { title, description, isPublic });
      const newDeck = response.data;
      set({ decks: [newDeck, ...get().decks] });
    } catch (err: any) {
      throw new Error(err.response?.data?.message || 'Không thể tạo bộ thẻ');
    }
  },

  updateDeck: async (id, title, description, isPublic) => {
    try {
      const response = await axiosInstance.put(`/decks/${id}`, { title, description, isPublic });
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

  cloneDeck: async (id) => {
    try {
      const response = await axiosInstance.post(`/decks/${id}/clone`);
      const clonedDeck = response.data;
      set({ decks: [clonedDeck, ...get().decks] });
    } catch (err: any) {
      throw new Error(err.response?.data?.message || 'Không thể sao chép bộ thẻ');
    }
  },
}));
