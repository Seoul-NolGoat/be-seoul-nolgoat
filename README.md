<div align="center">
<img src="https://github.com/user-attachments/assets/2d19977e-9167-4c4e-9506-41175f118a58" width="150">

# Seoul-NolGoat

<img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=white">
<img src="https://img.shields.io/badge/react-61DAFB?style=for-the-badge&logo=react&logoColor=white">
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">
<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">

서울시의 다양한 먹거리와 놀거리 조합을 추천해 주는 서비스입니다.<br>
식당, 카페, 노래방, 당구장, PC방 업종을 검색할 수 있으며 평점과 거리 기반으로 적절한 가게 조합을 추천받을 수 있습니다.<br>
또한 사용자가 직접 파티를 생성하고 다른 사용자들이 해당 파티에 참여하여 오프라인 모임을 가질 수 있는 기능도 제공합니다.<br><br>
</div>

<table>
  <tr>
    <th>로그인</th>
    <th>메인</th>
    <th>가게 검색</th>
  </tr>
  <tr>
    <th><img src="https://github.com/user-attachments/assets/035de2cf-5349-4a6e-8cb2-287673bf307e" width="250"></th>
    <th><img src="https://github.com/user-attachments/assets/7a611106-3b04-49e8-83af-202eab3d3ad3" width="250"></th>
    <th><img src="https://github.com/user-attachments/assets/854256a6-5fe8-487b-97d3-a4a2e82b85b3" width="250"></th>
  </tr>
  <tr>
    <th>가게 조합 검색</th>
    <th>파티 메인</th>
    <th>파티 생성</th>
  </tr>
  <tr>
    <th><img src="https://github.com/user-attachments/assets/e8b1a0a6-7b44-4db5-9b9e-60be824d620c" width="250"></th>
    <th><img src="https://github.com/user-attachments/assets/1d6aad7e-6b8f-44bc-b588-eca788e094e7" width="250"></th>
    <th><img src="https://github.com/user-attachments/assets/7085f176-28b5-462a-8c75-7840546b72b3" width="250"></th>

  </tr>
  <tr>
    <th>파티 상세</th>
    <th>즐겨찾기 한 가게</th>
    <th>내가 쓴 리뷰</th>
  </tr>
  <tr>
    <th><img src="https://github.com/user-attachments/assets/59e1bf2f-ba17-46b9-9cf6-6b9cd15d12b4" width="250"></th>
    <th><img src="https://github.com/user-attachments/assets/95edacca-552c-4aa8-8fa2-e4e46e45edef" width="250"></th>
    <th><img src="https://github.com/user-attachments/assets/5aaf00cc-eada-475b-8f6c-96cd5ccc6525" width="250"></th>
  </tr>
  <tr>
    <th>회원탈퇴</th>
  </tr>
  <tr>
    <th><img src="https://github.com/user-attachments/assets/5ea3845f-bfd5-43e7-a510-7429a0a3632d" width="250"></th>
  </tr>
</table>
<br>

<div>

## Architecture

### System Architecture

<img src="https://github.com/user-attachments/assets/0f219ccd-635d-4d60-9177-f696c086a92a" width="700"><br>

### Directory Structure

```
src/
├── main/
│ ├── java/wad/seoul_nolgoat/
│ │ ├── auth/                        # 인증 관련 (Security, JWT, OAuth2)
│ │ ├── config/                      # 설정 파일 (Security, Redis, Swagger 등)
│ │ ├── domain/                      # 엔티티 및 리포지토리
│ │ ├── exception/                   # 예외 관련 (커스텀 예외, 에러 코드, 전역 예외 처리 등) 
│ │ ├── filter/                      # 로깅 커스텀 필터
│ │ ├── service/                     # 비즈니스 로직
│ │ ├── util/mapper/                 # Entity <-> DTO 매핑
│ │ ├── web/                         # 컨트롤러 계층 (API)
│ │ └── SeoulNolgoatApplication.java # 메인 클래스
│
├── resources/
│ ├── static/images/                 # 이메일용 로고 이미지
│ ├── templates/                     # 이메일 템플릿 파일
│ ├── application.yml                # 메인 설정 파일 (다른 설정 파일들을 import)
│ ├── logback-dev.xml                # 개발용 로그 설정
│ └── logback-prod.xml               # 운영용 로그 설정
```

