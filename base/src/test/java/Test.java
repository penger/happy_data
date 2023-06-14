import cn.happy.component.JdbcComponent;
import cn.happy.factory.JdbcFactory;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class Test {

    public static void main(String[] args) {
        JdbcComponent.JdbcParam jdbcParam = JdbcFactory.getJdbc(JdbcFactory.DEFAULT_MYSQL);
        jdbcParam.setPassword("cuowu");
        try {
            Connection connection = JdbcComponent.getConnection(jdbcParam);
            DatabaseMetaData metaData = connection.getMetaData();

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
