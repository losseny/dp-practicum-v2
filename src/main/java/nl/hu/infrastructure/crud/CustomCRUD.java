package nl.hu.infrastructure.crud;

import java.sql.SQLException;
import java.util.List;

public interface CustomCRUD<T> {

    boolean save(T object) throws SQLException;

    boolean update(T object) throws SQLException;

    boolean delete(T object) throws SQLException;

    List<T> findAll() throws SQLException;

    T findById(int id) throws SQLException;
}
