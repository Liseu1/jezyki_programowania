package pl.edu.pwr.sockety.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;

public class GuiHelpers {

    public static void showError(String msg) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setContentText(msg);
            alert.showAndWait();
        });

    }
}
