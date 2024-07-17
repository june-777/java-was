package codesquad.servlet.handler;

import static codesquad.servlet.database.property.DataSource.JDBC_URL;
import static codesquad.servlet.database.property.DataSource.PASSWORD;
import static codesquad.servlet.database.property.DataSource.USER_NAME;

import codesquad.domain.ArticleStorage;
import codesquad.domain.UserStorage;
import codesquad.servlet.SessionStorage;
import codesquad.servlet.database.connection.ArticleDao;
import codesquad.servlet.database.connection.DatabaseConnector;
import codesquad.servlet.database.connection.UserDao;
import codesquad.servlet.database.property.DataSourceProvider;
import codesquad.servlet.handler.api.AllUserInfoHandler;
import codesquad.servlet.handler.api.ArticleWriteHandler;
import codesquad.servlet.handler.api.UserInfoHandler;
import codesquad.servlet.handler.api.UserLoginHandler;
import codesquad.servlet.handler.api.UserLogoutHandler;
import codesquad.servlet.handler.api.UserRegistrationHandler;
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

        DataSourceProvider dataSourceProvider =
                new DataSourceProvider(JDBC_URL.getProperty(), USER_NAME.getProperty(), PASSWORD.getProperty());
        DatabaseConnector databaseConnector = new DatabaseConnector(dataSourceProvider);
        UserStorage userStorage = new UserDao(databaseConnector);
        SessionStorage sessionStorage = SessionStorage.getInstance();
        ArticleStorage articleStorage = new ArticleDao(databaseConnector);

        handlers.get(HttpMethod.POST)
                .put("/user/create", new UserRegistrationHandler(userStorage));

        handlers.get(HttpMethod.POST)
                .put("/user/login", new UserLoginHandler(
                        userStorage, sessionStorage));

        handlers.get(HttpMethod.GET)
                .put("/user/logout", new UserLogoutHandler(sessionStorage));

        handlers.get(HttpMethod.GET)
                .put("/api/user/info", new UserInfoHandler(sessionStorage));

        handlers.get(HttpMethod.GET)
                .put("/api/user/list", new AllUserInfoHandler(userStorage));

        handlers.get(HttpMethod.POST)
                .put("/article", new ArticleWriteHandler(sessionStorage, articleStorage));
    }

    public Optional<Handler> findBy(HttpMethod httpMethod, String path) {
        return Optional.ofNullable(handlers.get(httpMethod).get(path));
    }

}
