
import React from 'react';
import { cn } from '@/lib/utils';

interface ChatButtonProps {
  onClick: () => void;
  isOpen: boolean;
}

const ChatButton: React.FC<ChatButtonProps> = ({ onClick, isOpen }) => {
  return (
    <button
      onClick={onClick}
      className={cn(
        "fixed bottom-6 right-6 p-4 rounded-full bg-black shadow-lg",
        "transition-all duration-300 ease-in-out hover:bg-opacity-90 z-50",
        "focus:outline-none focus:ring-2 focus:ring-gray-800 focus:ring-offset-2",
        isOpen ? "rotate-90 scale-90" : "rotate-0 scale-100"
      )}
      aria-label="Open chat"
    >
      {/* Custom Pineapple Icon */}
      <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" className="text-white">
        {/* Pineapple leaf */}
        <path d="M12 2C12 2 9 5 9 7C9 8.5 10 9.5 12 9.5C14 9.5 15 8.5 15 7C15 5 12 2 12 2Z" fill="#FFFFFF" />
        
        {/* Pineapple body */}
        <path d="M12 10C8.13401 10 5 13.134 5 17C5 20.866 8.13401 22 12 22C15.866 22 19 20.866 19 17C19 13.134 15.866 10 12 10Z" fill="#FFFFFF" />
        
        {/* Pineapple pattern */}
        <path d="M9 14C9 14 9 16 9 17M12 13C12 13 12 16 12 18M15 14C15 14 15 16 15 17" stroke="#000000" strokeWidth="1" strokeLinecap="round" />
        <path d="M8 15H16M7 17H17M8 19H16" stroke="#000000" strokeWidth="1" strokeLinecap="round" />
      </svg>
    </button>
  );
};

export default ChatButton;
