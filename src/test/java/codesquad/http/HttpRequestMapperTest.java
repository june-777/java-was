package codesquad.http;

import static org.assertj.core.api.Assertions.assertThat;

import codesquad.webserver.http.HttpHeaders;
import codesquad.webserver.http.HttpMethod;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpRequestBody;
import codesquad.webserver.http.HttpRequestMapper;
import codesquad.webserver.http.HttpVersion;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
        HttpRequest httpRequest = httpRequestMapper.parse(bufferedReader);

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

    @Nested
    @DisplayName("Http 헤더를 파싱하는 기능은")
    class Describe_ParseHeader {

        @Test
        @DisplayName("")
        void test() throws IOException {
            // given
            String requestHeader = """
                    Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7
                    Accept-Encoding: gzip, deflate, br, zstd
                    Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7
                    Cache-Control: max-age=0
                    Connection: keep-alive
                    Content-Length: 52
                    Content-Type: application/x-www-form-urlencoded
                    Host: localhost:8080
                    Origin: http://localhost:8080
                    Referer: http://localhost:8080/registration
                    Sec-Fetch-Dest: document
                    Sec-Fetch-Mode: navigate
                    Sec-Fetch-Site: same-origin
                    Sec-Fetch-User: ?1
                    Upgrade-Insecure-Requests: 1
                    User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36
                    sec-ch-ua: "Not/A)Brand";v="8", "Chromium";v="126", "Google Chrome";v="126"
                    sec-ch-ua-mobile: ?0
                    sec-ch-ua-platform: "macOS"
                    """;

            BufferedReader bufferedReader = new BufferedReader(new StringReader(requestHeader));

            // when
            HttpHeaders httpHeaders = httpRequestMapper.parseHeaders(bufferedReader);
            // then
            Assertions.assertThat(httpHeaders.size()).isEqualTo(19);
        }
    }

    @Nested
    @DisplayName("Http 요청 본문을 파싱하는 기능은")
    class Describe_ParseBody {

        @Test
        @DisplayName("파싱하여 '&'를 기준으로 분리하고 '='를 기준으로 key, value로 나눈 HttpRequestBody를 리턴한다.")
        void test() throws IOException {
            // given
            String requestBody = "name=JohnDoe&email=john@example.com";
            BufferedReader bufferedReader = new BufferedReader(new StringReader(requestBody));
            // when
            HttpRequestBody httpRequestBody = httpRequestMapper.parseBody(bufferedReader, requestBody.length());
            // then
            assertThat(httpRequestBody.isExists()).isTrue();
            assertThat(httpRequestBody.size()).isEqualTo(2);
            assertThat(httpRequestBody.getParamValue("name")).isEqualTo("JohnDoe");
            assertThat(httpRequestBody.getParamValue("email")).isEqualTo("john@example.com");
        }
    }
}