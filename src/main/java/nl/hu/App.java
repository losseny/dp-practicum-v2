package nl.hu;

import nl.hu.data.ReizigerDAOPsql;
import nl.hu.data.interfaces.ReizigerDAO;
import nl.hu.domain.Reiziger;
import nl.hu.infrastructure.config.Connect;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
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
        try {
            ReizigerDAOPsql reizigerDAOPsql = new ReizigerDAOPsql(CONNECTION);

            testReizigerDAO(reizigerDAOPsql);
            Connect.closeConnection();
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
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

    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        LocalDate gbdatum = Date.valueOf("1981-03-14").toLocalDate();
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", gbdatum);
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        // Voeg aanvullende tests van de ontbrekende CRUD-operaties in.


        //        update en find by id
        System.out.print("[Test] Eerst ( " + sietske + " ) , na ReizigerDAO.update() \n");
        Reiziger reiziger = new Reiziger(sietske.getId(), "H", "k", "Bert", gbdatum);
        rdao.update(reiziger);
        System.out.println(rdao.findById(reiziger.getId()) + "\n");

        //        delete
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.delete() ");
        rdao.delete(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");
    }
}
