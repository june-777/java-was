package codesquad.servlet.handler.api;

import codesquad.domain.ArticleStorage;
import codesquad.domain.UserStorage;
import codesquad.domain.model.Article;
import codesquad.domain.model.User;
import codesquad.servlet.handler.Handler;
import codesquad.utils.json.JsonMapper;
import codesquad.webserver.http.HttpMediaType;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArticleListHandler implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(ArticleListHandler.class);

    private final ArticleStorage articleStorage;
    private final UserStorage userStorage;

    public ArticleListHandler(ArticleStorage articleStorage, UserStorage userStorage) {
        this.articleStorage = articleStorage;
        this.userStorage = userStorage;
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        logger.debug("ArticleListHandler start");
        List<Article> articles = articleStorage.selectAll();

        List<User> users = new ArrayList<>();
        for (Article article : articles) {
            User author = userStorage.selectById(article.getAuthorId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ID 사용자입니다"));
            users.add(author);
        }

        List<ArticleDetailResponse> articleDetailResponses = new ArrayList<>();
        for (int idx = 0; idx < articles.size(); idx++) {
            Article article = articles.get(idx);
            User author = users.get(idx);
            articleDetailResponses.add(ArticleDetailResponse.of(article, author));
        }

        String json = JsonMapper.listToJson(articleDetailResponses);
        response.setContentType(HttpMediaType.APPLICATION_JSON);
        response.setBody(json.getBytes());
        logger.debug("ArticleListHandler end");
    }

}
