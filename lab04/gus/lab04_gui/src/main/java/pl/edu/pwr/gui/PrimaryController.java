package pl.edu.pwr.gui;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import pl.edu.pwr.client.ForeignerFetcher;
import pl.edu.pwr.client.model.ForeignerResponse;
import pl.edu.pwr.client.model.ForeignerSeries;
import pl.edu.pwr.gui.model.TableEntry;


public class PrimaryController {

    @FXML public TextField yearStartField;
    @FXML public TextField yearEndField;
    @FXML public LineChart<Number, Number> lineChart;
    @FXML public CheckBox tableModeCheckbox;

    @FXML public TableView<TableEntry> tableView;
    @FXML public TableColumn<TableEntry, String> colRegion;
    @FXML public TableColumn<TableEntry, Integer> colYear;
    @FXML public TableColumn<TableEntry, Integer> colValue;

    @FXML public ListView<String> voivodeshipList;

    private final ForeignerFetcher fetcher = new ForeignerFetcher();


    @FXML
    private void initialize() {
        colRegion.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().regionName()));
        colYear.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().year()));
        colValue.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().value()));

        voivodeshipList.getItems().addAll(
                "Dolnośląskie",
                "Kujawsko-pomorskie",
                "Lubelskie",
                "Lubuskie",
                "Łódzkie",
                "Małopolskie",
                "Mazowieckie",
                "Opolskie",
                "Podkarpackie",
                "Podlaskie",
                "Pomorskie",
                "Śląskie",
                "Świętokrzyskie",
                "Warmińsko-mazurskie",
                "Wielkopolskie",
                "Zachodniopomorskie"
        );

        voivodeshipList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        voivodeshipList.getSelectionModel().select("Dolnośląskie");

        toggleView();
    }

    @FXML
    public void toggleView() {
        boolean showTable = tableModeCheckbox.isSelected();
        tableView.setVisible(showTable);
        lineChart.setVisible(!showTable);
    }

    @FXML
    public void loadData() {
        int startYear, endYear;
        try {
            startYear = Integer.parseInt(yearStartField.getText());
            endYear = Integer.parseInt(yearEndField.getText());
        } catch (NumberFormatException e) {
            showError("Rok musi być liczbą!"); return;
        }

        if (startYear > endYear || startYear < 1999 || endYear > 2024) {
            showError("Niepoprawny zakres lat."); return;
        }

        var selectedItems = voivodeshipList.getSelectionModel().getSelectedItems();
        if (selectedItems.isEmpty()) {
            showError("Zaznacz przynajmniej jedno województwo (Ctrl+Click)."); return;
        }

        try {
            ForeignerResponse response = fetcher.fetchData(startYear, endYear);

            lineChart.getData().clear();
            tableView.getItems().clear();
            configureAxis(startYear, endYear);

            for (ForeignerSeries series : response.results()) {

                String apiName = series.name();

                boolean isSelected = selectedItems.stream()
                        .anyMatch(apiName::equalsIgnoreCase);

                if (isSelected) {
                    addSeriesToChart(series);
                    addDataToTable(series);
                }
            }

        } catch (Exception e) {
            showError("Błąd: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void configureAxis(int start, int end) {
        NumberAxis xAxis = (NumberAxis) lineChart.getXAxis();
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(start);
        xAxis.setUpperBound(end);
        xAxis.setTickUnit(1);
    }

    private void addSeriesToChart(ForeignerSeries seriesData) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(seriesData.name());

        for (var record : seriesData.values()) {
            series.getData().add(new XYChart.Data<>(record.year(), record.val()));
        }
        lineChart.getData().add(series);
    }

    private void addDataToTable(ForeignerSeries seriesData) {
        for (var record : seriesData.values()) {
            TableEntry entry = new TableEntry(record.year(), record.val(), seriesData.name());
            tableView.getItems().add(entry);
        }
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}