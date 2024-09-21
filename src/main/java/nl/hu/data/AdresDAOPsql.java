package nl.hu.data;


import nl.hu.data.interfaces.AdresDAO;
import nl.hu.domain.Adres;
import nl.hu.domain.Reiziger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class AdresDAOPsql implements AdresDAO {

    private final String adresId = "adres_id";
    private final String postcode = "postcode";
    private final String huisnummer = "huisnummer";
    private final String straat = "straat";
    private final String woonplaats = "woonplaats";

    public static final String add_adres_statement = "INSERT INTO adres (adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id) VALUES (?, ?, ?, ?, ?, ?)";  // veranderen
    public static final String update_adres_statement = "UPDATE adres SET postcode=?, huisnummer=?, straat=?, woonplaats=?, reiziger_id=? WHERE adres_id = ?";
    public static final String delete_row_statement = "DELETE FROM adres WHERE adres_id = ?";
    public static final String get_all_adressen = "SELECT * FROM adres ORDER BY adres_id";
    public static final String get_adres_id_statement = "SELECT * FROM adres WHERE adres_id = ? ORDER BY adres_id";
    public static final String get_adres_by_reiziger_statement = "SELECT * FROM adres WHERE reiziger_id = ? ORDER BY adres_id";

    private final Connection connection;

    public AdresDAOPsql(Connection connection) throws SQLException {
        this.connection = connection;
    }

    /**
     * @param adres
     * @return
     * @throws SQLException
     */
    @Override
    public boolean save(Adres adres) throws SQLException {
        if (findById(adres.getAdresId()) != null) {
            return false;
        }

        PreparedStatement preparedStatement = connection.prepareStatement(add_adres_statement);

        preparedStatement.setInt(1, adres.getAdresId());
        preparedStatement.setString(2, adres.getPostcode());
        preparedStatement.setString(3, adres.getHuisnummer());
        preparedStatement.setString(4, adres.getStraat());
        preparedStatement.setString(5, adres.getWoonplaats());
        preparedStatement.setInt(6, adres.getReiziger().getId());

        return preparedStatement.execute();
    }

    /**
     * @param adres
     * @return
     * @throws SQLException
     */
    @Override
    public boolean update(Adres adres) throws SQLException {

        if (findById(adres.getAdresId()) == null) {
            return false;
        }

        PreparedStatement preparedStatement = connection.prepareStatement(update_adres_statement);
        preparedStatement.setString(1, adres.getPostcode());
        preparedStatement.setString(2, adres.getHuisnummer());
        preparedStatement.setString(3, adres.getStraat());
        preparedStatement.setString(4, adres.getWoonplaats());
        preparedStatement.setInt(5, adres.getReiziger().getId());
        preparedStatement.setInt(6, adres.getAdresId());

        return preparedStatement.execute();
    }

    /**
     * @param adres
     * @return
     * @throws SQLException
     */
    @Override
    public boolean delete(Adres adres) throws SQLException {
        if (findById(adres.getAdresId()) == null) {
            return false;
        }
        PreparedStatement preparedStatement = connection.prepareStatement(delete_row_statement);
        preparedStatement.setInt(1, adres.getAdresId());
        return preparedStatement.execute();
    }

    /**
     * @return
     * @throws SQLException
     */
    @Override
    public List<Adres> findAll() throws SQLException {
        List<Adres> adresList = new ArrayList<>();

        ResultSet resultSet = connection.prepareStatement(get_all_adressen).executeQuery();

        while (resultSet.next()) {
            adresList.add(
                    new Adres(
                            resultSet.getInt(adresId),
                            resultSet.getString(postcode),
                            resultSet.getString(huisnummer),
                            resultSet.getString(straat),
                            resultSet.getString(woonplaats),
                            new Reiziger(


                            )
                    )
            );
        }
        resultSet.close();
        return adresList;
    }

    /**
     * @param id
     * @return
     * @throws SQLException
     */
    @Override
    public Adres findById(int id) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement(get_adres_id_statement);

        preparedStatement.setInt(1, id);

        ResultSet resultSet = preparedStatement.executeQuery();

        return getAdres(resultSet);
    }

    private Adres getAdres(ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) {
            resultSet.close();
            return null;
        }

        return new Adres(
                resultSet.getInt(adresId),
                resultSet.getString(postcode),
                resultSet.getString(huisnummer),
                resultSet.getString(straat),
                resultSet.getString(woonplaats)
        );
    }

    /**
     * @param reiziger
     * @return
     */
    @Override
    public Adres findByReiziger(Reiziger reiziger) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(get_adres_by_reiziger_statement);

        preparedStatement.setInt(1, reiziger.getId());
        ResultSet resultSet = preparedStatement.executeQuery();

        return getAdres(resultSet);
    }

}
