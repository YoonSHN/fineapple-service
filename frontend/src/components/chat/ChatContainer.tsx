// ChatContainer.tsx

import ChatButton from "./ChatButton";
import LoginPage from "@/components/auth/LoginPage.tsx";
import ChatWindow from "@/components/chat/ChatWindow.tsx";
import {useState} from "react";

const ChatContainer = () => {
    const [isChatOpen, setIsChatOpen] = useState(false);
    const [isLoginOpen, setIsLoginOpen] = useState(false);

    const handleChatButtonClick = () => {
        setIsLoginOpen(true); // 무조건 로그인 먼저
    };

    const handleLoginSuccess = () => {
        setIsLoginOpen(false);
        setIsChatOpen(true); // 로그인 성공 후 챗창 열기
    };

    const handleCloseChat = () => {
        setIsChatOpen(false);
    };

    return (
        <>
            <ChatButton onClick={handleChatButtonClick} isOpen={false} />
            {isLoginOpen && <LoginPage onLogin={handleLoginSuccess} />}
            {isChatOpen && <ChatWindow isOpen={true} onClose={handleCloseChat} onLogout={() => {}} />}
        </>
    );
};
