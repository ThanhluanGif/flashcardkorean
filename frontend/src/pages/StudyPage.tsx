import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { ArrowLeft, CheckCircle } from 'lucide-react';
import { useCardStore } from '../store/cardStore';
import { useDeckStore } from '../store/deckStore';
import type { Card } from '../types';
import { toast } from 'react-toastify';
import './StudyPage.css';

const StudyPage: React.FC = () => {
  const { deckId } = useParams<{ deckId: string }>();
  const id = Number(deckId);

  const { fetchReviewCards, submitReview, loading } = useCardStore();
  const { decks, fetchDecks } = useDeckStore();

  const [reviewCards, setReviewCards] = useState<Card[]>([]);
  const [currentIndex, setCurrentIndex] = useState(0);
  const [isFlipped, setIsFlipped] = useState(false);
  const [isFinished, setIsFinished] = useState(false);

  const currentDeck = decks.find(d => d.id === id);

  useEffect(() => {
    const loadCards = async () => {
      const cards = await fetchReviewCards(id);
      setReviewCards(cards);
    };
    loadCards();
    if (decks.length === 0) fetchDecks();
  }, [id, fetchReviewCards, fetchDecks, decks.length]);

  const handleFlip = () => {
    setIsFlipped(!isFlipped);
  };

  const handleGrade = async (grade: number) => {
    const currentCard = reviewCards[currentIndex];
    try {
      await submitReview(currentCard.id, grade);
      
      if (currentIndex < reviewCards.length - 1) {
        setIsFlipped(false);
        // Đợi hiệu ứng lật thẻ quay lại mặt trước rồi mới chuyển thẻ
        setTimeout(() => {
          setCurrentIndex(currentIndex + 1);
        }, 150);
      } else {
        setIsFinished(true);
      }
    } catch (error: any) {
      toast.error(error.message);
    }
  };

  if (loading && reviewCards.length === 0) {
    return (
      <div className="study-container">
        <div className="loading-spinner">Đang chuẩn bị thẻ ôn tập...</div>
      </div>
    );
  }

  if (reviewCards.length === 0 && !loading) {
    return (
      <div className="study-container">
        <div className="finished-card">
          <h2>Tuyệt vời!</h2>
          <p>Bạn đã hoàn thành tất cả các thẻ cần ôn tập trong bộ <strong>{currentDeck?.title}</strong> hôm nay.</p>
          <Link to="/" className="back-home-btn">Quay lại trang chủ</Link>
        </div>
      </div>
    );
  }

  if (isFinished) {
    return (
      <div className="study-container">
        <div className="finished-card">
          <CheckCircle size={64} color="#4caf50" style={{ marginBottom: '20px' }} />
          <h2>Hoàn thành buổi học!</h2>
          <p>Bạn đã ôn tập xong {reviewCards.length} thẻ. Hãy duy trì thói quen học mỗi ngày nhé!</p>
          <Link to="/" className="back-home-btn">Quay lại Dashboard</Link>
        </div>
      </div>
    );
  }

  const currentCard = reviewCards[currentIndex];
  const progress = ((currentIndex + 1) / reviewCards.length) * 100;

  return (
    <div className="study-container">
      <div className="study-progress">
        <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '14px', color: '#666' }}>
          <span>Đang học: {currentDeck?.title}</span>
          <span>{currentIndex + 1} / {reviewCards.length}</span>
        </div>
        <div className="progress-bar-bg">
          <div className="progress-bar-fill" style={{ width: `${progress}%` }}></div>
        </div>
      </div>

      <div className="flashcard-scene" onClick={handleFlip}>
        <div className={`flashcard ${isFlipped ? 'is-flipped' : ''}`}>
          <div className="flashcard-face flashcard-front">
            <h2>{currentCard.front}</h2>
            <div className="hint-text" style={{ marginTop: '20px' }}>Chạm để xem nghĩa</div>
          </div>
          <div className="flashcard-face flashcard-back">
            <h3>{currentCard.back}</h3>
            {currentCard.example && (
              <div className="example-text">
                <strong>Ví dụ:</strong><br />
                {currentCard.example}
              </div>
            )}
            <div className="hint-text" style={{ marginTop: 'auto' }}>Chạm để quay lại mặt trước</div>
          </div>
        </div>
      </div>

      <div className="study-controls">
        {!isFlipped ? (
          <p className="hint-text">Hãy cố gắng nhớ nghĩa trước khi lật thẻ</p>
        ) : (
          <div className="grading-buttons">
            <button className="grade-btn btn-again" onClick={() => handleGrade(0)}>
              Quên
              <span>&lt; 10p</span>
            </button>
            <button className="grade-btn btn-hard" onClick={() => handleGrade(1)}>
              Khó
              <span>1 ngày</span>
            </button>
            <button className="grade-btn btn-good" onClick={() => handleGrade(2)}>
              Tốt
              <span>3 ngày</span>
            </button>
            <button className="grade-btn btn-easy" onClick={() => handleGrade(3)}>
              Dễ
              <span>7 ngày</span>
            </button>
          </div>
        )}
      </div>

      <Link to="/" style={{ marginTop: '40px', color: '#999', textDecoration: 'none', display: 'flex', alignItems: 'center', gap: '5px' }}>
        <ArrowLeft size={16} /> Thoát buổi học
      </Link>
    </div>
  );
};

export default StudyPage;
