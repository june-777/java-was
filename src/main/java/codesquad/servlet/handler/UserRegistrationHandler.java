package codesquad.servlet.handler;

import codesquad.model.User;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserRegistrationHandler implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationHandler.class);


    @Override
    public void service(final HttpRequest request, final HttpResponse response) {

        String userId = request.getBodyParamValue("userId");
        String password = request.getBodyParamValue("password");
        String name = request.getBodyParamValue("name");
        String email = request.getBodyParamValue("email");
        User user = new User(userId, password, name, email);
        logger.debug("User Registration Success = {}", user);
        response.sendRedirect("/index.html");
    }

}
