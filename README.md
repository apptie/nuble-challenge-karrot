<div align="center">
  <h1>numble-challenge-karrot</h1>

  <a href="https://www.numble.it/ed40631b-7efd-49e9-8b2e-87a98d0cc991">
    <img src="https://user-images.githubusercontent.com/57691173/178731805-132d30e2-3b0f-4453-acd3-f1cc1392d183.png" height="128px" alt="numble-karrot">
  </a>

  <p>당근마켓 MVP 클론 코딩</p>
</div>

<hr>

# 목차
- [팀원](#팀원)
- [프로젝트 목표](#프로젝트-목표)
- [개발 및 배포환경](#개발-및-배포환경)
- [주요 기능](#주요-기능)

<hr>

# 팀원

<table>
  <tr>
    <td align="center"><a href="https://github.com/apptie"><img src="https://avatars.githubusercontent.com/u/57691173?v=4" width="100px;" alt=""/><br /><sub><b>apptie</b></sub></a><br /><a>ALL</a></td>
  </tr>
</table>
<p>해당 프로젝트는 혼자 진행한 단독 프로젝트입니다.</p>

<hr>

# 프로젝트 목표
22년 1월 7일부터 1월 23일 자정까지 17일 동안 당근마켓 초기 MVP 클론 코딩한 프로젝트입니다.

<hr>

# 개발 및 배포환경

<div align="center">
<img width="90%" src="https://user-images.githubusercontent.com/57691173/178737361-03bfc6c0-399e-4426-9f06-9d13b49a2c23.png">
 </div>

- Back End
    - Spring Boot
    - AWS RDS
      - MySQL
    - AWS EC2
- Front End
    - Thymeleaf
    - HTML, CSS, Javascript
- Open API
    - vworld api
    - ipify

<hr>

# 주요 기능

- 회원
  - 회원 가입
  - 로그인
  - 회원 정보 조회 및 수정
  - 등록한 상품 확인
- 상품
  - 상품 등록
  - 상품 조회
  - 상품 수정
  - 좋아요
- 댓글
  - 댓글 작성
  - 댓글 조회
- 거래 상태 변경

<hr>

## 회원
### 회원 가입

<div align="center">
  <img width="45%" src="https://user-images.githubusercontent.com/57691173/178753381-d55770ba-636f-4ee5-91c1-b2c741fc9219.jpg">
  <br>
  <img width="45%" src="https://user-images.githubusercontent.com/57691173/178753240-0b34c871-0925-4ed4-883b-a92386fe2075.jpg">
  <img width="45%" src="https://user-images.githubusercontent.com/57691173/178753254-1783626d-59bb-4d9d-a965-6709b8cf60c7.jpg">
</div>
<br>

- 홈에서 회원 가입 버튼 선택
- 회원 가입을 위해 정보 입력
여기에 그 오류뜨는 사진넣기
- Validator를 통해 조건에 맞지 않은 정보가 들어올 시 에러 메세지 출력

<div align="center">
  <img width="45%" src="https://user-images.githubusercontent.com/57691173/178753256-ac2ba92e-ad22-477c-b55a-b9369dcb4af0.jpg">
  <img width="45%" src="https://user-images.githubusercontent.com/57691173/178753261-e8ec08c6-a765-4fc2-b555-9bee9a44d14b.jpg">
</div>

<br>

- 대기하는 동안 회원 가입 메일 전송
- 메일 전송 후 안내 메세지 출력

<br>
<div align="center">
  <img width="45%" src="https://user-images.githubusercontent.com/57691173/178753264-8bf1ac77-b529-4b46-b9d5-fcd25a61522f.jpg">
  <img width="45%" src="https://user-images.githubusercontent.com/57691173/178753268-77298b72-178f-4131-9623-dbe8863edc36.jpg">
</div>

- 메일로 전송된 인증 url을 통해 메일 인증 후 회원 가입 완료

### 로그인

<div align="center">
  <img width="45%" src="https://user-images.githubusercontent.com/57691173/178753272-a881f022-117f-404b-b9b5-e9ee7d83484f.jpg">
  <img width="45%" src="https://user-images.githubusercontent.com/57691173/178753274-26aa58ed-066f-4b02-a2e0-e4ae6067a2ab.jpg">
</div>

- 홈에서 로그인 버튼 클릭 시 이메일과 비밀번호를 입력할 수 있는 페이지로 이동
- 로그인 이후 상품 리스트를 확인할 수 있는 페이지로 리다이렉트
  - 자신의 아이디가 출력되는 것을 통해 로그인 유무 확인 가능

### 회원 정보 조회 및 수정

<div align="center">
  <img width="45%" src="https://user-images.githubusercontent.com/57691173/178753278-69182ba6-da8f-453d-9322-6f2980fd95a8.jpg">
  <br>
  <img width="31%" src="https://user-images.githubusercontent.com/57691173/178753282-cef1b627-dd47-440f-8a46-e72a52955e49.jpg">
  <img width="31%" src="https://user-images.githubusercontent.com/57691173/178753283-fff58f2f-8ad4-42b3-8fcc-cc47764949a2.jpg">
  <img width="31%" src="https://user-images.githubusercontent.com/57691173/178753286-14bcc3c8-77ae-4e00-94a1-b5dfd2f43b9c.jpg">
  <img width="45%" src="https://user-images.githubusercontent.com/57691173/178753289-8c562715-1132-4564-a235-f0cb1715f68b.jpg">
  <img width="45%" src="https://user-images.githubusercontent.com/57691173/178753294-72e51843-ce79-43ef-8b0a-7df24748ac9c.jpg">
</div>

- 하단의 나의 당근 버튼을 통해 회원의 정보 조회 가능
  - 프로필 수정을 통해 프로필 사진과 닉네임 변경 가능

### 등록한 상품 확인

<div align="center">
  <img width="31%" src="https://user-images.githubusercontent.com/57691173/178753333-8313a76f-2fbe-4038-831e-d09bce8abf87.jpg">
  <img width="31%" src="https://user-images.githubusercontent.com/57691173/178753334-7c5cdd3b-3c12-40b5-aa6f-d69c06647527.jpg">
  <img width="31%" src="https://user-images.githubusercontent.com/57691173/178753338-ba8e4361-3513-44f4-882d-8fed10437de4.jpg">
</div>

- 회원이 등록한 상품 확인 가능
  - 전체 / 판매중 / 거래완료(예약중, 거래완료) 필터링 가능

<hr>

## 상품

### 상품 등록

<div align="center">
  <img width="45%" src="https://user-images.githubusercontent.com/57691173/178753294-72e51843-ce79-43ef-8b0a-7df24748ac9c.jpg">
  <br>
  <img width="45%" src="https://user-images.githubusercontent.com/57691173/178753297-cefde3c5-5dc0-4a61-9366-556e36895c98.jpg">
  <br>
  <img width="45%" src="https://user-images.githubusercontent.com/57691173/178753300-d9e14717-d85b-423e-b1ca-11f38216cb6f.jpg">
  <img width="45%" src="https://user-images.githubusercontent.com/57691173/178753304-863707ba-26c0-420a-a7bf-9c99230fb41b.jpg">
  <img width="45%" src="https://user-images.githubusercontent.com/57691173/178753309-53641b47-606e-4ac3-88f6-94b12484bd63.jpg">
  <img width="45%" src="https://user-images.githubusercontent.com/57691173/178753311-d3d674d1-813c-4c59-8cc6-9988dc4e4425.jpg">
  <img width="45%" src="https://user-images.githubusercontent.com/57691173/178753313-daf0ac8b-976b-49c3-b357-9de9695ad917.jpg">
  <img width="45%" src="https://user-images.githubusercontent.com/57691173/178753315-4aaf5860-6591-48d1-8eeb-2ec0c4cabf0a.jpg">
</div>

- 하단의 주황색 플러스 버튼을 통해 상품 등록 가능
- 상품 등록 시 이미지 버튼을 통해 사진 등록 가능
- 등록한 사진 클릭 시 삭제 가능
- 카테고리 선택 가능

### 상품 조회

<div align="center">
  <img width="45%" src="https://user-images.githubusercontent.com/57691173/178753329-581f7c32-ef47-4c42-b65e-10f4d3f30196.jpg">
</div>

- 로그인 이후 자신이 등록한 상품을 제외한 상품 리스트 조회 가능
  - 각 상품에 대한 제목, 거래 위치(판매자 지역), 댓글, 좋아요, 상태(거래 완료) 확인 가능

<div align="center">
  <img width="45%" src="https://user-images.githubusercontent.com/57691173/178753313-daf0ac8b-976b-49c3-b357-9de9695ad917.jpg">
  <img width="45%" src="https://user-images.githubusercontent.com/57691173/178753315-4aaf5860-6591-48d1-8eeb-2ec0c4cabf0a.jpg">
</div>

- 등록된 상품 상세보기에서 이미지 하단의 흰색 동그라미 클릭 시 등록한 사진 조회 가능
- 제목, 가격, 본문 확인 가능 

<div align="center">
  <img width="45%" src="https://user-images.githubusercontent.com/57691173/178753330-3a1647f9-7c72-44a8-8aac-3c799cdc2614.jpg">
</div>

- 상품을 등록한 판매자의 다른 상품 확인 가능

### 상품 수정

<div align="center">
  <img width="45%" src="https://user-images.githubusercontent.com/57691173/178753321-6ac4b1d9-c151-4bd8-8702-7109e9d570f4.jpg">
  <img width="45%" src="https://user-images.githubusercontent.com/57691173/178753323-ecad70e1-0bf2-4785-8295-afec267b7bc6.jpg">
</div>

- 우측 상단의 버튼을 클릭 시 게시글 수정 가능 

<div align="center">
  <img width="45%" src="https://user-images.githubusercontent.com/57691173/178753328-393c7ed1-e02d-4143-ad4f-baa523c7b586.jpg">
</div>

- 상품의 상태(판매중 / 예약중 / 거래완료) 변경 가능

<div align="center">
  <img width="31%" src="https://user-images.githubusercontent.com/57691173/178753355-70f48b10-81a4-4b4e-bf13-d04f56e9072e.jpg">
  <img width="31%" src="https://user-images.githubusercontent.com/57691173/178753357-7ec636c4-c242-440a-bcc5-b94ca82a5e5f.jpg">
  <img width="31%" src="https://user-images.githubusercontent.com/57691173/178753361-36113f87-8bb6-4cfe-8706-23aeeac82311.jpg">
</div>

- 판매 내역에서도 상품의 상태 변경 가능

### 좋아요 등록

<div align="center">
  <img width="45%" src="https://user-images.githubusercontent.com/57691173/178753342-9faeb5cd-08f8-45c0-a483-be71a6548f80.jpg">
  <img width="45%" src="https://user-images.githubusercontent.com/57691173/178753353-c29412e0-e696-412f-a7b1-bcf026f580af.jpg">
</div>

- 상품에서 하트 버튼을 통해 좋아요 추가 가능
  - 좋아요 했다면 빨간 하트, 좋아요 하지 않았다면 빈 하트로 표시
  - 좋아요 한 상품은 관심목록에서 확인 가능

### 좋아요 삭제

<div align="center">
  <img width="45%" src="https://user-images.githubusercontent.com/57691173/178753373-fa29007f-1714-47dc-b7e0-8bfa6a8fafb7.jpg">
  <img width="45%" src="https://user-images.githubusercontent.com/57691173/178753378-21e3963b-d3d0-4d63-9f30-adbec9c62283.jpg">
</div>

- 상품에서 하트 버튼을 통해 좋아요 삭제 가능
  - 좋아요 삭제한 상품은 관심목록에서 제외

## 댓글

### 댓글 작성

<div align="center">
  <img width="45%" src="https://user-images.githubusercontent.com/57691173/178753342-9faeb5cd-08f8-45c0-a483-be71a6548f80.jpg">
  <img width="45%" src="https://user-images.githubusercontent.com/57691173/178753343-3e587fad-cece-457f-89b8-63675df1f089.jpg">
</div>

- 하단의 댓글 쓰기를 통해 댓글 작성 가능

### 댓글 조회

<div align="center">
  <img width="45%" src="https://user-images.githubusercontent.com/57691173/178753342-9faeb5cd-08f8-45c0-a483-be71a6548f80.jpg">
  <img width="45%" src="https://user-images.githubusercontent.com/57691173/178753346-78a6abe4-6181-4385-8f95-c5fbf9db7fcd.jpg">
</div>

- 하단의 댓글 보기를 통해 댓글 조회 가능

### 댓글 수정

<div align="center">
  <img width="45%" src="https://user-images.githubusercontent.com/57691173/178753346-78a6abe4-6181-4385-8f95-c5fbf9db7fcd.jpg">
  <img width="45%" src="https://user-images.githubusercontent.com/57691173/178753350-6b82bd70-ecda-4fbc-ac93-040953707fe7.jpg">
</div>

- 댓글의 우측 하단의 edit 버튼을 통해 댓글 수정 가능