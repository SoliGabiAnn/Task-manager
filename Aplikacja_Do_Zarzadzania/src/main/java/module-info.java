module com.example.aplikacja_do_zarzadzania {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.aplikacja_do_zarzadzania to javafx.fxml;
    exports com.example.aplikacja_do_zarzadzania;
}