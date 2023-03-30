package dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionUtil {

    public Connection connectionToDatabase (String dbname, String user, String pass) {

        Connection connection = null;

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbname, user, pass);
            if (connection != null) {
                System.out.println("Connection Established");
            } else {
                System.out.println("Connection Failed");
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return connection;
    }

}
