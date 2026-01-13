package pl.edu.pwr.sockety.house;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class HouseMainController {

    @FXML private Label statusLabel;
    @FXML private Label volumeLabel;
    @FXML private ProgressBar capacityBar;

    private HouseService service;

    public void setService(HouseService service) {
        this.service = service;
        this.service.setOnUpdateListener(this::refreshView);
        refreshView();
    }

    private void refreshView() {
        Platform.runLater(() -> {
            int current = service.getCurrentVolume();
            int max = service.capacity;
            boolean ordered = service.isOrdered();

            volumeLabel.setText(current + " / " + max + " L");
            double progress = (double) current / max;
            capacityBar.setProgress(progress);

            if (ordered) {
                statusLabel.setText("OCZEKIWANIE NA CYSTERNĘ...");
                statusLabel.setStyle("-fx-text-fill: red;");
                capacityBar.setStyle("-fx-accent: orange;");
            } else {
                statusLabel.setText("System w normie");
                statusLabel.setStyle("-fx-text-fill: black;");
                capacityBar.setStyle("-fx-accent: green;");
            }
        });
    }
}