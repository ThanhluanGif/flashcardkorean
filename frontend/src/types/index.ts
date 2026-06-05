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
  createdAt: string;
  updatedAt: string;
}
