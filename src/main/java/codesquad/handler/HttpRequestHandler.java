package codesquad.handler;

import static codesquad.http.HttpStatus.BAD_REQUEST;

import codesquad.HandlerMapper;
import codesquad.http.HttpHeaders;
import codesquad.http.HttpMethod;
import codesquad.http.HttpPath;
import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.http.HttpResponseLine;
import codesquad.http.HttpVersion;
import java.io.IOException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestHandler.class);

    private final HandlerMapper handlerMapper;
    private final StaticResourceHandler staticResourceHandler;

    public HttpRequestHandler(HandlerMapper handlerMapper, StaticResourceHandler staticResourceHandler) {
        this.handlerMapper = handlerMapper;
        this.staticResourceHandler = staticResourceHandler;
    }

    public HttpResponse handle(final HttpRequest httpRequest, final HttpResponse httpResponse) throws IOException {
        HttpVersion httpVersion = httpRequest.getVersion();
        HttpMethod method = httpRequest.getMethod();
        HttpPath path = httpRequest.getPath();

        Optional<Handler> handler = handlerMapper.findHandler(path.getDefaultPath());
        logger.debug("handler found: {}", handler);
        if (handler.isPresent()) {
            Handler userRegistrationHandler = handler.get();
            return userRegistrationHandler.service(httpRequest, httpResponse);
        }

        if (method == HttpMethod.GET && path.isOnlyDefaultPath()) {
            return staticResourceHandler.service(httpRequest, httpResponse);
        }

        HttpResponseLine httpResponseLine = new HttpResponseLine(httpVersion, BAD_REQUEST);
        return new HttpResponse(httpResponseLine, HttpHeaders.empty(), new byte[0]);
    }

}
