package pl.edu.pwr.dao.db;

import pl.edu.pwr.dao.OrderDao;
import pl.edu.pwr.dao.RowMapper;
import pl.edu.pwr.exceptions.DataAccessException;
import pl.edu.pwr.exceptions.DatabaseConnectionException;
import pl.edu.pwr.model.Order;
import pl.edu.pwr.model.OrderStatus;
import pl.edu.pwr.util.DbConnectionProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoImpl implements OrderDao {

    private static final RowMapper<Order> orderMapper = rs ->
            new Order(rs.getInt("id"), rs.getInt("organizer_id"), rs.getInt("employee_id"), rs.getInt("offer_id"), OrderStatus.valueOf(rs.getString("status")));

    @Override
    public List<Order> getAll() throws DataAccessException, DatabaseConnectionException {
        String sql = "SELECT * FROM Orders";
        return DbHelper.findAll(sql, orderMapper);
    }

    @Override
    public Order getOne(int id) throws DatabaseConnectionException, DataAccessException {
        String sql = "SELECT * FROM Orders WHERE id = ?";
        return DbHelper.getOne(sql, id, orderMapper);
    }

    @Override
    public void create(Order order) throws DataAccessException, DatabaseConnectionException {
        String sql = "INSERT INTO Orders (organizer_id, employee_id, offer_id, status) VALUES (?, ?, ?, ?)";
        DbHelper.executeUpdate(sql,
                order.getOrganizerId(),
                order.getEmployeeId(),
                order.getOfferId(),
                order.getStatus().name()
        );
    }

    @Override
    public void update(Order order) throws DataAccessException, DatabaseConnectionException {
        String sql = "UPDATE Orders SET organizer_id = ?, employee_id = ?, offer_id = ?, status = ? WHERE id = ?";
        DbHelper.executeUpdate(sql,
                order.getOrganizerId(),
                order.getEmployeeId(),
                order.getOfferId(),
                order.getStatus().name(),
                order.getId()
        );
    }

    @Override
    public void delete(int id) throws DataAccessException, DatabaseConnectionException {
        String sql = "DELETE FROM Orders WHERE id = ?";
        DbHelper.executeUpdate(sql, id);
    }

    @Override
    public List<Order> findByEmployeeId(int employeeId) throws DataAccessException, DatabaseConnectionException {
        String sql = "SELECT * FROM Orders WHERE employee_id = ?";
        List<Order> results = new ArrayList<>();

        try (Connection connection = DbConnectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, employeeId);
            ResultSet response = statement.executeQuery();

            while (response.next()) {
                results.add(orderMapper.map(response));
            }

            return results;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to fetch orders by employee ID", e);
        } catch (DatabaseConnectionException e) {
            throw new DatabaseConnectionException("Failed to connect to database", e);
        }
    }
}