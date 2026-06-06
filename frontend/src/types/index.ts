export interface User {
  id: number;
  username: string;
  email: string;
  fullName: string;
  role: string;
}

export interface Deck {
  id: number;
  title: string;
  description: string;
  userId: number;
  isPublic: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface Card {
  id: number;
  front: string;
  back: string;
  example: string;
  deckId: number;
  status: 'NEW' | 'LEARNING' | 'REVIEW' | 'MASTERED';
  nextReviewDate: string;
  imageUrl?: string;
  audioUrl?: string;
  createdAt: string;
  updatedAt: string;
}

export interface PaginatedResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
  last: boolean;
  first: boolean;
  empty: boolean;
}

export interface UserStats {
  totalCards: number;
  totalDecks: number;
  cardsDueToday: number;
  statusCounts: {
    NEW: number;
    LEARNING: number;
    REVIEW: number;
    MASTERED: number;
  };
}
