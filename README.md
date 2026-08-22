## 실행 방법
- `./gradlew bootRun` 명령으로 9070 포트 실행
- `./gradlew test` 명령으로 JUnit 5 테스트 실행
- H2 인메모리 DB와 `ddl-auto: create-drop` 기반 스키마 재생성

## CSV 적재
- 애플리케이션 기동 시 저장소 루트 `books.csv` 자동 적재
- 기존 `books` 데이터 삭제 후 CSV 행 일괄 저장
- 헤더, 필수값, 날짜, 숫자 형식 검증

## DB 스키마 및 인덱스
- 단일 `books` 테이블 중심 도서 데이터 모델
- `title`, `author`, `publisher`, `category`, `published_date`, `isbn`, `price`, `stock` 컬럼 구성
- 도서명 검색용 `idx_books_title` 인덱스 구성

## 검색 및 Pagination
- 도서명 부분 일치 검색과 `ContainingIgnoreCase` Repository 메서드 활용
- `Pageable` 기반 페이지 번호, 페이지 크기, 전체 결과 수, 전체 페이지 수 제공
- 검색 조건 유지 기반 이전/다음 페이지 이동

## 설계 의도
- `Controller -> Service -> Repository` 계층 분리
- CSV 적재 로직과 검색 로직 분리
- 검색어 누락, 잘못된 페이지 파라미터, 결과 없음의 빈 화면 처리
