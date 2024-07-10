package codesquad.webserver.http;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class HttpRequest {

    private final HttpRequestLine httpRequestLine;
    private final HttpHeaders headers;
    private HttpRequestBody httpRequestBody;

    public HttpRequest(HttpRequestLine httpRequestLine, HttpHeaders headers, HttpRequestBody httpRequestBody) {
        this.httpRequestLine = httpRequestLine;
        this.headers = headers;
        this.httpRequestBody = httpRequestBody;
    }

    public HttpVersion getVersion() {
        return httpRequestLine.getVersion();
    }

    public HttpMethod getMethod() {
        return httpRequestLine.getMethod();
    }

    public HttpPath getPath() {
        return httpRequestLine.getPath();
    }

    public Map<String, String> getQueryStrings() {
        return Collections.unmodifiableMap(getPath().getQueryString());
    }

    public Optional<String> getHeaderValue(String headerName) {
        Map<String, String> header = headers.getHeaders();
        return Optional.ofNullable(header.get(headerName));
    }

    public String getBodyParamValue(String paramName) {
        return httpRequestBody.getParamValue(paramName);
    }

    public Cookie getCookie() {
        Optional<String> optCookieValues = getHeaderValue("Cookie");
        if (optCookieValues.isEmpty()) {
            return null;
        }

        String cookieValues = optCookieValues.get();
        String[] cookieValueParts = cookieValues.split(";");
        String[] nameAndValue = cookieValueParts[0].split("=");
        return new Cookie(nameAndValue[0], nameAndValue[1]);
    }

    @Override
    public String toString() {
        return httpRequestLine + "\n" + headers + "\n" + httpRequestBody;
    }
}
