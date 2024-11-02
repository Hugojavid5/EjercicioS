package org.hugo.ejercicios.BBDD;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class ConexionBBDD {
    private final Connection connection;
    public ConexionBBDD() throws SQLException, FileNotFoundException {
        Properties props = loadProperties();
        String url = props.getProperty("dburl");
        connection = DriverManager.getConnection(url, props);

        DatabaseMetaData databaseMetaData = connection.getMetaData();
    }
    public Connection getConnection() {
        return connection;
    }
    public Connection closeConexion() throws SQLException {
        connection.close();
        return connection;
    }
    public static Properties loadProperties() throws FileNotFoundException {
        try (FileInputStream fs = new FileInputStream("db.properties")) {
            Properties props = new Properties();
            props.load(fs);
            return props;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
