import React, { useEffect, useState } from 'react';
import { Plus, Edit2, Trash2, Play, Search, ChevronLeft, ChevronRight, Globe, Lock } from 'lucide-react';
import { Link } from 'react-router-dom';
import { useDeckStore } from '../store/deckStore';
import { useStatsStore } from '../store/statsStore';
import type { Deck } from '../types';
import { toast } from 'react-toastify';
import './Dashboard.css';

const DashboardPage: React.FC = () => {
  const { decks, loading, fetchDecks, totalPages, createDeck, updateDeck, deleteDeck } = useDeckStore();
  const { stats, fetchStats } = useStatsStore();
  
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingDeck, setEditingDeck] = useState<Deck | null>(null);
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [isPublic, setIsPublic] = useState(false);

  // Pagination & Search state
  const [page, setPage] = useState(0);
  const [keyword, setKeyword] = useState('');

  useEffect(() => {
    const timer = setTimeout(() => {
      fetchDecks(page, 10, keyword);
    }, 300); // Debounce search
    return () => clearTimeout(timer);
  }, [fetchDecks, page, keyword]);

  useEffect(() => {
    fetchStats();
  }, [fetchStats]);

  const handleOpenModal = (deck?: Deck) => {
    if (deck) {
      setEditingDeck(deck);
      setTitle(deck.title);
      setDescription(deck.description);
      setIsPublic(deck.isPublic);
    } else {
      setEditingDeck(null);
      setTitle('');
      setDescription('');
      setIsPublic(false);
    }
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setEditingDeck(null);
    setTitle('');
    setDescription('');
    setIsPublic(false);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (editingDeck) {
        await updateDeck(editingDeck.id, title, description, isPublic);
        toast.success('Cập nhật bộ thẻ thành công');
      } else {
        await createDeck(title, description, isPublic);
        toast.success('Tạo bộ thẻ mới thành công');
      }
      handleCloseModal();
      fetchStats();
    } catch (error: any) {
      toast.error(error.message);
    }
  };

  const handleDelete = async (id: number) => {
    if (window.confirm('Bạn có chắc chắn muốn xóa bộ thẻ này không?')) {
      try {
        await deleteDeck(id);
        toast.success('Đã xóa bộ thẻ');
        fetchStats();
      } catch (error: any) {
        toast.error(error.message);
      }
    }
  };

  return (
    <div className="dashboard-container">
      {stats && (
        <div className="stats-overview" style={{ marginBottom: '40px', display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '20px' }}>
          <div className="stat-card" style={{ background: '#f8f9fa', padding: '20px', borderRadius: '12px', border: '1px solid #eee', textAlign: 'center' }}>
            <div style={{ fontSize: '14px', color: '#666', marginBottom: '5px' }}>Tổng số từ vựng</div>
            <div style={{ fontSize: '28px', fontWeight: 'bold', color: '#333' }}>{stats.totalCards}</div>
          </div>
          <div className="stat-card" style={{ background: '#e3f2fd', padding: '20px', borderRadius: '12px', border: '1px solid #bbdefb', textAlign: 'center' }}>
            <div style={{ fontSize: '14px', color: '#1976d2', marginBottom: '5px' }}>Cần ôn tập hôm nay</div>
            <div style={{ fontSize: '28px', fontWeight: 'bold', color: '#0d47a1' }}>{stats.cardsDueToday}</div>
          </div>
          <div className="stat-card" style={{ background: '#e8f5e9', padding: '20px', borderRadius: '12px', border: '1px solid #c8e6c9', textAlign: 'center' }}>
            <div style={{ fontSize: '14px', color: '#388e3c', marginBottom: '5px' }}>Đã thành thạo</div>
            <div style={{ fontSize: '28px', fontWeight: 'bold', color: '#1b5e20' }}>{stats.statusCounts.MASTERED}</div>
          </div>
          <div className="stat-card" style={{ background: '#fff3e0', padding: '20px', borderRadius: '12px', border: '1px solid #ffe0b2', textAlign: 'center' }}>
            <div style={{ fontSize: '14px', color: '#f57c00', marginBottom: '5px' }}>Đang học</div>
            <div style={{ fontSize: '28px', fontWeight: 'bold', color: '#e65100' }}>{stats.statusCounts.LEARNING + stats.statusCounts.REVIEW}</div>
          </div>
        </div>
      )}

      <div className="dashboard-header">
        <h1>Bộ thẻ của tôi</h1>
        <div style={{ display: 'flex', gap: '15px', alignItems: 'center' }}>
          <Link to="/community" className="community-btn" style={{ textDecoration: 'none', color: '#4a90e2', display: 'flex', alignItems: 'center', gap: '5px', fontWeight: '500' }}>
            <Globe size={18} /> Khám phá cộng đồng
          </Link>
          <div className="search-wrapper" style={{ position: 'relative', display: 'flex', alignItems: 'center' }}>
            <Search size={18} style={{ position: 'absolute', left: '10px', color: '#888' }} />
            <input 
              type="text" 
              placeholder="Tìm bộ thẻ..." 
              value={keyword}
              onChange={(e) => {
                setKeyword(e.target.value);
                setPage(0);
              }}
              style={{ padding: '8px 12px 8px 35px', borderRadius: '6px', border: '1px solid #ddd', fontSize: '14px' }}
            />
          </div>
          <button className="add-deck-btn" onClick={() => handleOpenModal()}>
            <Plus size={20} />
            Tạo bộ thẻ mới
          </button>
        </div>
      </div>

      {loading ? (
        <div className="loading-spinner">Đang tải danh sách bộ thẻ...</div>
      ) : decks.length === 0 ? (
        <div className="empty-state">
          <h3>{keyword ? 'Không tìm thấy bộ thẻ nào phù hợp' : 'Bạn chưa có bộ thẻ nào'}</h3>
          <p>{keyword ? 'Hãy thử tìm kiếm với từ khóa khác' : 'Hãy tạo bộ thẻ đầu tiên để bắt đầu học tiếng Hàn ngay!'}</p>
        </div>
      ) : (
        <>
          <div className="deck-grid">
            {decks.map((deck) => (
              <div key={deck.id} className="deck-card">
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'start' }}>
                  <Link to={`/decks/${deck.id}`} style={{ textDecoration: 'none' }}>
                    <h3>{deck.title}</h3>
                  </Link>
                  {deck.isPublic ? <Globe size={16} color="#4caf50" title="Công khai" /> : <Lock size={16} color="#999" title="Riêng tư" />}
                </div>
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

          {totalPages > 1 && (
            <div className="pagination-controls" style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', gap: '15px', marginTop: '30px' }}>
              <button 
                disabled={page === 0} 
                onClick={() => setPage(page - 1)}
                className="pagination-btn"
                style={{ padding: '8px', borderRadius: '50%', border: '1px solid #ddd', background: 'white', cursor: page === 0 ? 'not-allowed' : 'pointer', display: 'flex' }}
              >
                <ChevronLeft size={20} />
              </button>
              <span style={{ fontSize: '14px', fontWeight: '500' }}>Trang {page + 1} / {totalPages}</span>
              <button 
                disabled={page === totalPages - 1} 
                onClick={() => setPage(page + 1)}
                className="pagination-btn"
                style={{ padding: '8px', borderRadius: '50%', border: '1px solid #ddd', background: 'white', cursor: page === totalPages - 1 ? 'not-allowed' : 'pointer', display: 'flex' }}
              >
                <ChevronRight size={20} />
              </button>
            </div>
          )}
        </>
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
              <div className="form-group" style={{ display: 'flex', alignItems: 'center', gap: '10px', marginTop: '10px' }}>
                <input 
                  type="checkbox" 
                  id="isPublic" 
                  checked={isPublic} 
                  onChange={(e) => setIsPublic(e.target.checked)} 
                  style={{ width: '18px', height: '18px' }}
                />
                <label htmlFor="isPublic" style={{ marginBottom: 0, cursor: 'pointer' }}>Công khai bộ thẻ này cho mọi người</label>
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
