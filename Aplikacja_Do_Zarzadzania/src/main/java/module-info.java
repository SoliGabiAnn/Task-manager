module com.example.aplikacja_do_zarzadzania {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    exports kod_aplikacji;
    opens kod_aplikacji to com.fasterxml.jackson.databind;


    opens com.example.aplikacja_do_zarzadzania to javafx.fxml;
    exports com.example.aplikacja_do_zarzadzania;
}