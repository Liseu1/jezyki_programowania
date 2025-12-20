module lab04.gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires lab04.client;

    exports pl.edu.pwr.gui;
    exports pl.edu.pwr.gui.model;
    opens pl.edu.pwr.gui to javafx.fxml;
}