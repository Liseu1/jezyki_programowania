/*
Uruchamiane z linii komend (po mvn clean i mvn install) (Windows):
java -p "lab04_gui/target/dependencies;lab04_gui/target/lab04_gui-1.0-SNAPSHOT.jar;lab04_client/target/lab04_client-1.0-SNAPSHOT.jar" -m lab04.gui/pl.edu.pwr.gui.App
(Linux):
java -p "lab04_gui/target/dependencies:lab04_gui/target/lab04_gui-1.0-SNAPSHOT.jar:lab04_client/target/lab04_client-1.0-SNAPSHOT.jar" -m lab04.gui/pl.edu.pwr.gui.App
 */
package pl.edu.pwr.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends javafx.application.Application{

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        stage.setTitle("Turystyka");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
