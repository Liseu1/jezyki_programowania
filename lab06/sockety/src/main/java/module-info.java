module pl.edu.pwr.sockety {
    requires javafx.controls;
    requires javafx.fxml;

    exports pl.edu.pwr.sockety.tanker to javafx.graphics;
    opens pl.edu.pwr.sockety.tanker to javafx.fxml;

    exports pl.edu.pwr.sockety.office to javafx.graphics;
    opens pl.edu.pwr.sockety.office to javafx.fxml;

    exports pl.edu.pwr.sockety.house to javafx.graphics;
    opens pl.edu.pwr.sockety.house to javafx.fxml;

    exports pl.edu.pwr.sockety.sewage to javafx.graphics;
    opens pl.edu.pwr.sockety.sewage to javafx.fxml;

    exports pl.edu.pwr.sockety.office.model;
}