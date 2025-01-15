# Spring Boot 다양한 HttpClient 예제 및 테스트 프로젝트

이 프로젝트는 Spring Boot 환경에서 다양한 HttpClient(RestClient, WebClient, Http Interface)를 활용하는 방법을 다룹니다.

- RestClient와 WebClient: 유틸리티 성격의 Helper 클래스를 제공합니다.
- Http Interface: 사용 예제를 포함하여 간결한 코드 작성 방식을 소개합니다.
- 각 HttpClient에 대한 테스트 케이스도 별도로 작성하였으며 동작을 확인할 수 있습니다.

## 기술 스펙

---

- **Java 17**
- **Spring Boot 3.4.1**
- **MockServer 5.15.0**
- **JUnit 5**

## Dependencies

---

```groovy
implementation 'org.springframework.boot:spring-boot-starter-web'

  implementation 'org.springframework.boot:spring-boot-starter-webflux'

  compileOnly 'org.projectlombok:lombok'
  annotationProcessor 'org.springframework.boot:spring-boot-configuration-processor'
  annotationProcessor 'org.projectlombok:lombok'
  testImplementation 'org.springframework.boot:spring-boot-starter-test'
  testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
  // MockServer Netty
  testImplementation 'org.mock-server:mockserver-netty:5.15.0'

  // MockServer Client
  testImplementation 'org.mock-server:mockserver-client-java:5.15.0'
```

## 디렉토리 구조

---

``` 
├── main
│   ├── java
│   │   └── com
│   │       └── example
│   │           └── httpclients
│   │               ├── HttpClientsApplication.java
│   │               ├── httpInterface
│   │               │   ├── SampleHttpInterfaceService.java
│   │               │   ├── config
│   │               │   │   └── HttpInterfaceConfig.java
│   │               │   └── cto
│   │               │       └── SampleDto.java
│   │               ├── restClient
│   │               │   └── RestClientHelper.java
│   │               └── webClient
│   │                   ├── WebClientHelper.java
│   │                   └── config
│   │                       └── WebClientConfig.java
│   └── resources
│       └── application.yml
└── test
    └── java
        └── com
            └── example
                └── httpclients
                    ├── HttpClientsApplicationTests.java
                    ├── httpInterface
                    │   └── SampleHttpInterfaceServiceTest.java
                    ├── restClient
                    │   └── RestClientHelperTest.java
                    └── webClient
                        └── WebClientHelperTest.java

```

### **1. main/java/com/example/httpclients**

- `HttpClientsApplication.java`: Spring Boot 애플리케이션의 진입점.

#### **1-1. httpInterface**

- `SampleHttpInterfaceService.java` : Spring의 Http Interface를 활용하여 외부 API 호출을 처리하는 서비스 클래스.
- `config/HttpInterfaceConfig.java` : Http Interface를 설정하는 Config 클래스.
- `cto/SampleDto.java` : Http Interface 호출 결과를 매핑하기 위한 DTO 클래스.

#### **1-2. restClient**

- `RestClientHelper.java` : `RestTemplate`를 사용하여 HTTP 요청을 보내는 유틸리티 클래스.

#### **1-3. webClient**

- `WebClientHelper.java` : `WebClient`를 사용하여 HTTP 요청을 보내는 유틸리티 클래스.
- `config/WebClientConfig.java` : `WebClient`를 설정하는 Config 클래스.

### **2. main/resources**

- `application.yml` : Spring Boot의 설정 파일.

### **3. test/java/com/example/httpclients**

- `httpInterface/SampleHttpInterfaceServiceTest.java` : Http Interface 서비스의 테스트 케이스.
- `restClient/RestClientHelperTest.java` : `RestClient` 유틸리티 클래스 테스트 케이스.
- `webClient/WebClientHelperTest.java` : `WebClient` 유틸리티 클래스 테스트 케이스.



## 프로젝트 실행 방법

**프로젝트 클론**

```bash
git clone https://github.com/DevK-Jung/http-clients.git
```

**빌드 및 실행**

```bash
./gradlew bootRun
```

