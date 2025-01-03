package com.example.demo;

import com.example.demo.samochód.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class HelloController {
    public Label markaLabel;
    public Label modelTextField;
    public Label nrRejTextField;
    public Label pozycjaXTextField;
    public Label pozycjaYTextField;
    public Button dodajSamochdoButton;

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

    public ImageView carImageView;
    public ImageView mapaImageView;

    @FXML
    private Label welcomeText;
    @FXML
    private ComboBox<String> wybierzSamochodComboBox;
    @FXML
    Samochod tmpCar;
    List<Samochod> createdCars= new ArrayList<>();
    List<String> carModels= new ArrayList<>();

    static Samochod samochod;


    public void addCarToList(String sprzegloNazwa, int sprzegloWaga, int sprzegloCena, int iloscBiegow, int skrzyniaCena, int skrzyniaWaga,
                                    String skrzyniaNazwa, int maxObroty, String silnikNazwa, int silnikWaga, int silnikCena, String numerRejs,
                                    String model, String marka, int waga, int x, int y, int maxspeed) {
        System.out.println("Naciśnięto");
        createdCars.add(newCar(sprzegloNazwa, sprzegloWaga,sprzegloCena, iloscBiegow, skrzyniaCena, skrzyniaWaga, skrzyniaNazwa,maxObroty, silnikNazwa, silnikCena, silnikWaga, numerRejs, model, marka, waga,x,y,maxspeed ));
        carModels.add(samochod.getModel());
        wybierzSamochodComboBox.getItems().addAll(carModels);
        samochod.setController(this);
        aktbiegLabel.setText(String.valueOf(samochod.getaktBieg()));

    }
    public Samochod newCar(String nazwaSprzeglo, int wagaSprzeglo, int cenaSprzeglo, int iloscBiegow, int cenaSkrzynia, int wagaSkrzynia,
                           String nazwaSkrzynia, int maxObroty, String nazwaSilnik, int wagaSilnik, int cenaSilnik, String nrRejestracyjny,
                           String model, String marka, int waga, int x, int y, int maxpredkosc) {
        samochod = new Samochod(iloscBiegow, maxObroty, nrRejestracyjny, marka, model,maxpredkosc, nazwaSilnik, nazwaSkrzynia, wagaSilnik,
                wagaSkrzynia, wagaSprzeglo, cenaSilnik, cenaSkrzynia, cenaSprzeglo);
        samochod.setController(this);
        markaLabel.setText(marka);
        modelTextField.setText(model);
        nrRejTextField.setText(nrRejestracyjny);
        pozycjaXTextField.setText(String.valueOf(x));
        pozycjaYTextField.setText(String.valueOf(y));

        return samochod;
    }


    @FXML
    public void initialize() {
        System.out.println("HelloController initialized");      // Load and set the car image
        Image mapa = new Image(getClass().getResource("/mapa.jpg").toExternalForm());
        Image carImage = new Image(getClass().getResource("/car.jpg").toExternalForm());
        System.out.println("Image width: " +  carImage.getWidth() + ", height: " + carImage.getHeight());
        mapaImageView.setImage(mapa);
        carImageView.setImage(carImage);
        carImageView.setFitWidth(50);                           // Set appropriate dimensions for your image
        carImageView.setFitHeight(50);
        carImageView.setTranslateX(0);
        carImageView.setTranslateY(0);

//        mapa.setOnMouseClicked(event -> { double x = event.getX();
//            double y = event.getY();
//            Pozycja nowaPozycja = new Pozycja(x, y);
//            samochod.jedzDo(nowaPozycja);
//        });

    }

    public void openAddCarWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DodajSamochod.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Dodaj nowy samochód");
        stage.show();
        DodajSamochodController dodajSamochodController = loader.getController();
        dodajSamochodController.setParentController(this);
    }

    void refresh(){
        markaLabel.setText(samochod.getMarka());
        modelTextField.setText(samochod.getModel());
        nrRejTextField.setText(samochod.getnrRej());
        pozycjaXTextField.setText(String.valueOf(samochod.getpozX()));
        pozycjaYTextField.setText(String.valueOf(samochod.getpozY()));
        aktbiegLabel.setText(String.valueOf(samochod.getaktBieg()));
        aktualneObrotyLabel.setText(String.valueOf(samochod.aktualneObroty()));
        maksymalneObrotyLabel.setText(String.valueOf(samochod.maxObroty()));
        stanWlaczeniaSilnikaLabel.setText(stanSilnika());
    }

    @FXML
    private void wlaczSamochod() throws SamochodException, SprzegloException {
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
            refresh();
        }
    }

    @FXML
    public void wylaczSamochod() throws SprzegloException {
        try{
            samochod.wylacz();
            refresh();
            System.out.println("Samochód wyłączony!");
        }catch (SprzegloException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Sprzegło");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
        }
    }

    public String stanSilnika() {
        String stanSilnika;
        if(samochod.stanSilnika()){
           stanSilnika = "Włączony";
        }else{
            stanSilnika = "Wyłączony";
        }
        return stanSilnika;
    }

    @FXML
    public void zwiekszBieg() throws SkrzyniaException, SamochodException, SilnikException, SprzegloException {
        try{
            samochod.skrzyniaZwiekszB();
            System.out.println("Zwiekszam bieg!");
            while (samochod.aktualneObroty() > 2000){
                samochod.zmniejszObroty();
            }
            refresh();
        }catch (SamochodException e) {
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
        refresh();
    }

    public void zmniejszBieg() throws SkrzyniaException, SamochodException {
        try{
            samochod.skrzyniaZmniejszB();
            System.out.println("Zmniejszam bieg!");
        }catch (SamochodException e) {
            alertDialog("Samochód", e);
        }catch (SilnikException e1){
            alertDialog("Silnik", e1);
        }catch (SkrzyniaException e2){
            alertDialog("Skrzynia", e2);
        }catch (SprzegloException e3){
            alertDialog("Sprzęgło", e3);
        }
        refresh();
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

    public void zwiekszObroty() throws SamochodException {
        try {
            samochod.zwiekszObroty();
        } catch (SamochodException e) {
            alertDialog("Samochód", e);
        }
        refresh();
    }

    public void zmniejszObroty() throws SamochodException {
        try {
            samochod.zmniejszObroty();
        } catch (SamochodException e) {
            alertDialog("Samochód", e);
        }catch (SilnikException e1){
            alertDialog("Silnik", e1);
        }
        refresh();
    }


    private void alertDialog(String element, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(element);
        alert.setHeaderText(e.toString());
        alert.showAndWait();
    }
}

