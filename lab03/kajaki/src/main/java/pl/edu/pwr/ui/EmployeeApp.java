package pl.edu.pwr.ui;

import pl.edu.pwr.exceptions.DataAccessException;
import pl.edu.pwr.exceptions.DatabaseConnectionException;
import pl.edu.pwr.exceptions.ReservationException;
import pl.edu.pwr.model.Offer;
import pl.edu.pwr.model.Order;
import pl.edu.pwr.model.Reservation;
import pl.edu.pwr.services.EmployeeService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.util.List;

public class EmployeeApp extends JFrame {

    private final EmployeeService employeeService;

    private JTable orderTable;
    private DefaultTableModel orderTableModel;
    private DefaultTableModel reservationTableModel;

    private JTextField employeeIdField;
    private JTextArea offerDetailsArea;

    public EmployeeApp(EmployeeService employeeService) {
        this.employeeService = employeeService;

        setTitle("Aplikacja Pracownika");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        mainPanel.add(createTopPanel(), BorderLayout.NORTH);
        mainPanel.add(createMainViewPanel(), BorderLayout.CENTER);

        this.add(mainPanel);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));

        panel.add(new JLabel("Twoje ID Pracownika:"));
        employeeIdField = new JTextField("1", 5);
        panel.add(employeeIdField);

        JButton loadOrdersButton = new JButton("Pobierz Moje Zlecenia");
        loadOrdersButton.addActionListener(_ -> loadOrders());
        panel.add(loadOrdersButton);

        return panel;
    }

    private JSplitPane createMainViewPanel() {
        String[] orderColumns = {"ID Zlecenia", "ID Oferty", "ID Organizatora", "Status Zlecenia"};
        orderTableModel = new DefaultTableModel(orderColumns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        orderTable = new JTable(orderTableModel);

        orderTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                handleOrderSelection();
            }
        });

        JScrollPane orderScrollPane = new JScrollPane(orderTable);
        orderScrollPane.setBorder(BorderFactory.createTitledBorder("Moje Zlecenia"));

        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Szczegóły Zlecenia"));

        offerDetailsArea = new JTextArea();
        offerDetailsArea.setEditable(false);
        offerDetailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        offerDetailsArea.setText("Wybierz zlecenie, aby zobaczyć szczegóły...");
        detailsPanel.add(new JScrollPane(offerDetailsArea), BorderLayout.NORTH);

        String[] resColumns = {"ID Rezerwacji", "ID Klienta", "Miejsc", "Status"};
        reservationTableModel = new DefaultTableModel(resColumns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable reservationTable = new JTable(reservationTableModel);

        detailsPanel.add(new JScrollPane(reservationTable), BorderLayout.CENTER);
        detailsPanel.add(createActionPanel(), BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, orderScrollPane, detailsPanel);
        splitPane.setResizeWeight(0.4);
        return splitPane;
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton startButton = new JButton("Rozpocznij Realizację");
        startButton.addActionListener(_ -> handleStartRealization());
        panel.add(startButton);

        JButton completeButton = new JButton("Zakończ Realizację");
        completeButton.addActionListener(_ -> handleCompleteRealization());
        panel.add(completeButton);

        return panel;
    }

    private void loadOrders() {
        try {
            int employeeId = Integer.parseInt(employeeIdField.getText());
            List<Order> orders = employeeService.getMyAssignedOrders(employeeId);

            orderTableModel.setRowCount(0);
            for (Order order : orders) {
                orderTableModel.addRow(new Object[]{
                        order.getId(),
                        order.getOfferId(),
                        order.getOrganizerId(),
                        order.getStatus()
                });
            }
            reservationTableModel.setRowCount(0);
            offerDetailsArea.setText("Wybierz zlecenie, aby zobaczyć szczegóły...");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID Pracownika musi być liczbą.", "Błąd formatu", JOptionPane.ERROR_MESSAGE);
        } catch (DataAccessException | DatabaseConnectionException e) {
            JOptionPane.showMessageDialog(this, "Błąd podczas ładowania zleceń: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleOrderSelection() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow == -1) return;

        int offerId = (int) orderTableModel.getValueAt(selectedRow, 1);

        try {
            Offer offer = employeeService.getOfferDetails(offerId);
            List<Reservation> reservations = employeeService.getReservationsForOffer(offerId);

            String details = String.format("Szczegóły Oferty (ID: %d):\nTermin: %s\nLokalizacja: %s\n",
                    offer.getId(), offer.getTerm().toString(), offer.getLocation());
            offerDetailsArea.setText(details);

            reservationTableModel.setRowCount(0);
            for (Reservation res : reservations) {
                reservationTableModel.addRow(new Object[]{
                        res.getId(),
                        res.getClientId(),
                        res.getSlots(),
                        res.getStatus()
                });
            }
        } catch (DataAccessException | DatabaseConnectionException e) {
            JOptionPane.showMessageDialog(this, "Błąd podczas ładowania szczegółów zlecenia: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleStartRealization() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Najpierw wybierz zlecenie z listy.", "Błąd", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int orderId = (int) orderTableModel.getValueAt(selectedRow, 0);

        try {
            employeeService.startOrderRealization(orderId);
            JOptionPane.showMessageDialog(this, "Rozpoczęto realizację zlecenia. Statusy rezerwacji zaktualizowane.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
            loadOrders();
        } catch (ReservationException ex) {
            JOptionPane.showMessageDialog(this, "Błąd: " + ex.getMessage(), "Błąd biznesowy", JOptionPane.ERROR_MESSAGE);
        } catch (DataAccessException | DatabaseConnectionException ex) {
            JOptionPane.showMessageDialog(this, "Błąd bazy danych: " + ex.getMessage(), "Błąd krytyczny", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleCompleteRealization() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Najpierw wybierz zlecenie z listy.", "Błąd", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int orderId = (int) orderTableModel.getValueAt(selectedRow, 0);

        try {
            employeeService.completeOrderRealization(orderId);
            JOptionPane.showMessageDialog(this, "Zakończono realizację zlecenia. Statusy rezerwacji zaktualizowane.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
            loadOrders();
        } catch (ReservationException ex) {
            JOptionPane.showMessageDialog(this, "Błąd: " + ex.getMessage(), "Błąd biznesowy", JOptionPane.ERROR_MESSAGE);
        } catch (DataAccessException | DatabaseConnectionException ex) {
            JOptionPane.showMessageDialog(this, "Błąd bazy danych: " + ex.getMessage(), "Błąd krytyczny", JOptionPane.ERROR_MESSAGE);
        }
    }
}