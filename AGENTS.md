# 저장소 지침

## 프로젝트 구조

이 프로젝트는 `pretask`라는 이름의 Gradle 기반 Java 21 Spring Boot 프로젝트입니다.

- `src/main/java/com/example/pretask/`에는 애플리케이션 소스 코드가 있습니다. 현재 진입점은 `PretaskApplication.java`입니다.
- `src/main/resources/`에는 `application.yaml`을 포함한 런타임 설정이 있습니다.
- `src/test/java/com/example/pretask/`에는 JUnit/Spring 테스트가 있습니다. 테스트 패키지명은 테스트 대상 소스 패키지와 맞춥니다.
- `books.csv`는 저장소 루트의 데이터 파일입니다. 형식 변경은 애플리케이션 동작에 영향을 주는 변경으로 보고, 관련 코드나 테스트에 가정을 문서화합니다.
- `gradle/wrapper/`, `gradlew`, `gradlew.bat`는 고정된 Gradle Wrapper를 제공합니다. 시스템 Gradle 대신 이 파일들을 사용합니다.

## 애플리케이션 구조

- Java + Spring Boot 기반으로 구현합니다.
- 화면은 Thymeleaf 기반 서버 사이드 렌더링(SSR)을 사용합니다.
- 데이터 모델은 단일 `books` 테이블을 사용합니다.
- 별도의 테이블 정규화는 고려하지 않습니다. 도서 관련 데이터는 필요한 경우에도 `books` 테이블 중심으로 설계합니다.

## 빌드, 테스트, 개발 명령

- `./gradlew bootRun`은 Spring Boot 애플리케이션을 로컬에서 실행합니다.
- `./gradlew test`는 JUnit Platform 테스트 스위트를 실행합니다.
- `./gradlew build`는 컴파일, 테스트, 패키징을 수행합니다.
- `./gradlew clean`은 생성된 빌드 결과물을 삭제합니다.

명령은 저장소 루트에서 실행합니다. Windows에서는 같은 작업에 `gradlew.bat`를 사용합니다.

## 코딩 스타일 및 명명 규칙

Java 21을 사용하고 기존 Spring Boot 스타일을 따릅니다. 소스 파일은 생성된 애플리케이션 클래스와 같이 탭 들여쓰기를 사용합니다.

프로젝트 네임스페이스를 의도적으로 변경하지 않는 한 패키지는 `com.example.pretask` 아래에 둡니다.

웹, 서비스, 영속성, 도메인 타입을 추가할 때는 `BookController`, `BookService`, `BookRepository`, `BookEntity`처럼 역할이 분명한 Spring 명명을 선호합니다.

Spring 설정은 `application.yaml`에 두고, 환경별 비밀값은 버전 관리에 포함하지 않습니다.

## 테스트 지침

테스트는 Gradle의 `useJUnitPlatform()` 설정과 Spring Boot 테스트 의존성을 통해 JUnit 5를 사용합니다.

테스트 클래스는 테스트 대상 클래스나 동작을 기준으로 `BookServiceTests`, `BookControllerTests`처럼 이름 짓습니다.

새 비즈니스 로직, 요청 처리, 영속성 동작, CSV 파싱 가정에는 집중된 테스트를 추가합니다. Pull Request를 열기 전 `./gradlew test`를 실행합니다.

## 커밋 및 Pull Request 지침

현재 이력에는 초기 커밋만 있으므로 엄격한 커밋 규칙은 없습니다. `Add book import service`, `Validate CSV headers`처럼 간결한 명령형 커밋 메시지를 사용합니다.

Pull Request에는 변경 요약, 변경 이유, 테스트 결과, 설정 또는 데이터 형식 영향이 있다면 그 내용을 포함합니다. Thymeleaf UI 변경이 있는 경우에만 스크린샷을 첨부합니다.
