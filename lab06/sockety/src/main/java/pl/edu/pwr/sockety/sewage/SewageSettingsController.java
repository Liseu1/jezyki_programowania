package pl.edu.pwr.sockety.sewage;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pl.edu.pwr.sockety.utils.GuiHelpers;

import java.io.IOException;

public class SewageSettingsController {

    @FXML
    private TextField portField;

    @FXML
    public void onStart() {
        try {
            int port = Integer.parseInt(portField.getText());

            SewagePlantService service = new SewagePlantService(port);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("sewage-main-view.fxml"));
            Parent root = loader.load();

            SewageMainController controller = loader.getController();
            controller.setService(service);

            Stage stage = (Stage) portField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Oczyszczalnia - Port: " + port);

        } catch (NumberFormatException e) {
            GuiHelpers.showError("Numer portu musi być liczbą całkowitą!");
        } catch (IOException e) {
            e.printStackTrace();
            GuiHelpers.showError("Nie udało się załadować widoku głównego Oczyszczalni.");
        }
    }
}