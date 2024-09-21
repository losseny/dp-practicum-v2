package nl.hu.data;


import nl.hu.data.interfaces.ReizigerDAO;
import nl.hu.domain.Reiziger;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class ReizigerDAOPsql implements ReizigerDAO {

    private final Connection connection;
    private final String idNaam = "reiziger_id";
    private final String voorletterColum = "voorletters";
    private final String tussenvoegselColum = "tussenvoegsel";
    private final String achternaamColum = "achternaam";
    private final String geboortedatumColum = "geboortedatum";

    private final String add_reiziger_statement = "INSERT INTO reiziger (reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) VALUES (?, ?, ?, ?, ?)";
    private final String get_reiziger_by_id_statement = "SELECT * FROM reiziger WHERE reiziger_id = ? ORDER BY reiziger_id";
    private final String get_reizigers_statement = "SELECT * FROM reiziger ORDER BY reiziger_id";
    private final String update_reiziger_statement = "UPDATE reiziger SET voorletters=?, tussenvoegsel=?, achternaam=?, geboortedatum=? WHERE reiziger_id = ?";
    private final String delete_row_by_id_statement = "DELETE FROM reiziger WHERE reiziger_id = ?";

    public ReizigerDAOPsql(Connection connection) {
        this.connection = connection;
    }

    /**
     * @param reiziger
     * @return
     */
    @Override
    public boolean save(Reiziger reiziger) throws SQLException {
        if (findById(reiziger.getId()) != null) {
            return false;
        }

        PreparedStatement preparedStatement = connection.prepareStatement(add_reiziger_statement);
        preparedStatement.setInt(1, reiziger.getId());
        preparedStatement.setString(2, reiziger.getVoorletter());
        preparedStatement.setString(3, reiziger.getTussenvoegsel());
        preparedStatement.setString(4, reiziger.getAchternaam());
        preparedStatement.setDate(5, Date.valueOf(reiziger.getGeboortedatum()));
        return preparedStatement.execute();

    }

    /**
     * @param reiziger
     * @return
     */
    @Override
    public boolean update(Reiziger reiziger) throws SQLException {
        if (findById(reiziger.getId()) == null) {
            return false;
        }

        PreparedStatement preparedStatement = connection.prepareStatement(update_reiziger_statement);

        preparedStatement.setString(1, reiziger.getVoorletter());
        preparedStatement.setString(2, reiziger.getTussenvoegsel());
        preparedStatement.setString(3, reiziger.getAchternaam());
        preparedStatement.setDate(4, Date.valueOf(reiziger.getGeboortedatum()));
        preparedStatement.setInt(5, reiziger.getId());

        return preparedStatement.execute();
    }

    /**
     * @param reiziger
     * @return
     */
    @Override
    public boolean delete(Reiziger reiziger) throws SQLException {
        if (findById(reiziger.getId()) == null) {
            return false;
        }
        PreparedStatement preparedStatement = connection.prepareStatement(delete_row_by_id_statement);
        preparedStatement.setInt(1, reiziger.getId());
        return preparedStatement.execute();
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Reiziger findById(int id) throws SQLException {
        PreparedStatement preparedStatementReiziger = connection.prepareStatement(get_reiziger_by_id_statement);
        preparedStatementReiziger.setInt(1, id);
        ResultSet resultSet = preparedStatementReiziger.executeQuery();


        if (!resultSet.next()) {
            return null;
        }
        return new Reiziger(
                resultSet.getInt(idNaam),
                resultSet.getString(voorletterColum),
                resultSet.getString(tussenvoegselColum),
                resultSet.getString(achternaamColum),
                LocalDate.parse(resultSet.getDate(geboortedatumColum).toString())
        );
    }


    /**
     * @return List
     */
    @Override
    public List<Reiziger> findAll() throws SQLException {
        List<Reiziger> reizigerList = new ArrayList<>();

        ResultSet resultSet = connection.prepareStatement(get_reizigers_statement).executeQuery();

        while (resultSet.next()) {
            reizigerList.add(
                    new Reiziger(
                            resultSet.getInt(idNaam),
                            resultSet.getString(voorletterColum),
                            resultSet.getString(tussenvoegselColum),
                            resultSet.getString(achternaamColum),
                            LocalDate.parse(resultSet.getDate(geboortedatumColum).toString())
                    )
            );
        }
        return reizigerList;
    }
}
