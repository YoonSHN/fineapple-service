from langchain.chains.base import Chain
from typing import Any, Dict, List
from langchain.memory import ConversationBufferMemory

class AnswerOnlyWrapper(Chain):
    chain: Chain
    memory: ConversationBufferMemory

    @property
    def input_keys(self) -> List[str]:
        return self.chain.input_keys

    @property
    def output_keys(self) -> List[str]:
        return ["answer"]

    def _call(self, inputs: Dict[str, Any], run_manager=None) -> Dict[str, Any]:
        """
        대화 이력 추가 → 체인 실행 → 이력 저장 → 응답 반환으로 구현 예정
        """
        history = self.memory.chat_memory.messages
        inputs["chat_history"] = history

        outputs = self.chain.invoke(inputs)

        self.memory.save_context(
            {"input": inputs["question"]},
            {"output": outputs["answer"]}
        )

        return {"answer": outputs["answer"]}
