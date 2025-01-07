package com.example.httpclients.restClient;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class RestClientHelperTest {

    @Autowired
    RestClientHelper restClientHelper;

    // Base URL for test API
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    private static final HttpHeaders DEFAULT_HEADERS = new HttpHeaders();

    @Test
    @DisplayName("GET - 단일 DTO")
    void get() {
        String url = BASE_URL + "/posts/1";

        SampleDto result = restClientHelper.get(url, DEFAULT_HEADERS, null, SampleDto.class);

        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getTitle()).isNotNull();
    }

    @Test
    @DisplayName("GET - 제네릭 리스트")
    void getTypeReference() {
        String url = BASE_URL + "/posts";

        List<SampleDto> result = restClientHelper.get(
                url,
                DEFAULT_HEADERS,
                null,
                new ParameterizedTypeReference<>() {
                });

        assertThat(result).hasSize(100);
        assertThat(result)
                .extracting("title")
                .contains("qui est esse");
    }

    @Test
    @DisplayName("POST - 단일 객체 전송 및 응답 확인")
    void post() {
        String url = BASE_URL + "/posts";

        SampleDto request = new SampleDto(101L, "title", "body");

        SampleDto result = restClientHelper.post(url, DEFAULT_HEADERS, request, SampleDto.class);

        assertThat(result.getUserId()).isEqualTo(101L);
        assertThat(result.getTitle()).isEqualTo("title");
    }

    @Test
    @DisplayName("PUT - 데이터 업데이트")
    void put() {
        String url = BASE_URL + "/posts/1";

        SampleDto request = new SampleDto(1L, "updated Title", "updated body");

        SampleDto result = restClientHelper.put(url, DEFAULT_HEADERS, request, SampleDto.class);

        assertThat(result.getTitle()).isEqualTo("updated Title");
        assertThat(result.getBody()).isEqualTo("updated body");
    }

    @Test
    @DisplayName("DELETE - 리소스 삭제")
    void delete() {
        String url = BASE_URL + "/posts/1";

        restClientHelper.delete(url, DEFAULT_HEADERS, null, Void.class);

        // DELETE 요청은 일반적으로 응답 본문이 없으므로 예외 없이 실행되었는지 확인
        assertThat(true).isTrue(); // 성공적으로 예외 없이 실행되었음을 의미
    }

    public static class SampleDto {
        private Long userId;
        private Long id;
        private String title;
        private String body;

        public SampleDto() {
        }

        public SampleDto(Long userId, String title, String body) {
            this.userId = userId;
            this.title = title;
            this.body = body;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }
    }

}