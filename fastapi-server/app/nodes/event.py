from app.models.chat import ChatState


async def event_node(state: ChatState) -> ChatState:
    message = (
        "현재 진행 중인 이벤트가 없습니다.\n"
        "추후 새로운 프로모션이 업데이트될 예정입니다.\n"
        "자세한 내용은 이벤트 페이지를 확인해주세요."
    )
    return state.copy(update={"answer": message})