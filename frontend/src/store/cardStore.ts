import { create } from 'zustand';
import axiosInstance from '../api/axiosInstance';
import type { Card } from '../types';

interface CardState {
  cards: Card[];
  loading: boolean;
  error: string | null;
  fetchCardsByDeck: (deckId: number) => Promise<void>;
  createCard: (deckId: number, front: string, back: string, example: string) => Promise<void>;
  updateCard: (cardId: number, front: string, back: string, example: string) => Promise<void>;
  deleteCard: (cardId: number) => Promise<void>;
  fetchReviewCards: (deckId: number) => Promise<Card[]>;
  submitReview: (cardId: number, grade: number) => Promise<void>;
}

export const useCardStore = create<CardState>((set, get) => ({
  cards: [],
  loading: false,
  error: null,

  fetchCardsByDeck: async (deckId) => {
    set({ loading: true, error: null });
    try {
      const response = await axiosInstance.get(`/cards/deck/${deckId}`);
      set({ cards: response.data, loading: false });
    } catch (err: any) {
      set({ error: err.response?.data?.message || 'Không thể tải danh sách thẻ', loading: false });
    }
  },

  fetchReviewCards: async (deckId) => {
    set({ loading: true, error: null });
    try {
      const response = await axiosInstance.get(`/cards/deck/${deckId}/review`);
      set({ loading: false });
      return response.data;
    } catch (err: any) {
      set({ error: err.response?.data?.message || 'Không thể tải danh sách ôn tập', loading: false });
      return [];
    }
  },

  submitReview: async (cardId, grade) => {
    try {
      await axiosInstance.post(`/cards/${cardId}/review`, { grade });
    } catch (err: any) {
      throw new Error(err.response?.data?.message || 'Không thể gửi kết quả ôn tập');
    }
  },

  createCard: async (deckId, front, back, example) => {
    try {
      const response = await axiosInstance.post(`/cards/deck/${deckId}`, { front, back, example });
      set({ cards: [...get().cards, response.data] });
    } catch (err: any) {
      throw new Error(err.response?.data?.message || 'Không thể tạo thẻ');
    }
  },

  updateCard: async (cardId, front, back, example) => {
    try {
      const response = await axiosInstance.put(`/cards/${cardId}`, { front, back, example });
      set({
        cards: get().cards.map((card) => (card.id === cardId ? response.data : card)),
      });
    } catch (err: any) {
      throw new Error(err.response?.data?.message || 'Không thể cập nhật thẻ');
    }
  },

  deleteCard: async (cardId) => {
    try {
      await axiosInstance.delete(`/cards/${cardId}`);
      set({ cards: get().cards.filter((card) => card.id !== cardId) });
    } catch (err: any) {
      throw new Error(err.response?.data?.message || 'Không thể xóa thẻ');
    }
  },
}));
