package dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionUtil {
    private static Properties loadPropertiesFile () {

        Properties properties = new Properties();

        try (InputStream inputStream = new FileInputStream("src/main/resources/jdbc.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            System.out.println("Unable to find property file" + "\nError details: " + e.getMessage());
        }
        return properties;
    }

    public static Connection createConnection () {

        Connection connection = null;

        Properties prop = loadPropertiesFile();
        String url = prop.getProperty("url");
        String username = prop.getProperty("username");
        String password = prop.getProperty("password");

        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println("Unable to create connection" + "\nError details: " + e.getMessage());
            System.exit(0);
        }
        return connection;
    }
}
