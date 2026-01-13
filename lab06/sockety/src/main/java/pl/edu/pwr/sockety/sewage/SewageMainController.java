package pl.edu.pwr.sockety.sewage;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class SewageMainController {

    @FXML private TextArea statusArea;
    @FXML private TextArea historyArea;

    private SewagePlantService service;

    public void setService(SewagePlantService service) {
        this.service = service;
        this.service.setOnUpdateListener(this::refreshView);
        refreshView();
    }

    private void refreshView() {
        Platform.runLater(() -> {
            statusArea.setText(service.getStorageSummary());
            historyArea.setText(service.getHistorySummary());
        });
    }
}