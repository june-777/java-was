package codesquad.servlet.handler;

import codesquad.servlet.HandlerMapper;
import codesquad.utils.time.ZonedDateTimeGenerator;
import codesquad.webserver.http.HttpMethod;
import codesquad.webserver.http.HttpPath;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;
import java.io.IOException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestHandler.class);

    private final HandlerMapper handlerMapper;
    private final StaticResourceHandler staticResourceHandler;
    private final ZonedDateTimeGenerator zonedDateTimeGenerator;

    public HttpRequestHandler(HandlerMapper handlerMapper,
                              StaticResourceHandler staticResourceHandler,
                              ZonedDateTimeGenerator zonedDateTimeGenerator
    ) {
        this.handlerMapper = handlerMapper;
        this.staticResourceHandler = staticResourceHandler;
        this.zonedDateTimeGenerator = zonedDateTimeGenerator;
    }

    public void handle(final HttpRequest httpRequest, final HttpResponse httpResponse) throws IOException {
        HttpMethod method = httpRequest.getMethod();
        HttpPath path = httpRequest.getPath();

        Optional<Handler> handler = handlerMapper.findHandler(path.getDefaultPath());
        if (handler.isPresent()) {
            Handler userRegistrationHandler = handler.get();
            userRegistrationHandler.service(httpRequest, httpResponse);
            setDefaultHeaders(httpResponse);
            return;
        }

        if (method == HttpMethod.GET && path.isOnlyDefaultPath()) {
            staticResourceHandler.service(httpRequest, httpResponse);
            setDefaultHeaders(httpResponse);
            return;
        }

        setDefaultHeaders(httpResponse);
        httpResponse.setBadRequest();
    }

    private void setDefaultHeaders(HttpResponse httpResponse) {
        httpResponse.setDate(zonedDateTimeGenerator.now());
        httpResponse.setServer();
        httpResponse.setConnectionClose();
    }

}