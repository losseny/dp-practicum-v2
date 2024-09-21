package nl.hu.data.interfaces;


import nl.hu.domain.Adres;
import nl.hu.domain.Reiziger;
import nl.hu.infrastructure.crud.CustomCRUD;

import java.sql.SQLException;

public interface AdresDAO extends CustomCRUD<Adres> {
    Adres findByReiziger(Reiziger reiziger) throws SQLException;

}
