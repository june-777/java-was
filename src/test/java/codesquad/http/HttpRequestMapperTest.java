package codesquad.http;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HttpRequestMapperTest {

    HttpRequestMapper httpRequestMapper = new HttpRequestMapper();

    @Test
    @DisplayName("[Success] HTTP 규약에 맞는 요청")
    void parserTest() throws IOException {
        String request = "GET /index.html HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "User-Agent: Mozilla/5.0\r\n" +
                "\r\n";

        BufferedReader bufferedReader = new BufferedReader(new StringReader(request));
        HttpRequest httpRequest = httpRequestMapper.from(bufferedReader);

        assertFirstLine(httpRequest);
        assertHeaders(httpRequest);
    }

    private static void assertFirstLine(HttpRequest httpRequest) {
        assertThat(httpRequest.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(httpRequest.getPath()).isEqualTo("/index.html");
        assertThat(httpRequest.getVersion()).isEqualTo(HttpVersion.HTTP1_1);
    }

    private void assertHeaders(HttpRequest httpRequest) {
        assertThat(httpRequest.getHeaderValue("Host"))
                .isPresent()
                .get().isEqualTo("localhost:8080");
        assertThat(httpRequest.getHeaderValue("User-Agent"))
                .isPresent()
                .get().isEqualTo("Mozilla/5.0");
    }
}