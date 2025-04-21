import React, { useState, useRef, useEffect } from 'react';
import { SendHorizontal } from 'lucide-react';
import { cn } from '@/lib/utils';

interface MessageInputProps {
  onSendMessage: (content: string) => void;
  isOpen: boolean;
  isLoggedIn: boolean;
}

const MessageInput: React.FC<MessageInputProps> = ({ onSendMessage, isOpen, isLoggedIn }) => {
  const [input, setInput] = useState('');
  const inputRef = useRef<HTMLInputElement>(null);

  useEffect(() => {
    if (isOpen && isLoggedIn) {
      setTimeout(() => {
        inputRef.current?.focus();
      }, 300);
    }
  }, [isOpen, isLoggedIn]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!input.trim()) return;
    
    onSendMessage(input);
    setInput('');
  };

  return (
    <form onSubmit={handleSubmit} className="p-3 border-t border-gray-100 bg-white">
      <div className="relative flex items-center">
        <input
          ref={inputRef}
          type="text"
          value={input}
          onChange={(e) => setInput(e.target.value)}
          placeholder="Type your message..."
          className="w-full py-2 px-4 rounded-full bg-gray-100 border-transparent focus:border-transparent focus:ring-0 focus:outline-none text-sm"
        />
        <button
          type="submit"
          disabled={!input.trim()}
          className={cn(
            "absolute right-1 p-1.5 rounded-full",
            input.trim() ? "bg-black text-white" : "bg-gray-200 text-gray-400"
          )}
          aria-label="Send message"
        >
          <SendHorizontal className="h-4 w-4" />
        </button>
      </div>
    </form>
  );
};

export default MessageInput;
