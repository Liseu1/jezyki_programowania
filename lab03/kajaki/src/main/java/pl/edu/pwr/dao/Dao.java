package pl.edu.pwr.dao;

import pl.edu.pwr.exceptions.DataAccessException;
import pl.edu.pwr.exceptions.DatabaseConnectionException;

import java.util.List;

public interface Dao<T> {
    List<T> getAll() throws DataAccessException, DatabaseConnectionException;

    T getOne(int id) throws DatabaseConnectionException, DataAccessException;

    void create(T t) throws DatabaseConnectionException, DataAccessException;

    void update(T t) throws DatabaseConnectionException, DataAccessException;

    void delete(int id) throws DatabaseConnectionException, DataAccessException;
}
