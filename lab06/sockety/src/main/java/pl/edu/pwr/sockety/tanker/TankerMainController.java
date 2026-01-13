package pl.edu.pwr.sockety.tanker;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class TankerMainController {

    @FXML private Label statusLabel;
    @FXML private Label volumeLabel;
    @FXML private ProgressBar capacityBar;

    private TankerService service;

    public void setService(TankerService service) {
        this.service = service;
        this.service.setOnUpdateListener(this::refreshView);
        refreshView();
    }

    private void refreshView() {
        Platform.runLater(() -> {
            int current = service.getCurrentVolume();
            int max = service.getCapacity();

            volumeLabel.setText(current + " / " + max + " L");
            double progress = (double) current / max;
            capacityBar.setProgress(progress);

            if (progress > 0.9) {
                capacityBar.setStyle("-fx-accent: red;");
                statusLabel.setText("STAN: PEŁNY (Wymagany zrzut)");
            } else {
                capacityBar.setStyle("-fx-accent: green;");
                statusLabel.setText("STAN: GOTOWY DO PRACY");
            }
        });
    }

    @FXML
    public void onDump() {
        service.emptyTank();
    }
}