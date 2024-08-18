# 📗 Project 1 . Oz의 집(Legacy Project)

- 프로젝트 기간 : 2023. 12. 17 ~ 2024. 01. 19 (34일)
- 역할 : 팀장
- 참여 인원 : 4명

## 1. 프로젝트 소개

### 개발 환경

Java8, STS3, Maven, Oracle, DBeaver

### 기술 스택

**[ Back-end ]**

Mybatis, Spring Security, Qauth2.0, JavaMail API, 사업자등록번호상태조회 API

**[ Front-end ]**

JavaScript, JSP, HTML, CSS, Ajax

### 프로젝트 설계

- **프로젝트 서비스 구조도**
    
    ![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/2f6ae8d3-1237-42e1-bc99-66a722eb95dc/ee21b574-f33f-4811-bf4e-056efc82a47d/Untitled.png)
    

- **프로젝트 설계 및 협업 방식**
    - 34일이라는 짧은 시간 내에 프로젝트를 완료해야 했기 때문에 **애자일 방식**으로 협업을 진행하였습니다.
    - 매일 아침 **15분정도의 일일 스크럼 회의**를 통해 개발 진행 정도와 이슈사항을 공유했고, 그 결과  전체적인 개발 진행상황 파악이 가능하여 효율적으로 프로젝트를 마무리할 수 있었습니다.
    - 노션을 통한 개발 협업 및 개별 목표 관리를 진행하였습니다.
        
        ![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/2f6ae8d3-1237-42e1-bc99-66a722eb95dc/83263c7c-8833-4c3d-91c4-8c2add876d1a/Untitled.png)
        

### **프로젝트 요약**

- OZ의 집은 ’오늘의 집’을 모티브로 하여 구축한 가구판매 사이트로, 가구판매 + 블로깅 + 중고거래가 가능한 다기능 쇼핑몰입니다.
- ‘OZ의 집’의 사용자는 **이용자**(Client), **판매자**(Merchant), **관리자**(Admin)로 나뉘어 있으며, 각 사용자마다 접근할 수 있는 페이지가 다릅니다.

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/2f6ae8d3-1237-42e1-bc99-66a722eb95dc/78d8793e-42f4-4264-855e-0ace9b80a3d4/78af20ac-7fdc-4ad7-a5e7-5dc2208d0dd8.png)

- **프로젝트를 2개로 나누어 구현한 이유**
    1. 비정상적인 접근을 차단
    2. 관리자가 제어하는 데이터의 기밀성을 유지
    
- **프로젝트 메인 페이지**
    
    ![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/2f6ae8d3-1237-42e1-bc99-66a722eb95dc/1f683ed9-a24c-4d6d-91f4-5c211d971699/Untitled.png)
    

- **주요 ERD**

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/2f6ae8d3-1237-42e1-bc99-66a722eb95dc/8ce9bb46-6ba9-4ca5-bc96-ce3e31eb13fd/Untitled.png)

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/2f6ae8d3-1237-42e1-bc99-66a722eb95dc/d7ebb3e0-c7ef-42a2-b826-4d8c0a834c60/Untitled.png)

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/2f6ae8d3-1237-42e1-bc99-66a722eb95dc/e229325e-28d8-4bf2-b3c4-ab46f02be813/Untitled.png)
