package nl.hu.data;


import nl.hu.data.interfaces.OVChipkaartDAO;
import nl.hu.data.interfaces.ReizigerDAO;
import nl.hu.domain.OVChipkaart;
import nl.hu.domain.Reiziger;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class OVChipkaartDAOPsql implements OVChipkaartDAO {

    private final Connection connection;
    private final ReizigerDAO reizigerDAO;

    private final String kaart_nummer = "kaart_nummer";
    private final String geldig_tot = "geldig_tot";
    private final String klasse = "klasse";
    private final String saldo = "saldo";

    public final String get_all_ov_chipkaarten = "SELECT * FROM ov_chipkaart ORDER BY kaart_nummer";
    public static final String GET_REIZIGER_OV_STATEMENT = "SELECT * FROM ov_chipkaart WHERE reiziger_id = ? ORDER BY reiziger_id";
    public final String GET_REIZIGER_OV_ID_STATEMENT = "SELECT * FROM ov_chipkaart WHERE kaart_nummer = ? ORDER BY reiziger_id";
    public static final String ADD_OV_STATEMENT = "INSERT INTO ov_chipkaart (kaart_nummer, geldig_tot, klasse, saldo, reiziger_id) VALUES (?, ?, ?, ?, ?)";
    public static final String UPDATE_OV_STATEMENT = "UPDATE ov_chipkaart SET geldig_tot=?, klasse=?, saldo=? WHERE kaart_nummer = ?";
    public static final String DELETE_ROW_OV = "DELETE FROM ov_chipkaart WHERE kaart_nummer = ?";

    public OVChipkaartDAOPsql(Connection connection, ReizigerDAO reizigerDAO) {
        this.connection = connection;
        this.reizigerDAO = reizigerDAO;
    }

    /**
     * @param reiziger
     * @return
     */
    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) throws SQLException {

        Reiziger reizigerById = reizigerDAO.findById(reiziger.getId());

        if (reizigerById == null) {
            return null;
        } else {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_REIZIGER_OV_STATEMENT);
            preparedStatement.setInt(1, reiziger.getId());

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                reizigerById.getOvChipkaartList().add(
                        new OVChipkaart(
                                resultSet.getInt(kaart_nummer),
                                LocalDate.parse(resultSet.getDate(geldig_tot).toString()),
                                resultSet.getInt(klasse),
                                resultSet.getDouble(saldo)
                        )
                );
            }
        }
        return reiziger.getOvChipkaartList();
    }

    /**
     * @param ovChipkaart
     * @return
     * @throws SQLException
     */
    @Override
    public boolean save(OVChipkaart ovChipkaart) throws SQLException {
        if (findById(ovChipkaart.getKaart_nummer()) != null) {
            return false;
        }

        PreparedStatement preparedStatement = connection.prepareStatement(ADD_OV_STATEMENT);
        preparedStatement.setInt(1, ovChipkaart.getKaart_nummer());

        preparedStatement.setDate(2, Date.valueOf(ovChipkaart.getGeldigTot()));
        preparedStatement.setInt(3, ovChipkaart.getKlasse());
        preparedStatement.setDouble(4, ovChipkaart.getSaldo());
        preparedStatement.setInt(5, ovChipkaart.getReiziger().getId());

        return preparedStatement.execute();
    }

    /**
     * @param ovChipkaart
     * @return
     * @throws SQLException
     */
    @Override
    public boolean update(OVChipkaart ovChipkaart) throws SQLException {

        if (findById(ovChipkaart.getKaart_nummer()) == null) {
            return false;
        }

        PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_OV_STATEMENT);
        preparedStatement.setDate(1, Date.valueOf(ovChipkaart.getGeldigTot()));
        preparedStatement.setInt(2, ovChipkaart.getKlasse());
        preparedStatement.setDouble(3, ovChipkaart.getSaldo());
        preparedStatement.setInt(4, ovChipkaart.getKaart_nummer());

        return preparedStatement.execute();
    }

    /**
     * @param ovChipkaart
     * @return
     * @throws SQLException
     */
    @Override
    public boolean delete(OVChipkaart ovChipkaart) throws SQLException {

        if (findById(ovChipkaart.getKaart_nummer()) == null) {
            return false;
        }
        PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ROW_OV);
        preparedStatement.setInt(1, ovChipkaart.getKaart_nummer());
        return preparedStatement.execute();
    }

    /**
     * @return
     * @throws SQLException
     */
    @Override
    public List<OVChipkaart> findAll() throws SQLException {
        List<OVChipkaart> reizigerList = new ArrayList<>();

        ResultSet resultSet = connection.prepareStatement(get_all_ov_chipkaarten).executeQuery();

        while (resultSet.next()) {
            reizigerList.add(
                    new OVChipkaart(
                            resultSet.getInt(kaart_nummer),
                            LocalDate.parse(resultSet.getDate(geldig_tot).toString()),
                            resultSet.getInt(klasse),
                            resultSet.getDouble(saldo)
                    )
            );
        }
        return reizigerList;
    }

    /**
     * @param id
     * @return
     * @throws SQLException
     */
    @Override
    public OVChipkaart findById(int id) throws SQLException {
        PreparedStatement preparedStatementReiziger = connection.prepareStatement(GET_REIZIGER_OV_ID_STATEMENT);
        preparedStatementReiziger.setInt(1, id);
        ResultSet resultSet = preparedStatementReiziger.executeQuery();


        if (!resultSet.next()) {
            return null;
        }
        return new OVChipkaart(
                resultSet.getInt(kaart_nummer),
                LocalDate.parse(resultSet.getDate(geldig_tot).toString()),
                resultSet.getInt(klasse),
                resultSet.getDouble(saldo)
        );
    }
}
