package nl.hu.infrastructure.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {

    private static final String DATABASE_URL = "jdbc:postgresql://localhost:15432/student-db-database-v2";
    private static final String PASSWORD = "admin";
    private static final String USERNAME = "admin";

    private static Connection CONNECTION;


    public static Connection getConnection() throws SQLException {
        if (CONNECTION == null) {
            CONNECTION = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
        }
        System.out.println("Connected to database...");
        return CONNECTION;
    }

    public static void closeConnection() throws SQLException {
        if (CONNECTION != null) {
            CONNECTION.close();
            CONNECTION = null;
        }
    }
}
