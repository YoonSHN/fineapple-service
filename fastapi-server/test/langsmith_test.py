from langsmith import Client
from dotenv import load_dotenv
from langsmith.evaluation import evaluate
from app.services.LLMService import LLMService

load_dotenv()
client = Client()

dataset = client.create_dataset(
    dataset_name="응답평가용",
    description="고객센터 intent별 응답 품질 평가용"
)

examples = [
    {
        "input": {"text": "배송은 언제 오나요?"},
        "output": {"text": "일반적으로 배송은 2~3일 소요됩니다."}
    },
    {
        "input": {"text": "환불 받으려면 어떻게 하나요?"},
        "output": {"text": "마이페이지 > 주문내역에서 환불 요청을 진행하실 수 있습니다."}
    },
    {
        "input": {"text": "주문번호 좀 알려줘"},
        "output": {"text": "회원님의 최근 주문번호는 2024040500001입니다."}
    },
]


for example in examples:
    client.create_example(
        inputs=example["input"],
        outputs=example["output"],
        dataset_id=dataset.id
    )


llm = LLMService().llm

# 평가 실행
results = evaluate(
    target=llm,
    data=dataset,
    input_key="text",
    prediction_key="text",
    evaluators=["qa", "conciseness", "criteria"],
    criteria={
        "kindness": "응답이 친절하고 고객 응대에 적절합니까?",
        "accuracy": "질문에 대해 정확한 정보를 제공합니까?"
    },
    experiment_prefix="고객센터_응답_품질평가_v1"
)
