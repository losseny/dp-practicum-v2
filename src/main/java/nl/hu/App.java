package nl.hu;

import nl.hu.database.config.Connect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Hello world!
 */
public class App {

    private static final Connection CONNECTION;

    static {
        System.out.println("Connecting to database...");
        try {
            CONNECTION = Connect.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws SQLException {
        testFetchAll();
    }

    /**
     * P6. Haal alle (geannoteerde) entiteiten uit de database.
     */
    private static void testFetchAll() throws SQLException {
        PreparedStatement preparedStatement = CONNECTION.prepareStatement("SELECT * FROM reiziger ORDER BY reiziger_id");
        ResultSet resultSet = preparedStatement.executeQuery();
        System.out.println("Alle reizigers:");
        while (resultSet.next()) {
            System.out.printf(
                    "\t#%d: %s. %s %s (%s)%n",
                    resultSet.getRow(),
                    resultSet.getString("voorletters"),
                    Optional.ofNullable(resultSet.getString("tussenvoegsel")).orElse(""),
                    resultSet.getString("achternaam"),
                    resultSet.getDate("geboortedatum")
            );

        }
    }
}
