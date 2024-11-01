package BBDD;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionBBDD {
    private final Connection connection;

    public ConexionBBDD() throws SQLException {
        Properties connConfig = new Properties();
        connConfig.setProperty("user", "root");
        connConfig.setProperty("password", "mypass");
        connection = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:33066/animales?serverTimezone=Europe/Madrid", connConfig);
        connection.setAutoCommit(true);
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        connection.setAutoCommit(true);
    }
    public Connection getConnection() {
        return connection;
    }
    public Connection closeConnection() throws SQLException{
        connection.close();
        return connection;
    }

}
