
![chrome-capture-2025-07-09 (3)](https://github.com/user-attachments/assets/dedac5d9-a66a-4452-b021-b0b950d93080)

# 📎 Takku (따쿠) 프로젝트 - 펀딩 기반 소상공인 쿠폰 플랫폼
> “누구나 쉽게 펀딩 만들고, AI가 대신 홍보해드립니다.”
> 
> 
> 소상공인은 상품만 올리면, **AI가 자동으로 홍보하고 팔아주는**
> 
> 진입장벽 없는 펀딩 쿠폰 플랫폼
> 

---
## 👥 Team Members

> Takku는 전원 풀스택 개발자로 구성된 팀입니다.
> 
> 
> 각자가 맡은 영역을 기반으로 협력하며 프로젝트 전 과정을 함께했습니다.
> 

| 최원정(팀장) | 최은진 | 한진호 | 김세연 | 최희정 |
|-------------|--------|--------|--------|--------|
| ![Pic_20250602_121053](https://github.com/user-attachments/assets/6582a9b7-ff56-42ef-a008-221f85c37345) | ![Pic_20250605_123618](https://github.com/user-attachments/assets/e4f45eea-2acd-4c17-a922-75c4b254c343) | ![Pic_20250602_125551](https://github.com/user-attachments/assets/f7d62064-c427-4b6b-9388-840fc01e3e3c) | ![Pic_20250602_010119](https://github.com/user-attachments/assets/eb1bc6e3-034c-4cc2-97b9-f6e18440418b) | ![Pic_20250602_121403](https://github.com/user-attachments/assets/76910895-064a-47d1-8654-7108c29a77f3) |



## 🧑‍💻 프로젝트 소개

- Takku(따쿠)는 **오프라인 소상공인을 위한**

**AI 자동 홍보 + 펀딩형 쿠폰 판매 플랫폼**입니다.

- 상품 등록만 하면, **AI가 자동으로 마케팅 문구 생성**
- 소비자는 **펀딩 방식으로 쿠폰을 선구매**
- **빠른 정산**과 **간편한 쿠폰 사용 경험 제공**

**IT 비전문가도 쉽게 사용할 수 있는 UI**를 통해

소상공인의 진입장벽을 낮추는 것을 목표로 합니다.
![chrome-capture-2025-07-09](https://github.com/user-attachments/assets/af0291d6-cf8d-4f94-b02a-4303d3460c06)

---

## 🌟 프로젝트 주요 목표

- ✅ 소상공인의 안정적인 매출 확보
- ✅ AI 자동 마케팅 기능 제공
- ✅ 접근성 높은 사용자 경험(UI/UX)
- ✅ 소비자에게는 희소성 있는 쿠폰 혜택

---

## 🔍 주요 기능 요약

### 1. 펀딩 기반 쿠폰 발행

- 소상공인이 직접 상품 및 쿠폰 등록
- 목표 수량 펀딩 성공 시 수익 정산 → 자금 선확보
- QR 기반 쿠폰 사용으로 간편한 소비자 UX

### 2. AI 기반 홍보 자동화

- 입력 정보 기반 **마케팅 문구 자동 생성 (Groq API 활용)**
- SNS/해시태그/스토리 중심 문구 제공
- **희소성·스토리 강조 → 단순 할인과 차별화**

### 3. 접근성 좋은 UI

- 큰 버튼, 쉬운 용어, 단계별 가이드
- 통계, 성과 분석, 고급 기능 제공

### 4. 판매/매출 분석 대시보드

- 플랫폼 통계
- 상점 통계
- 펀딩 통계
- 상품 통계

### 5. 소비자 혜택

- 펀딩을 통한 저렴한 사전 구매
- 리뷰 기반 스마트 소비 유도
- 지역 소상공인 상품 탐색 가능

---

## ⚙️ 사용 기술 스택

| 구분 | 기술 |
| --- | --- |
| Backend | `Spring Framework (Legacy MVC)` / `Servlet` / `MyBatis` / `Oracle DB` |
| Frontend | `JSP` / `HTML`, `CSS`, `JavaScript`, `jQuery` |
| Infra | `Apache Tomcat` (Context path: `/project`) / EC2 (Ubuntu) |
| API 연동 | **Groq AI API** (AI 문구 생성), **Iamport** (결제), **SOLAPI** (문자 발송), **외부 AI 분석 API** |
| 협업 도구 | GitHub / Swagger (API 명세 확인: http://localhost:9999/project/swagger) |

---

## 🛠️ 주요 설정 정보

> 아래 설정은 application.properties 기준 정리된 주요 인프라/보안/외부 API 설정 요약입니다:
> 
- **DB:** Oracle XE
- **AI:** Groq API (Qwen 모델 기반 자동 문구 생성)
- **파일 업로드:** EC2 및 로컬 개발 환경 대응
- **결제 API:** Iamport 연동
- **문자 발송:** SOLAPI 연동
- **외부 AI 분석 서버:** [https://takku-ai-api-production.up.railway.app](https://takku-ai-api-production.up.railway.app/) → DOWN 예정

---

## 📌 참고 URL

- **Swagger UI:** http://localhost:9999/project/swagger
    
    (→ `/project/`는 Tomcat의 Context Path입니다.)
    

---

## 💡 핵심 가치

> "소상공인이 마케팅 부담 없이 수익을 창출하고,
> 
> 
> 소비자는 가치를 기반으로 소비할 수 있도록.
> 
> 기술로 **구매자와 소상공인을 연결하는 플랫폼**을 만들고자 합니다."
> 

---
