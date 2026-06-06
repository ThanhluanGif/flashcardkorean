import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { ArrowLeft, Plus, Edit2, Trash2 } from 'lucide-react';
import { useCardStore } from '../store/cardStore';
import { useDeckStore } from '../store/deckStore';
import type { Card } from '../types';
import { toast } from 'react-toastify';
import './CardExplorer.css';
import './Dashboard.css'; // Re-use modal styles

const CardExplorerPage: React.FC = () => {
  const { deckId } = useParams<{ deckId: string }>();
  const id = Number(deckId);

  const { cards, loading, fetchCardsByDeck, createCard, updateCard, deleteCard } = useCardStore();
  const { decks, fetchDecks } = useDeckStore();
  
  const [filter, setFilter] = useState<'ALL' | 'NEW' | 'LEARNING' | 'REVIEW' | 'MASTERED'>('ALL');
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingCard, setEditingCard] = useState<Card | null>(null);
  
  const [front, setFront] = useState('');
  const [back, setBack] = useState('');
  const [example, setExample] = useState('');

  const currentDeck = decks.find(d => d.id === id);

  useEffect(() => {
    fetchCardsByDeck(id);
    if (decks.length === 0) fetchDecks();
  }, [id, fetchCardsByDeck, fetchDecks, decks.length]);

  const filteredCards = cards.filter(card => 
    filter === 'ALL' ? true : card.status === filter
  );

  const handleOpenModal = (card?: Card) => {
    if (card) {
      setEditingCard(card);
      setFront(card.front);
      setBack(card.back);
      setExample(card.example);
    } else {
      setEditingCard(null);
      setFront('');
      setBack('');
      setExample('');
    }
    setIsModalOpen(true);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (editingCard) {
        await updateCard(editingCard.id, front, back, example);
        toast.success('Cập nhật thẻ thành công');
      } else {
        await createCard(id, front, back, example);
        toast.success('Thêm thẻ mới thành công');
      }
      setIsModalOpen(false);
    } catch (error: any) {
      toast.error(error.message);
    }
  };

  const handleDelete = async (cardId: number) => {
    if (window.confirm('Bạn có chắc muốn xóa thẻ này?')) {
      try {
        await deleteCard(cardId);
        toast.success('Đã xóa thẻ');
      } catch (error: any) {
        toast.error(error.message);
      }
    }
  };

  const getStatusClass = (status: string) => {
    switch (status) {
      case 'NEW': return 'status-new';
      case 'LEARNING': return 'status-learning';
      case 'REVIEW': return 'status-review';
      case 'MASTERED': return 'status-mastered';
      default: return '';
    }
  };

  return (
    <div className="explorer-container">
      <Link to="/" className="back-link">
        <ArrowLeft size={16} /> Quay lại danh sách bộ thẻ
      </Link>
      
      <div className="explorer-header">
        <div>
          <h1>{currentDeck?.title || 'Bộ thẻ'}</h1>
          <p style={{ color: '#666' }}>Danh sách tất cả các từ vựng trong bộ này</p>
        </div>
        <button className="add-deck-btn" onClick={() => handleOpenModal()}>
          <Plus size={20} /> Thêm thẻ mới
        </button>
      </div>

      <div className="filter-bar">
        {(['ALL', 'NEW', 'LEARNING', 'REVIEW', 'MASTERED'] as const).map((f) => (
          <button
            key={f}
            className={`filter-btn ${filter === f ? 'active' : ''}`}
            onClick={() => setFilter(f)}
          >
            {f === 'ALL' ? 'Tất cả' : f}
          </button>
        ))}
      </div>

      {loading ? (
        <div className="loading-spinner">Đang tải thẻ...</div>
      ) : filteredCards.length === 0 ? (
        <div className="empty-state">
          <h3>Không tìm thấy thẻ nào</h3>
          <p>Hãy bắt đầu thêm từ vựng mới vào bộ thẻ này.</p>
        </div>
      ) : (
        <div className="card-list">
          {filteredCards.map((card) => (
            <div key={card.id} className="card-item">
              <div className="card-front">{card.front}</div>
              <div className="card-back">{card.back}</div>
              <div>
                <span className={`status-badge ${getStatusClass(card.status)}`}>
                  {card.status}
                </span>
              </div>
              <div className="card-actions">
                <button className="action-btn edit" onClick={() => handleOpenModal(card)}>
                  <Edit2 size={18} />
                </button>
                <button className="action-btn delete" onClick={() => handleDelete(card.id)}>
                  <Trash2 size={18} />
                </button>
              </div>
            </div>
          ))}
        </div>
      )}

      {isModalOpen && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h2>{editingCard ? 'Chỉnh sửa thẻ' : 'Thêm thẻ mới'}</h2>
            <form onSubmit={handleSubmit}>
              <div className="form-row">
                <div className="form-group">
                  <label>Mặt trước (Tiếng Hàn)</label>
                  <input
                    type="text"
                    value={front}
                    onChange={(e) => setFront(e.target.value)}
                    required
                    placeholder="Ví dụ: 사과"
                  />
                </div>
                <div className="form-group">
                  <label>Mặt sau (Nghĩa)</label>
                  <input
                    type="text"
                    value={back}
                    onChange={(e) => setBack(e.target.value)}
                    required
                    placeholder="Ví dụ: Quả táo"
                  />
                </div>
              </div>
              <div className="form-group">
                <label>Ví dụ / Ghi chú</label>
                <textarea
                  style={{ width: '100%', padding: '12px', borderRadius: '6px', border: '1px solid #ddd', minHeight: '80px' }}
                  value={example}
                  onChange={(e) => setExample(e.target.value)}
                  placeholder="Nhập ví dụ minh họa hoặc cách dùng..."
                />
              </div>
              <div className="modal-actions">
                <button type="button" className="cancel-btn" onClick={() => setIsModalOpen(false)}>
                  Hủy
                </button>
                <button type="submit" className="save-btn">
                  {editingCard ? 'Cập nhật' : 'Thêm mới'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default CardExplorerPage;
