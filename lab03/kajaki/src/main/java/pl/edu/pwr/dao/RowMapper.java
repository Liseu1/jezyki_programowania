package pl.edu.pwr.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMapper<T> {
    public T map (ResultSet data) throws SQLException;
}
