package codesquad.servlet.database.connection;

import codesquad.domain.model.User;
import codesquad.servlet.database.exception.InvalidDataAccessException;
import codesquad.servlet.database.exception.InvalidDataSourceException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    private final DatabaseConnector databaseConnector;

    public UserDao(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    public Long insert(User user) {
        String sql = "INSERT INTO users (user_id, password, name, email) VALUES (?, ?, ?, ?)";

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, user.getUserId());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setString(4, user.getEmail());

            int rowCounts = preparedStatement.executeUpdate();
            if (rowCounts == 0) {
                throw new InvalidDataAccessException("Failed to insert user into database. No rows affected");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long generatedKey = generatedKeys.getLong(1);
                    user.setPrimaryKey(generatedKey);
                    return generatedKey;
                } else {
                    throw new InvalidDataAccessException("Failed to generate primary key for user.");
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
