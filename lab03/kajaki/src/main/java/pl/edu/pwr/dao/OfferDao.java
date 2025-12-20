package pl.edu.pwr.dao;

import pl.edu.pwr.exceptions.DataAccessException;
import pl.edu.pwr.exceptions.DatabaseConnectionException;
import pl.edu.pwr.model.Offer;
import java.time.LocalDate;
import java.util.List;

public interface OfferDao extends Dao<Offer> {

    List<Offer> findByDate(LocalDate date) throws DataAccessException, DatabaseConnectionException;

}