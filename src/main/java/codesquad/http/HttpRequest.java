package codesquad.http;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class HttpRequest {

    private final HttpRequestFirstLine httpRequestFirstLine;
    private final HttpHeaders headers;

    public HttpRequest(HttpRequestFirstLine httpRequestFirstLine, HttpHeaders headers) {
        this.httpRequestFirstLine = httpRequestFirstLine;
        this.headers = headers;
    }

    public HttpVersion getVersion() {
        return httpRequestFirstLine.getVersion();
    }

    public HttpMethod getMethod() {
        return httpRequestFirstLine.getMethod();
    }

    public HttpPath getPath() {
        return httpRequestFirstLine.getPath();
    }

    public Map<String, String> getQueryStrings() {
        return Collections.unmodifiableMap(getPath().getQueryString());
    }

    public Optional<String> getHeaderValue(String headerName) {
        Map<String, String> header = headers.getHeaders();
        return Optional.ofNullable(header.get(headerName));
    }

    @Override
    public String toString() {
        return httpRequestFirstLine + "\n" + headers;
    }
}
