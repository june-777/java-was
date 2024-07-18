package codesquad.servlet.handler.api;

import codesquad.domain.model.Article;
import codesquad.domain.model.User;

public class ArticleDetailResponse {

    private final Long id;
    private final String author;
    private final String title;
    private final String content;

    private ArticleDetailResponse(Long id, String author, String title, String content) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.content = content;
    }

    public static ArticleDetailResponse of(Article article, User user) {
        return new ArticleDetailResponse(article.getId(), user.getName(), article.getTitle(), article.getContent());
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\" : \"" + id + "\"," +
                "\"author\" : \"" + author + "\"," +
                "\"title\" : \"" + title + "\"," +
                "\"content\" : \"" + content + "\"" +
                "}";
    }
}
