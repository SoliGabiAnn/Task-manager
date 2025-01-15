package com.example.demo;


import com.example.demo.samochód.SkrzyniaBiegow;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DodajSamochodController {

    public CheckBox wlasnaSkrzyniaCheckBox;
    //samochod
    @FXML
    private TextField markaTextField;
    @FXML
    private TextField modelTextField;
    @FXML
    private TextField numerRejsTextField;
    @FXML
    private TextField wagaTextField;
    @FXML
    private TextField xTextField;
    @FXML
    private TextField yTextField;
    @FXML
    private TextField maxSpeedTextField;

    //skrzynia
    @FXML
    private TextField skrzyniaNazwaTextField;
    @FXML
    private TextField skrzyniaCenaTextField;
    @FXML
    private TextField skrzyniaWagaTextField;
    @FXML
    private TextField iloscBiegowTextField;
    @FXML
    public ComboBox <SkrzyniaBiegow> wybierzSkrzynieComboBox;


    //sprzeglo
    @FXML
    private TextField sprzegloNazwaTextField;
    @FXML
    private TextField sprzegloCenaTextField;
    @FXML
    private TextField sprzegloWagaTextField;
    @FXML
    public ComboBox wybierzSprzegloCombBox;

    //silnik
    @FXML
    private TextField silnikNazwaTextField;
    @FXML
    private TextField silnikCenaTextField;
    @FXML
    private TextField silnikWagaTextField;
    @FXML
    private TextField maxObrotyTextField;

    public ComboBox wybierzSilnikComboBox;


    @FXML
    private Button onCancelButton;
    @FXML
    private Button onConfirmButton;

    private HelloController parentController = new HelloController();


    @FXML
    private void onConfirmButton() {
        String marka = markaTextField.getText();
        String model = modelTextField.getText();
        String numerRejs = numerRejsTextField.getText();
        int waga;
        int x;
        int y;
        String skrzyniaNazwa;
        int skrzyniaCena;
        int skrzyniaWaga;
        int iloscBiegow;
        String sprzegloNazwa;
        int sprzegloCena;
        int sprzegloWaga;
        String silnikNazwa;
        int silnikCena;
        int silnikWaga;
        int maxObroty;
        int maxspeed;

        wybierzSkrzynieComboBox.setItems(FXCollections.observableArrayList(
                new SkrzyniaBiegow(6, "C30 1.6 3M5R7002NE", "aaa", 120, 50, 3000, 500)
        ));

        if (wlasnaSkrzyniaCheckBox.isSelected()) {
            // Pobierz dane z pól tekstowych
            skrzyniaNazwa = skrzyniaNazwaTextField.getText();
            skrzyniaCena = (int) Double.parseDouble(skrzyniaCenaTextField.getText());
            skrzyniaWaga = (int) Double.parseDouble(skrzyniaWagaTextField.getText());
            iloscBiegow = Integer.parseInt(iloscBiegowTextField.getText());
        } else {
            // Pobierz dane z ComboBoxa
            SkrzyniaBiegow selectedSkrzynia = (SkrzyniaBiegow) wybierzSilnikComboBox.getValue();
            skrzyniaNazwa = selectedSkrzynia.getNazwa();
            skrzyniaCena = selectedSkrzynia.getCena();
            skrzyniaWaga = selectedSkrzynia.getWaga();
            iloscBiegow = selectedSkrzynia.getIloscBiegow();


//                String skrzyniaNazwa = getValueFromComboBoxOrTextField(wybierzSkrzynieComboBox, skrzyniaNazwaTextField);
//                int skrzyniaCena = Integer.parseInt(skrzyniaCenaTextField.getText());
//                int skrzyniaWaga = Integer.parseInt(skrzyniaWagaTextField.getText());
//                int iloscBiegow = Integer.parseInt(iloscBiegowTextField.getText());
        }
        try {
            waga = (int) Double.parseDouble(wagaTextField.getText());
            x = Integer.parseInt(xTextField.getText());
            y = Integer.parseInt(yTextField.getText());
//                skrzyniaNazwa = skrzyniaNazwaTextField.getText();
//                skrzyniaCena = (int) Double.parseDouble(skrzyniaCenaTextField.getText());
//                skrzyniaWaga = (int) Double.parseDouble(skrzyniaWagaTextField.getText());
//                iloscBiegow = Integer.parseInt(iloscBiegowTextField.getText());
            sprzegloNazwa = sprzegloNazwaTextField.getText();
            sprzegloCena = (int) Double.parseDouble(sprzegloCenaTextField.getText());
            sprzegloWaga = (int) Double.parseDouble(sprzegloWagaTextField.getText());
            silnikNazwa = silnikNazwaTextField.getText();
            silnikCena = (int) Double.parseDouble(silnikCenaTextField.getText());
            silnikWaga = (int) Double.parseDouble(silnikWagaTextField.getText());
            maxObroty = (int) Double.parseDouble(maxObrotyTextField.getText());
            maxspeed = (int) Double.parseDouble(maxSpeedTextField.getText());

            parentController.newCar(sprzegloNazwa, sprzegloWaga, sprzegloCena, iloscBiegow, skrzyniaCena, skrzyniaWaga,
                    skrzyniaNazwa, maxObroty, silnikNazwa, silnikWaga, silnikCena, numerRejs, model, marka, waga, x, y, maxspeed);
            parentController.addCarToList(sprzegloNazwa, sprzegloWaga, sprzegloCena, iloscBiegow, skrzyniaCena, skrzyniaWaga, skrzyniaNazwa, maxObroty, silnikNazwa, silnikWaga, silnikCena, numerRejs, model, marka, waga, x, y, maxspeed);
            ;
            onCancelButton();
        } catch (NumberFormatException e) {
            System.out.println("Niepoprawne dane. Spróbuj ponownie.");
            return;
        }
        Stage stage = (Stage) onConfirmButton.getScene().getWindow();
        stage.close();
    }

    public void onCancelButton() {
        Stage stage = (Stage) onCancelButton.getScene().getWindow();
        stage.close();
    }

    public void setParentController(HelloController controller) {
        parentController = controller;
    }

    private String getValueFromComboBoxOrTextField(ComboBox<String> comboBox, TextField textField) {
        return comboBox.getValue() != null ? comboBox.getValue() : textField.getText();
    }
}
