package com.example.demo;

import com.example.demo.samochód.Samochód;
import javafx.fxml.FXML;
import javafx.scene.control.*;


public class HelloController {
    public ToggleGroup stanSamochodu;
    public ToggleButton wlaczSamochodButton;
    public ToggleButton wylaczSamochodButton;

    public Button zmniejszBiegButton;
    public Button zwiekszBiegButton;

    public ToggleGroup stanSprzegla;
    public ToggleButton zwolnijSprzegloButton;
    public ToggleButton wscisnijSprzegloButton;
    
    public ToggleGroup stanSinlnika;
    public ToggleButton wlaczSilnikButton;
    public ToggleButton wylaczSilnikButton;


    @FXML
    private Label welcomeText;

    @FXML
    public void initialize() {
         var samochod = new Samochód(6, "111", "Reunalt", 250, "Sam", "Elo",7000, 5000, 50000, 4000, 5000, 5000);
    }

    @FXML
    private void wlaczSamochod() {
        System.out.println("Samochód uruchomiony!");


    }

    @FXML
    public void wylaczSamochod() {
        System.out.println("Samochód wyłączony!");
    }
    @FXML
    public void zwiekszBieg() {
        System.out.println("Zwieksz bieg!");
    }
}