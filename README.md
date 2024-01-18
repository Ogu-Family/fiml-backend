# Funding Is My Life

> Tumblbug 클론 코딩 프로젝트

## 프로젝트 소개

크라우드 펀딩 플랫폼인 텀블벅을 클론 코딩한 프로젝트입니다.

## 프로젝트 목적

- 텀블벅 기능을 구현하기 위한 도메인 분석 및 설계
- 프로젝트 생성부터 후원, 그리고 정산까지 프로젝트의 전체 흐름을 이해하고 구현
- Spring Boot, JPA, QueryDSL, MySQL, Gradle 등을 활용한 기본적인 백엔드 기술 스택 학습
- Git을 통한 형상 관리 및 GitHub Project를 활용한 이슈 관리 및 협업
- 테스트 코드(단위 테스트, 통합 테스트) 작성을 통한 신뢰성 높은 코드 작성

### 텀블벅 주요 기능 및 구현 기능 목록

텀블벅에서 제공하는 모든 기능이 아닌 주요 기능을 분석하여 이번 프로젝트에서 구현할 기능을 아래와 같이 선정하였습니다.

- 회원가입 및 로그인 인증
- 후원 프로젝트 생성 및 수정
- 후원 프로젝트에 대한 리워드 생성 및 수정
- 프로젝트 상세 및 리스트 조회
- 프로젝트 공지사항 및 댓글 작성
- 프로젝트 후원
- 후원 기간이 종료된 프로젝트에 대한 결제
- 결제가 완료된 프로젝트에 대한 창작자에게 정산
- 창작자 팔로우 및 프로젝트 좋아요

### 텀블벅 후원의 흐름

1. 창작자가 구상한 프로젝트를 소개하는 프로젝트 페이지를 작성하여 창작물 제작에 필요한 예산을 모금
2. 펀딩에 성공하면 약속한 프로젝트를 진행하고 후원자에게 선물을 전달
3. 후원자는 취지에 공감하는 프로젝트에 후원하고, 펀딩이 성공하면 창작자가 약속한 선물을 받아봄
4. 창작자는 모금한 금액을 활용하여 후원자와 약속한 프로젝트를 진행하고 선물을 전달할 수 있도록 최선을 다해줘야 함

## 프로젝트 핵심 기능

### 프로젝트 생명주기

1. 작성 중
    - 프로젝트 최초 생성 및 데이터 업데이트
    - 펀딩에 필요한 정보를 단계 별로 나누어 데이터 업데이트
    - ‘작성 중’ 상태로 프로젝트 테이블에 등록
    - ‘작성 중’인 상태의 프로젝트는 리스트와 상세 조회가 되지 않도록 설정
2. 준비 중
    - 모든 값들이 올바르게 생성되었는지 유효성 검사 수행
    - 프로젝트 최종 업로드하여 ‘준비 중’ 상태로 업데이트
    - 프로젝트 리스트에 노출 되어 다른 유저가 조회 가능한 상태로 됨
3. 진행 중
    - 프로젝트 후원 시작 상태
    - 펀딩 시작 시간이 되면 ‘진행 중’ 상태로 업데이트
    - 유저가 실제로 후원 및 후원 취소를 할 수 있는 상태
4. 펀딩 성공 / 펀딩 실패: 펀딩 종료 시점에 후원 금액에 따라 성공/실패로 변경
    - 펀딩 종료 시간이 되면 금액에 따라 상태 변경
    - 더 이상 후원 할 수 없는 상태
5. 정산 완료: 성공한 후원 중 모든 후원이 결제 된 상태
6. 취소: 프로젝트 중단 혹은 삭제 된 상태

### 후원 - 결제 - 정산 플로우

#### 텀블벅의 기능

