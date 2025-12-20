package pl.edu.pwr.dao.db;

import pl.edu.pwr.dao.ReservationDao;
import pl.edu.pwr.dao.RowMapper;
import pl.edu.pwr.exceptions.DataAccessException;
import pl.edu.pwr.exceptions.DatabaseConnectionException;
import pl.edu.pwr.model.Reservation;
import pl.edu.pwr.model.ReservationStatus;
import pl.edu.pwr.util.DbConnectionProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReservationDaoImpl implements ReservationDao {

    private static final RowMapper<Reservation> reservationMapper = rs ->
            new Reservation(rs.getInt("id"), rs.getInt("organizer_id"), rs.getInt("offer_id"), rs.getInt("client_id"), rs.getInt("slots"), ReservationStatus.valueOf(rs.getString("status")));

    @Override
    public List<Reservation> getAll() throws DataAccessException, DatabaseConnectionException {
        String sql = "SELECT * FROM Reservations";
        return DbHelper.findAll(sql, reservationMapper);
    }

    @Override
    public Reservation getOne(int id) throws DatabaseConnectionException, DataAccessException {
        String sql = "SELECT * FROM Reservations WHERE id = ?";
        return DbHelper.getOne(sql, id, reservationMapper);
    }

    @Override
    public void create(Reservation reservation) throws DataAccessException, DatabaseConnectionException {
        String sql = "INSERT INTO Reservations (organizer_id, offer_id, client_id, slots, status) VALUES (?, ?, ?, ?, ?)";
        DbHelper.executeUpdate(sql,
                reservation.getOrganizerId(),
                reservation.getOfferId(),
                reservation.getClientId(),
                reservation.getSlots(),
                reservation.getStatus().name()
        );
    }

    @Override
    public void update(Reservation reservation) throws DataAccessException, DatabaseConnectionException {
        String sql = "UPDATE Reservations SET organizer_id = ?, offer_id = ?, client_id = ?, slots = ?, status = ? WHERE id = ?";
        DbHelper.executeUpdate(sql,
                reservation.getOrganizerId(),
                reservation.getOfferId(),
                reservation.getClientId(),
                reservation.getSlots(),
                reservation.getStatus().name(),
                reservation.getId()
        );
    }

    @Override
    public void delete(int id) throws DataAccessException, DatabaseConnectionException {
        String sql = "DELETE FROM Reservations WHERE id = ?";
        DbHelper.executeUpdate(sql, id);
    }

    @Override
    public List<Reservation> findByClientId(int clientId) throws DataAccessException, DatabaseConnectionException {
        String sql = "SELECT * FROM Reservations WHERE client_id = ?";
        List<Reservation> results = new ArrayList<>();

        try (Connection connection = DbConnectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, clientId);
            ResultSet response = statement.executeQuery();

            while (response.next()) {
                results.add(reservationMapper.map(response));
            }

            return results;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to fetch reservations by client ID", e);
        } catch (DatabaseConnectionException e) {
            throw new DatabaseConnectionException("Failed to connect to database", e);
        }
    }
    @Override
    public List<Reservation> findByOfferId(int offerId) throws DataAccessException, DatabaseConnectionException {
        String sql = "SELECT * FROM Reservations WHERE offer_id = ?";
        List<Reservation> results = new ArrayList<>();

        try (Connection connection = DbConnectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, offerId);
            ResultSet response = statement.executeQuery();

            while (response.next()) {
                results.add(reservationMapper.map(response));
            }

            return results;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to fetch reservations by offer ID", e);
        } catch (DatabaseConnectionException e) {
            throw new DatabaseConnectionException("Failed to connect to database", e);
        }
    }
}