package codesquad.webserver.parser;

import codesquad.servlet.execption.ClientException;
import codesquad.servlet.execption.GlobalExceptionHandler;
import codesquad.servlet.execption.MethodNotAllowedException;
import codesquad.webserver.http.HttpHeaders;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpRequestBody;
import codesquad.webserver.http.HttpRequestLine;
import codesquad.webserver.http.HttpResponse;
import codesquad.webserver.http.HttpStatus;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestMapper {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestMapper.class);

    private final HttpRequestParser httpRequestParser;
    private final GlobalExceptionHandler globalExceptionHandler;

    public HttpRequestMapper(HttpRequestParser httpRequestParser, GlobalExceptionHandler globalExceptionHandler) {
        this.httpRequestParser = httpRequestParser;
        this.globalExceptionHandler = globalExceptionHandler;
    }

    public HttpRequest mapFrom(InputStream inputStream, HttpResponse httpResponse) {
        try {
            HttpRequestLine httpRequestLine = httpRequestParser.parseHttpRequestFirstLine(inputStream);
            HttpHeaders httpHeaders = httpRequestParser.parseHeaders(inputStream);
            HttpRequestBody httpRequestBody = httpRequestParser.parseBody(inputStream,
                    httpHeaders.getContentLength());

            logger.debug("http request line: {}", httpRequestLine);
            logger.debug("http request headers: {}", httpHeaders);
            logger.debug("http request body: {}", httpRequestBody);
            return new HttpRequest(httpRequestLine, httpHeaders, httpRequestBody);

        } catch (MethodNotAllowedException e) {
            globalExceptionHandler.handle(e, httpResponse);
            throw e;
        } catch (Exception e) {
            ClientException clientException = new ClientException(e, HttpStatus.BAD_REQUEST);
            globalExceptionHandler.handle(clientException, httpResponse);
            throw clientException;
        }
    }

}
