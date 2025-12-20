package pl.edu.pwr.ui;

import pl.edu.pwr.exceptions.*;
import pl.edu.pwr.model.Employee;
import pl.edu.pwr.model.Offer;
import pl.edu.pwr.model.Reservation;
import pl.edu.pwr.model.ReservationStatus;
import pl.edu.pwr.services.OrganizerService;
import pl.edu.pwr.util.TimeSimulator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class OrganizerApp extends JFrame {

    private final OrganizerService organizerService;

    private JTable offerTable;
    private DefaultTableModel offerTableModel;
    private DefaultTableModel reservationTableModel;
    private JLabel timeLabel;

    private JTextField offerTermField, offerLocationField, offerMaxSlotsField, offerOrganizerIdField;
    private JTextField resIdField;
    private JComboBox<ReservationStatus> resStatusComboBox;
    private JTextField orderOfferIdField, orderOrganizerIdField;
    private JComboBox<Employee> employeeComboBox;

    public OrganizerApp(OrganizerService organizerService) {
        this.organizerService = organizerService;

        setTitle("Aplikacja Organizatora");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);

        initComponents();
        loadAllData();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        mainPanel.add(createTopPanel(), BorderLayout.NORTH);
        mainPanel.add(createMainViewPanel(), BorderLayout.CENTER);
        mainPanel.add(createActionPanel(), BorderLayout.SOUTH);

        this.add(mainPanel);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton refreshButton = new JButton("Odśwież Wszystko");
        refreshButton.addActionListener(_ -> loadAllData());
        buttonPanel.add(refreshButton);

        JButton advanceTimeButton = new JButton("Przesuń Czas (Symulacja)");
        advanceTimeButton.addActionListener(_ -> handleAdvanceTime());
        buttonPanel.add(advanceTimeButton);

        timeLabel = new JLabel();
        timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        panel.add(buttonPanel, BorderLayout.WEST);
        panel.add(timeLabel, BorderLayout.EAST);

        return panel;
    }

    private JSplitPane createMainViewPanel() {
        String[] offerColumns = {"ID", "Termin", "Lokalizacja", "Max Miejsc", "Wolne Miejsca", "ID Org."};
        offerTableModel = new DefaultTableModel(offerColumns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        offerTable = new JTable(offerTableModel);

        offerTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                handleOfferSelection();
            }
        });

        JScrollPane offerScrollPane = new JScrollPane(offerTable);
        offerScrollPane.setBorder(BorderFactory.createTitledBorder("Oferty"));

        String[] resColumns = {"ID Rez.", "ID Oferty", "ID Klienta", "Miejsc", "Status"};
        reservationTableModel = new DefaultTableModel(resColumns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable reservationTable = new JTable(reservationTableModel);

        JScrollPane reservationScrollPane = new JScrollPane(reservationTable);
        reservationScrollPane.setBorder(BorderFactory.createTitledBorder("Rezerwacje dla wybranej oferty"));

        JPanel reservationPanel = new JPanel(new BorderLayout());
        reservationPanel.add(reservationScrollPane, BorderLayout.CENTER);
        reservationPanel.add(createMassActionPanel(), BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, offerScrollPane, reservationPanel);
        splitPane.setResizeWeight(0.5);
        return splitPane;
    }

    private JPanel createMassActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton confirmAllButton = new JButton("Potwierdź wszystkie 'ZALOZONE'");
        confirmAllButton.addActionListener(_ -> handleConfirmAll());
        panel.add(confirmAllButton);

        JButton cancelAllButton = new JButton("Odwołaj wszystkie aktywne");
        cancelAllButton.addActionListener(_ -> handleCancelAll());
        panel.add(cancelAllButton);

        return panel;
    }

    private JTabbedPane createActionPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Stwórz Ofertę", createOfferPanel());
        tabbedPane.addTab("Zarządzaj Pojedynczą Rezerwacją", createReservationPanel());
        tabbedPane.addTab("Stwórz Zlecenie", createOrderPanel());

        return tabbedPane;
    }

    private JPanel createOfferPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Termin (YYYY-MM-DD HH:MM):"));
        offerTermField = new JTextField("2025-11-20 09:00");
        panel.add(offerTermField);

        panel.add(new JLabel("Lokalizacja:"));
        offerLocationField = new JTextField("Rzeka Nysa");
        panel.add(offerLocationField);

        panel.add(new JLabel("Maks. Miejsc:"));
        offerMaxSlotsField = new JTextField("50");
        panel.add(offerMaxSlotsField);

        panel.add(new JLabel("Twoje ID Organizatora:"));
        offerOrganizerIdField = new JTextField("1");
        panel.add(offerOrganizerIdField);

        JButton createButton = new JButton("Stwórz Ofertę");
        createButton.addActionListener(_ -> handleCreateOffer());
        panel.add(createButton);

        return panel;
    }

    private JPanel createReservationPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("ID Rezerwacji:"));
        resIdField = new JTextField(5);
        panel.add(resIdField);

        panel.add(new JLabel("Nowy Status:"));
        resStatusComboBox = new JComboBox<>(ReservationStatus.values());
        panel.add(resStatusComboBox);

        JButton updateStatusButton = new JButton("Aktualizuj Status");
        updateStatusButton.addActionListener(_ -> handleUpdateReservationStatus());
        panel.add(updateStatusButton);

        return panel;
    }

    private JPanel createOrderPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("ID Oferty:"));
        orderOfferIdField = new JTextField(5);
        panel.add(orderOfferIdField);

        panel.add(new JLabel("Twoje ID Organizatora:"));
        orderOrganizerIdField = new JTextField("1");
        panel.add(orderOrganizerIdField);

        panel.add(new JLabel("Wybierz Pracownika:"));
        employeeComboBox = new JComboBox<>();
        panel.add(employeeComboBox);

        JButton createOrderButton = new JButton("Stwórz Zlecenie i Potwierdź Rezerwacje");
        createOrderButton.addActionListener(_ -> handleCreateOrder());
        panel.add(createOrderButton);

        return panel;
    }

    private void updateDateLabel() {
        timeLabel.setText("Aktualna data: " + TimeSimulator.getSimulatedDate().toString());
    }

    private void loadAllData() {
        updateDateLabel();
        loadOffers();
        loadEmployees();
    }

    private void handleAdvanceTime() {
        try {
            TimeSimulator.advanceDay();
            organizerService.checkAndCancelPendingReservations();

            updateDateLabel();
            loadAllData();

            JOptionPane.showMessageDialog(this, "Czas został przesunięty o jeden dzień. Statusy rezerwacji mogły zostać zaktualizowane.", "Symulacja Czasu", JOptionPane.INFORMATION_MESSAGE);

        } catch (DataAccessException | DatabaseConnectionException e) {
            JOptionPane.showMessageDialog(this, "Wystąpił błąd podczas automatycznej aktualizacji statusów: " + e.getMessage(), "Błąd krytyczny", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleOfferSelection() {
        int selectedRow = offerTable.getSelectedRow();
        if (selectedRow != -1) {
            int offerId = (int) offerTableModel.getValueAt(selectedRow, 0);
            loadReservationsForOffer(offerId);
            orderOfferIdField.setText(String.valueOf(offerId));
        }
    }

    private void loadOffers() {
        try {
            List<Offer> offers = organizerService.getAllOffers();
            offerTableModel.setRowCount(0);
            for (Offer offer : offers) {
                offerTableModel.addRow(new Object[]{
                        offer.getId(),
                        offer.getTerm(),
                        offer.getLocation(),
                        offer.getMaxSlots(),
                        offer.getVacantSlots(),
                        offer.getOrganizerId()
                });
            }
            reservationTableModel.setRowCount(0);
        } catch (DataAccessException | DatabaseConnectionException e) {
            JOptionPane.showMessageDialog(this, "Błąd podczas ładowania ofert: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadReservationsForOffer(int offerId) {
        try {
            List<Reservation> reservations = organizerService.getReservationsForOffer(offerId);
            reservationTableModel.setRowCount(0);
            for (Reservation res : reservations) {
                reservationTableModel.addRow(new Object[]{
                        res.getId(),
                        res.getOfferId(),
                        res.getClientId(),
                        res.getSlots(),
                        res.getStatus()
                });
            }
        } catch (DataAccessException | DatabaseConnectionException e) {
            JOptionPane.showMessageDialog(this, "Błąd podczas ładowania rezerwacji: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadEmployees() {
        try {
            List<Employee> employees = organizerService.getAllEmployees();
            employeeComboBox.removeAllItems();
            for (Employee emp : employees) {
                employeeComboBox.addItem(emp);
            }
        } catch (DataAccessException | DatabaseConnectionException e) {
            JOptionPane.showMessageDialog(this, "Błąd podczas ładowania pracowników: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleCreateOffer() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime term = LocalDateTime.parse(offerTermField.getText(), formatter);

            String location = offerLocationField.getText();
            int maxSlots = Integer.parseInt(offerMaxSlotsField.getText());
            int organizerId = Integer.parseInt(offerOrganizerIdField.getText());

            if (location.trim().isEmpty()) {
                throw new OfferCreationException("Lokalizacja nie może być pusta.");
            }

            organizerService.createOffer(term, location, maxSlots, organizerId);
            JOptionPane.showMessageDialog(this, "Oferta została pomyślnie utworzona!", "Sukces", JOptionPane.INFORMATION_MESSAGE);
            loadOffers();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Błędny format daty. Oczekiwano: YYYY-MM-DD HH:MM", "Błąd formatu", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Maks. Miejsc i ID Organizatora muszą być liczbami.", "Błąd formatu", JOptionPane.ERROR_MESSAGE);
        } catch (OfferCreationException ex) {
            JOptionPane.showMessageDialog(this, "Błąd tworzenia oferty: " + ex.getMessage(), "Błąd biznesowy", JOptionPane.ERROR_MESSAGE);
        } catch (DataAccessException | DatabaseConnectionException ex) {
            JOptionPane.showMessageDialog(this, "Błąd bazy danych: " + ex.getMessage(), "Błąd krytyczny", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdateReservationStatus() {
        try {
            int resId = Integer.parseInt(resIdField.getText());
            ReservationStatus newStatus = (ReservationStatus) resStatusComboBox.getSelectedItem();

            organizerService.updateReservationStatus(resId, newStatus);
            JOptionPane.showMessageDialog(this, "Status rezerwacji zaktualizowany.", "Sukces", JOptionPane.INFORMATION_MESSAGE);

            handleOfferSelection();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID Rezerwacji musi być liczbą.", "Błąd formatu", JOptionPane.ERROR_MESSAGE);
        } catch (ReservationException ex) {
            JOptionPane.showMessageDialog(this, "Błąd aktualizacji: " + ex.getMessage(), "Błąd biznesowy", JOptionPane.ERROR_MESSAGE);
        } catch (DataAccessException | DatabaseConnectionException ex) {
            JOptionPane.showMessageDialog(this, "Błąd bazy danych: " + ex.getMessage(), "Błąd krytyczny", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleCreateOrder() {
        try {
            int offerId = Integer.parseInt(orderOfferIdField.getText());
            int organizerId = Integer.parseInt(orderOrganizerIdField.getText());
            Employee selectedEmployee = (Employee) employeeComboBox.getSelectedItem();

            if (selectedEmployee == null) {
                throw new OrderCreationException("Należy wybrać pracownika.");
            }

            organizerService.createOrderAndConfirmReservations(offerId, selectedEmployee.getId(), organizerId);
            JOptionPane.showMessageDialog(this, "Zlecenie zostało utworzone, a rezerwacje potwierdzone.", "Sukces", JOptionPane.INFORMATION_MESSAGE);

            loadReservationsForOffer(offerId);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID Oferty i ID Organizatora muszą być liczbami.", "Błąd formatu", JOptionPane.ERROR_MESSAGE);
        } catch (OrderCreationException ex) {
            JOptionPane.showMessageDialog(this, "Błąd tworzenia zlecenia: " + ex.getMessage(), "Błąd biznesowy", JOptionPane.ERROR_MESSAGE);
        } catch (DataAccessException | DatabaseConnectionException ex) {
            JOptionPane.showMessageDialog(this, "Błąd bazy danych: " + ex.getMessage(), "Błąd krytyczny", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleConfirmAll() {
        int selectedRow = offerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Najpierw wybierz ofertę z górnej tabeli.", "Błąd", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int offerId = (int) offerTableModel.getValueAt(selectedRow, 0);

        try {
            organizerService.confirmAllReservationsForOffer(offerId);
            JOptionPane.showMessageDialog(this, "Wszystkie rezerwacje 'ZALOZONE' zostały potwierdzone.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
            loadReservationsForOffer(offerId);
        } catch (ReservationException ex) {
            JOptionPane.showMessageDialog(this, "Błąd: " + ex.getMessage(), "Błąd biznesowy", JOptionPane.ERROR_MESSAGE);
        } catch (DataAccessException | DatabaseConnectionException ex) {
            JOptionPane.showMessageDialog(this, "Błąd bazy danych: " + ex.getMessage(), "Błąd krytyczny", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleCancelAll() {
        int selectedRow = offerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Najpierw wybierz ofertę z górnej tabeli.", "Błąd", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int offerId = (int) offerTableModel.getValueAt(selectedRow, 0);

        int choice = JOptionPane.showConfirmDialog(this,
                "Czy na pewno chcesz odwołać WSZYSTKIE aktywne rezerwacje dla tej oferty?\nTej operacji nie można cofnąć.",
                "Potwierdzenie odwołania",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            organizerService.cancelAllReservationsForOffer(offerId);
            JOptionPane.showMessageDialog(this, "Wszystkie aktywne rezerwacje zostały odwołane.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
            loadReservationsForOffer(offerId);
        } catch (ReservationException ex) {
            JOptionPane.showMessageDialog(this, "Błąd: " + ex.getMessage(), "Błąd biznesowy", JOptionPane.ERROR_MESSAGE);
        } catch (DataAccessException | DatabaseConnectionException ex) {
            JOptionPane.showMessageDialog(this, "Błąd bazy danych: " + ex.getMessage(), "Błąd krytyczny", JOptionPane.ERROR_MESSAGE);
        }
    }
}