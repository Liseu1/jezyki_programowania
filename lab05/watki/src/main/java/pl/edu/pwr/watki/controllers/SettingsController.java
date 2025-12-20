package pl.edu.pwr.watki.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import pl.edu.pwr.watki.Application;

import java.io.IOException;

public class SettingsController {
    private Application application;
    @FXML
    public TextField entryRampField;
    @FXML
    public TextField exitRampField;
    @FXML
    public TextField feederCountField;
    @FXML

    public void setApplication(Application application) {
        this.application = application;
    }

    @FXML
    protected void loadSettings() {
        try {
            int entryRampSize = Integer.parseUnsignedInt(entryRampField.getText());
            int exitRampSize = Integer.parseUnsignedInt(exitRampField.getText());
            int feederSize = Integer.parseUnsignedInt(feederCountField.getText());

            if (feederSize < 2) {
                showError("Liczba podajników musi być >= 2!");
            }
            if (entryRampSize < 1 || exitRampSize < 1) {
                showError("Rampy musza mieć szerokość co najmniej 1!");
            }
            application.changeSceneToMain(entryRampSize, exitRampSize, feederSize);
        } catch (NumberFormatException e) {
            showError("Wartości muszą byc dodatnimi liczbami całkowitymi!");
        } catch (IOException e) {
            showError("Nieznany błąd podczas zmiany sceny");
        }


    }



    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
