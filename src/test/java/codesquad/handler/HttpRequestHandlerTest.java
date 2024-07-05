package codesquad.handler;

import static codesquad.http.HttpMethod.GET;
import static codesquad.http.HttpVersion.HTTP1_1;
import static org.assertj.core.api.Assertions.assertThat;

import codesquad.HandlerMapper;
import codesquad.http.HttpHeaders;
import codesquad.http.HttpPath;
import codesquad.http.HttpRequest;
import codesquad.http.HttpRequestFirstLine;
import codesquad.http.HttpResponse;
import codesquad.http.HttpStatus;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HttpRequestHandlerTest {

    HttpRequestHandler httpRequestHandler = new HttpRequestHandler(
            new HandlerMapper(),
            new StaticResourceHandler(new StaticResourceReader(), new MappingMediaTypeFileExtensionResolver()));

    @Test
    @DisplayName("[Success] /index.html로 요청을 보내면 200 OK 응답이 발생한다.")
    void requestIndex() throws IOException {
        HttpResponse httpResponse = httpRequestHandler.handle(
                new HttpRequest(new HttpRequestFirstLine(GET, HttpPath.ofOnlyDefaultPath("/index.html"), HTTP1_1),
                        HttpHeaders.empty()),
                new HttpResponse(null, null, null));
        assertThat(httpResponse.getHttpStatus()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("[Success] /indexx.html로 요청을 보내면 400 응답이 발생한다.")
    void incorrectFileNameIndex() throws IOException {
        HttpResponse httpResponse = httpRequestHandler.handle(
                new HttpRequest(new HttpRequestFirstLine(GET, HttpPath.ofOnlyDefaultPath("/indexx.html"), HTTP1_1),
                        HttpHeaders.empty()),
                new HttpResponse(null, null, null));
        assertThat(httpResponse.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}