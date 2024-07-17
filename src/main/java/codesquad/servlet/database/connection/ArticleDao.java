package codesquad.servlet.database.connection;

import codesquad.domain.ArticleStorage;
import codesquad.domain.model.Article;
import codesquad.servlet.database.exception.InvalidDataAccessException;
import codesquad.servlet.database.exception.InvalidDataSourceException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArticleDao implements ArticleStorage {

    private static final Logger logger = LoggerFactory.getLogger(ArticleDao.class);

    private final DatabaseConnector databaseConnector;

    public ArticleDao(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    @Override
    public Long insert(Article article) {
        String sql = "INSERT INTO article (author_id, title, content) VALUES (?, ?, ?)";

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setLong(1, article.getAuthorId());
            preparedStatement.setString(2, article.getTitle());
            preparedStatement.setString(3, article.getContent());

            int rowCounts = preparedStatement.executeUpdate();
            if (rowCounts == 0) {
                throw new InvalidDataAccessException("Failed to insert article into database. No rows affected");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long generatedKey = generatedKeys.getLong(1);
                    article.setPrimaryKey(generatedKey);
                    return generatedKey;
                } else {
                    throw new InvalidDataAccessException("Failed to generate primary key for article.");
                }
            }

        } catch (InvalidDataAccessException e) {
            logger.debug("[SQL Insert Exception]", e);
            throw e;
        } catch (SQLException e) {
            logger.debug("[Database Connection Exception]", e);
            throw new InvalidDataSourceException(e);
        }
    }
}
