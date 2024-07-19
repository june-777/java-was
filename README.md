# Java WAS

2024 우아한 테크캠프 프로젝트 WAS
### Configuration
- java JDK 17
- H2
- AssertJ
- AWS

## 기능 목록
### HTTP 요청 파싱
- HTTP 요청을 `RequestLine`, `Header`, `Body`로 구분하여 파싱하는 기능
- CRLF / CR / LF 모든 요청을 파싱할 수 있도록 리팩터링
- 멀티 파트 폼 요청 및 이미지 이진 데이터를 파싱하는 기능

### 쿠키 / 세션 기반 인증 필터

### 회원 정보
- User 회원을 저장(Insert)하는 기능
- User 회원을 PK로 조회(Select)하는 기능
- User 회원을 모두 조회(SelectAll)하는 기능
- User 회원을 PK로 삭제(Delete)하는 기능

### 게시글 작성
- Article 게시글을 저장(Insert)하는 기능
- Article 게시글을 PK로 조회(Select)하는 기능
- Article 게시글을 모두 조회(SelectAll)하는 기능
- Article 게시글을 PK로 삭제(Delete)하는 기능

### 이미지 업로드
- 게시글 작성 시 이미지를 첨부하는 기능
- 이미지를 서버에 저장(write)하는 기능
- 서버의 이미지를 읽는(read) 기능
- 업로드한 이미지를 게시글 조회에서 보여주는 기능

### 댓글 작성
- Comment 댓글을 저장(Insert)하는 기능	
- Comment 댓글을 게시글 ID로 조회(Select)하는 기능	
- 댓글 작성시 게시글의 ID를 query string에 포함하여 POST로 요청하는 기능

### 예외 핸들링
- 예외를 공통 처리하는 글로벌 예외 핸들러 기능	
- 로그인하지 않은 사용자가 글쓰기 버튼을 클릭하면 로그인 페이지로 리다이렉트
- 로그인하지 않은 사용자가 댓글 작성 버튼을 클릭하면 로그인 페이지로 리다이렉트	
- HTTP Request 파싱 오류 → 400 페이지 응답	
- 정적 파일을 찾지 못한 경우 → 404 페이지 응답	
- 허용하지 않는 메서드 → 405 페이지 응답	
- 서버 에러 → 500 페이지 응답	

## 결과 화면

회원가입 폼

<img width="500" alt="image" src="https://github.com/woowa-techcamp-2024/java-was/assets/68291395/0d077202-6fb6-47de-8051-298767655347">

회원가입

<img width="1214" alt="image" src="https://github.com/woowa-techcamp-2024/java-was/assets/68291395/dd1aefb5-8117-4cf0-bb54-d4d0f4fcfb7a">

잘못된 요청
<img width="436" alt="image" src="https://github.com/woowa-techcamp-2024/java-was/assets/68291395/d7c8970c-5e6c-4144-a47c-a3349e67e738">

## 설계

![image](https://github.com/woowa-techcamp-2024/java-was/assets/68291395/b0774a0c-37aa-4c9a-92ec-7422d89bf863)

