package pl.edu.pwr.sockety.office;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import pl.edu.pwr.sockety.office.model.HouseRequest;
import pl.edu.pwr.sockety.office.model.Tanker;
import pl.edu.pwr.sockety.utils.GuiHelpers;

public class OfficeMainController {

    @FXML private ListView<Tanker> tankersList;
    @FXML private ListView<HouseRequest> requestsList;
    @FXML private Label debtLabel;

    private OfficeService service;

    public void setService(OfficeService service) {
        this.service = service;
        this.service.setOnUpdateListener(this::refreshLists);
        this.service.startServer();
    }

    private void refreshLists() {
        Platform.runLater(() -> {
            int selectedIdx = tankersList.getSelectionModel().getSelectedIndex();

            tankersList.getItems().clear();
            tankersList.getItems().addAll(service.getTankers());

            if (selectedIdx >= 0 && selectedIdx < tankersList.getItems().size()) {
                tankersList.getSelectionModel().select(selectedIdx);
            }

            requestsList.getItems().clear();
            requestsList.getItems().addAll(service.getRequests());
        });
    }

    @FXML
    public void onSend() {
        Tanker selectedTanker = tankersList.getSelectionModel().getSelectedItem();
        HouseRequest selectedRequest = requestsList.getSelectionModel().getSelectedItem();

        if (selectedTanker == null || selectedRequest == null) {
            GuiHelpers.showError("Wybierz cysternę i dom!");
            return;
        }
        boolean success = service.sendTanker(selectedTanker, selectedRequest);
        if (!success) GuiHelpers.showError("Błąd wysyłania (cysterna zajęta?)");
    }

    @FXML
    public void onCheckStatus() {
        Tanker selectedTanker = tankersList.getSelectionModel().getSelectedItem();
        if (selectedTanker == null) {
            GuiHelpers.showError("Wybierz cysternę z listy!");
            return;
        }

        String result = service.getTankerStatus(selectedTanker.getId());
        debtLabel.setText("Dług ID " + selectedTanker.getId() + ": " + result + " L");
    }

    @FXML
    public void onPayOff() {
        Tanker selectedTanker = tankersList.getSelectionModel().getSelectedItem();
        if (selectedTanker == null) {
            GuiHelpers.showError("Wybierz cysternę z listy!");
            return;
        }

        boolean success = service.payOffTanker(selectedTanker.getId());
        if (success) {
            debtLabel.setText("Opłacono ID " + selectedTanker.getId() + "!");
            debtLabel.setStyle("-fx-text-fill: green;");
        } else {
            debtLabel.setText("Błąd płatności!");
            debtLabel.setStyle("-fx-text-fill: red;");
        }
    }
}