import React, { useRef, useEffect } from 'react';
import Message from './Message';
import { MessageType } from '@/types/chat';

interface ChatMessagesProps {
  messages: MessageType[];
  isBotTyping?: boolean;
}

const ChatMessages: React.FC<ChatMessagesProps> = ({ messages, isBotTyping }) => {
  const messagesEndRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages, isBotTyping]);

  return (
      <div className="flex-1 overflow-y-auto max-h-[500px]">
        <div className="p-4 bg-gray-50 space-y-4">
          {messages.map(message => (
              <Message key={message.id} message={message} />
          ))}

          {isBotTyping && (
              <div className="text-sm text-gray-500 animate-pulse px-4 py-2 bg-white border border-gray-200 rounded-2xl max-w-[80%]">
                답변 중입니다<span className="inline-block animate-bounce ml-1">...</span>
              </div>
          )}

          <div ref={messagesEndRef} />
        </div>
      </div>
  );
};

export default ChatMessages;
