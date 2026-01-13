package pl.edu.pwr.sockety.house;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pl.edu.pwr.sockety.utils.GuiHelpers;

import java.io.IOException;

public class HouseSettingsController {

    @FXML private TextField portField;
    @FXML private TextField capacityField;
    @FXML private TextField officeIpField;
    @FXML private TextField officePortField;

    @FXML
    public void onStart() {
        try {
            int port = Integer.parseInt(portField.getText());
            int capacity = Integer.parseInt(capacityField.getText());
            String officeIp = officeIpField.getText();
            int officePort = Integer.parseInt(officePortField.getText());

            HouseService service = new HouseService(port, capacity, officePort, officeIp);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("house-main-view.fxml"));
            Parent root = loader.load();

            HouseMainController controller = loader.getController();
            controller.setService(service);

            Stage stage = (Stage) portField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Dom - Port: " + port);

        } catch (NumberFormatException e) {
            GuiHelpers.showError("Błędne dane liczbowe!");
        } catch (IOException e) {
            e.printStackTrace();
            GuiHelpers.showError("Błąd ładowania widoku");
        }
    }
}