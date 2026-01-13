package pl.edu.pwr.sockety.office;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pl.edu.pwr.sockety.utils.GuiHelpers;

public class OfficeSettingsController {
    @FXML private TextField portField;
    @FXML private TextField sewageIpField;
    @FXML private TextField sewagePortField;

    @FXML
    public void onStart() {
        try {
            int port = Integer.parseInt(portField.getText());
            String sewageIp = sewageIpField.getText();
            int sewagePort = Integer.parseInt(sewagePortField.getText());

            OfficeService service = new OfficeService(port, sewageIp, sewagePort);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("office-main-view.fxml"));
            Parent root = loader.load();

            OfficeMainController controller = loader.getController();
            controller.setService(service);

            Stage stage = (Stage) portField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Office - Port: " + port);

        } catch (Exception e) {
            GuiHelpers.showError("Błąd danych wejściowych");
        }
    }
}