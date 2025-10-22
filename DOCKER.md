# Docker로 Home Inventory System 실행하기

이 문서는 Docker Compose를 사용하여 Home Inventory System을 로컬 환경에서 실행하는 방법을 설명합니다.

## 사전 요구사항

- Docker Desktop 또는 Docker Engine 설치 필요
- Docker Compose V2 이상

### Docker 설치 확인

```bash
docker --version
docker compose version
```

## 프로젝트 구조

```
homeInventorySystem/
├── docker-compose.yml          # Docker Compose 설정
├── Dockerfile                  # Spring Boot 애플리케이션 이미지
├── .dockerignore              # Docker 빌드 제외 파일
└── home-inventory-management/ # Spring Boot 프로젝트
```

## 실행 방법

### 1. 전체 시스템 시작 (PostgreSQL + Spring Boot App)

```bash
docker compose up -d
```

이 명령은 다음을 수행합니다:
- PostgreSQL 15 컨테이너 시작
- Spring Boot 애플리케이션 빌드 및 시작
- 자동으로 스키마 초기화

### 2. 로그 확인

```bash
# 전체 로그 보기
docker compose logs -f

# 특정 서비스 로그만 보기
docker compose logs -f app
docker compose logs -f postgres
```

### 3. 서비스 상태 확인

```bash
docker compose ps
```

### 4. 애플리케이션 접속

- **Spring Boot API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **PostgreSQL**: localhost:5432
  - Database: `homedb`
  - Username: `sa`
  - Password: `mycat123`

### 5. 시스템 종료

```bash
# 컨테이너 중지
docker compose stop

# 컨테이너 중지 및 제거
docker compose down

# 컨테이너, 볼륨, 네트워크 모두 제거
docker compose down -v
```

## 개발 모드

### 애플리케이션만 재빌드

코드를 수정한 후 애플리케이션만 다시 빌드하려면:

```bash
docker compose up -d --build app
```

### PostgreSQL만 실행

데이터베이스만 필요한 경우:

```bash
docker compose up -d postgres
```

그런 다음 로컬에서 Spring Boot 애플리케이션을 직접 실행:

```bash
cd home-inventory-management
./gradlew bootRun --args='--spring.profiles.active=develop'
```

## 트러블슈팅

### 포트 충돌

포트 5432나 8080이 이미 사용 중인 경우, `docker-compose.yml`에서 포트를 변경:

```yaml
services:
  postgres:
    ports:
      - "15432:5432"  # 외부 포트 변경
  app:
    ports:
      - "18080:8080"  # 외부 포트 변경
```

### 빌드 캐시 제거

빌드 문제가 발생한 경우, 캐시를 무시하고 처음부터 빌드:

```bash
docker compose build --no-cache
docker compose up -d
```

### 데이터베이스 초기화

데이터베이스를 완전히 초기화하려면:

```bash
docker compose down -v
docker compose up -d
```

### 컨테이너 내부 접속

디버깅이 필요한 경우:

```bash
# Spring Boot 컨테이너
docker compose exec app sh

# PostgreSQL 컨테이너
docker compose exec postgres psql -U sa -d homedb
```

## 환경 변수 커스터마이징

`.env` 파일을 생성하여 환경 변수를 오버라이드할 수 있습니다:

```bash
# .env 파일 생성
cat > .env << EOF
POSTGRES_DB=homedb
POSTGRES_USER=sa
POSTGRES_PASSWORD=mycat123
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=develop
EOF
```

그런 다음 `docker-compose.yml`에서 사용:

```yaml
services:
  postgres:
    environment:
      POSTGRES_DB: ${POSTGRES_DB}
      POSTGRES_USER: ${POSTGRES_USER}
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
```

## 프로덕션 배포

프로덕션 환경을 위한 별도의 compose 파일 사용:

```bash
# docker-compose.prod.yml 사용
docker compose -f docker-compose.yml -f docker-compose.prod.yml up -d
```

## 유용한 명령어

```bash
# 리소스 사용량 확인
docker compose stats

# 컨테이너 재시작
docker compose restart app

# 특정 컨테이너만 중지
docker compose stop app

# 모든 Docker 리소스 정리 (주의!)
docker system prune -a --volumes
```

## Health Check

애플리케이션의 헬스 체크 엔드포인트:

```bash
# 애플리케이션 health check
curl http://localhost:8080/actuator/health

# PostgreSQL health check
docker compose exec postgres pg_isready -U sa -d homedb
```

## 참고사항

- 첫 실행 시 Gradle 의존성 다운로드로 인해 빌드 시간이 오래 걸릴 수 있습니다
- PostgreSQL 데이터는 Docker volume에 저장되어 컨테이너 재시작 시에도 유지됩니다
- `docker compose down -v` 명령 사용 시 모든 데이터가 삭제되니 주의하세요
