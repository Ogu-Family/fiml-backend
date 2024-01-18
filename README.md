# Funding Is My Life

> Tumblbug 클론 코딩 프로젝트

## 프로젝트 소개

크라우드 펀딩 플랫폼인 텀블벅을 클론 코딩한 프로젝트입니다.

### 프로젝트 목적

- 텀블벅 기능을 구현하기 위한 도메인 분석 및 설계
- 프로젝트 생성부터 후원, 그리고 정산까지 프로젝트의 전체 흐름을 이해하고 구현
- Spring Boot, JPA, QueryDSL, MySQL, Gradle 등을 활용한 기본적인 백엔드 기술 스택 학습
- Git을 통한 형상 관리 및 GitHub Project를 활용한 이슈 관리 및 협업
- 테스트 코드(단위 테스트, 통합 테스트) 작성을 통한 신뢰성 높은 코드 작성

<br>

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

<br>

### 텀블벅 후원의 흐름

![image](https://github.com/Ogu-Family/fiml-backend/assets/113650170/ab181b92-b147-4341-962c-1826d3bcc513)

1. 프로젝트 생성
2. 프로젝트 업로드: 필요한 정보를 모두 입력 후 최종 업로드
3. 프로젝트 노출: 프로젝트가 노출되고 사용자들이 프로젝트를 확인 가능
4. 후원 시작: 후원 시작일이 되면 후원 가능 상태로 변경되면서 사용자들의 후원 시작
5. 후원 중: 후원 신청 시 결제 정보 생성, 펀딩 종료 전까지 후원 변경/취소 가능
6. 후원 종료: 펀딩 종료 시점에 후원 금액에 따라 성공/실패로 변경
7. 후원 종료 후 7일: 후원 종료일 다음 날부터 7일 동안 24시간 간격으로 결제 시도
8. 정산 완료: 결제 완료 금액 수수료 계산 후 창작자에게 정산

<br>

## 핵심 도메인 및 비즈니스 로직 설계

텀블벅에서 제공하는 기능을 구현하기 위해 핵심 도메인 및 비즈니스 로직을 아래와 같이 설계하였습니다.

### 프로젝트 상태 관리

![image](https://github.com/Ogu-Family/fiml-backend/assets/113650170/dd8a7f29-d291-4921-ac42-47b3be5ac9e7)

1. 작성 중: 펀딩에 필요한 정보를 입력받는 단계로, 일반 사용자는 프로젝트를 조회할 수 없는 상태
    - 펀딩에 필요한 정보를 단계 별로 나누어 여러 API를 통해 데이터 업데이트
2. 준비 중: 프로젝트가 최종 업로드 되어, 사용자가 프로젝트를 조회할 수 있는 상태
    - 모든 값들이 올바르게 생성되었는지 유효성 검사 수행하여 올바르지 않은 상태의 프로젝트가 업로드 되지 않도록 방지
3. 진행 중: 프로젝트 후원 시작 상태로, 사용자가 실제로 후원 및 후원 취소를 할 수 있는 상태
    - 스케줄러를 통해 펀딩 시작 시간이 되면 ‘진행 중’ 상태로 업데이트
4. 펀딩 성공 / 펀딩 실패: 펀딩 종료 시점에 후원 금액에 따라 성공/실패로 변경
    - 펀딩 종료 시간이 되면 금액에 따라 상태 변경
5. 정산 완료: 결제된 금액 창작자에게 정산
    - 스케줄러를 통해 펀딩 완료 된 프로젝트 중 정산이 완료되지 않은 프로젝트를 조회하여 정산 진행
6. 취소: 프로젝트 중단 혹은 삭제 된 상태

<br>

### 후원-결제-정산 플로우

![image](https://github.com/Ogu-Family/fiml-backend/assets/113650170/c23c2835-aac3-4a38-97b5-316a546a731e)

1. 후원 시작 및 종료
    - 후원 신청 시 펀딩 종료일 다음 날을 결제 요청일로 하는 결제 정보 생성
    - 펀딩 종료 전까지 후원 변경/취소 가능
2. 결제 시도
    - 스프링 스케줄러를 통해 결제가 진행되어야 하는 후원 조회 및 7일 동안 결제 시도
        - 결제 실패 시: 다음 날을 결제 요청일로 하는 새로운 결제 정보 생성
        - 7일 동안 결제를 실패한 경우: 후원 상태를 결제 실패로 변경하여 최종적으로 후원 실패 상태로 변경
3. 정산
    - 스프링 스케줄러를 통해 정산이 진행되어야 하는 프로젝트 조회 및 정산 정보 생성

** 실제 결제 API 대신 회원의 소지 금액 정보 컬럼을 추가하여 해당 금액만큼 차감하는 방식으로 구현하였습니다.

<br>

### 회원 및 인증

- JWT 활용한 회원 인증 및 식별
- `@AuthenticationPrincipal` 어노테이션을 통해 로그인한 회원 정보를 가져올 수 있도록 구현

<br>

## ERD

![image](https://github.com/Ogu-Family/fiml-backend/assets/113650170/6141dde8-a6bc-4714-9d9f-2eca6cbd90fe)

<br>

## 팀원 소개

|  Name   |             [권효승](https://github.com/hyoguoo)              |             [박유진](https://github.com/eugene225)              |             [이유정](https://github.com/letskuku)              |
|:-------:|:----------------------------------------------------------:|:------------------------------------------------------------:|:-----------------------------------------------------------:|
| Profile | <img width="100px" src="https://github.com/hyoguoo.png" /> | <img width="100px" src="https://github.com/eugene225.png" /> | <img width="100px" src="https://github.com/letskuku.png" /> |

<br>

## 기술 스택

<img src="https://img.shields.io/badge/Java 17-008FC7?style=for-the-badge&logo=Java&logoColor=white"></img>
<img src="https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=JUnit5&logoColor=white"></img>

<img src="https://img.shields.io/badge/Spring 6.1.1-58CC02?style=for-the-badge&logo=Spring&logoColor=white"/></img>
<img src="https://img.shields.io/badge/Spring Boot 3.2.0-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white"/></img>
<img src="https://img.shields.io/badge/Spring Data JPA-ECD53F?style=for-the-badge&logo=JPA&logoColor=white"/></img>
<img src="https://img.shields.io/badge/Query DSL-669DF6?style=for-the-badge&logo=JPA&logoColor=white"/></img>

<img src="https://img.shields.io/badge/MySQL 8.0-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"></img>
<img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=Gradle&logoColor=white"></img>

<br>

## 협업 도구

<img src="https://img.shields.io/badge/Git-F05032.svg?style=for-the-badge&logo=Git&logoColor=white"></img>
<img src="https://img.shields.io/badge/GitHub Project-181717.svg?style=for-the-badge&logo=GitHub&logoColor=white"></img>
<img src="https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=Notion&logoColor=white"></img>
<img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=Swagger&logoColor=white"></img>
