package pl.edu.pwr.dao.db;

import pl.edu.pwr.dao.Dao;
import pl.edu.pwr.dao.RowMapper;
import pl.edu.pwr.exceptions.DataAccessException;
import pl.edu.pwr.exceptions.DatabaseConnectionException;
import pl.edu.pwr.model.Organizer;

import java.util.List;

public class OrganizerDao implements Dao<Organizer> {

    private static final RowMapper<Organizer> organizerMapper = rs ->
            new Organizer(rs.getInt("id"), rs.getString("name"));
    @Override
    public List<Organizer> getAll() throws DataAccessException, DatabaseConnectionException {
        String sql = "SELECT * FROM Organizers";

        return DbHelper.findAll(sql, organizerMapper);
    }

    @Override
    public Organizer getOne(int id) throws DatabaseConnectionException, DataAccessException {
        String sql = "SELECT * FROM Organizers where id = ?";

        return DbHelper.getOne(sql, id, organizerMapper);
    }

    @Override
    public void create(Organizer organizer) throws DataAccessException, DatabaseConnectionException {
        String sql = "INSERT INTO Organizers (name) VALUES (?)";
        DbHelper.executeUpdate(sql, organizer.getName());
    }

    @Override
    public void update(Organizer organizer) throws DataAccessException, DatabaseConnectionException {
        String sql = "UPDATE Organizers SET name = ? WHERE id = ?";
        DbHelper.executeUpdate(sql,
                organizer.getName(),
                organizer.getId()
        );
    }
    @Override
    public void delete(int id) throws DataAccessException, DatabaseConnectionException {
        String sql = "DELETE FROM Organizers WHERE id = ?";
        DbHelper.executeUpdate(sql, id);
    }
}
