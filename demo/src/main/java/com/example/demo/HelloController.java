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
    public ImageView mapaImageView;
    public Pane mapa;

    @FXML
    private ComboBox<Samochod> wybierzSamochodComboBox;
    private ObservableList<Samochod> samochody = FXCollections.observableArrayList();
//    @FXML
//    Samochod tmpCar;
//    List<Samochod> createdCars = new ArrayList<>();
//    List<String> carModels = new ArrayList<>();

    static Samochod samochod;

    public void addCarToList(String sprzegloNazwa, int sprzegloWaga, int sprzegloCena, int iloscBiegow, int skrzyniaCena, int skrzyniaWaga, String skrzyniaNazwa, int maxObroty, String silnikNazwa, int silnikWaga, int silnikCena, String numerRejs, String model, String marka, int waga, int x, int y, int maxspeed) {
        var samochod = newCar(sprzegloNazwa, sprzegloWaga, sprzegloCena, iloscBiegow, skrzyniaCena, skrzyniaWaga, skrzyniaNazwa, maxObroty, silnikNazwa, silnikCena, silnikWaga, numerRejs, model, marka, waga, x, y, maxspeed);
//        carModels.add(samochod.getModel());
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

    }

    public Samochod newCar(String nazwaSprzeglo, int wagaSprzeglo, int cenaSprzeglo, int iloscBiegow, int cenaSkrzynia, int wagaSkrzynia, String nazwaSkrzynia, int maxObroty, String nazwaSilnik, int wagaSilnik, int cenaSilnik, String nrRejestracyjny, String model, String marka, int waga, int x, int y, int maxpredkosc) {
        samochod = new Samochod(iloscBiegow, maxObroty, nrRejestracyjny, marka, model, maxpredkosc, nazwaSilnik, nazwaSkrzynia, nazwaSprzeglo, wagaSilnik, wagaSkrzynia, wagaSprzeglo, cenaSilnik, cenaSkrzynia, cenaSprzeglo);
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
        System.out.println("HelloController initialized");      // Load and set the car image

        Image carImage = new Image(Objects.requireNonNull(getClass().getResource("/car.jpg")).toExternalForm());
        carImageView.setImage(carImage);
        carImageView.setFitWidth(60); // Set appropriat dimensions for your image
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
    }

//    @FXML
//    private void moveCar(MouseEvent event) {
//        double newX = event.getX() - carImageView.getFitWidth() / 2;
//        double newY = event.getY() - carImageView.getFitHeight() / 2;
//
//        carImageView.setX(newX);
//        carImageView.setY(newY);
//
//        System.out.println("Samochód przesunięty na: X=" + newX + " Y=" + newY);
//    }

    public void openAddCarWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DodajSamochod.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Dodaj nowy samochód");
        stage.show();
        DodajSamochodController dodajSamochodController = loader.getController();
        dodajSamochodController.setParentController(this);
    }

    void refresh() {
        markaLabel.setText(samochod.getMarka());
        modelTextField.setText(samochod.getModel());
        nrRejTextField.setText(samochod.getnrRej());
        pozycjaXLabel.setText(String.valueOf(samochod.getpozX()));
        pozycjaYLabel.setText(String.valueOf(samochod.getpozY()));
        nazwaSkrzyniaLabel.setText(String.valueOf(samochod.getSkrzynia().getNazwa()));
        cenaSkrzyniaLabel.setText(String.valueOf(samochod.getSkrzynia().getCena()));
        wagaSkrzyniaLabel.setText(String.valueOf(samochod.getSkrzynia().getWaga()));
        iloscBiegowLabel.setText(String.valueOf(samochod.getSkrzynia().getIloscBiegow()));
        aktbiegLabel.setText(String.valueOf(samochod.getaktBieg()));
        nazwaSprzegloLabel.setText(String.valueOf(samochod.getSkrzynia().getSprzeglo().getNazwa()));
        cenaSprzegloLabel.setText(String.valueOf(samochod.getSkrzynia().getCena()));
        wagaSprzegloLabel.setText(String.valueOf(samochod.getSkrzynia().getWaga()));
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
            this.refresh();

        }
    }

    @FXML
    public void wylaczSamochod() throws SprzegloException, SamochodException {
        try {
            samochod.wylacz();
            refresh();
            System.out.println("Samochód wyłączony!");
        } catch (SprzegloException e) {
            alertDialog("Sprzegło", e);
        }
        if (samochod.getSkrzynia().getSprzeglo().getstanSp()) {
            samochod.zatrzymajSilnik();
            refresh();
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
            System.out.println("Zwiekszam bieg!");
            while (samochod.aktualneObroty() > 2000) {
                samochod.zmniejszObroty();
            }
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
        refresh();
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
        refresh();
    }

    public void zwolnijSprzeglo() {
        samochod.sprzegloZwolnij();
        this.refresh();

    }

    public void wcisnijSprzeglo() throws SkrzyniaException, SprzegloException {
        samochod.sprzegloWcisnij();
        if (samochod.isStanWlaczenia()) {
            stanWlaczeniaSilnikaLabel.setText("Włączony");
        }
        this.refresh();
    }

    public void zwiekszObroty() throws SilnikException, SprzegloException, SkrzyniaException {
        try {
            samochod.zwiekszObroty();
        } catch (SamochodException e) {
            alertDialog("Samochód", e);
        }
        this.refresh();
    }

    public void zmniejszObroty() throws SamochodException, SilnikException {
        try {
            samochod.zmniejszObroty();
        } catch (SamochodException e) {
            alertDialog("Samochód", e);
        } catch (SilnikException e1) {
            alertDialog("Silnik", e1);
        }
        this.refresh();

    }


    private void alertDialog(String element, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(element);
        alert.setHeaderText(e.toString());
        alert.showAndWait();
    }



    public void onCarDeleteButton(ActionEvent actionEvent) {
//        samochod.removeListener(this);
        samochody.remove(samochod);
        wybierzSamochodComboBox.setItems(samochody);
        wybierzSamochodComboBox.getSelectionModel().selectFirst();
    }
}