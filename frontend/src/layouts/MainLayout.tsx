import React from 'react';
import { Link, Outlet, useNavigate } from 'react-router-dom';
import { useAuthStore } from '../store/authStore';
import { LogOut, BookOpen } from 'lucide-react';
import './MainLayout.css';

const MainLayout: React.FC = () => {
  const { user, logout } = useAuthStore();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <div className="main-layout">
      <header className="header">
        <Link to="/" className="logo-container">
          <BookOpen size={28} />
          <h2>FlashcardKorean</h2>
        </Link>

        <nav className="user-nav">
          <div className="user-info">
            <span className="user-name">{user?.fullName || user?.username}</span>
            <div className="user-avatar">
              {(user?.fullName || user?.username || 'U').charAt(0).toUpperCase()}
            </div>
          </div>
          <button onClick={handleLogout} className="logout-btn">
            <LogOut size={18} />
            <span>Đăng xuất</span>
          </button>
        </nav>
      </header>

      <main className="main-content">
        <Outlet />
      </main>
    </div>
  );
};

export default MainLayout;
