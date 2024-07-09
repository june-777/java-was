package codesquad.webserver.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestMapper {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestMapper.class);

    public HttpRequest parse(final BufferedReader bufferedReader) throws IOException {
        String requestLine;

        requestLine = bufferedReader.readLine();
        HttpRequestLine firstLine = parseHttpRequestFirstLine(requestLine);
        HttpHeaders headers = parseHeaders(bufferedReader);
        HttpRequestBody httpRequestBody = parseBody(bufferedReader, headers.getContentLength());

        logger.debug("request line: {}", firstLine);
        logger.debug("request headers: {}", headers);
        logger.debug("request body: {}", httpRequestBody);

        return new HttpRequest(firstLine, headers, httpRequestBody);
    }

    public HttpRequestLine parseHttpRequestFirstLine(String requestLine) throws UnsupportedEncodingException {
        String[] startLineParts = requestLine.split(" ");

        if (startLineParts.length != 3) {
            throw new IllegalArgumentException("Invalid request line: " + requestLine);
        }

        HttpMethod method = HttpMethod.valueOf(startLineParts[0]);
        String defaultPath = startLineParts[1];
        HttpVersion version = HttpVersion.of(startLineParts[2]);

        String[] pathParts = defaultPath.split("\\?");
        if (pathParts.length == 1) {
            HttpPath httpPath = HttpPath.ofOnlyDefaultPath(pathParts[0]);
            return new HttpRequestLine(method, httpPath, version);
        }

        defaultPath = pathParts[0];
        String queryStrings = pathParts[1];

        String[] queryStringParts = queryStrings.split("&");
        Map<String, String> allQueryStrings = new HashMap<>();
        for (String queryStringPart : queryStringParts) {
            String decodedQueryString = URLDecoder.decode(queryStringPart, "UTF-8");
            String[] queryStringComponentPart = decodedQueryString.split("=");
            allQueryStrings.put(queryStringComponentPart[0], queryStringComponentPart[1]);
        }

        HttpPath httpPath = HttpPath.of(defaultPath, allQueryStrings);
        return new HttpRequestLine(method, httpPath, version);
    }

    public HttpHeaders parseHeaders(BufferedReader bufferedReader) throws IOException {
        String requestLine;
        Map<String, String> headers = new HashMap<>();
        while ((requestLine = bufferedReader.readLine()) != null) {
            if (requestLine.isBlank()) {
                break;
            }
            String[] headerParts = requestLine.split(": ", 2);
            headers.put(headerParts[0], headerParts[1]);
        }
        return HttpHeaders.of(headers);
    }

    public HttpRequestBody parseBody(BufferedReader bufferedReader, int contentLength) throws IOException {
        if (contentLength <= 0) {
            return HttpRequestBody.ofEmpty();
        }

        char[] buffer = new char[contentLength];
        int bytesRead = bufferedReader.read(buffer, 0, contentLength);
        if (bytesRead != contentLength) {
            throw new IllegalArgumentException("Invalid content length: " + bytesRead);
        }
        String bodyMessage = URLDecoder.decode(new String(buffer), "UTF-8");
        Map<String, String> params = new HashMap<>();
        String[] bodyParts = bodyMessage.split("&");
        for (String bodyPart : bodyParts) {
            params.put(bodyPart.split("=")[0], bodyPart.split("=")[1]);
        }
        return new HttpRequestBody(params);
    }
}