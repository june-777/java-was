package codesquad.http;

import static org.assertj.core.api.Assertions.assertThat;

import codesquad.utils.string.StringUtils;
import codesquad.webserver.http.HttpMethod;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpRequestMapper;
import codesquad.webserver.http.HttpRequestParser;
import codesquad.webserver.http.HttpVersion;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HttpRequestMapperTest {

    HttpRequestMapper httpRequestMapper = new HttpRequestMapper(new HttpRequestParser());

    @Test
    @DisplayName("[Success] HTTP 규약에 맞는 요청")
    void parserTest() throws IOException {
        String request = "GET /index.html HTTP/1.1" + StringUtils.CRLF +
                "Host: localhost:8080" + StringUtils.CRLF +
                "User-Agent: Mozilla/5.0" + StringUtils.CRLF +
                StringUtils.CRLF;

        BufferedReader bufferedReader = new BufferedReader(new StringReader(request));
        HttpRequest httpRequest = httpRequestMapper.mapFrom(bufferedReader);

        assertFirstLine(httpRequest);
        assertHeaders(httpRequest);
    }

    private static void assertFirstLine(HttpRequest httpRequest) {
        assertThat(httpRequest.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(httpRequest.getPath().getDefaultPath()).isEqualTo("/index.html");
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