package com.example.demo;

import com.example.demo.samochód.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;


public class HelloController {
    public Label marakaTextField;
    public Label modelTextField;
    public Label nrRejTextField;
    public Label pozycjaXTextField;
    public Label pozycjaYTextField;

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
    public Label aktbiegLabel;


    @FXML
    private Label welcomeText;

    Samochód samochod;
    SkrzyniaBiegow skrzynia;


    @FXML
    public void initialize() {
        samochod = new Samochód(6, "111", "Reunalt", "Clio", 250, "Sam", "Elo", 7000, 5000, 50000, 4000, 5000, 5000);
        marakaTextField.setText(samochod.getMarka());
        modelTextField.setText(samochod.getModel());
        nrRejTextField.setText(samochod.getnrRej());
        pozycjaXTextField.setText(String.valueOf(samochod.getpozX()));
        pozycjaYTextField.setText(String.valueOf(samochod.getpozY()));
        aktbiegLabel.setText(String.valueOf(samochod.getaktBieg()));
    }

    @FXML
    private void wlaczSamochod() {
        System.out.println("Samochód uruchomiony!");
        samochod.wlacz();
    }

    @FXML
    public void wylaczSamochod() {
        System.out.println("Samochód wyłączony!");
        samochod.wylacz();
    }

    @FXML
    public void zwiekszBieg() throws SkrzyniaException, SamochódException {
        try{
            samochod.skrzyniaZwiekszB();
            System.out.println("Zwiekszam bieg!");
        }catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Skrzynia biegów");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
        }
        aktbiegLabel.setText(String.valueOf(samochod.getaktBieg()));
    }



    public void zmniejszBieg() throws SkrzyniaException, SamochódException {
        try{
            samochod.skrzyniaZmniejszB();
            System.out.println("Zmniejszam bieg!");
        }catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Skrzynia biegów");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
        }
        aktbiegLabel.setText(String.valueOf(samochod.getaktBieg()));
    }

    public void zwolnijSprzeglo() {

    }
}

