package com.example.demo;

import com.example.demo.samochód.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DodajSamochodController {

    @FXML
    private TextField markaTextField;
    @FXML
    private TextField modelTextField;
    @FXML
    private TextField numerRejsTextField;
    @FXML
    private TextField wagaTextField;
    @FXML
    private TextField maxSpeedTextField;

    @FXML
    private RadioButton wlasnaSkrzyniaRadio;
    @FXML
    private RadioButton predefSkrzyniaRadio;
    @FXML
    private RadioButton wlasneSprzegloRadio;
    @FXML
    private RadioButton predefSprzegloRadio;
    @FXML
    private RadioButton wlasnySilnikRadio;
    @FXML
    private RadioButton predefSilnikRadio;

    @FXML
    private VBox skrzyniaInputGroup;
    @FXML
    private VBox sprzegloInputGroup;
    @FXML
    private VBox silnikInputGroup;

    @FXML
    private TextField skrzyniaNazwaTextField;
    @FXML
    private TextField skrzyniaCenaTextField;
    @FXML
    private TextField skrzyniaWagaTextField;
    @FXML
    private TextField iloscBiegowTextField;
    @FXML
    private ComboBox<SkrzyniaBiegow> wybierzSkrzynieComboBox;

    @FXML
    private TextField sprzegloNazwaTextField;
    @FXML
    private TextField sprzegloCenaTextField;
    @FXML
    private TextField sprzegloWagaTextField;
    @FXML
    private ComboBox<Sprzeglo> wybierzSprzegloComboBox;

    @FXML
    private TextField silnikNazwaTextField;
    @FXML
    private TextField silnikCenaTextField;
    @FXML
    private TextField silnikWagaTextField;
    @FXML
    private TextField maxObrotyTextField;
    @FXML
    private ComboBox<Silnik> wybierzSilnikComboBox;

    @FXML
    private Button cancelButton;
    @FXML
    private Button confirmButton;

    private HelloController parentController;


    @FXML
    public void initialize() {
        ToggleGroup skrzyniaGroup = new ToggleGroup();
        wlasnaSkrzyniaRadio.setToggleGroup(skrzyniaGroup);
        predefSkrzyniaRadio.setToggleGroup(skrzyniaGroup);

        ToggleGroup sprzegloGroup = new ToggleGroup();
        wlasneSprzegloRadio.setToggleGroup(sprzegloGroup);
        predefSprzegloRadio.setToggleGroup(sprzegloGroup);

        ToggleGroup silnikGroup = new ToggleGroup();
        wlasnySilnikRadio.setToggleGroup(silnikGroup);
        predefSilnikRadio.setToggleGroup(silnikGroup);

        wlasnaSkrzyniaRadio.selectedProperty().addListener((obs, oldVal, newVal) -> {
            skrzyniaInputGroup.setDisable(!newVal);
            wybierzSkrzynieComboBox.setDisable(newVal);
        });

        wlasneSprzegloRadio.selectedProperty().addListener((obs, oldVal, newVal) -> {
            sprzegloInputGroup.setDisable(!newVal);
            wybierzSprzegloComboBox.setDisable(newVal);
        });

        wlasnySilnikRadio.selectedProperty().addListener((obs, oldVal, newVal) -> {
            silnikInputGroup.setDisable(!newVal);
            wybierzSilnikComboBox.setDisable(newVal);
        });

        initializeComboBoxes();

    }

    private void initializeComboBoxes() {
        wybierzSkrzynieComboBox.setItems(FXCollections.observableArrayList(
                new SkrzyniaBiegow(6, "C30 1.6 3M5R7002NE", 12, 3000),
                new SkrzyniaBiegow(5, "B20 1.4 2M4R5001NE", 10, 2500)
        ));

        wybierzSkrzynieComboBox.setCellFactory(param -> new ListCell<SkrzyniaBiegow>() {
            @Override
            protected void updateItem(SkrzyniaBiegow item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNazwa());
                }
            }
        });

        wybierzSkrzynieComboBox.setButtonCell(new ListCell<SkrzyniaBiegow>() {
            @Override
            protected void updateItem(SkrzyniaBiegow item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNazwa());
                }
            }
        });

        wybierzSprzegloComboBox.setItems(FXCollections.observableArrayList(
                new Sprzeglo("SP240, Sport Clutch", 8, 1500),
                new Sprzeglo("SP180, Standard Clutch", 6, 1200)
        ));

        wybierzSprzegloComboBox.setCellFactory(param -> new ListCell<Sprzeglo>() {
            @Override
            protected void updateItem(Sprzeglo item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNazwa());
                }
            }
        });

        wybierzSprzegloComboBox.setButtonCell(new ListCell<Sprzeglo>() {
            @Override
            protected void updateItem(Sprzeglo item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNazwa());
                }
            }
        });

        wybierzSilnikComboBox.setItems(FXCollections.observableArrayList(
                new Silnik("2.0T, Turbo Engine", 25, 3000, 6000),
                new Silnik("1.8N, Natural Engine", 20, 2500, 5000)
        ));


        wybierzSilnikComboBox.setCellFactory(param -> new ListCell<Silnik>() {
            @Override
            protected void updateItem(Silnik item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNazwa());
                }
            }
        });

        wybierzSilnikComboBox.setButtonCell(new ListCell<Silnik>() {
            @Override
            protected void updateItem(Silnik item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNazwa());
                }
            }
        });

    }

    @FXML
    private void onConfirmButton() {
        try {
            String marka = markaTextField.getText();
            String model = modelTextField.getText();
            String numerRejs = numerRejsTextField.getText();
            int waga = Integer.parseInt(wagaTextField.getText());
            int maxspeed = Integer.parseInt(maxSpeedTextField.getText());

            String skrzyniaNazwa;
            int skrzyniaCena;
            int skrzyniaWaga;
            int iloscBiegow;

            if (wlasnaSkrzyniaRadio.isSelected()) {
                skrzyniaNazwa = skrzyniaNazwaTextField.getText();
                skrzyniaCena = Integer.parseInt(skrzyniaCenaTextField.getText());
                skrzyniaWaga = Integer.parseInt(skrzyniaWagaTextField.getText());
                iloscBiegow = Integer.parseInt(iloscBiegowTextField.getText());
            } else {
                SkrzyniaBiegow selectedSkrzynia = wybierzSkrzynieComboBox.getValue();
                skrzyniaNazwa = selectedSkrzynia.getNazwa();
                skrzyniaCena = selectedSkrzynia.getCena();
                skrzyniaWaga = selectedSkrzynia.getWaga();
                iloscBiegow = selectedSkrzynia.getIloscBiegow();
            }

            String sprzegloNazwa;
            int sprzegloCena;
            int sprzegloWaga;

            if (wlasneSprzegloRadio.isSelected()) {
                sprzegloNazwa = sprzegloNazwaTextField.getText();
                sprzegloCena = Integer.parseInt(sprzegloCenaTextField.getText());
                sprzegloWaga = Integer.parseInt(sprzegloWagaTextField.getText());
            } else {
                Sprzeglo selectedSprzeglo = wybierzSprzegloComboBox.getValue();
                sprzegloNazwa = selectedSprzeglo.getNazwa();
                sprzegloCena = selectedSprzeglo.getCena();
                sprzegloWaga = selectedSprzeglo.getWaga();
            }

            String silnikNazwa;
            int silnikCena;
            int silnikWaga;
            int maxObroty;

            if (wlasnySilnikRadio.isSelected()) {
                silnikNazwa = silnikNazwaTextField.getText();
                silnikCena = Integer.parseInt(silnikCenaTextField.getText());
                silnikWaga = Integer.parseInt(silnikWagaTextField.getText());
                maxObroty = Integer.parseInt(maxObrotyTextField.getText());
            } else {
                Silnik selectedSilnik = wybierzSilnikComboBox.getValue();
                silnikNazwa = selectedSilnik.getNazwa();
                silnikCena = selectedSilnik.getCena();
                silnikWaga = selectedSilnik.getWaga();
                maxObroty = selectedSilnik.getMaxObroty();
            }

            parentController.newCar(sprzegloNazwa, sprzegloWaga, sprzegloCena, iloscBiegow,
                    skrzyniaCena, skrzyniaWaga, skrzyniaNazwa, maxObroty, silnikNazwa,
                    silnikWaga, silnikCena, numerRejs, model, marka, waga, 0, 0, maxspeed);

            parentController.addCarToList(sprzegloNazwa, sprzegloWaga, sprzegloCena, iloscBiegow,
                    skrzyniaCena, skrzyniaWaga, skrzyniaNazwa, maxObroty, silnikNazwa,
                    silnikWaga, silnikCena, numerRejs, model, marka, waga, 0, 0, maxspeed);

            parentController.wylaczSamochodButton.setDisable(false);
            parentController.wlaczSamochodButton.setDisable(false);
            parentController.zwiekszBiegButton.setDisable(false);
            parentController.zmniejszBiegButton.setDisable(false);
            parentController.wscisnijSprzegloButton.setDisable(false);
            parentController.zwolnijSprzegloButton.setDisable(false);
            parentController.zwiekszObrotyButton.setDisable(false);
            parentController.zmniejszObrotyButton.setDisable(false);

            onCancelButton();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Input");
            alert.setContentText("Please check all numeric fields and try again.");
            alert.showAndWait();
        }
    }

    @FXML
    public void onCancelButton() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void setParentController(HelloController controller) {
        this.parentController = controller;
    }
}