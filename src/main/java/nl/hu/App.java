package nl.hu;

import nl.hu.data.AdresDAOPsql;
import nl.hu.data.OVChipkaartDAOPsql;
import nl.hu.data.ReizigerDAOPsql;
import nl.hu.data.interfaces.AdresDAO;
import nl.hu.data.interfaces.ReizigerDAO;
import nl.hu.domain.Adres;
import nl.hu.domain.OVChipkaart;
import nl.hu.domain.Reiziger;
import nl.hu.infrastructure.config.Connect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Hello world!
 */
public class App {

    private static final Connection CONNECTION;
    private static final Reiziger newReiziger = new Reiziger(10, "Z", "P", "Hart", LocalDate.parse("2005-12-23"));
    private static Adres adres = new Adres(6, "2000FT", "9", "Herengracht", "Amsterdam", newReiziger);

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
        ReizigerDAOPsql reizigerDAOPsql = null;
        AdresDAOPsql adresDAOPsql = null;
        OVChipkaartDAOPsql ovChipkaartDAOPsql;
        try {
            reizigerDAOPsql = new ReizigerDAOPsql(CONNECTION);
            adresDAOPsql = new AdresDAOPsql(CONNECTION);
            ovChipkaartDAOPsql = new OVChipkaartDAOPsql(CONNECTION, reizigerDAOPsql);

            testReizigerDAO(reizigerDAOPsql);
            testAdresDAO(adresDAOPsql);
            testOVChipkaartDAO(ovChipkaartDAOPsql);

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            System.out.println(sqlException.getMessage());
        } finally {
            if (reizigerDAOPsql != null) {
                reizigerDAOPsql.delete(newReiziger);
                System.out.println("Deleted new reiziger");
            }
            if (adresDAOPsql != null) {
                adresDAOPsql.delete(adres);
                System.out.println("Deleted new adres");
            }
            Connect.closeConnection();
            System.out.println("\nConnection closed");
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
        LocalDate gbdatum = LocalDate.parse("1981-03-14");
        Reiziger sietske = new Reiziger(9, "S", "", "Boers", gbdatum, new Adres(reizigers.get(reizigers.size() - 1).getId(), "postcode", "3", "woonstraat", "Amsterdam"));

        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        // Voeg aanvullende tests van de ontbrekende CRUD-operaties in.


        //        update en find by id
        System.out.print("[Test] Eerst ( " + sietske + " ) , na ReizigerDAO.update() \n");
        Reiziger reiziger = new Reiziger(sietske.getId(), "H", "k", "Bert", gbdatum, new Adres(sietske.getAdres().getAdresId(), "2004TH", "2", "reigerstraat", "amsterdam", sietske));

        rdao.update(reiziger);
        System.out.println(rdao.findById(reiziger.getId()) + "\n");

        //        delete
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.delete() ");
        rdao.delete(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        //        voor adres test!!!
        rdao.save(newReiziger);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");
    }

    private static void testAdresDAO(AdresDAO adao) throws SQLException {
        System.out.println("\n---------- Test AdresDAO -------------");

        // Haal alle adressen op uit de database
        List<Adres> adresList = adao.findAll();
        System.out.println("[Test] AdresDAO.findAll() geeft de volgende adressen:");
        for (Adres adres : adresList) {
            System.out.println(adres);
        }

        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        System.out.print("[Test] Eerst " + adresList.size() + " adressen, na AdresDAO.save() ");
        adao.save(adres);
        adresList = adao.findAll();
        System.out.println(adresList.size() + " adressen\n");

        //        update en find by id
        System.out.print("[Test] Eerst ( " + adres + " ), voor AdresDAO.update(): \n");
        adao.update(new Adres(adres.getAdresId(), "1709XG", "5", "Van Woustraat", "Amsterdam", newReiziger));
        adres = adao.findById(adres.getAdresId());
        System.out.print("[Test] Eerst ( " + adres + " ), na adresDAO.update(): \n\n");

        //        find by reiziger
        adres = adao.findByReiziger(newReiziger);
        System.out.println("[Test] AdresDAO.findByReiziger() geeft de volgende adres:");
        System.out.println(adres + "\n");

        //        delete
        System.out.print("[Test] Eerst " + adresList.size() + " adressen, na AdresDAO.delete() ");
        adao.delete(adres);
        adresList = adao.findAll();
        System.out.println(adresList.size() + " adressen\n");

    }

    private static void testOVChipkaartDAO(OVChipkaartDAOPsql ovdao) throws SQLException {
        System.out.println("\n---------- Test OVChipkaartDAO -------------");

        // Haal alle OVChipkaarten op uit de database
        List<OVChipkaart> ovChipkaartList = ovdao.findAll();
        System.out.println("[Test] OVChipkaartDAO.findAll() geeft de volgende OVChipkaarten:");
        for (OVChipkaart ovChipkaart : ovChipkaartList) {
            System.out.println(ovChipkaart);
        }

        System.out.println();

        // Maak een nieuwe OVChipkaart aan en persisteer deze in de database
        OVChipkaart newOVChipkaart = new OVChipkaart(12345, LocalDate.parse("2025-12-31"), 2, 50.0, newReiziger);
        System.out.print("[Test] Eerst " + ovChipkaartList.size() + " OVChipkaarten, na OVChipkaartDAO.save() ");
        ovdao.save(newOVChipkaart);
        ovChipkaartList = ovdao.findAll();
        System.out.println(ovChipkaartList.size() + " OVChipkaarten\n");

        // Update en find by id
        System.out.print("[Test] Eerst ( " + newOVChipkaart + " ), voor OVChipkaartDAO.update(): \n");
        newOVChipkaart.setKlasse(1);
        newOVChipkaart.setSaldo(75.0);
        ovdao.update(newOVChipkaart);
        OVChipkaart updatedOVChipkaart = ovdao.findById(newOVChipkaart.getKaart_nummer());
        System.out.print("[Test] Eerst ( " + updatedOVChipkaart + " ), na OVChipkaartDAO.update(): \n\n");

        // Find by reiziger
        List<OVChipkaart> ovChipkaartenByReiziger = ovdao.findByReiziger(newReiziger);
        System.out.println("[Test] OVChipkaartDAO.findByReiziger() geeft de volgende OVChipkaarten:");
        for (OVChipkaart ovChipkaart : ovChipkaartenByReiziger) {
            System.out.println(ovChipkaart);
        }
        System.out.println();

        // Delete
        System.out.print("[Test] Eerst " + ovChipkaartList.size() + " OVChipkaarten, na OVChipkaartDAO.delete() ");
        ovdao.delete(newOVChipkaart);
        ovChipkaartList = ovdao.findAll();
        System.out.println(ovChipkaartList.size() + " OVChipkaarten\n");
    }
}
