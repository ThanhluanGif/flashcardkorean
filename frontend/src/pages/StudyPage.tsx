import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { ArrowLeft, CheckCircle, Volume2 } from 'lucide-react';
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

  const playAudio = (e: React.MouseEvent, url: string) => {
    e.stopPropagation(); // Ngăn lật thẻ khi click vào loa
    const audio = new Audio(url);
    audio.play().catch(() => toast.error('Không thể phát âm thanh'));
  };

  const handleGrade = async (grade: number) => {
    const currentCard = reviewCards[currentIndex];
    try {
      // Gọi API cập nhật SRS cho backend
      await submitReview(currentCard.id, grade);
      
      // Nếu chọn "Quên/Lại" (grade 0), thêm thẻ này vào cuối danh sách để học lại ngay trong phiên này
      if (grade === 0) {
        setReviewCards([...reviewCards, { ...currentCard }]);
      }

      if (currentIndex < reviewCards.length - 1) {
        setIsFlipped(false);
        setTimeout(() => {
          setCurrentIndex(currentIndex + 1);
        }, 150);
      } else if (grade !== 0) {
        setIsFinished(true);
      } else {
        // Nếu là thẻ cuối cùng nhưng chọn "Lại", thì vẫn lật lại mặt trước
        setIsFlipped(false);
        setTimeout(() => {
          setCurrentIndex(currentIndex + 1);
        }, 150);
      }
    } catch (error: any) {
      toast.error(error.message);
    }
  };

  const handlePrevious = () => {
    if (currentIndex > 0) {
      setIsFlipped(false);
      setTimeout(() => {
        setCurrentIndex(currentIndex - 1);
      }, 150);
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
          <p>Bạn đã ôn tập xong {currentIndex + 1} lượt thẻ. Hãy duy trì thói quen học mỗi ngày nhé!</p>
          <Link to="/" className="back-home-btn">Quay lại Dashboard</Link>
        </div>
      </div>
    );
  }

  const currentCard = reviewCards[currentIndex];
  const progress = Math.min(((currentIndex + 1) / reviewCards.length) * 100, 100);

  return (
    <div className="study-container">
      <div className="study-header" style={{ width: '100%', display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
        <Link to="/" className="exit-btn" style={{ color: '#666', textDecoration: 'none', display: 'flex', alignItems: 'center', gap: '5px' }}>
          <ArrowLeft size={18} /> Thoát
        </Link>
        <div style={{ fontWeight: '600', color: '#333' }}>{currentDeck?.title}</div>
        <div style={{ width: '40px' }}></div> {/* Spacer */}
      </div>

      <div className="study-progress">
        <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '13px', color: '#888', marginBottom: '5px' }}>
          <span>Tiến độ session này</span>
          <span>{currentIndex + 1} / {reviewCards.length}</span>
        </div>
        <div className="progress-bar-bg">
          <div className="progress-bar-fill" style={{ width: `${progress}%` }}></div>
        </div>
      </div>

      <div className="flashcard-scene" onClick={handleFlip}>
        <div className={`flashcard ${isFlipped ? 'is-flipped' : ''}`}>
          <div className="flashcard-face flashcard-front">
            {currentCard.imageUrl && (
              <img src={currentCard.imageUrl} alt="Flashcard hint" style={{ maxWidth: '100%', maxHeight: '160px', marginBottom: '20px', borderRadius: '12px', objectFit: 'cover' }} />
            )}
            <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
              <h2>{currentCard.front}</h2>
              {currentCard.audioUrl && (
                <button 
                  className="audio-btn" 
                  onClick={(e) => playAudio(e, currentCard.audioUrl!)}
                  style={{ background: '#f0f7ff', border: 'none', cursor: 'pointer', color: '#4a90e2', padding: '10px', borderRadius: '50%', display: 'flex' }}
                >
                  <Volume2 size={24} />
                </button>
              )}
            </div>
            <div className="hint-text" style={{ marginTop: '30px', opacity: 0.7 }}>Chạm để lật thẻ</div>
          </div>
          <div className="flashcard-face flashcard-back">
            <h3>{currentCard.back}</h3>
            {currentCard.example && (
              <div className="example-text">
                <div style={{ fontSize: '12px', color: '#3182ce', fontWeight: 'bold', marginBottom: '4px', textTransform: 'uppercase' }}>Ví dụ</div>
                {currentCard.example}
              </div>
            )}
            <div className="hint-text" style={{ marginTop: 'auto', opacity: 0.7 }}>Chạm để xem lại mặt trước</div>
          </div>
        </div>
      </div>

      <div className="study-controls">
        {!isFlipped ? (
          <div className="action-buttons-container">
            <button 
              className="action-secondary-btn" 
              onClick={handlePrevious} 
              disabled={currentIndex === 0}
            >
              Quay lại
            </button>
            <button 
              className="action-primary-btn" 
              onClick={handleFlip}
            >
              Lật thẻ
            </button>
          </div>
        ) : (
          <div className="grading-buttons">
            <button className="grade-btn btn-again" onClick={() => handleGrade(0)}>
              Lại
              <span>&lt; 1p</span>
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
    </div>
  );
};

export default StudyPage;
