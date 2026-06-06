import React, { useEffect, useState } from 'react';
import { Plus, Edit2, Trash2, Play } from 'lucide-react';
import { Link } from 'react-router-dom';
import { useDeckStore } from '../store/deckStore';
import type { Deck } from '../types';
import { toast } from 'react-toastify';
import './Dashboard.css';

const DashboardPage: React.FC = () => {
  const { decks, loading, fetchDecks, createDeck, updateDeck, deleteDeck } = useDeckStore();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingDeck, setEditingDeck] = useState<Deck | null>(null);
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');

  useEffect(() => {
    fetchDecks();
  }, [fetchDecks]);

  const handleOpenModal = (deck?: Deck) => {
    if (deck) {
      setEditingDeck(deck);
      setTitle(deck.title);
      setDescription(deck.description);
    } else {
      setEditingDeck(null);
      setTitle('');
      setDescription('');
    }
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setEditingDeck(null);
    setTitle('');
    setDescription('');
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (editingDeck) {
        await updateDeck(editingDeck.id, title, description);
        toast.success('Cập nhật bộ thẻ thành công');
      } else {
        await createDeck(title, description);
        toast.success('Tạo bộ thẻ mới thành công');
      }
      handleCloseModal();
    } catch (error: any) {
      toast.error(error.message);
    }
  };

  const handleDelete = async (id: number) => {
    if (window.confirm('Bạn có chắc chắn muốn xóa bộ thẻ này không?')) {
      try {
        await deleteDeck(id);
        toast.success('Đã xóa bộ thẻ');
      } catch (error: any) {
        toast.error(error.message);
      }
    }
  };

  return (
    <div className="dashboard-container">
      <div className="dashboard-header">
        <h1>Bộ thẻ của tôi</h1>
        <button className="add-deck-btn" onClick={() => handleOpenModal()}>
          <Plus size={20} />
          Tạo bộ thẻ mới
        </button>
      </div>

      {loading ? (
        <div className="loading-spinner">Đang tải danh sách bộ thẻ...</div>
      ) : decks.length === 0 ? (
        <div className="empty-state">
          <h3>Bạn chưa có bộ thẻ nào</h3>
          <p>Hãy tạo bộ thẻ đầu tiên để bắt đầu học tiếng Hàn ngay!</p>
        </div>
      ) : (
        <div className="deck-grid">
          {decks.map((deck) => (
            <div key={deck.id} className="deck-card">
              <Link to={`/decks/${deck.id}`} style={{ textDecoration: 'none' }}>
                <h3>{deck.title}</h3>
              </Link>
              <p>{deck.description || 'Không có mô tả'}</p>
              <div className="deck-card-footer">
                <div className="deck-actions">
                  <button className="action-btn edit" onClick={() => handleOpenModal(deck)}>
                    <Edit2 size={18} />
                  </button>
                  <button className="action-btn delete" onClick={() => handleDelete(deck.id)}>
                    <Trash2 size={18} />
                  </button>
                </div>
                <Link to={`/study/${deck.id}`} className="study-btn">
                  <Play size={16} style={{ marginRight: '6px', display: 'inline' }} />
                  Học ngay
                </Link>
              </div>
            </div>
          ))}
        </div>
      )}

      {isModalOpen && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h2>{editingDeck ? 'Chỉnh sửa bộ thẻ' : 'Tạo bộ thẻ mới'}</h2>
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label>Tên bộ thẻ</label>
                <input
                  type="text"
                  value={title}
                  onChange={(e) => setTitle(e.target.value)}
                  required
                  placeholder="Ví dụ: TOPIK I - Từ vựng cơ bản"
                />
              </div>
              <div className="form-group">
                <label>Mô tả</label>
                <textarea
                  style={{ width: '100%', padding: '12px', borderRadius: '6px', border: '1px solid #ddd', minHeight: '100px' }}
                  value={description}
                  onChange={(e) => setDescription(e.target.value)}
                  placeholder="Nhập mô tả cho bộ thẻ này..."
                />
              </div>
              <div className="modal-actions">
                <button type="button" className="cancel-btn" onClick={handleCloseModal}>
                  Hủy
                </button>
                <button type="submit" className="save-btn">
                  {editingDeck ? 'Cập nhật' : 'Tạo mới'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default DashboardPage;
