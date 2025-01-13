package com.example.httpclients.webClient;

import com.example.httpclients.httpInterface.cto.SampleDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import org.mockserver.verify.VerificationTimes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.util.AssertionErrors.fail;

@SpringBootTest
class WebClientHelperTest {

    @Autowired
    WebClientHelper webClientHelper;

    private static ClientAndServer mockServer;
    private static MockServerClient mockServerClient;

    @BeforeAll
    static void startMockServer() {
        mockServer = ClientAndServer.startClientAndServer(8099);
        mockServerClient = new MockServerClient("localhost", 8099);
    }

    @AfterAll
    static void stopMockServer() {
        if (mockServer != null) {
            mockServer.stop();
        }
    }


    @Test
    void getMono() throws JsonProcessingException {
        String url = "http://localhost:8099/posts/1";
        SampleDto expectedDto = new SampleDto(1L, "userId");
        String jsonBody = toJson(expectedDto);

        mockServerClient.when(
                HttpRequest.request()
                        .withMethod(HttpMethod.GET.name())
                        .withPath("/posts/1")
        ).respond(
                HttpResponse.response()
                        .withStatusCode(HttpStatus.OK.value())
                        .withBody(jsonBody, MediaType.APPLICATION_JSON)
        );

        SampleDto result1 = webClientHelper.getMono(url, null, null, SampleDto.class)
                .block();

        assertThat(result1).isEqualTo(expectedDto);

        // Act & Assert: 비동기 호출
        webClientHelper.getMono(url, null, null, SampleDto.class)
                .doOnNext(result -> {
                    System.out.println("sampleDto = " + result);
                    assertThat(result).isEqualTo(expectedDto);
                })
                .doOnError(error -> fail("Request failed: " + error.getMessage())) // 에러 처리
                .block(); // 비동기 결과를 기다림

        mockServerClient.verify(
                HttpRequest.request()
                        .withMethod(HttpMethod.GET.name())
                        .withPath("/posts/1"),
                VerificationTimes.exactly(2)
        );
    }

    @Test
    void getFlux() throws JsonProcessingException {
        // Arrange
        String url = "http://localhost:8099/posts";
        List<SampleDto> expectedList = List.of(
                new SampleDto(1L, "userId1"),
                new SampleDto(2L, "userId2"),
                new SampleDto(3L, "userId3")
        );
        String jsonBody = toJson(expectedList);

        mockServerClient.when(
                HttpRequest.request()
                        .withMethod(HttpMethod.GET.name())
                        .withPath("/posts")
        ).respond(
                HttpResponse.response()
                        .withStatusCode(HttpStatus.OK.value())
                        .withBody(jsonBody, MediaType.APPLICATION_JSON)
        );

        // Act: 동기 호출
        List<SampleDto> result1 = webClientHelper.getFlux(url, null, null, SampleDto.class)
                .collectList()
                .block();

        // Assert: 동기 호출 검증
        assertThat(result1).isEqualTo(expectedList);

        // Act & Assert: 비동기 호출
        webClientHelper.getFlux(url, null, null, SampleDto.class)
                .doOnNext(result -> {
                    System.out.println("Next item: " + result);
                    assertThat(expectedList).contains(result);
                })
                .doOnComplete(() -> System.out.println("Flux stream completed"))
                .doOnError(error -> fail("Request failed: " + error.getMessage()))
                .subscribe();

        // Verify: MockServer 호출 검증
        mockServerClient.verify(
                HttpRequest.request()
                        .withMethod(HttpMethod.GET.name())
                        .withPath("/posts"),
                VerificationTimes.exactly(2) // block() + subscribe()
        );
    }

    private String toJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}