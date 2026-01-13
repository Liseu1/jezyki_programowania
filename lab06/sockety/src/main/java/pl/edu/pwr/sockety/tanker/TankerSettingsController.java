package pl.edu.pwr.sockety.tanker;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pl.edu.pwr.sockety.utils.GuiHelpers;

import java.io.IOException;

public class TankerSettingsController {

    @FXML private TextField portField;
    @FXML private TextField capacityField;

    @FXML private TextField officeIpField;
    @FXML private TextField officePortField;

    @FXML private TextField sewageIpField;
    @FXML private TextField sewagePortField;

    @FXML
    public void onStart() {
        try {
            int port = Integer.parseInt(portField.getText());
            int capacity = Integer.parseInt(capacityField.getText());

            String officeIp = officeIpField.getText();
            int officePort = Integer.parseInt(officePortField.getText());

            String sewageIp = sewageIpField.getText();
            int sewagePort = Integer.parseInt(sewagePortField.getText());

            TankerService service = new TankerService(port, capacity, officePort, officeIp, sewagePort, sewageIp);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("tanker-main-view.fxml"));
            Parent root = loader.load();

            TankerMainController controller = loader.getController();
            controller.setService(service);

            Stage stage = (Stage) portField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Tanker - Port: " + port);

        } catch (NumberFormatException e) {
            GuiHelpers.showError("Wprowadź poprawne liczby!");
        } catch (IOException e) {
            e.printStackTrace();
            GuiHelpers.showError("Błąd ładowania widoku");
        }
    }
}