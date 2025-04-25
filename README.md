# 파인(FINE) 애플

'파인(FINE) 애플'은 정교하고 세밀한 브랜드의 철학을 반영한 쇼핑몰입니다.
브랜드의 이름은 "파인"이 주는 섬세한 아름다움과 정밀함을 강조하며,
"애플"은 그 자체의 상징성과 함께, 소비자에게 고급스럽고 가치 있는 제품을 제공하는 이미지를 전달합니다.

마치 애플 로고에서 한 입 베어먹은 형태처럼,'파인 애플'은 과학적이고 세련된 접근을 통해 고객에게 깊이 있는 제품 경험을 선사하는 것을 목표로 합니다.

## 토이 프로젝트3 기능 구현 과정 및 결과

### 시스템 아키텍쳐
![FineApple_시스템 아키텍쳐](https://github.com/user-attachments/assets/6abc614f-a5c3-4df0-a1b9-f23baafbab0c)


### Fast API 기반 챗봇 서버 구축(필수)
|완료여부|항목|자료|
|--|--|--|
|✅|Fast API 프레임워크를 이용해 기본 챗봇 서버 구축|[FastAPI 기반 RESTful API 서버 구성 완료](/fastapi-server/app/main.py)
|✅|OpenAI API를 연동하여 사용자의 요청에 따라 챗봇의 적절한 응답 반환|[사용자 질문에 대해 LLM을 통해 의도 분류 및 응답 생성](/fastapi-server/app/services)
|✅|챗봇 서버의 기본 구조 및 라우팅 설계|[FastAPI 앱 구성 및 라우팅 등록](/fastapi-server/app/main.py)

### 챗봇 기능 구현(필수)
|완료여부|항목|자료|
|--|--|--|
|✅|쇼핑몰의 FAQ와 공지사항을 기반으로 챗봇이 사용자의 질문에 답할 수 있도록 데이터셋 구축|[FAQ와 공지사항 등에 대한 md파일 제작](/fastapi-server/data/docs)
|✅|사용자 질문에 대해 챗봇이 응답할 수 있도록 OpenAI API를 활용한 질문 응답 기능 구현|[intent 기반 쿼리 처리 + LLM 응답으로 사용자에게 전달](/fastapi-server/app//services/chat_logic.py)
|✅|챗봇이 사용자의 주문 상태 조회나 간단한 상품 검색을 지원하도록 기능 확장|[/orders, /products 관련 내부 로직 구현](/fastapi-server/app/services/order_service.py)

### 자바 스프링 서버와의 통신 및 데이터 연동(권장)
|완료여부|항목|자료|
|--|--|--|
|✅|자바 스프링 서버와 Fast API 챗봇 서버 간의 RESTful API 연동|[JWT 인증 기반 Spring → FastAPI 사용자 ID 연동](/fastapi-server/app/services/auth_service.py)
|✅|사용자 정보, 주문 상태, 상품 정보 등의 데이터를 자바 스프링 서버에서 Fast API 챗봇 서버로 안전하게 전달하기 위한 API 설계|[Spring 서버와 동일한 MySQL DB → FastAPI에서 MyBatis로 접근](/fastapi-server/app/database/db.py)


## 토이1&2 기능 구현 과정 및 결과

### 프로젝트 구현 및 실행 조건
|완료여부|항목|자료|
|--|--|--|
|✅|ERD툴을 이용한 논리 모델링 작성|![ERD](images/ERD전체모델링.png)
|✅|물리모델링 & 테이블 생성 및 데이터 입력 스크립트 작성|[스크립트 디렉토리](/src/main/resources/db/migration)
|✅|mysql도커|[MYSQL 8.4.3을 사용하는 도커](/docker-compose.yml)

### 프로젝트 요구 사항(필수)
기본 정보 테이블 & 행위 테이블 설계
|완료여부|항목|자료|
|--|--|--|
|✅|회원, 장바구니, 주문, 이력관리 등 테이블 생성|[테이블 생성 테이블](/src/main/resources/db/migration/V1__insert_initial_data.sql)
|✅|결제, 환불관련 및 모든 테이블 데이터 삽입|[데이터 삽입 전체 SQL문](/src/main/resources/db/migration/V5__insert_initial_data.sql)
|✅|공통코드 테이블 |[공통코드 테이블](src/main/resources/db/migration/V2__insert_initial_data.sql)

카테고리 & 코드 테이블 설계
|완료여부|항목|자료|
|--|--|--|
|✅|상품의 분류를 3계층으로 나눈 테이블 작성|![사진](images/상품_카테고리.png)
|✅|고객 직업, 주문상태, 결제 방법 등의 코드를 통합한 테이블 작성|![사진](images/공통코드.png)

테이블 공통 필수 사항
|완료여부|항목|자료|
|--|--|--|
|✅|각 테이블에는 PK컬럼과 시스템 컬럼을 필수로 추가|![사진](images/pk와시스템컬럼.png)

### 프로젝트 요구 사항(권장)
쿠폰 & 포인트 테이블 추가
|완료여부|항목|자료|
|--|--|--|
|✅|쿠폰 사용 조건과 정책을 위한 테이블 설계|![사진](images/쿠폰.png)
|❌|상품 구매시 발생하는 포인트를 관리하는 테이블 설계| 결제복잡도를 줄이기 위해 포인트 테이블을 따로 설계하지 않았음.

자바(JDBC)로 각 테이블의 데이터를 조회 및 검증
|완료여부|항목|자료|
|--|--|--|
|✅|JDBC로 MYSQL 연결 확인|![사진](images/JDBC검증.png)
|✅|TDD로 테이블의 제약조건 (PK, FK, 기본값, CHECK)이 잘 적용되었는지 확인|![사진](images/물리모델링검증.png)![사진](images/db빌드성공.png)

재고 관리와 거래처 테이블 추가
|완료여부|항목|자료|
|--|--|--|
|✅|상품의 재고와 입고를 관리하는 테이블 설계|![사진](images/상품재고입고관리.png)
|❌|상품을 매입하는 거래처를 관리하는 테이블 설계| 거래처는 따로 구현하지 않음. 상품관리를 담당하는 스토어를 구현

기타 요구사항
|완료여부|항목|자료|
|--|--|--|
|✅|직원이 고객 지원 문의에 답변하는 시스템 및 스토어 근무 직원 엔티티 설계와 개발|![사진](images/직원고객센터.png)|
## 자료
### 화면 정의서
본 프로젝트의 화면 정의서는 사용자 인터페이스(UI)와 각 화면의 역할을 명확히 정의합니다.또한 이 정의서는 개발자가 각 화면을 구현할 때의 기준이 되며, 일관된 사용자 경험(UX)을 보장하기 위해 설계되었습니다.

#### Member(멤버)

![사진](images/멤버.png)


#### Product(상품)
![사진](images/상품.png)


#### Payment(결제)
![사진](images/결제.png)

#### Coupon(쿠폰)
![사진](images/쿠폰.png)


### Erd Diagram

![사진](images/ERD전체모델링.png)


### 논리설계서


![사진](images/논리설계도1.png)
![사진](images/논리설계도2.png)
![사진](images/공통코드엑셀.png)

