package codesquad.servlet.handler.api;

import static codesquad.webserver.http.HttpMethod.POST;
import static codesquad.webserver.http.HttpVersion.HTTP1_1;
import static org.assertj.core.api.Assertions.assertThat;

import codesquad.domain.InMemoryUserStorage;
import codesquad.servlet.SessionStorage;
import codesquad.webserver.http.HttpHeaders;
import codesquad.webserver.http.HttpPath;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpRequestBody;
import codesquad.webserver.http.HttpRequestLine;
import codesquad.webserver.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class UserRegistrationHandlerTest {

    InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
    SessionStorage sessionStorage = new SessionStorage();
    UserRegistrationHandler userRegistrationHandler = new UserRegistrationHandler(inMemoryUserStorage, sessionStorage);

    @Nested
    @DisplayName("회원가입 API는")
    class Describe_UserRegistrationAPISpecTest {

        @Test
        @DisplayName("[Success] 성공 시 /index.html로 리다이렉트 응답을 내려준다.")
        void redirectSpec() {
            // given
            HttpRequest httpRequest = createRegistrationRequest(
                    createUserRegistrationRequestBodyParams("아이디", "비밀번호", "이름", "이메일")
            );
            HttpResponse httpResponse = HttpResponse.ok();

            // when
            userRegistrationHandler.service(httpRequest, httpResponse);

            // then
            assertThat(httpResponse.getRedirect()).isPresent();
            assertThat(httpResponse.getRedirect().get()).isEqualTo("/index.html");
        }

        @Test
        @DisplayName("[Success] 성공 시 쿠키를 응답으로 내려준다.")
        void cookieSpec() {
            // given
            HttpRequest httpRequest = createRegistrationRequest(
                    createUserRegistrationRequestBodyParams("아이디", "비밀번호", "이름", "이메일")
            );
            HttpResponse httpResponse = HttpResponse.ok();

            // when
            userRegistrationHandler.service(httpRequest, httpResponse);

            // then
            boolean exists = httpResponse.getCookies().stream()
                    .anyMatch(cookie -> cookie.getName().equals("sid"));
            assertThat(exists).isTrue();
        }

        private HttpRequest createRegistrationRequest(Map<String, String> requestBodyParams) {
            HttpRequestLine httpRequestLine = new HttpRequestLine(POST, HttpPath.of("/create", Map.of()), HTTP1_1);
            HttpHeaders httpHeaders = HttpHeaders.empty();
            HttpRequestBody httpRequestBody = new HttpRequestBody(requestBodyParams);
            return new HttpRequest(httpRequestLine, httpHeaders, httpRequestBody);
        }

        private Map<String, String> createUserRegistrationRequestBodyParams(String userId, String password,
                                                                            String name, String email
        ) {
            HashMap<String, String> requestBodyParams = new HashMap<>();
            requestBodyParams.put("userId", userId);
            requestBodyParams.put("password", password);
            requestBodyParams.put("name", name);
            requestBodyParams.put("email", email);
            return requestBodyParams;
        }

    }
}