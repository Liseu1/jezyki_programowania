module pl.edu.pwr.kajaki {

    requires java.desktop;
    requires java.sql;
    requires com.h2database;

    exports pl.edu.pwr.services;
    exports pl.edu.pwr.ui;
    exports pl.edu.pwr.model;
    exports pl.edu.pwr.exceptions;
}