import React, { useState, useEffect } from 'react';
import { cn } from '@/lib/utils';
import { MessageType } from '@/types/chat';
import ChatHeader from './ChatHeader';
import ChatMessages from './ChatMessages';
import MessageInput from './MessageInput';
import ChatModals from './ChatModals';
import { Product } from '@/types/product';

interface ChatWindowProps {
    isOpen: boolean;
    onClose: () => void;
    onLogout: () => void;
}

const ChatWindow: React.FC<ChatWindowProps> = ({ isOpen, onClose, onLogout }) => {
    const [messages, setMessages] = useState<MessageType[]>([
        {
            id: '1',
            content: '안녕하세요! 무엇을 도와드릴까요?',
            sender: 'bot',
            timestamp: new Date(),
        },
    ]);
    const [isBotTyping, setIsBotTyping] = useState(false);
    const [showComparison, setShowComparison] = useState(false);
    const [showRecommendation, setShowRecommendation] = useState(false);
    const [comparisonProducts, setComparisonProducts] = useState<Product[]>([]);
    const [recommendationProducts, setRecommendationProducts] = useState<Product[]>([]);

    // 챗봇 열릴 때 intro 메시지 호출
    useEffect(() => {
        if (isOpen) {
            const fetchIntroMessage = async () => {
                try {
                    const res = await fetch('/fastapi/api/chat/intro', {
                        method: 'GET',
                        credentials: 'include',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                    });

                    if (!res.ok) {
                        const err = await res.json();
                        throw new Error(err.detail || '오류 발생');
                    }

                    const data = await res.json();
                    console.log('Intro message data:', data);

                    if (data.content && typeof data.content === 'string') {
                        const introMessage: MessageType = {
                            id: (Date.now() + 1).toString(),
                            content: data.content,
                            sender: 'bot',
                            timestamp: new Date(),
                        };
                        setMessages((prev) => [...prev, introMessage]);

                        if (data.type === 'recommendation' && Array.isArray(data.products)) {
                            setRecommendationProducts(data.products);
                            setShowRecommendation(true);
                        }
                    } else {
                        const errorMessage: MessageType = {
                            id: (Date.now() + 2).toString(),
                            content: '응답 데이터가 유효하지 않습니다.',
                            sender: 'bot',
                            timestamp: new Date(),
                        };
                        setMessages((prev) => [...prev, errorMessage]);
                    }
                } catch (err: any) {
                    const errorMessage: MessageType = {
                        id: (Date.now() + 3).toString(),
                        content: err.message,
                        sender: 'bot',
                        timestamp: new Date(),
                    };
                    setMessages((prev) => [...prev, errorMessage]);
                }
            };

            fetchIntroMessage();
        }
    }, [isOpen]);

    const handleSendMessage = async (input: string) => {
        const userMessage: MessageType = {
            id: Date.now().toString(),
            content: input,
            sender: 'user',
            timestamp: new Date(),
        };
        setMessages((prev) => [...prev, userMessage]);

        setIsBotTyping(true);

        try {
            const res = await fetch('/fastapi/api/chat', {
                method: 'POST',
                credentials: 'include',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ query: input }),
            });

            if (!res.ok) {
                const err = await res.json();
                throw new Error(err.detail || '오류 발생');
            }

            const data = await res.json();

            if (data.content && typeof data.content === 'string') {
                setMessages((prev) =>
                    prev.filter((msg) => msg.id !== 'loading').concat({
                        id: (Date.now() + 1).toString(),
                        content: data.content,
                        sender: 'bot',
                        timestamp: new Date(),
                    })
                );
            } else {
                setMessages((prev) =>
                    prev.filter((msg) => msg.id !== 'loading').concat({
                        id: (Date.now() + 2).toString(),
                        content: '응답 데이터가 유효하지 않습니다.',
                        sender: 'bot',
                        timestamp: new Date(),
                    })
                );
            }

            if (data.type === 'comparison' && Array.isArray(data.products)) {
                setComparisonProducts(data.products);
                setShowComparison(true);
            } else if (data.type === 'recommendation' && Array.isArray(data.products)) {
                setRecommendationProducts(data.products);
                setShowRecommendation(true);
            }
        } catch (err: any) {
            setMessages((prev) =>
                prev.filter((msg) => msg.id !== 'loading').concat({
                    id: (Date.now() + 3).toString(),
                    content: err.message,
                    sender: 'bot',
                    timestamp: new Date(),
                })
            );
        }

        setIsBotTyping(false);
    };

    return (
        <>
            <div
                className={cn(
                    'fixed bottom-24 right-6 w-[350px] sm:w-[400px] rounded-2xl bg-white shadow-xl',
                    'flex flex-col border border-gray-200 transition-all duration-300 ease-in-out z-40',
                    'overflow-hidden',
                    isOpen
                        ? 'opacity-100 transform-none max-h-[600px]'
                        : 'opacity-0 translate-y-10 max-h-0 pointer-events-none'
                )}
            >
                <ChatHeader onClose={onClose} onLogout={onLogout} />
                <ChatMessages messages={messages} isBotTyping={isBotTyping} />
                <MessageInput onSendMessage={handleSendMessage} isOpen={isOpen} isLoggedIn={false} />
            </div>

            <ChatModals
                showComparison={showComparison}
                showRecommendation={showRecommendation}
                onCloseComparison={() => setShowComparison(false)}
                onCloseRecommendation={() => setShowRecommendation(false)}
                comparisonProducts={comparisonProducts}
                recommendationProducts={recommendationProducts}
            />
        </>
    );
};

export default ChatWindow;
