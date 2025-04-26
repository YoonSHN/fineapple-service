// ChatWidget.tsx
import React, { useState } from 'react';
import '@/index.css';
import ChatButton from './ChatButton';
import ChatWindow from './ChatWindow';
import LoginPage from '@/components/auth/LoginPage';
import { Dialog, DialogContent } from '@/components/ui/dialog';

const ChatWidget: React.FC = () => {
  const [isOpen, setIsOpen] = useState(false);
  const [showLoginDialog, setShowLoginDialog] = useState(false);

  // 챗 버튼 클릭 시 무조건 로그인 창부터 띄우기
  const handleChatButtonClick = () => {
    setShowLoginDialog(true);
  };

  // LoginPage 에서 로그인 성공 시 호출되는 콜백
  const handleLogin = () => {
    setShowLoginDialog(false);
    setIsOpen(true);          // 로그인 완료 후 챗 윈도우 열기
  };

  // 챗 윈도우 닫기
  const handleCloseChat = () => {
    setIsOpen(false);
  };

  // 로그아웃 → 챗 윈도우만 닫기
  const handleLogout = () => {
    setIsOpen(false);
  };

  return (
      <>
        <ChatButton onClick={handleChatButtonClick} isOpen={isOpen} />

        {/* FastAPI 기반 LoginPage */}
        <Dialog open={showLoginDialog} onOpenChange={setShowLoginDialog}>
          <DialogContent className="sm:max-w-md">
            <LoginPage onLogin={handleLogin} />
          </DialogContent>
        </Dialog>

        {isOpen && (
            <ChatWindow
                isOpen={isOpen}
                onClose={handleCloseChat}
                onLogout={handleLogout}
            />
        )}
      </>
  );
};

export default ChatWidget;
