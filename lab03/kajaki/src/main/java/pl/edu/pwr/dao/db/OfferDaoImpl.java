package pl.edu.pwr.dao.db;

import pl.edu.pwr.dao.OfferDao;
import pl.edu.pwr.dao.RowMapper;
import pl.edu.pwr.exceptions.DataAccessException;
import pl.edu.pwr.exceptions.DatabaseConnectionException;
import pl.edu.pwr.model.Offer;
import pl.edu.pwr.util.DbConnectionProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OfferDaoImpl implements OfferDao {

    public static final RowMapper<Offer> offerMapper = rs ->
            new Offer(
                    rs.getInt("id"),
                    rs.getTimestamp("term").toLocalDateTime(),
                    rs.getString("location"),
                    rs.getInt("max_slots"),
                    rs.getInt("vacant_slots"),
                    rs.getInt("organizer_id")
            );

    @Override
    public List<Offer> getAll() throws DataAccessException, DatabaseConnectionException {
        String sql = "SELECT * FROM Offers";
        return DbHelper.findAll(sql, offerMapper);
    }

    @Override
    public Offer getOne(int id) throws DatabaseConnectionException, DataAccessException {
        String sql = "SELECT * FROM Offers WHERE id = ?";
        return DbHelper.getOne(sql, id, offerMapper);
    }

    @Override
    public void create(Offer offer) throws DataAccessException, DatabaseConnectionException {
        String sql = "INSERT INTO Offers (term, location, max_slots, vacant_slots, organizer_id) VALUES (?, ?, ?, ?, ?)";
        DbHelper.executeUpdate(sql,
                offer.getTerm(),
                offer.getLocation(),
                offer.getMaxSlots(),
                offer.getVacantSlots(),
                offer.getOrganizerId()
        );
    }

    @Override
    public void update(Offer offer) throws DataAccessException, DatabaseConnectionException {
        String sql = "UPDATE Offers SET term = ?, location = ?, max_slots = ?, vacant_slots = ?, organizer_id = ? WHERE id = ?";
        DbHelper.executeUpdate(sql,
                offer.getTerm(),
                offer.getLocation(),
                offer.getMaxSlots(),
                offer.getVacantSlots(),
                offer.getOrganizerId(),
                offer.getId()
        );
    }

    @Override
    public void delete(int id) throws DataAccessException, DatabaseConnectionException {
        String sql = "DELETE FROM Offers WHERE id = ?";
        DbHelper.executeUpdate(sql, id);
    }

    @Override
    public List<Offer> findByDate(LocalDate date) throws DataAccessException, DatabaseConnectionException {
        String sql = "SELECT * FROM Offers WHERE CAST(term AS DATE) = ?";
        List<Offer> results = new ArrayList<>();

        try (Connection connection = DbConnectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, date);
            ResultSet response = statement.executeQuery();

            while (response.next()) {
                results.add(offerMapper.map(response));
            }

            return results;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to fetch offers by date", e);
        } catch (DatabaseConnectionException e) {
            throw new DatabaseConnectionException("Failed to connect to database", e);
        }
    }
}