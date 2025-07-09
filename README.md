# Homework 2

## 프로젝트 개요

Spring Boot 기반의 마이크로서비스 아키텍처를 사용한 멀티모듈 프로젝트입니다. 
회원 관리와 결제 시스템을 분리된 서비스로 구현하여 확장성과 유지보수성을 향상시켰습니다.

## 아키텍처

```
homework-ips/
├── service/
│   ├── member-service/     # 회원 관리 서비스 (JPA + Redis)
│   └── payment-service/    # 결제 관리 서비스 (R2DBC + Event)
├── docker-compose.yaml     # 전체 시스템 오케스트레이션
└── README.md
```
### 회원 관리 서비스
- 조회 수 증가 및 조회 기능(redis) 사용 이유
> - 조회 수 증가 기능은 짧은 시간에 빈번하게 호출되는 기능이 될 수 있어
> Redis에 캐시/증분 연산을 위임함으로써 DB는 적은 I/O만 처리할 수 있어 선택했습니다.

> - 관계형 데이터베이스에 직접 쓰게 되면, Lock 경합, I/O 병목이 발생할 수 있기 때문에
> Redis 를 통해 조회 수 처리를 진행했습니다.

> - 데이터 정합성을 위해 일정 조회수가 넘으면 주기적 백업까지 구현했습니다.

- 페이징 및 정렬 기능
> - 인덱스 같은 경우 정렬 기준에 따라서 처리했습니다. covering index(name, registered_at) 인덱스를 생성하였습니다.

> - **count query** 최적화 같은 경우 1 ~ 10 페이지가 만약 10개라고 한다면 101개의 쿼리만 조회하면 됩니다.
> 99 개라면 다음이라는 버튼이 생기지 않아도 되고, 101개라면 다음이라는 버튼이 활성화 되면 되기 때문에 101개의 쿼리만 조회하도록 
> 식으로 최적화를 진행했습니다.

>- 조회 수 증가 및 전체 조회 시 **Redis**를 사용하기 때문에 정렬(name, registered_at) 를 기존 관계형 데이터베이스에서 처리하는 것이 아닌
> **zset ranking**을 통해 사용자 생성될때 0으로 초기화하고 **rua script**를 활용하여 배치적으로 들고와서 정렬을 진행하도록 처리했습니다.


### 결제 서비스
- 결제 서비스에 리액티브 프로그래밍을 적용한 이유
> 외부 API와 통신이 핵심인 기능인 만큼 대부분 I/O 중심의 작업 기능이라고 생각합니다.
> 또한 동시에 결제 요청이 몰릴 수 있기 때문에 Non-blocking 방식으로 고려했습니다.
> 포인트 결제는 결제 완료 후 이벤트 기반으로 포인트 적립하는 식으로 확장성을 고려하여 느슨하게 처리했습니다.


## 기술 스택

## 서비스 구성

### Member Service (포트: 8080)
- **기술**: Spring MVC + JPA + Redis
- **기능**:
  - 회원 조회수 캐싱 (Redis)
  - 회원 조회 시 페이징 및 정렬 지원
- **초기화**: 50개의 샘플 회원 데이터 자동 생성

### Payment Service (포트: 8081)
- **기술**: Spring WebFlux + R2DBC + Event-driven
- **기능**:
  - 리액티브 결제 처리
  - 토스페이먼츠 API 연동
  - 이벤트 기반 포인트 적립
  - 결제 이력 관리
- **초기화**: 결제 및 포인트 테이블 자동 생성

## 데이터베이스 스키마

### Member Service
```sql
-- 회원 테이블
CREATE TABLE members (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    view_count BIGINT DEFAULT 0,
    registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### Payment Service
```sql
-- 결제 이벤트 테이블
create table payment_events
(
  id             bigint auto_increment primary key,
  buyer_id       bigint not null,
  order_id       varchar(255) not null,
  order_name     varchar(255) not null,
  payment_key    varchar(255) null,
  payment_type   enum ('NORMAL') null,
  payment_status enum ('NOT_STARTED','EXECUTING', 'STARTED', 'SUCCESS', 'FAILURE', 'UNKNOWN') default 'NOT_STARTED' null,
  amount         decimal(12, 2) not null,
  approved_at    timestamp null,
  payment_method enum ('EASY_PAY') null,
  psp_raw_data   json null,
  failed_count   bigint default 0 not null,
  retryable_count bigint default 0 not null,
  created_at     timestamp default CURRENT_TIMESTAMP null,
  updated_at     timestamp null on update CURRENT_TIMESTAMP
);

