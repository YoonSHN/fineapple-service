
import React from 'react';
import { cn } from '@/lib/utils';
import { MessageType } from '@/types/chat';

interface MessageProps {
  message: MessageType;
}

const Message: React.FC<MessageProps> = ({ message }) => {
  const isBot = message.sender === 'bot';

  const sentences = message.content.split(/(?<=[.?!])\s+/);

  return (
      <div className={cn(
          "flex",
          isBot ? "justify-start" : "justify-end"
      )}>
        <div className={cn(
            "max-w-[80%] px-4 py-2 rounded-2xl text-sm animate-fade-in whitespace-pre-line",
            isBot
                ? "bg-white text-black border border-gray-200 rounded-tl-sm"
                : "bg-black text-white rounded-tr-sm"
        )}>
          {sentences.map((line, idx) => (
              <p key={idx} className="mb-1">{line}</p>
          ))}
        </div>
      </div>
  );
};


export default Message;
