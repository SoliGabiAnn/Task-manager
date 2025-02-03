package com.example.demo;

import com.example.demo.samochód.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;


import java.io.IOException;
import java.net.http.WebSocket;

import java.util.Objects;


public class HelloController implements WebSocket.Listener {
    @FXML
    public Label markaLabel;
    @FXML
    public Label modelTextField;
    @FXML
    public Label nrRejTextField;
    @FXML
    public Label pozycjaXLabel;
    @FXML
    public Label pozycjaYLabel;
    public Button dodajSamochdoButton;
    public Button usunSamochodButton;

    public ToggleGroup stanSamochodu;
    public ToggleButton wlaczSamochodButton;
    public ToggleButton wylaczSamochodButton;

    public Button zmniejszBiegButton;
    public Button zwiekszBiegButton;
    public Label aktbiegLabel;
    public Label nazwaSkrzyniaLabel;
    public Label cenaSkrzyniaLabel;
    public Label wagaSkrzyniaLabel;
    public Label iloscBiegowLabel;

    public ToggleGroup stanSprzegla;
    public ToggleButton zwolnijSprzegloButton;
    public ToggleButton wscisnijSprzegloButton;
    public Label nazwaSprzegloLabel;
    public Label wagaSprzegloLabel;
    public Label cenaSprzegloLabel;

    public Button zwiekszObrotyButton;
    public Button zmniejszObrotyButton;
    public Label aktualneObrotyLabel;
    public Label maksymalneObrotyLabel;
    public Label stanWlaczeniaSilnikaLabel;
    public Label nazwaSilnikLabel;
    public Label cenaSilnikLabel;
    public Label wagaSilnikLabel;

    public ImageView carImageView;
    public Pane mapa;
    public Label wagaSamochod;

    @FXML
    private ComboBox<Samochod> wybierzSamochodComboBox;
    private ObservableList<Samochod> samochody = FXCollections.observableArrayList();


    static Samochod samochod;

    public void addCarToList(String sprzegloNazwa, int sprzegloWaga, int sprzegloCena, int iloscBiegow, int skrzyniaCena, int skrzyniaWaga, String skrzyniaNazwa, int maxObroty, String silnikNazwa, int silnikWaga, int silnikCena, String numerRejs, String model, String marka, int waga, int x, int y, int maxspeed) {
        var samochod = newCar(sprzegloNazwa, sprzegloWaga, sprzegloCena, iloscBiegow, skrzyniaCena, skrzyniaWaga, skrzyniaNazwa, maxObroty, silnikNazwa, silnikWaga,silnikCena ,numerRejs, model, marka, waga, x, y, maxspeed);
        samochody.add(samochod);

        samochod.setController(this);
        aktbiegLabel.setText(String.valueOf(samochod.getaktBieg()));
        nazwaSkrzyniaLabel.setText(String.valueOf(samochod.getSkrzynia().getNazwa()));
        cenaSkrzyniaLabel.setText(String.valueOf(samochod.getSkrzynia().getCena()));
        wagaSkrzyniaLabel.setText(String.valueOf(samochod.getSkrzynia().getWagaSkrzynia()));
        iloscBiegowLabel.setText(String.valueOf(iloscBiegow));
        nazwaSprzegloLabel.setText(String.valueOf(sprzegloNazwa));
        cenaSprzegloLabel.setText(String.valueOf(sprzegloCena));
        wagaSprzegloLabel.setText(String.valueOf(sprzegloWaga));
        nazwaSilnikLabel.setText(String.valueOf(silnikNazwa));
        cenaSilnikLabel.setText(String.valueOf(silnikCena));
        wagaSilnikLabel.setText(String.valueOf(silnikWaga));
        aktualneObrotyLabel.setText(String.valueOf(samochod.aktualneObroty()));
        maksymalneObrotyLabel.setText(String.valueOf(samochod.maxObroty()));
        stanWlaczeniaSilnikaLabel.setText(stanSilnika());
        wybierzSamochodComboBox.getSelectionModel().select(samochod);
        wagaSamochod.setText(String.valueOf(samochod.getWagaSamochodu()));

    }

