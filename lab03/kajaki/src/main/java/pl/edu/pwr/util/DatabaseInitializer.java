package pl.edu.pwr.util;

import pl.edu.pwr.exceptions.DataAccessException;
import pl.edu.pwr.exceptions.DatabaseConnectionException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void setupDatabase() throws DatabaseConnectionException, DataAccessException {
        try (Connection connection = DbConnectionProvider.getConnection();
             Statement statement = connection.createStatement()){

            String clientsQuery = "CREATE TABLE IF NOT EXISTS Clients (id int NOT NULL AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255) NOT NULL);";
            statement.execute(clientsQuery);
            String EmployeeQuery = "CREATE TABLE IF NOT EXISTS Employees (id int NOT NULL AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255) NOT NULL);";
            statement.execute(EmployeeQuery);
            String organizerQuery = "CREATE TABLE IF NOT EXISTS Organizers (id int NOT NULL AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255) NOT NULL);";
            statement.execute(organizerQuery);
            String offerQuery = "CREATE TABLE IF NOT EXISTS Offers (id int NOT NULL AUTO_INCREMENT PRIMARY KEY, term DATETIME NOT NULL, location VARCHAR(255) NOT NULL, max_slots int NOT NULL, vacant_slots int NOT NULL, organizer_id int NOT NULL, FOREIGN KEY (organizer_id) REFERENCES Organizers(id));";
            statement.execute(offerQuery);
            String reservationsQuery = "CREATE TABLE IF NOT EXISTS Reservations (id int NOT NULL AUTO_INCREMENT PRIMARY KEY, organizer_id int NOT NULL, offer_id int NOT NULL, client_id int NOT NULL, slots int NOT NULL, status VARCHAR(255) NOT NULL, FOREIGN KEY (organizer_id) REFERENCES Organizers(id), FOREIGN KEY (offer_id) REFERENCES Offers(id),FOREIGN KEY (client_id) REFERENCES Clients(id));";
            statement.execute(reservationsQuery);
            String ordersQuery = "CREATE TABLE IF NOT EXISTS Orders (id int NOT NULL AUTO_INCREMENT PRIMARY KEY, organizer_id int NOT NULL, employee_id int NOT NULL, offer_id int NOT NULL, status VARCHAR(255) NOT NULL, FOREIGN KEY (organizer_id) REFERENCES Organizers(id), FOREIGN KEY (offer_id) REFERENCES Offers(id), FOREIGN KEY (employee_id) REFERENCES Employees(id));";
            statement.execute(ordersQuery);

        } catch(DatabaseConnectionException e) {
            throw e;
        } catch (SQLException e) {
            throw new DataAccessException("SQL error during db initialization", e);
        }
    }
}