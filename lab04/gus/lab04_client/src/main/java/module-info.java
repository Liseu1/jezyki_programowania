module lab04.client {
    requires com.fasterxml.jackson.core;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;

    exports pl.edu.pwr.client.model;
    exports pl.edu.pwr.client;
}