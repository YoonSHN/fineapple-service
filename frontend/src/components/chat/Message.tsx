
import React from 'react';
import { cn } from '@/lib/utils';
import { MessageType } from '@/types/chat';

interface MessageProps {
  message: MessageType;
}

const Message: React.FC<MessageProps> = ({ message }) => {
  const isBot = message.sender === 'bot';
  
  return (
    <div className={cn(
      "flex",
      isBot ? "justify-start" : "justify-end"
    )}>
      <div className={cn(
        "max-w-[80%] px-4 py-2 rounded-2xl text-sm animate-fade-in",
        isBot 
          ? "bg-white text-black border border-gray-200 rounded-tl-sm" 
          : "bg-black text-white rounded-tr-sm"
      )}>
        {message.content}
      </div>
    </div>
  );
};

export default Message;