    public Samochod newCar(String nazwaSprzeglo, int wagaSprzeglo, int cenaSprzeglo, int iloscBiegow, int cenaSkrzynia, int wagaSkrzynia, String nazwaSkrzynia, int maxObroty, String nazwaSilnik, int wagaSilnik, int cenaSilnik, String nrRejestracyjny, String model, String marka, double waga, int x, int y, int maxpredkosc) {
        samochod = new Samochod(iloscBiegow, maxObroty, nrRejestracyjny, marka, model, maxpredkosc, waga,nazwaSilnik, nazwaSkrzynia, nazwaSprzeglo, wagaSilnik, wagaSkrzynia, wagaSprzeglo, cenaSilnik, cenaSkrzynia, cenaSprzeglo);
        samochod.setController(this);
        markaLabel.setText(marka);
        modelTextField.setText(model);
        nrRejTextField.setText(nrRejestracyjny);
        pozycjaXLabel.setText(String.valueOf(x));
        pozycjaYLabel.setText(String.valueOf(y));
        return samochod;
    }


    @FXML
    public void initialize() {
        System.out.println("HelloController initialized");

        Image carImage = new Image(Objects.requireNonNull(getClass().getResource("/car.jpg")).toExternalForm());
        carImageView.setImage(carImage);
        carImageView.setFitWidth(80);
        carImageView.setFitHeight(60);
        carImageView.setTranslateX(0);
        carImageView.setTranslateY(0);


        mapa.setOnMouseClicked(event -> {
            if (samochod != null) {

                double x = event.getX() - 30;
                double y = event.getY() - 30;
                Pozycja nowaPozycja = new Pozycja(x, y);
                try {
                    samochod.jedzDo(nowaPozycja);
                } catch (SkrzyniaException e) {
                    throw new RuntimeException(e);
                }

            }
        });
        mapa.setStyle("-fx-background-color: #c8dfc8;");

        wybierzSamochodComboBox.setOnAction(event -> {
            samochod = wybierzSamochodComboBox.getSelectionModel().getSelectedItem();
            if (samochod != null) {
                samochod.addListener((Listener) this);
                this.refresh();
            }
        });
        wybierzSamochodComboBox.setItems(samochody);
        wybierzSamochodComboBox.setCellFactory(new Callback<ListView<Samochod>, ListCell<Samochod>>() {
            @Override
            public ListCell<Samochod> call(ListView<Samochod> param) {
                return new ListCell<Samochod>() {
                    @Override
                    protected void updateItem(Samochod item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.getMarka() + " " + item.getModel());
                        }
                    }
                };
            }
        });
        wybierzSamochodComboBox.setButtonCell(new ListCell<Samochod>() {
            @Override
            protected void updateItem(Samochod item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getMarka() + " " + item.getModel());
                }
            }
        });

        wybierzSamochodComboBox.setOnAction(event ->
        {
            samochod = wybierzSamochodComboBox.getSelectionModel().getSelectedItem();
            this.refresh();

        });

        if (samochod != null) {
            samochod.start();
        }
        if(samochod == null){
            wylaczSamochodButton.setDisable(true);
            wlaczSamochodButton.setDisable(true);
            zwiekszBiegButton.setDisable(true);
            zmniejszBiegButton.setDisable(true);
            wscisnijSprzegloButton.setDisable(true);
            zwolnijSprzegloButton.setDisable(true);
            zwiekszObrotyButton.setDisable(true);
            zmniejszObrotyButton.setDisable(true);
        }
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

    public void refresh() {
            markaLabel.setText(samochod.getMarka());
            modelTextField.setText(samochod.getModel());
            nrRejTextField.setText(samochod.getnrRej());
            pozycjaXLabel.setText(String.valueOf(samochod.getpozX()));
            pozycjaYLabel.setText(String.valueOf(samochod.getpozY()));
            nazwaSkrzyniaLabel.setText(String.valueOf(samochod.getSkrzynia().getNazwa()));
            cenaSkrzyniaLabel.setText(String.valueOf(samochod.getSkrzynia().getCena()));
            wagaSkrzyniaLabel.setText(String.valueOf(samochod.getSkrzynia().getWagaSkrzynia()));
            iloscBiegowLabel.setText(String.valueOf(samochod.getSkrzynia().getIloscBiegow()));
            aktbiegLabel.setText(String.valueOf(samochod.getaktBieg()));
            nazwaSprzegloLabel.setText(String.valueOf(samochod.getSkrzynia().getSprzeglo().getNazwa()));
            cenaSprzegloLabel.setText(String.valueOf(samochod.getSkrzynia().getCena()));
            wagaSprzegloLabel.setText(String.valueOf(samochod.getSkrzynia().getSprzeglo().getWaga()));
            nazwaSilnikLabel.setText(String.valueOf(samochod.getSilnik().getNazwa()));
            cenaSilnikLabel.setText(String.valueOf(samochod.getSilnik().getCena()));
            wagaSilnikLabel.setText(String.valueOf(samochod.getSilnik().getWaga()));
            aktualneObrotyLabel.setText(String.valueOf(samochod.aktualneObroty()));
            maksymalneObrotyLabel.setText(String.valueOf(samochod.maxObroty()));
            stanWlaczeniaSilnikaLabel.setText(stanSilnika());
        Platform.runLater(() -> {
            carImageView.setTranslateX(samochod.getpozX());
            carImageView.setTranslateY(samochod.getpozY());
        });
    }

    @FXML
    private void wlaczSamochod() throws SamochodException, SprzegloException {
        try {
            samochod.wlacz();
            this.refresh();
        } catch (SprzegloException e) {
            alertDialog("Sprzęgło", e);
        }
        if (samochod.getSkrzynia().getSprzeglo().getstanSp()) {
            samochod.uruchomSilnik();
            stanWlaczeniaSilnikaLabel.setText("Włączony");
            this.refresh();

        }
    }

    @FXML
    public void wylaczSamochod() throws SprzegloException, SamochodException {
        try {
            samochod.wylacz();
            this.refresh();
            System.out.println("Samochód wyłączony!");
        } catch (SprzegloException e) {
            alertDialog("Sprzegło", e);
        }
        if (samochod.getSkrzynia().getSprzeglo().getstanSp()) {
            samochod.zatrzymajSilnik();
            this.refresh();
        }
    }

    public String stanSilnika() {
        String stanSilnika;
        if (samochod.stanSilnika()) {
            stanSilnika = "Włączony";
        } else {
            stanSilnika = "Wyłączony";
        }
        return stanSilnika;
    }

    @FXML
    public void zwiekszBieg() throws SamochodException, SprzegloException, SkrzyniaException, SilnikException {
        try {
            samochod.skrzyniaZwiekszB();
            samochod.sprzegloZwolnij();
            while (samochod.aktualneObroty() > 2000) {
                samochod.zmniejszObroty();
            }
            samochod.sprzegloWcisnij();
            this.refresh();

        } catch (SamochodException e) {
            alertDialog("Samochód", e);
        } catch (SilnikException e1) {
            alertDialog("Silnike", e1);
        } catch (SkrzyniaException e2) {
            alertDialog("Skrzynia", e2);
        } catch (SprzegloException e3) {
            alertDialog("Sprzeglo", e3);
        }
    }

    public void zmniejszBieg() throws SilnikException, SprzegloException, SkrzyniaException, SamochodException {
        try {
            samochod.skrzyniaZmniejszB();
            this.refresh();
            System.out.println("Zmniejszam bieg!");
        } catch (SamochodException e) {
            alertDialog("Samochód", e);
        } catch (SilnikException e1) {
            alertDialog("Silnik", e1);
        } catch (SkrzyniaException e2) {
            alertDialog("Skrzynia", e2);
        } catch (SprzegloException e3) {
            alertDialog("Sprzęgło", e3);
        }
    }

    public void zwolnijSprzeglo() {
        samochod.sprzegloZwolnij();
        this.refresh();
    }

    public void wcisnijSprzeglo() throws SkrzyniaException, SprzegloException {
        samochod.sprzegloWcisnij();
        this.refresh();
    }

    public void zwiekszObroty() throws SilnikException, SprzegloException, SkrzyniaException {
        try {
            samochod.zwiekszObroty();
            this.refresh();
        } catch (SamochodException e) {
            alertDialog("Samochód", e);
        } catch (SprzegloException e1) {
            alertDialog("Sprzeglo", e1);
        } catch (SilnikException e2){
            alertDialog("Silnik", e2);
        }

    }

    public void zmniejszObroty() throws SamochodException, SilnikException {
        try {
            samochod.zmniejszObroty();
            this.refresh();
        } catch (SamochodException e) {
            alertDialog("Samochód", e);
        } catch (SilnikException e1) {
            alertDialog("Silnik", e1);
        }
    }


    private void alertDialog(String element, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(element);
        alert.setHeaderText(e.getMessage());
        alert.showAndWait();
    }


    public void onCarDeleteButton(ActionEvent actionEvent) {
        samochod.removeListener((Listener) this);
        samochody.remove(samochod);
        wybierzSamochodComboBox.setItems(samochody);
        wybierzSamochodComboBox.getSelectionModel().selectFirst();
    }
}