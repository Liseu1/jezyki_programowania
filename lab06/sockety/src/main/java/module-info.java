module pl.edu.pwr.sockety {
    requires javafx.controls;
    requires javafx.fxml;


    opens pl.edu.pwr.sockety to javafx.fxml;
    exports pl.edu.pwr.sockety;
    exports pl.edu.pwr.sockety.controllers;
    opens pl.edu.pwr.sockety.controllers to javafx.fxml;
}