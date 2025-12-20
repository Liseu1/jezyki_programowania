package pl.edu.pwr;

import pl.edu.pwr.dao.Dao;
import pl.edu.pwr.dao.OfferDao;
import pl.edu.pwr.dao.OrderDao;
import pl.edu.pwr.dao.ReservationDao;
import pl.edu.pwr.dao.db.*;
import pl.edu.pwr.exceptions.DataAccessException;
import pl.edu.pwr.exceptions.DatabaseConnectionException;
import pl.edu.pwr.model.*;
import pl.edu.pwr.services.ClientService;
import pl.edu.pwr.services.EmployeeService;
import pl.edu.pwr.services.OrganizerService;
import pl.edu.pwr.ui.ClientApp;
import pl.edu.pwr.ui.EmployeeApp;
import pl.edu.pwr.ui.OrganizerApp;
import pl.edu.pwr.util.Config;
import pl.edu.pwr.util.DatabaseInitializer;
import pl.edu.pwr.util.Seeder;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        Object dbPathResult = JOptionPane.showInputDialog(
                null,
                "Podaj ścieżkę JDBC do bazy danych:",
                "Konfiguracja Startowa Bazy Danych",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "jdbc:h2:./db/kayak_db"
        );

        if (dbPathResult == null || dbPathResult.toString().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nie podano ścieżki bazy danych. Aplikacja zostanie zamknięta.", "Błąd krytyczny", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        Config.dbPath = dbPathResult.toString();
        System.out.println("Używam ścieżki bazy danych: " + Config.dbPath);

        try {
            DatabaseInitializer.setupDatabase();

            OfferDao offerDao = new OfferDaoImpl();
            ReservationDao reservationDao = new ReservationDaoImpl();
            Dao<Client> clientDao = new ClientDao();
            OrderDao orderDao = new OrderDaoImpl();
            Dao<Employee> employeeDao = new EmployeeDao();
            Dao<Organizer> organizerDao = new OrganizerDao();

            Seeder.seedDatabase(organizerDao, clientDao, employeeDao);

            OrganizerService organizerService = new OrganizerService(offerDao, reservationDao, orderDao, employeeDao, organizerDao);
            ClientService clientService = new ClientService(offerDao, reservationDao, clientDao, organizerService);
            EmployeeService employeeService = new EmployeeService(orderDao, reservationDao, offerDao);

            SwingUtilities.invokeLater(() -> {
                ClientApp clientApp1 = new ClientApp(clientService);
                clientApp1.setTitle("Aplikacja klienta - Instancja 1");
                clientApp1.setLocationByPlatform(true);
                clientApp1.setVisible(true);
            });

            SwingUtilities.invokeLater(() -> {
                ClientApp clientApp2 = new ClientApp(clientService);
                clientApp2.setTitle("Aplikacja klienta - Instancja 2");
                clientApp2.setLocationByPlatform(true);
                clientApp2.setVisible(true);
            });

            SwingUtilities.invokeLater(() -> {
                OrganizerApp organizerApp = new OrganizerApp(organizerService);
                organizerApp.setLocationByPlatform(true);
                organizerApp.setVisible(true);
            });

            SwingUtilities.invokeLater(() -> {
                EmployeeApp employeeApp = new EmployeeApp(employeeService);
                employeeApp.setLocationByPlatform(true);
                employeeApp.setVisible(true);
            });

        } catch (DatabaseConnectionException | DataAccessException e) {
            JOptionPane.showMessageDialog(null, "BŁĄD KRYTYCZNY: Aplikacja nie może wystartować.\n" + e.getMessage(), "Błąd Startowy", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1);
        }
    }
}