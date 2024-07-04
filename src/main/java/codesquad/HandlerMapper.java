package codesquad;

import codesquad.handler.Handler;
import codesquad.handler.UserRegistrationHandler;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class HandlerMapper {

    private final Map<String, Handler> handlers = new ConcurrentHashMap<>();

    public HandlerMapper() {
        handlers.put("/create", new UserRegistrationHandler());
    }

    public Optional<Handler> findHandler(String path) {
        return Optional.ofNullable(handlers.get(path));
    }

}
