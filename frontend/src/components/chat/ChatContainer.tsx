// ChatContainer.tsx

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
            <ChatButton onClick={handleChatButtonClick} />
            {isLoginOpen && <LoginPage onLogin={handleLoginSuccess} />}
            {isChatOpen && <ChatWindow isOpen={true} onClose={handleCloseChat} onLogout={() => {}} />}
        </>
    );
};
