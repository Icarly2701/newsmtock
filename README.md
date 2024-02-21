# :newspaper: NEWS & STOCK

> 경제 뉴스 및 자유롭게 토론하는 게시판
> <br> 배포 사이트 : http://newstock.site

## :post_office: 프로젝트 소개

#### 가볍게 뉴스 헤드라인을 확인하고, 사람들과 소통하는 게시판

- News & Stock 게시판 프로젝트

> **_기획 배경_**
>
> 1. 주식에서 뉴스는 굉장히 중요 <br/>
> 2. 여러 서비스에서 뉴스 헤드라인을 제공 => 뉴스 헤드라인을 위한 웹사이트 <br/>
> 3. 뉴스 헤드라인에서 사용자가 원하는 관심있는 뉴스 제공 <br/>

> **_서비스 주요 기능_**
>
> - 네이버 뉴스 검색 API 연동
> - 관심어 설정 및 관심어로 뉴스 가져오기
> - 게시판 CRUD
> - 게시판 및 뉴스를 키워드 통해 조회

### ❄️ 프로젝트 기간

- 2024년 1월 04일 ~ 2024년 2월 21일 (D+48, 약 7주)

<br/>

## 💫 기술스택 및 개발환경

### 📚 Teck Stack & Tools

#### WEB FRONTEND

<div>
<img src="https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=html5&logoColor=white"> 
<img src="https://img.shields.io/badge/css-1572B6?style=for-the-badge&logo=css3&logoColor=white">
<img src="https://img.shields.io/badge/Thymeleaf-%23005C0F.svg?style=for-the-badge&logo=Thymeleaf&logoColor=white">
<img src="https://img.shields.io/badge/Visual%20Studio%20Code-007ACC.svg?&style=for-the-badge&logo=Visual%20Studio%20Code&logoColor=white"/>
</div>

#### BACKEND

<div>
<img  src="https://img.shields.io/badge/java 17-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white">
<img src="https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white">
<img src="https://img.shields.io/badge/Spring Boot 3.2.1-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
<img src="https://img.shields.io/badge/IntelliJ%20IDEA-000000.svg?&style=for-the-badge&logo=IntelliJ%20IDEA&logoColor=white">
</div>

#### RELEASE

<div>
<img src="https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white">
<img src="https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white">
</div>

#### DATABASE

<div>
<img src="https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white">
</div>

### 📌 DATABASE ERD