- 후원 신청 시 결제 정보 등록, 펀딩 종료 전까지 후원 변경/취소 가능
- 프로젝트의 목표 금액을 달성하면 펀딩 종료일 다음 날부터 7일 동안 오후 2시에 24시간 간격으로 결제가 진행
- 모금액 정산 입금은 프로젝트의 결제 종료일로부터 은행 영업일 기준 7일 후(공휴일 및 주말 제외)에 진행

#### 구현한 기능

- 후원 신청 시 펀딩 종료일 다음 날을 결제 요청일로 하는 결제 정보 생성, 펀딩 종료 전까지 후원 변경/취소 가능
- 매일 오후 2시에 스프링 스케줄러를 통해 결제가 진행되어야 하는 결제 정보 조회 및 결제 시도
    - 결제 실패 시나리오 1: 7일 동안 결제를 실패한 경우 후원 상태를 결제 실패로 변경
    - 결제 실패 시나리오 2: 다음 날을 결제 요청일로 하는 새로운 결제 정보 생성
- 매일 오전 9시에 스프링 스케줄러를 통해 정산이 진행되어야 하는 프로젝트 조회 및 정산 정보 생성  
  → 실제 결제 API를 붙이는 대신 회원이 미리 돈을 충전해놓았다는 가정 하에 스프링 스케줄러로 정해진 날짜 및 시간에 결제/정산 시도하는 방식으로 구현

### 회원

- JWT 활용한 회원 인증 및 식별
- `@AuthenticationPrincipal` 어노테이션을 통해 로그인한 회원 정보를 가져올 수 있도록 구현

## ERD

![image](https://github.com/Ogu-Family/fiml-backend/assets/113650170/6141dde8-a6bc-4714-9d9f-2eca6cbd90fe)

## 팀원 소개

|  Name   |             [권효승](https://github.com/hyoguoo)              |             [박유진](https://github.com/eugene225)              |             [이유정](https://github.com/letskuku)              |
|:-------:|:----------------------------------------------------------:|:------------------------------------------------------------:|:-----------------------------------------------------------:|
| Profile | <img width="100px" src="https://github.com/hyoguoo.png" /> | <img width="100px" src="https://github.com/eugene225.png" /> | <img width="100px" src="https://github.com/letskuku.png" /> |

## 기술 스택

<img src="https://img.shields.io/badge/Java 17-008FC7?style=flat-square&logo=Java&logoColor=white"></img>
<img src="https://img.shields.io/badge/JUnit5-25A162?style=flat-square&logo=JUnit5&logoColor=white"></img>

<img src="https://img.shields.io/badge/Spring 6.1.1-58CC02?style=flat-square&logo=Spring&logoColor=white"/></img>
<img src="https://img.shields.io/badge/Spring Boot 3.2.0-6DB33F?style=flat-square&logo=Spring Boot&logoColor=white"/></img>
<img src="https://img.shields.io/badge/Spring Data JPA-ECD53F?style=flat-square&logo=JPA&logoColor=white"/></img>
<img src="https://img.shields.io/badge/Query DSL-669DF6?style=flat-square&logo=JPA&logoColor=white"/></img>

<img src="https://img.shields.io/badge/MySQL 8.0-4479A1?style=flat-square&logo=MySQL&logoColor=white"></img>
<img src="https://img.shields.io/badge/Gradle-02303A?style=flat-square&logo=Gradle&logoColor=white"></img>

<br>

## 협업 도구

<img src="https://img.shields.io/badge/Git-F05032.svg?style=flat-square&logo=Git&logoColor=white"></img>
<img src="https://img.shields.io/badge/GitHub Project-181717.svg?style=flat-square&logo=GitHub&logoColor=white"></img>
<img src="https://img.shields.io/badge/Notion-000000?style=flat-square&logo=Notion&logoColor=white"></img>
<img src="https://img.shields.io/badge/Slack-4A154B?style=flat-square&logo=Slack&logoColor=white"></img>
<img src="https://img.shields.io/badge/Swagger-85EA2D?style=flat-square&logo=Swagger&logoColor=white"></img>
