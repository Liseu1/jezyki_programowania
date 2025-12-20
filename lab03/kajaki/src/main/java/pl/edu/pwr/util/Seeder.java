package pl.edu.pwr.util;

import pl.edu.pwr.dao.Dao;
import pl.edu.pwr.exceptions.DataAccessException;
import pl.edu.pwr.exceptions.DatabaseConnectionException;
import pl.edu.pwr.model.Client;
import pl.edu.pwr.model.Employee;
import pl.edu.pwr.model.Organizer;

public class Seeder {

    public static void seedDatabase(Dao<Organizer> organizerDao, Dao<Client> clientDao, Dao<Employee> employeeDao)
            throws DataAccessException, DatabaseConnectionException {

        if (organizerDao.getAll().isEmpty()) {
            organizerDao.create(new Organizer(0, "Główny Organizator"));
            System.out.println("Seeder: Utworzono domyślnego organizatora (ID=1)");
        }

        if (clientDao.getAll().isEmpty()) {
            clientDao.create(new Client(0, "Testowy Klient 1"));
            clientDao.create(new Client(0, "Testowy Klient 2"));
            System.out.println("Seeder: Utworzono domyślnych klientów (ID=1, ID=2)");
        }

        if (employeeDao.getAll().isEmpty()) {
            employeeDao.create(new Employee(0, "Pracownik A"));
            employeeDao.create(new Employee(0, "Pracownik B"));
            System.out.println("Seeder: Utworzono domyślnych pracowników (ID=1, ID=2)");
        }
    }
}