<br>

## ERD

<img src="https://github.com/user-attachments/assets/f548aed2-edd2-438a-94fd-130e65526d6e" width="700">
<br><br>

## Tech Stack

### Back-End

<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">
<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
<img src="https://img.shields.io/badge/hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white"><br>
<img src="https://img.shields.io/badge/springsecurity-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white">
<img src="https://img.shields.io/badge/postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white">
<img src="https://img.shields.io/badge/swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=white">

- `Java 17`과 `Spring Boot 3.x`로 백엔드 애플리케이션을 개발하고 REST API 서버를 구축했습니다.
    - `Java 17`은 텍스트 블록, record 클래스 등 새로운 기능들을 적극 활용해 코드 가독성을 높여보고자 선택했습니다.
    - `Spring Boot 3.x`는 Java EE 대신 Jakarta EE 기반으로 전환되었는데 Jakarta EE는 지속적으로 업그레이드되어 최신 기술을 반영할 수 있다는 이점이 있어 선택했습니다.
- 운영 및 개발 환경에서는 `MySQL`을, 테스트 환경에서는 `H2`를 사용했습니다.
- `Spring Data JPA(Hibernate)`로 ORM 기반 데이터 접근을 구현했고 `QueryDSL`로 동적 쿼리를 타입 안정성 있게 작성했습니다.
- `Spring Security`와 `JWT`를 활용해 인증 및 인가 기능을 구현했습니다.
- `Postman`으로 API 테스트를 진행했습니다.
- `Swagger`로 API 명세를 자동화하고 문서화했습니다.

<br>

### Infra

<img src="https://img.shields.io/badge/amazonec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white">
<img src="https://img.shields.io/badge/amazons3-569A31?style=for-the-badge&logo=amazons3&logoColor=white">
<img src="https://img.shields.io/badge/nginx-009639?style=for-the-badge&logo=nginx&logoColor=white">
<img src="https://img.shields.io/badge/githubactions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white"><br>
<img src="https://img.shields.io/badge/logstash-005571?style=for-the-badge&logo=logstash&logoColor=white">
<img src="https://img.shields.io/badge/elasticsearch-005571?style=for-the-badge&logo=elasticsearch&logoColor=white">
<img src="https://img.shields.io/badge/kibana-005571?style=for-the-badge&logo=kibana&logoColor=white"><br>

- `AWS EC2` 인스턴스(Ubuntu)에 백엔드 서버를 배포했습니다.
- `AWS S3`로 정적 리소스(사용자 프로필 이미지 및 리뷰 이미지)를 관리했습니다.
- `Nginx`로 리버스 프록시를 구성하고 도메인과 HTTPS를 적용했습니다.
- `GitHub Actions`를 활용해 작업 브랜치(dev)에 머지 시에는 컴파일 에러를 검증하고 배포 브랜치(main)에 머지 시에는 자동으로 배포되는 CI/CD 파이프라인을 구축했습니다.

<br>

### Version Control

<img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">

**Git Branch Strategy**

- `main`
    - 배포 브랜치
    - 직접 커밋 금지
    - dev 브랜치에서의 머지만 허용 (Create a merge commit)
- `dev`
    - 메인 개발 브랜치
    - 직접 커밋 금지
    - 하위 브랜치에서의 머지만 허용 (Rebase & merge)
    - 모든 개발 작업은 dev 브랜치를 기준으로 개별 브랜치를 생성하여 진행했습니다.

<br>

## Collaboration

<img src="https://img.shields.io/badge/notion-000000?style=for-the-badge&logo=notion&logoColor=white">
<img src="https://img.shields.io/badge/discord-5865F2?style=for-the-badge&logo=discord&logoColor=white"><br>

- `Notion`을 활용해 코드 컨벤션, 회의 내용 등 공동 작업 자료를 정리했습니다.
- `Discord`를 통해 온라인 회의를 진행했습니다.

</div>
