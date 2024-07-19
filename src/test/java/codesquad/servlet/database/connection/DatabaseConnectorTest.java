package codesquad.servlet.database.connection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import codesquad.configuration.TestDatabaseConfiguration;
import codesquad.servlet.database.property.DataSourceProvider;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DatabaseConnectorTest {

    Logger logger = LoggerFactory.getLogger(DatabaseConnectorTest.class);
    DatabaseConnector databaseConnector = TestDatabaseConfiguration.getDatabaseConnector();

    @Test
    @DisplayName("[Success] DataSource 프로퍼티 정보로 데이터베이스 연결을 성공한다.")
    void test() {
        try {
            Connection connection = databaseConnector.getConnection();
            assertThat(connection).isNotNull();

            DatabaseMetaData metaData = connection.getMetaData();
            logger.info("metaData: " + metaData);
//            assertThat(metaData.getURL()).isEqualTo(TestDatabaseConfiguration.getDataSourceProvider().getJdbcUrl());

        } catch (SQLException e) {
            logger.debug("데이터베이스 연결 테스트 실패", e);
        }
    }

    @Test
    @DisplayName("[Exception] 잘못된 DataSource 정보면 DB 커넥션을 획득하는데 실패한다")
    void test2() {
        // given
        DataSourceProvider dataSourceProvider = new DataSourceProvider(
                "이상한JdbcURL", "이상한사용자이름", "이상한비밀번호");
        DatabaseConnector databaseConnector = new DatabaseConnector(dataSourceProvider);

        // when then
        assertThatThrownBy(databaseConnector::getConnection).isInstanceOf(SQLException.class);
    }

}