-- 결제 이력 테이블
CREATE TABLE payment_histories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    payment_order_id BIGINT NOT NULL,
    previous_status VARCHAR(50),
    new_status VARCHAR(50),
    reason VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
);

-- 포인트 이벤트 테이블
CREATE TABLE point_events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id VARCHAR(255) NOT NULL,
    buyer_id BIGINT NOT NULL,
    points BIGINT NOT NULL,
    point_type VARCHAR(50) DEFAULT 'REWARD',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```
## Application 실행 가이드

### 1. 사전 요구사항
- Docker Desktop 설치
- Java 21 설치 (로컬 개발 시)

### 2. 전체 시스템 실행
```bash
# 로컬 빌드 (첫 실행 시)
./gradlew clean build

# Docker Compose로 전체 시스템 실행
docker compose up --build

# 백그라운드 실행
docker compose up -d --build
```

### 3. 서비스 접근
- **Member Service**: http://localhost:8080
- **Payment Service**: http://localhost:8081
- **MySQL**: localhost:3306 (root/root_password)
- **Redis**: localhost:6379

### 4. 개별 서비스 실행 (로컬 개발)
```bash
# Member Service
./gradlew :service:member-service:bootRun

# Payment Service  
./gradlew :service:payment-service:bootRun
```

## API 명세서

### Member Service API (http://localhost:8080)

#### 1. 회원 목록 조회 (페이징)
```http
GET /api/v1/members
```

**Query Parameters:**

| Parameter   | Type   | Default         | Required | Description                                             |
|-------------|--------|------------------|----------|---------------------------------------------------------|
| `sortType`  | String | `REGISTERED_AT` | No       | 정렬 기준 (`NAME`, `VIEW_COUNT`, `REGISTERED_AT`)      |
| `direction` | String | `ASC`           | No       | 정렬 방향 (`ASC`, `DESC`)                              |
| `page`      | Long   | `1`             | No       | 페이지 번호 (1부터 시작)                                |
| `pageSize`  | Long   | `10`            | No       | 페이지당 아이템 수                                      |


**Response Example:**
```json
{
  "status": "OK",
  "message": "Members retrieved successfully",
  "data": {
    "members":[{"id":1,"name":"이영희","viewCount":0,"registeredAt":"2025-07-09T01:23:07"},{"id":2,"name":"박철수","viewCount":0,"registeredAt":"2025-07-09T01:23:07"},{"id":3,"name":"최지은","viewCount":0,"registeredAt":"2025-07-09T01:23:07"},{"id":4,"name":"정민호","viewCount":0,"registeredAt":"2025-07-09T01:23:07"},{"id":5,"name":"강수연","viewCount":0,"registeredAt":"2025-07-09T01:23:07"},{"id":6,"name":"윤준서","viewCount":0,"registeredAt":"2025-07-09T01:23:07"},{"id":7,"name":"임서영","viewCount":0,"registeredAt":"2025-07-09T01:23:07"},{"id":8,"name":"한도현","viewCount":0,"registeredAt":"2025-07-09T01:23:07"},{"id":9,"name":"조미래","viewCount":0,"registeredAt":"2025-07-09T01:23:07"},{"id":10,"name":"신우진","viewCount":0,"registeredAt":"2025-07-09T01:23:07"}],
    "memberCount":49
  }
}
```

**cURL Example:**
```bash
# 기본 조회
curl -X GET "http://localhost:8080/api/v1/members"

# 정렬 옵션 포함
curl -X GET "http://localhost:8080/api/v1/members?sortType=VIEW_COUNT&direction=DESC&page=1&pageSize=20"
```

#### 2. 회원 조회수 증가
```http
POST /api/v1/members/{memberId}/view-count
```

**Path Parameters:**
```http
{memberId} - 회원 ID (예: 1)
```

**Response Example:**
```json
{
  "status": "OK",
  "message": "View count updated successfully",
  "data": 6
}
```

**cURL Example:**
```bash
curl -X POST "http://localhost:8080/api/v1/members/1/view-count"
```

### Payment Service API (http://localhost:8081)

#### 1. 결제 페이지 (Checkout)
```http
GET localhost:8081/
```

**Response:** HTML 결제 페이지 (Thymeleaf 템플릿)

#### 2. 토스 결제 확인
```http
POST /api/v1/toss/confirm
Content-Type: application/json
```

**Response Example:**
```json
{
  "status": "OK",
  "message": "OK",
  "data": {
    "paymentKey" : "payment_key_from_toss",
    "orderId" : "order_123456",
    "amount" : 10000
  }
}
```

### 로그 확인
```bash
# 전체 서비스 로그
docker-compose logs -f

# 특정 서비스 로그
docker-compose logs -f member-service
docker-compose logs -f payment-service
```