package pl.edu.pwr.dao.db;

import pl.edu.pwr.dao.Dao;
import pl.edu.pwr.dao.RowMapper;
import pl.edu.pwr.exceptions.DataAccessException;
import pl.edu.pwr.exceptions.DatabaseConnectionException;
import pl.edu.pwr.model.Employee;

import java.util.List;

public class EmployeeDao implements Dao<Employee> {

    private static final RowMapper<Employee> employeeMapper = rs ->
            new Employee(rs.getInt("id"), rs.getString("name"));
    @Override
    public List<Employee> getAll() throws DataAccessException, DatabaseConnectionException {
        String sql = "SELECT * FROM Employees";

        return DbHelper.findAll(sql, employeeMapper);
    }

    @Override
    public Employee getOne(int id) throws DatabaseConnectionException, DataAccessException {
        String sql = "SELECT * FROM Employees WHERE id = ?";

        return DbHelper.getOne(sql, id, employeeMapper);
    }

    @Override
    public void create(Employee employee) throws DataAccessException, DatabaseConnectionException {
        String sql = "INSERT INTO Employees (name) VALUES (?)";
        DbHelper.executeUpdate(sql, employee.getName());
    }

    @Override
    public void update(Employee employee) throws DataAccessException, DatabaseConnectionException {
        String sql = "UPDATE Employees SET name = ? WHERE id = ?";
        DbHelper.executeUpdate(sql,
                employee.getName(),
                employee.getId()
        );
    }

    @Override
    public void delete(int id) throws DataAccessException, DatabaseConnectionException {
        String sql = "DELETE FROM Employees WHERE id = ?";
        DbHelper.executeUpdate(sql, id);
    }
}
