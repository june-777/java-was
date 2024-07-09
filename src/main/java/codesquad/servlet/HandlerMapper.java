package codesquad.servlet;

import codesquad.servlet.handler.Handler;
import codesquad.servlet.handler.UserRegistrationHandler;
import codesquad.webserver.http.HttpMethod;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HandlerMapper {

    private final Map<HttpMethod, Map<String, Handler>> handlers;

    public HandlerMapper() {
        handlers = new HashMap<>();
        for (HttpMethod httpMethod : HttpMethod.values()) {
            handlers.put(httpMethod, new HashMap<>());
        }
        handlers.get(HttpMethod.POST).put("/create", new UserRegistrationHandler());
    }

    public Optional<Handler> findBy(HttpMethod httpMethod, String path) {
        return Optional.ofNullable(handlers.get(httpMethod).get(path));
    }

}
