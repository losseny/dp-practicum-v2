package nl.hu.data.interfaces;


import nl.hu.domain.OVChipkaart;
import nl.hu.domain.Reiziger;
import nl.hu.infrastructure.crud.CustomCRUD;

import java.sql.SQLException;
import java.util.List;

public interface OVChipkaartDAO extends CustomCRUD<OVChipkaart> {

    List<OVChipkaart> findByReiziger(Reiziger reiziger) throws SQLException;
}
