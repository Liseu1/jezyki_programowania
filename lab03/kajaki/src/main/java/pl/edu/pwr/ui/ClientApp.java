package pl.edu.pwr.ui;

import pl.edu.pwr.exceptions.DataAccessException;
import pl.edu.pwr.exceptions.DatabaseConnectionException;
import pl.edu.pwr.exceptions.ReservationException;
import pl.edu.pwr.model.Offer;
import pl.edu.pwr.model.Reservation;
import pl.edu.pwr.services.ClientService;
import pl.edu.pwr.util.TimeSimulator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

public class ClientApp extends JFrame {

    private final ClientService clientService;
    private DefaultTableModel tableModel;

    private JTextField offerIdField;
    private JTextField slotsField;
    private JTextField clientIdField;
    private JLabel timeLabel;

    public ClientApp(ClientService clientService) {
        this.clientService = clientService;

        setTitle("Aplikacja klienta");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        initComponents();
        loadOffers();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel topPanel = createTopPanel();
        mainPanel.add(topPanel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Termin", "Lokalizacja", "Max Miejsc", "Wolne Miejsca", "ID Organizatora"};

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable offerTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(offerTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel reservationPanel = createReservationPanel();
        mainPanel.add(reservationPanel, BorderLayout.SOUTH);

        this.add(mainPanel);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton refreshButton = new JButton("Odśwież Listę Ofert");
        refreshButton.addActionListener(_ -> loadOffers());
        buttonPanel.add(refreshButton);

        JButton myReservationsButton = new JButton("Pokaż Moje Rezerwacje");
        myReservationsButton.addActionListener(_ -> handleShowMyReservations());
        buttonPanel.add(myReservationsButton);

        JButton advanceTimeButton = new JButton("Przesuń Czas (Symulacja)");
        advanceTimeButton.addActionListener(_ -> handleAdvanceTime());
        buttonPanel.add(advanceTimeButton);

        timeLabel = new JLabel();
        timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        panel.add(buttonPanel, BorderLayout.WEST);
        panel.add(timeLabel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createReservationPanel() {
        JPanel panel = new JPanel(new FlowLayout());

        panel.add(new JLabel("Twoje ID Klienta:"));
        clientIdField = new JTextField("1", 5);
        panel.add(clientIdField);

        panel.add(new JLabel("ID Oferty:"));
        offerIdField = new JTextField(5);
        panel.add(offerIdField);

        panel.add(new JLabel("Liczba miejsc:"));
        slotsField = new JTextField(5);
        panel.add(slotsField);

        JButton reserveButton = new JButton("Zarezerwuj");
        reserveButton.addActionListener(_ -> handleReservation());
        panel.add(reserveButton);

        return panel;
    }

    private void updateDateLabel() {
        timeLabel.setText("Aktualna data: " + TimeSimulator.getSimulatedDate().toString());
    }

    private void handleAdvanceTime() {
        try {
            TimeSimulator.advanceDay();
            clientService.checkAndCancelPendingReservations();

            updateDateLabel();
            loadOffers();

            JOptionPane.showMessageDialog(this, "Czas został przesunięty o jeden dzień. Statusy rezerwacji mogły zostać zaktualizowane.", "Symulacja Czasu", JOptionPane.INFORMATION_MESSAGE);

        } catch (DataAccessException | DatabaseConnectionException e) {
            JOptionPane.showMessageDialog(this, "Wystąpił błąd podczas automatycznej aktualizacji statusów: " + e.getMessage(), "Błąd krytyczny", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleShowMyReservations() {
        try {
            int clientId = Integer.parseInt(clientIdField.getText());

            List<Reservation> reservations = clientService.getMyReservations(clientId);

            if (reservations.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nie masz jeszcze żadnych rezerwacji.", "Moje Rezerwacje", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder message = new StringBuilder("Twoje rezerwacje:\n\n");
            for (Reservation res : reservations) {
                message.append(String.format("ID Rezerwacji: %d | ID Oferty: %d | Miejsc: %d | Status: %s\n",
                        res.getId(),
                        res.getOfferId(),
                        res.getSlots(),
                        res.getStatus().name()
                ));
            }
            JOptionPane.showMessageDialog(this, message.toString(), "Moje Rezerwacje", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Wprowadź poprawne ID Klienta w polu 'Twoje ID Klienta'.", "Błąd formatu", JOptionPane.ERROR_MESSAGE);
        } catch (DataAccessException | DatabaseConnectionException ex) {
            JOptionPane.showMessageDialog(this, "Błąd bazy danych: " + ex.getMessage(), "Błąd krytyczny", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void handleReservation() {
        try {
            int clientId = Integer.parseInt(clientIdField.getText());
            int offerId = Integer.parseInt(offerIdField.getText());
            int slots = Integer.parseInt(slotsField.getText());

            clientService.makeReservation(clientId, offerId, slots);

            JOptionPane.showMessageDialog(this, "Rezerwacja zakończona pomyślnie!", "Sukces", JOptionPane.INFORMATION_MESSAGE);

            loadOffers();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID Klienta, ID Oferty i Liczba Miejsc muszą być liczbami.", "Błąd formatu", JOptionPane.ERROR_MESSAGE);
        } catch (ReservationException ex) {
            JOptionPane.showMessageDialog(this, "Błąd rezerwacji: " + ex.getMessage(), "Błąd biznesowy", JOptionPane.ERROR_MESSAGE);
        } catch (DataAccessException | DatabaseConnectionException ex) {
            JOptionPane.showMessageDialog(this, "Błąd bazy danych: " + ex.getMessage(), "Błąd krytyczny", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void loadOffers() {
        updateDateLabel();
        try {
            List<Offer> offers = clientService.getAvailableOffers();

            tableModel.setRowCount(0);

            for (Offer offer : offers) {
                Object[] row = {
                        offer.getId(),
                        offer.getTerm(),
                        offer.getLocation(),
                        offer.getMaxSlots(),
                        offer.getVacantSlots(),
                        offer.getOrganizerId()
                };
                tableModel.addRow(row);
            }
        } catch (DataAccessException | DatabaseConnectionException e) {
            JOptionPane.showMessageDialog(this, "Błąd podczas ładowania ofert: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}