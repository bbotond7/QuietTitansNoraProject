package hu.unideb.inf.config;

import java.io.IOException;
import java.util.Properties;

public class ContactConfiguration {

    private static Properties props = new Properties();

    static {
        try {
            props.load(ContactConfiguration.class.getResourceAsStream("/application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Properties getProps() {
        return props;
    }

    public static String getValue(String key) {
        return props.getProperty(key);
    }

}
