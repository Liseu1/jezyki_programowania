module pl.edu.pwr.watki {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens pl.edu.pwr.watki to javafx.fxml;
    exports pl.edu.pwr.watki;
    exports pl.edu.pwr.watki.controllers;
    opens pl.edu.pwr.watki.controllers to javafx.fxml;
    exports pl.edu.pwr.watki.model;
}