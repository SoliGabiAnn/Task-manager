package com.example.demo;

import com.example.demo.samochód.*;
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
    public Label aktbiegLabel;

    public ToggleGroup stanSprzegla;
    public ToggleButton zwolnijSprzegloButton;
    public ToggleButton wscisnijSprzegloButton;

    public Button zwiekszObrotyButton;
    public Button zmniejszObrotyButton;
    public Label aktualneObrotyLabel;
    public Label maksymalneObrotyLabel;
    public Label stanWlaczeniaSilnikaLabel;

    @FXML
    private Label welcomeText;
    public ComboBox wybierzSamochdoComboBox;

    Samochód samochod;

    @FXML
    public void initialize() {
        samochod = new Samochód(6, "111", "Reunalt", "Clio", 250, "Sam", "Elo", 7000, 5000, 50000, 4000, 5000, 5000);
        marakaTextField.setText(samochod.getMarka());
        modelTextField.setText(samochod.getModel());
        nrRejTextField.setText(samochod.getnrRej());
        pozycjaXTextField.setText(String.valueOf(samochod.getpozX()));
        pozycjaYTextField.setText(String.valueOf(samochod.getpozY()));
        aktbiegLabel.setText(String.valueOf(samochod.getaktBieg()));
        aktualneObrotyLabel.setText(String.valueOf(samochod.aktualneObroty()));
        maksymalneObrotyLabel.setText(String.valueOf(samochod.maxObroty()));
        stanWlaczeniaSilnikaLabel.setText("Wyłączony");
        wybierzSamochdoComboBox.getItems().add("Samochód 1");
    }

    @FXML
    private void wlaczSamochod() throws SamochódException, SprzegloException {
        try{
            samochod.wlacz();
            System.out.println("Samochód włączony!");
        }catch (SprzegloException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Sprzegło");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
        }
        if(samochod.stansprzegla()){
            samochod.uruchomSilnik();
            aktualneObrotyLabel.setText(String.valueOf(samochod.aktualneObroty()));
            stanWlaczeniaSilnikaLabel.setText("Włączony");
        }
    }

    @FXML
    public void wylaczSamochod() throws SprzegloException {
        try{
            samochod.wylacz();
            System.out.println("Samochód wyłączony!");
        }catch (SprzegloException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Sprzegło");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void zwiekszBieg() throws SkrzyniaException, SamochódException, SilnikException, SprzegloException {
        try{
            samochod.skrzyniaZwiekszB();
            System.out.println("Zwiekszam bieg!");
            while (samochod.aktualneObroty() > 2000){
                samochod.zmniejszObroty();
            }
            aktualneObrotyLabel.setText(String.valueOf(samochod.aktualneObroty()));
        }catch (SamochódException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Samochód");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
        }catch (SilnikException e1){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Silnik");
            alert.setHeaderText(e1.getMessage());
            alert.showAndWait();
        }catch (SkrzyniaException e2){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Skrzynia");
            alert.setHeaderText(e2.getMessage());
            alert.showAndWait();
        }catch (SprzegloException e3){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Sprzęgło");
            alert.setHeaderText(e3.getMessage());
            alert.showAndWait();
        }
        aktbiegLabel.setText(String.valueOf(samochod.getaktBieg()));
    }

    public void zmniejszBieg() throws SkrzyniaException, SamochódException {
        try{
            samochod.skrzyniaZmniejszB();
            System.out.println("Zmniejszam bieg!");
        }catch (SamochódException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Samochód");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
        }catch (SilnikException e1){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Silnik");
            alert.setHeaderText(e1.getMessage());
            alert.showAndWait();
        }catch (SkrzyniaException e2){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Skrzynia");
            alert.setHeaderText(e2.getMessage());
            alert.showAndWait();
        }catch (SprzegloException e3){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Sprzęgło");
            alert.setHeaderText(e3.getMessage());
            alert.showAndWait();
        }
        aktbiegLabel.setText(String.valueOf(samochod.getaktBieg()));
    }

    public void zwolnijSprzeglo() {
        samochod.sprzegloZwolnij();
    }

    public void wcisnijSprzeglo() throws SkrzyniaException, SprzegloException {
        samochod.sprzegloWcisnij();
        if(samochod.isStanWlaczenia()){
            stanWlaczeniaSilnikaLabel.setText("Włączony");
        }
    }

    public void zwiekszObroty() throws SamochódException {
        try {
            samochod.zwiekszObroty();
        } catch (SamochódException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Samochód");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
        }
        aktualneObrotyLabel.setText(String.valueOf(samochod.aktualneObroty()));
    }

    public void zmniejszObroty() throws SamochódException {
        try {
            samochod.zmniejszObroty();
        } catch (SamochódException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Samochód");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
        }catch (SilnikException e1){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Silnik");
            alert.setHeaderText(e1.getMessage());
            alert.showAndWait();
        }
        aktualneObrotyLabel.setText(String.valueOf(samochod.aktualneObroty()));
    }
}

