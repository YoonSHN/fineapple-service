
import React from 'react';
import ChatWidget from '@/components/chat/ChatWidget';

const Index = () => {
  return (
    <div className="min-h-screen bg-white">
      {/* Simple Header */}
      <header className="py-6 text-center px-4">
        <h1 className="text-3xl font-semibold text-apple-text">
          Fineapple Support
        </h1>
        <p className="mt-2 text-apple-darkGray">
          무엇이든 물어보세요!
        </p>
      </header>

      {/* Chat Widget */}
      <ChatWidget />
    </div>
  );
};

export default Index;