![ERD](https://github.com/Icarly2701/newsmtock/assets/101169766/4abd65d7-906a-426f-82ba-543080b81cf2)

### 📌 WBS

| 구분          | 활동                                      | 작업일              | 메모                                                                                                |
| ------------- | ----------------------------------------- | ------------------- | --------------------------------------------------------------------------------------------------- |
| 기획          | 주식관련된 뉴스 정보 제공                 |
| 프로젝특 확정 | 24.01.04                                  | 특이사항 없음       |
| 기획          | 기획서 문서 작성                          | 24.01.04            | 특이사항 없음                                                                                       |
| 개발          | 도메인 및 테이블 작성                     | 24.01.05            | 일대다, 다대다 같은 연관관계 정해야함, 실제 ERD 작성하면서 다시 수정                                |
| 개발          | ERD 설계                                  | 24.01.08            | 특이사항 없음, 변경사항 생길 시 기록 후 수정예정                                                    |
| 개발          | 프로젝트 환경설정 및 도메인 설정          | 24.01.09 ~ 24.01.10 | 1. 환경설정 시 들여쓰기 확인 2. JPA와 데이터베이스는 다른 부분 확인                                 |
| 개발          | 프로젝트 UI 작성, 피그마 작성             | 24.01.11 ~ 24.01.15 | 퍼블리싱 작업 필요                                                                                  |
| 개발          | 로그인, 회원가입 UI 퍼블리싱              | 24.01.16 ~ 24.01.17 | 로그인, 회원가입 버튼 수정 필                                                                       |
| 개발          | 메인 페이지, 마이 페이지 퍼블리싱         | 24.01.18~ 24.01.17  |                                                                                                     |
| 개발          | 상세 페이지, 글 쓰기 페이지 퍼블리싱      | 24.01.19~ 24.01.20  |                                                                                                     |
| 개발          | 검색 페이지, 상세 페이지 퍼블리싱         | 24.01.21~ 24.01.22  |                                                                                                     |
| 개발          | 네이버 뉴스 api 사용 및 타임리프 적용     | 24.01.23~ 24.01.26  |                                                                                                     |
| 개발          | 뉴스 페이지                               | 24.01.29~ 24.02.02  | 좋아요, 싫어요 기능 구체적으로, 테이블로 표시하는 부분 리팩토링, 테이블에 뉴스 표시 시 랜덤 성 부과 |
| 개발          | 유저 페이지, 및 프로젝트 디자인 전면 수정 | 24.02.03~ 24.02.08  | 전면 디자인 수정 및 유저페이지(마이페이지) 작성, 관심어 설정 구현                                   |
| 개발          | 게시판 페이지                             | 24.02.08~ 24.02.16  | 기본적인 crud 및 이미지 저장 시 로컬에 저장 (s3버킷으로 저장)                                       |
| 배포          | 프로젝트 Docker와 AWS를 활용하여 배포     | 24.02.19~ 24.02.21  | http://newstock.site <= 위 사이트로 배포                                                            |

## :post_office: UI 및 작업 내역

### 로그인 및 회원가입

##### 로그인

![로그인 페이지](https://github.com/Icarly2701/newsmtock/assets/101169766/11046e33-0923-49c9-a919-10e037c139bf)

##### 회원가입

![회원가입 페이지](https://github.com/Icarly2701/newsmtock/assets/101169766/7d8febb3-0158-4e93-9759-7e313bbde316)

### 메인 페이지

##### 로그인 이전

![메인 페이지 (로그인 전)](https://github.com/Icarly2701/newsmtock/assets/101169766/757a9081-4c25-48f3-9d14-9be1ee6e6687)

##### 로그인 이후

![메인 페이지 (로그인 후)](https://github.com/Icarly2701/newsmtock/assets/101169766/10ab636c-83e6-4af2-8da2-0955a82646f4)

### 경제 뉴스 페이지

##### 경제 뉴스 페이지

![경제 뉴스 페이지](https://github.com/Icarly2701/newsmtock/assets/101169766/dd9e879c-5b33-4145-8430-ddac41dd303c)

##### 경제 뉴스 상세 페이지

![뉴스 상세 페이지(비로그인)](https://github.com/Icarly2701/newsmtock/assets/101169766/e098a3d1-9293-41f4-bb3e-bb14b83c5ce0)

### 게시판 페이지

##### 게시판 페이지

![게시판 페이지](https://github.com/Icarly2701/newsmtock/assets/101169766/e656b9e8-0554-4f70-8d53-22a092825cdf)

##### 게시판 상세 페이지 1

![게시판 상세 페이지 1](https://github.com/Icarly2701/newsmtock/assets/101169766/6b46696b-bf13-41c9-81d0-dbd47ed7164a)

##### 게시판 상세 페이지 2

![게시판 상세 페이지 2](https://github.com/Icarly2701/newsmtock/assets/101169766/e08942f7-830d-4ff9-9bf2-75a9d9b58afa)

### 게시글 생성 및 수정

##### 게시글 작성 페이지

![글 작성 페이지](https://github.com/Icarly2701/newsmtock/assets/101169766/5e107648-5eb6-4d0d-b3b9-788665f74057)

##### 게시글 수정 페이지

![글 수정 페이지](https://github.com/Icarly2701/newsmtock/assets/101169766/0420df7e-f25b-498e-8878-6c48d2c7ade0)

### 마이페이지 및 검색 페이지

##### 마이 페이지

![마이페이지](https://github.com/Icarly2701/newsmtock/assets/101169766/be92f5cd-64bb-4a5f-b3ce-f2b613b0c33e)

##### 검색 페이지

![뉴스 검색 페이지](https://github.com/Icarly2701/newsmtock/assets/101169766/444a0685-3366-4480-989c-037ec1947c1b)
