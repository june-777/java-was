package codesquad.handler;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.model.User;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserRegistrationHandler implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationHandler.class);

    @Override
    public HttpResponse service(final HttpRequest request, final HttpResponse response) {
        logger.debug("UserRegistrationHandler Start");

        Map<String, String> queryStrings = request.getQueryStrings();

        String userId = queryStrings.get("userId");
        String password = queryStrings.get("password");
        String name = queryStrings.get("name");
        String email = queryStrings.get("email");
        User user = new User(userId, password, name, email);
        logger.debug("user = {}", user);
        response.sendRedirect("/");

        logger.debug("UserRegistrationHandler End");
        return response;
    }

}
