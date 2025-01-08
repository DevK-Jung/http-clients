package com.example.httpclients.httpInterface;

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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class SampleHttpInterfaceServiceTest {

    @Autowired
    private SampleHttpInterfaceService sampleHttpInterfaceService;

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
    void getSample() throws JsonProcessingException {
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

        SampleDto result1 = sampleHttpInterfaceService.getSample(1L);
        SampleDto result2 = sampleHttpInterfaceService.getSample(1L);

        assertThat(result1).isEqualTo(expectedDto);
        assertThat(result2).isEqualTo(expectedDto);

        mockServerClient.verify(
                HttpRequest.request()
                        .withMethod(HttpMethod.GET.name())
                        .withPath("/posts/1"),
                VerificationTimes.exactly(2)
        );
    }

    @Test
    void postSample() throws JsonProcessingException {
        SampleDto requestDto = new SampleDto("userId");
        SampleDto responseDto = new SampleDto(1L, "userId");

        String requestBody = toJson(requestDto);
        String responseBody = toJson(responseDto);

        mockServerClient.when(
                HttpRequest.request()
                        .withMethod(HttpMethod.POST.name())
                        .withPath("/posts")
                        .withBody(requestBody)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(HttpStatus.OK.value())
                        .withBody(responseBody, MediaType.APPLICATION_JSON)
        );

        SampleDto result = sampleHttpInterfaceService.postSample(requestDto);

        assertThat(result).isEqualTo(responseDto);

        mockServerClient.verify(
                HttpRequest.request()
                        .withMethod(HttpMethod.POST.name())
                        .withPath("/posts")
                        .withBody(requestBody),
                VerificationTimes.once()
        );
    }

    @Test
    void putSample() throws JsonProcessingException {
        SampleDto requestDto = new SampleDto(1L, "updatedUserId");
        SampleDto responseDto = new SampleDto(1L, "updatedUserId");

        String requestBody = toJson(requestDto);
        String responseBody = toJson(responseDto);

        mockServerClient.when(
                HttpRequest.request()
                        .withMethod(HttpMethod.PUT.name())
                        .withPath("/posts")
                        .withBody(requestBody)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(HttpStatus.OK.value())
                        .withBody(responseBody, MediaType.APPLICATION_JSON)
        );

        SampleDto result = sampleHttpInterfaceService.putSample(requestDto);

        assertThat(result).isEqualTo(responseDto);

        mockServerClient.verify(
                HttpRequest.request()
                        .withMethod(HttpMethod.PUT.name())
                        .withPath("/posts")
                        .withBody(requestBody),
                VerificationTimes.once()
        );
    }

    @Test
    void deleteSample() {
        mockServerClient.when(
                HttpRequest.request()
                        .withMethod(HttpMethod.DELETE.name())
                        .withPath("/posts/1")
        ).respond(
                HttpResponse.response()
                        .withStatusCode(HttpStatus.NO_CONTENT.value())
        );

        sampleHttpInterfaceService.deleteSample(1L);

        mockServerClient.verify(
                HttpRequest.request()
                        .withMethod(HttpMethod.DELETE.name())
                        .withPath("/posts/1"),
                VerificationTimes.once()
        );
    }

    private String toJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}