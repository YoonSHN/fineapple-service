
import React from 'react';
import { LogOut, X } from 'lucide-react';
import { Button } from '@/components/ui/button';

interface ChatHeaderProps {
  onClose: () => void;
  onLogout: () => void;
}

const ChatHeader: React.FC<ChatHeaderProps> = ({ onClose, onLogout }) => {
  return (
    <div className="flex items-center justify-between px-4 py-3 border-b border-gray-100">
      <div className="flex items-center space-x-2">
        <div className="w-2 h-2 bg-black rounded-full"></div>
        <h3 className="font-medium text-black">Fineapple Support</h3>
      </div>
      <div className="flex items-center space-x-2">
        <Button 
          variant="ghost" 
          size="sm" 
          onClick={onLogout} 
          className="p-1 rounded-full hover:bg-gray-100 transition-colors"
        >
          <LogOut className="h-4 w-4 text-gray-700" />
        </Button>
        <button 
          onClick={onClose}
          className="p-1 rounded-full hover:bg-gray-100 transition-colors"
          aria-label="Close chat"
        >
          <X className="h-5 w-5 text-gray-700" />
        </button>
      </div>
    </div>
  );
};

export default ChatHeader;
