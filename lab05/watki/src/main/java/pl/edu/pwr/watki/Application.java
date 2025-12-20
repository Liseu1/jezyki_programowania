package pl.edu.pwr.watki;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.edu.pwr.watki.controllers.MainController;
import pl.edu.pwr.watki.controllers.SettingsController;

import java.io.IOException;

public class Application extends javafx.application.Application {
    private Stage mainStage;
    @Override
    public void start(Stage stage) throws IOException {
        this.mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("settings-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        SettingsController settingsController = fxmlLoader.getController();
        settingsController.setApplication(this);

        stage.setTitle("Watki");
        stage.setScene(scene);
        stage.show();
    }

    public void changeSceneToMain(int entryRampCount, int exitRampCount, int feederCount) throws  IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        MainController mainController = fxmlLoader.getController();
        mainController.initialize(entryRampCount, exitRampCount, feederCount);
        mainStage.setScene(scene);


    }
}
