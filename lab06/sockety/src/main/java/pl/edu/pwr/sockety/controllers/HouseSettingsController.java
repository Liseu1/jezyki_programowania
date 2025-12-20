package pl.edu.pwr.sockety.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class HouseSettingsController {

    @FXML private TextField portField;
    @FXML private TextField cesspoolField;

    public void loadSettings() {
        int port;
        float cesspoolSize;
        try {
            port = Integer.parseInt(portField.getText());
            cesspoolSize = Float.parseFloat(cesspoolField.getText());
        } catch (NumberFormatException e) {
            showError("Port musi być liczbą całkowitą, i obie wartości muszą być liczbą!");
            return;
        }
        if (cesspoolSize < 0) {
            showError("Wielkość szamba musi być dodatnia!");
        }




        if (port < 0 || port > 65535) {
            showError("Port musi byc w przedziale [0, 65535]!");
        }


    }


    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
