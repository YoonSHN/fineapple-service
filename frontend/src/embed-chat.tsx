import React from 'react';
import ReactDOM from 'react-dom/client';
import ChatWidget from '@/components/chat/ChatWidget.tsx';

const mount = () => {
    const container = document.getElementById('fineapple-chat-root');
    if (container) {
        const root = ReactDOM.createRoot(container);
        root.render(<ChatWidget />);
    }
};

if (document.readyState === 'complete') {
    mount();
} else {
    window.addEventListener('DOMContentLoaded', mount);
}
