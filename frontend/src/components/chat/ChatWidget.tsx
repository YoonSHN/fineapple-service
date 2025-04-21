import React, { useState, useEffect } from 'react';
import '@/index.css'
import ChatButton from './ChatButton';
import ChatWindow from './ChatWindow';
import LoginPage from '@/components/auth/LoginPage';
import { Dialog, DialogContent } from '@/components/ui/dialog';

const ChatWidget: React.FC = () => {
  const [isOpen, setIsOpen] = useState(false);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [showLoginDialog, setShowLoginDialog] = useState(false);

  useEffect(() => {
    const user = localStorage.getItem('user');
    if (user) {
      setIsLoggedIn(true);
    }
  }, []);

  const toggleChat = () => {
    if (isLoggedIn) {
      setIsOpen(prev => !prev);
    } else {
      setShowLoginDialog(true);
    }
  };

  const handleLogin = () => {
    setIsLoggedIn(true);
    setShowLoginDialog(false);
    setIsOpen(true);
  };

  const handleLogout = () => {
    localStorage.removeItem('user');
    setIsLoggedIn(false);
    setIsOpen(false);
  };

  return (
      <>
        {/* 오버레이 완전 제거 */}
        <ChatButton onClick={toggleChat} isOpen={isOpen} />

        {isOpen && (
            <ChatWindow
                isOpen={isOpen}
                onClose={() => setIsOpen(false)}
                isLoggedIn={isLoggedIn}
                onLogout={handleLogout}
            />
        )}

        <Dialog open={showLoginDialog} onOpenChange={setShowLoginDialog}>
          <DialogContent className="sm:max-w-md">
            <LoginPage onLogin={handleLogin} />
          </DialogContent>
        </Dialog>
      </>
  );
};

export default ChatWidget;
