package config;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    private static final String DB_URL = "jdbc:sqlite:database/parkir.db";

    public static Connection connect() {
        try {
            return DriverManager.getConnection(DB_URL);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
