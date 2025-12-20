package pl.edu.pwr.dao.db;

import pl.edu.pwr.dao.Dao;
import pl.edu.pwr.dao.RowMapper;
import pl.edu.pwr.exceptions.DataAccessException;
import pl.edu.pwr.exceptions.DatabaseConnectionException;
import pl.edu.pwr.model.Client;

import java.util.List;

public class ClientDao implements Dao<Client> {

    private static final RowMapper<Client> clientMapper = rs ->
            new Client(rs.getInt("id"), rs.getString("name"));

    @Override
    public List<Client> getAll() throws DataAccessException, DatabaseConnectionException {
        String sql = "SELECT * FROM Clients";

        return DbHelper.findAll(sql, clientMapper);
    }

    @Override
    public Client getOne(int id) throws DataAccessException, DatabaseConnectionException {
        String sql = "SELECT * FROM Clients Where id = ?;";

        return DbHelper.getOne(sql, id, clientMapper);
    }

    @Override
    public void create(Client client) throws DataAccessException, DatabaseConnectionException {
        String sql = "INSERT INTO Clients (name) VALUES (?)";


        DbHelper.executeUpdate(sql, client.getName());
    }

    @Override
    public void update(Client client) throws DataAccessException, DatabaseConnectionException {
        String sql = "UPDATE Clients SET name = ? WHERE id = ?";
        DbHelper.executeUpdate(sql,
                client.getName(),
                client.getId()
        );
    }

    @Override
    public void delete(int id) throws DataAccessException, DatabaseConnectionException {
        String sql = "DELETE FROM Clients WHERE id = ?";
        DbHelper.executeUpdate(sql, id);
    }

}