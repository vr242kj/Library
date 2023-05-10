package com.example.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionUtil {
    private static final Properties properties = loadPropertiesFile();

    private static Properties loadPropertiesFile () {
        Properties prop = new Properties();

        try (InputStream inputStream = new FileInputStream("src/main/resources/jdbc.properties")) {
            prop.load(inputStream);

        } catch (IOException e) {
            System.out.println("Unable to find property file" + "\nError details: " + e.getMessage());
            System.exit(0);
        }
        return prop;
    }

    public static Connection createConnection () {

        Connection connection = null;

        String url = properties.getProperty("url");
        String username = properties.getProperty("user");
        String password = properties.getProperty("password");

        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println("Unable to create connection" + "\nError details: " + e.getMessage());
            System.exit(0);
        }
        return connection;
    }
}
