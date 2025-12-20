package pl.edu.pwr.dao;

import pl.edu.pwr.exceptions.DataAccessException;
import pl.edu.pwr.exceptions.DatabaseConnectionException;
import pl.edu.pwr.model.Reservation;
import java.util.List;

public interface ReservationDao extends Dao<Reservation> {

    List<Reservation> findByClientId(int clientId) throws DataAccessException, DatabaseConnectionException;

    List<Reservation> findByOfferId(int offerId) throws DataAccessException, DatabaseConnectionException;

}