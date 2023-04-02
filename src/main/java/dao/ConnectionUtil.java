package dao;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class ConnectionUtil {

    public Properties loadPropertiesFile() throws Exception {

        Properties prop = new Properties();
        InputStream in = new FileInputStream("src/main/resources/jdbc.properties");
        prop.load(in);
        in.close();
        return prop;
    }

    public Properties propertiesForConnection () {

        Properties prop = new Properties();
        try {
            prop = loadPropertiesFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return prop;
    }
}
