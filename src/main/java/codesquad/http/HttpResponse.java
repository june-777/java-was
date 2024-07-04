package codesquad.http;

import static codesquad.http.HttpHeaders.HeaderName.LOCATION;
import static codesquad.utils.StringUtils.CRLF;

import java.util.Arrays;
import java.util.Map;

public class HttpResponse {

    private final HttpResponseLine httpResponseLine;
    private final HttpHeaders headers;
    private final byte[] body;

    public HttpResponse(HttpResponseLine httpResponseLine, HttpHeaders headers, byte[] body) {
        this.httpResponseLine = httpResponseLine;
        this.headers = headers;
        this.body = body;
    }

    public String getResponseLine() {
        String delimiter = " ";
        StringBuilder sb = new StringBuilder();
        HttpVersion version = httpResponseLine.getVersion();
        HttpStatus status = httpResponseLine.getStatus();

        sb.append(version.getName()).append(delimiter)
                .append(status.getCode()).append(delimiter)
                .append(status.getRepresentation());
        return sb.toString();
    }

    public HttpStatus getHttpStatus() {
        return httpResponseLine.getStatus();
    }

    public String getHeaders() {
        String delimiter = ": ";
        Map<String, String> headers = this.headers.getHeaders();
        StringBuilder sb = new StringBuilder();
        for (String headerName : headers.keySet()) {
            sb.append(headerName).append(delimiter).append(headers.get(headerName)).append(CRLF);
        }
        return sb.toString();
    }

    public byte[] getBody() {
        return body;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Status: ").append(httpResponseLine.getStatus());
        sb.append("Headers: ").append(headers);
        sb.append("Response Body: ").append(Arrays.toString(body));
        return sb.toString();
    }
}
