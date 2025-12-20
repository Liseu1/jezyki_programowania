package pl.edu.pwr.util;

import pl.edu.pwr.exceptions.DataAccessException;
import pl.edu.pwr.exceptions.DatabaseConnectionException;

import java.sql.SQLException;

public class DbConnectionProvider {
    public static java.sql.Connection getConnection() throws DatabaseConnectionException {
        try {
            return java.sql.DriverManager.getConnection(Config.dbPath);
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to find the database", e);
        }
    }
}
