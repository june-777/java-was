package codesquad.http;

import java.util.Map;

public class HttpResponse {

    private final HttpStatus httpStatus;
    private final HttpHeaders headers;
    private final String body;

    public HttpResponse(HttpStatus httpStatus, HttpHeaders headers, String body) {
        this.httpStatus = httpStatus;
        this.headers = headers;
        this.body = body;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public Map<String, String> getHeaders() {
        return headers.getHeaders();
    }

    public String getBody() {
        return body;
    }
}
