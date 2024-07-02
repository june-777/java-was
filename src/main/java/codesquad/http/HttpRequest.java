package codesquad.http;

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

    public String getPath() {
        return httpRequestFirstLine.getPath();
    }

    @Override
    public String toString() {
        return httpRequestFirstLine + "\n" + headers;
    }
}
