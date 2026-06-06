import React, { useEffect, useState } from 'react';
import { ArrowLeft, Search, ChevronLeft, ChevronRight, Copy, Globe } from 'lucide-react';
import { Link } from 'react-router-dom';
import { useDeckStore } from '../store/deckStore';
import { toast } from 'react-toastify';
import './Dashboard.css'; // Re-use styles

const CommunityPage: React.FC = () => {
  const { publicDecks, loading, fetchPublicDecks, publicTotalPages, cloneDeck } = useDeckStore();
  
  // Pagination & Search state
  const [page, setPage] = useState(0);
  const [keyword, setKeyword] = useState('');

  useEffect(() => {
    const timer = setTimeout(() => {
      fetchPublicDecks(page, 12, keyword);
    }, 300);
    return () => clearTimeout(timer);
  }, [fetchPublicDecks, page, keyword]);

  const handleClone = async (id: number, title: string) => {
    try {
      await cloneDeck(id);
      toast.success(`Đã sao chép bộ thẻ "${title}" về tài khoản của bạn`);
    } catch (error: any) {
      toast.error(error.message);
    }
  };

  return (
    <div className="dashboard-container">
      <Link to="/" className="back-link" style={{ textDecoration: 'none', color: '#666', display: 'flex', alignItems: 'center', gap: '5px', marginBottom: '20px' }}>
        <ArrowLeft size={16} /> Quay lại Dashboard
      </Link>

      <div className="dashboard-header">
        <div>
          <h1>Cộng đồng Flashcard</h1>
          <p style={{ color: '#666' }}>Khám phá và học tập từ các bộ thẻ được chia sẻ bởi mọi người</p>
        </div>
        <div className="search-wrapper" style={{ position: 'relative', display: 'flex', alignItems: 'center' }}>
          <Search size={18} style={{ position: 'absolute', left: '10px', color: '#888' }} />
          <input 
            type="text" 
            placeholder="Tìm kiếm bộ thẻ công khai..." 
            value={keyword}
            onChange={(e) => {
              setKeyword(e.target.value);
              setPage(0);
            }}
            style={{ padding: '8px 12px 8px 35px', borderRadius: '6px', border: '1px solid #ddd', fontSize: '14px', width: '300px' }}
          />
        </div>
      </div>

      {loading ? (
        <div className="loading-spinner">Đang tải cộng đồng...</div>
      ) : publicDecks.length === 0 ? (
        <div className="empty-state" style={{ marginTop: '50px' }}>
          <Globe size={48} color="#ddd" style={{ marginBottom: '15px' }} />
          <h3>Chưa có bộ thẻ nào được chia sẻ</h3>
          <p>Hãy là người đầu tiên chia sẻ kiến thức của bạn!</p>
        </div>
      ) : (
        <>
          <div className="deck-grid" style={{ gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))' }}>
            {publicDecks.map((deck) => (
              <div key={deck.id} className="deck-card" style={{ display: 'flex', flexDirection: 'column' }}>
                <div style={{ flex: 1 }}>
                  <h3 style={{ marginBottom: '10px' }}>{deck.title}</h3>
                  <p style={{ fontSize: '14px', color: '#666', lineHeight: '1.5' }}>{deck.description || 'Không có mô tả'}</p>
                </div>
                <div className="deck-card-footer" style={{ marginTop: '20px', paddingTop: '15px', borderTop: '1px solid #f0f0f0' }}>
                  <div style={{ fontSize: '12px', color: '#999' }}>ID Người tạo: {deck.userId}</div>
                  <button 
                    onClick={() => handleClone(deck.id, deck.title)}
                    style={{ 
                      display: 'flex', 
                      alignItems: 'center', 
                      gap: '5px', 
                      padding: '6px 12px', 
                      borderRadius: '6px', 
                      border: '1px solid #4a90e2', 
                      background: 'white', 
                      color: '#4a90e2', 
                      cursor: 'pointer',
                      fontSize: '13px',
                      fontWeight: '500'
                    }}
                  >
                    <Copy size={14} /> Sao chép
                  </button>
                </div>
              </div>
            ))}
          </div>

          {publicTotalPages > 1 && (
            <div className="pagination-controls" style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', gap: '15px', marginTop: '40px' }}>
              <button 
                disabled={page === 0} 
                onClick={() => setPage(page - 1)}
                style={{ padding: '8px', borderRadius: '50%', border: '1px solid #ddd', background: 'white', cursor: page === 0 ? 'not-allowed' : 'pointer' }}
              >
                <ChevronLeft size={20} />
              </button>
              <span style={{ fontSize: '14px', fontWeight: '500' }}>Trang {page + 1} / {publicTotalPages}</span>
              <button 
                disabled={page === publicTotalPages - 1} 
                onClick={() => setPage(page + 1)}
                style={{ padding: '8px', borderRadius: '50%', border: '1px solid #ddd', background: 'white', cursor: page === publicTotalPages - 1 ? 'not-allowed' : 'pointer' }}
              >
                <ChevronRight size={20} />
              </button>
            </div>
          )}
        </>
      )}
    </div>
  );
};

export default CommunityPage;
