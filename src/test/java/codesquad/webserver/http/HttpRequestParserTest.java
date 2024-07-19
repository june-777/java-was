package codesquad.webserver.http;

import static org.assertj.core.api.Assertions.assertThat;

import codesquad.webserver.parser.HttpRequestParser;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class HttpRequestParserTest {

    HttpRequestParser httpRequestParser = new HttpRequestParser();

    @Nested
    @DisplayName("Http Request Header를 파싱하는 기능은")
    class Describe_ParseHeader {

        @Test
        @DisplayName("[Success] 헤더를 LF 기준으로 분리할 수 있다.")
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
                    User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36
                    sec-ch-ua: "Not/A)Brand";v="8", "Chromium";v="126", "Google Chrome";v="126"
                    sec-ch-ua-mobile: ?0
                    sec-ch-ua-platform: "macOS
                    \r\n""";

            InputStream inputStream = new ByteArrayInputStream(requestHeader.getBytes(StandardCharsets.UTF_8));

            // when
            HttpHeaders httpHeaders = httpRequestParser.parseHeaders(inputStream);
            // then
            Assertions.assertThat(httpHeaders.size()).isEqualTo(14);
        }

        @Test
        @DisplayName("[Success] 헤더를 CR 기준으로 분리할 수 있다.")
        void test2() throws IOException {
            // given
            String requestHeader =
                    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\r"
                            + "Accept-Encoding: gzip, deflate, br, zstd\r"
                            + "Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7\r"
                            + "Cache-Control: max-age=0\r"
                            + "Connection: keep-alive\r"
                            + "Content-Length: 52\r"
                            + "Content-Type: application/x-www-form-urlencoded\r"
                            + "Host: localhost:8080\r"
                            + "Origin: http://localhost:8080\r"
                            + "Referer: http://localhost:8080/registration\r"
                            + "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36\r"
                            + "sec-ch-ua: \"Not/A)Brand\";v=\"8\", \"Chromium\";v=\"126\", \"Google Chrome\";v=\"126\"\r"
                            + "sec-ch-ua-mobile: ?0\r"
                            + "sec-ch-ua-platform: \"macOS\r"
                            + "\r\n";

            InputStream inputStream = new ByteArrayInputStream(requestHeader.getBytes(StandardCharsets.UTF_8));

            // when
            HttpHeaders httpHeaders = httpRequestParser.parseHeaders(inputStream);
            // then
            Assertions.assertThat(httpHeaders.size()).isEqualTo(14);
        }

        @Test
        @DisplayName("[Success] 헤더를 CRLF 기준으로 분리할 수 있다.")
        void test3() throws IOException {
            // given
            String requestHeader =
                    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\r\n"
                            + "Accept-Encoding: gzip, deflate, br, zstd\r\n"
                            + "Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7\r\n"
                            + "Cache-Control: max-age=0\r\n"
                            + "Connection: keep-alive\r\n"
                            + "Content-Length: 52\r\n"
                            + "Content-Type: application/x-www-form-urlencoded\r\n"
                            + "Host: localhost:8080\r\n"
                            + "Origin: http://localhost:8080\r\n"
                            + "Referer: http://localhost:8080/registration\r\n"
                            + "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36\r\n"
                            + "sec-ch-ua: \"Not/A)Brand\";v=\"8\", \"Chromium\";v=\"126\", \"Google Chrome\";v=\"126\"\r\n"
                            + "sec-ch-ua-mobile: ?0\r\n"
                            + "sec-ch-ua-platform: \"macOS\r\n"
                            + "\r\n";

            InputStream inputStream = new ByteArrayInputStream(requestHeader.getBytes(StandardCharsets.UTF_8));

            // when
            HttpHeaders httpHeaders = httpRequestParser.parseHeaders(inputStream);
            // then
            Assertions.assertThat(httpHeaders.size()).isEqualTo(14);
        }
    }

    @Nested
    @DisplayName("Http Request Body를 파싱하는 기능은")
    class Describe_ParseBody {

        @Test
        @DisplayName("[Success] 파싱하여 '&'를 기준으로 분리하고 '='를 기준으로 key, value로 나눈 HttpRequestBody를 리턴한다.")
        void httpRequestBodyTest() throws IOException {
            // given
            String requestBody = "name=JohnDoe&email=john@example.com";
            byte[] body = requestBody.getBytes(StandardCharsets.UTF_8);
            // when
            Map<String, String> params = httpRequestParser.parseBody(body, requestBody.length());
            // then
            assertThat(params.isEmpty()).isFalse();
            assertThat(params.size()).isEqualTo(2);
            assertThat(params.get("name")).isEqualTo("JohnDoe");
            assertThat(params.get("email")).isEqualTo("john@example.com");
        }

        @Test
        @DisplayName("[Success] Request Body value에 '&'이 포함되어 있어도 파싱을 성공한다")
        void httpRequestBodyTestWithAmpersand() throws IOException {
            // given
            String requestBody = "name=%26%26%26name%26&email=%26%26%26@example.com";
            byte[] body = requestBody.getBytes(StandardCharsets.UTF_8);
            // when
            Map<String, String> params = httpRequestParser.parseBody(body, requestBody.length());
            // then
            assertThat(params.isEmpty()).isFalse();
            assertThat(params.size()).isEqualTo(2);
            assertThat(params.get("name")).isEqualTo("&&&name&");
            assertThat(params.get("email")).isEqualTo("&&&@example.com");
        }
    }

}
