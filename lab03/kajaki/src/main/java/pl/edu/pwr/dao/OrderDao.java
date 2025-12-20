package pl.edu.pwr.dao;

import pl.edu.pwr.exceptions.DataAccessException;
import pl.edu.pwr.exceptions.DatabaseConnectionException;
import pl.edu.pwr.model.Order;
import java.util.List;

public interface OrderDao extends Dao<Order> {

    List<Order> findByEmployeeId(int employeeId) throws DataAccessException, DatabaseConnectionException;

}