package codesquad.http;

public class HttpRequestFirstLine {

    private final HttpMethod method;
    private final String path;
    private final String version;

    public HttpRequestFirstLine(HttpMethod method, String path, String version) {
        this.method = method;
        this.path = path;
        this.version = version;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return method + " " + path + " " + version + "\n";
    }
}
