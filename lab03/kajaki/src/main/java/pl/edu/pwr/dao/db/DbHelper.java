package pl.edu.pwr.dao.db;

import pl.edu.pwr.dao.RowMapper;
import pl.edu.pwr.exceptions.DataAccessException;
import pl.edu.pwr.exceptions.DatabaseConnectionException;
import pl.edu.pwr.exceptions.RecordNotFoundException;
import pl.edu.pwr.util.DbConnectionProvider;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbHelper {
    public static <T> List<T>  findAll(String sql, RowMapper<T> mapper) throws DataAccessException, DatabaseConnectionException {
        try (Connection connection = DbConnectionProvider.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet response = statement.executeQuery(sql);
            List<T> result = new ArrayList<>();
            while(response.next()) {
                result.add(mapper.map(response));
            }
            return result;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to fetch data", e);
        } catch(DatabaseConnectionException e) {
            throw new DatabaseConnectionException("Failed to connect to database", e);
        }
    }

    public static <T> T getOne(String sql, int id, RowMapper<T> mapper) throws DataAccessException, DatabaseConnectionException {
        try (Connection connection = DbConnectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet response = statement.executeQuery();
            if(response.next())
            {
                return mapper.map(response);
            } else {
                throw new RecordNotFoundException("Record with given ID does not exist");
            }


        } catch (SQLException e) {
            throw new DataAccessException("Failed to fetch data", e);
        } catch (DatabaseConnectionException e) {
            throw new DatabaseConnectionException("Failed to connect to database", e);
        }
    }

    public static void executeUpdate(String sql, Object... args) throws DataAccessException, DatabaseConnectionException {
        try (Connection connection = DbConnectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            for (int i = 0; i < args.length; i++) {
                statement.setObject(i + 1, args[i]);
            }

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Failed to execute update", e);
        } catch (DatabaseConnectionException e) {
            throw new DatabaseConnectionException("Failed to connect to database", e);
        }
    }
}
