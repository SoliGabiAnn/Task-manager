module com.example.aplikacja_do_zarzadzania {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;

    opens taskmanager to com.fasterxml.jackson.databind;


    exports taskmanager.exception;
    opens taskmanager.exception to com.fasterxml.jackson.databind;
    exports taskmanager.model;
    opens taskmanager.model to com.fasterxml.jackson.databind;
    exports taskmanager.util;
    exports taskmanager.controller;
    opens taskmanager.util to com.fasterxml.jackson.databind, javafx.fxml;
    exports taskmanager;
    opens taskmanager.controller to com.fasterxml.jackson.databind, javafx.fxml;
}