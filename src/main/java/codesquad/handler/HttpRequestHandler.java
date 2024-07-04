package codesquad.handler;

import static codesquad.http.HttpStatus.BAD_REQUEST;
import static codesquad.http.HttpStatus.NOT_FOUND;
import static codesquad.http.HttpStatus.OK;

import codesquad.http.HttpHeaders;
import codesquad.http.HttpMediaType;
import codesquad.http.HttpMethod;
import codesquad.http.HttpPath;
import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.HttpVersion;
import java.io.FileNotFoundException;
import java.io.IOException;

public class HttpRequestHandler {

    private final StaticResourceReader staticResourceReader;
    private final MappingMediaTypeFileExtensionResolver mappingMediaTypeFileExtensionResolver;

    public HttpRequestHandler(StaticResourceReader staticResourceReader,
                              MappingMediaTypeFileExtensionResolver mappingMediaTypeFileExtensionResolver
    ) {
        this.staticResourceReader = staticResourceReader;
        this.mappingMediaTypeFileExtensionResolver = mappingMediaTypeFileExtensionResolver;
    }

    public HttpResponse handle(final HttpRequest httpRequest) throws IOException {
        // TODO: reuqetURL 이 API인지, 정적 리소스 요청인지 구분하는 로직 필요
        HttpVersion httpVersion = httpRequest.getVersion();
        HttpMethod method = httpRequest.getMethod();
        HttpPath path = httpRequest.getPath();

        if (method == HttpMethod.GET) {
            // TODO: 정적 자원 처리
            if (path.isOnlyDefaultPath()) {
                try {
                    String pathValue = path.getDefaultPath();
                    if (path.isDirectoryPath()) {
                        pathValue += "/index.html";
                    }
                    String fileExtension = getFileExtension(pathValue);
                    HttpMediaType httpMediaType = mappingMediaTypeFileExtensionResolver.resolve(fileExtension);
                    byte[] body = staticResourceReader.getFileContents(pathValue);

                    HttpHeaders httpHeaders = HttpHeaders.empty();
                    httpHeaders.setContentType(httpMediaType);
                    httpHeaders.setContentLength(body.length);
                    return new HttpResponse(httpVersion, OK, httpHeaders, body);
                } catch (FileNotFoundException e) {
                    HttpHeaders httpHeaders = HttpHeaders.empty();
                    return new HttpResponse(httpVersion, NOT_FOUND, httpHeaders,
                            NOT_FOUND.getRepresentation().getBytes());
                }

                // TODO: 동적 자원 처리 (Query String도 있는 경우)

            }
        }

        return new HttpResponse(httpVersion, BAD_REQUEST, HttpHeaders.empty(), new byte[0]);
    }

    private String getFileExtension(String path) {
        int lastIndexOfDot = path.lastIndexOf('.');
        if (lastIndexOfDot == -1) {
            return "";
        }
        return path.substring(lastIndexOfDot + 1);
    }

}
