package com.example.aplikacja_do_zarzadzania;

import javafx.scene.control.*;
import kod_aplikacji.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.fxml.FXML;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


public class HelloController {

    public Button AddButton;
    public VBox projectContainer;
    public TextField projectNameTextField;
    public DatePicker projectDueDateDatePicker;
    public DatePicker projectStartDateDatePicker;
    public TextField taskNameTextField;

    Accordion accordion = new Accordion();
    private TitledPane selectedTitlePane;
    User user = new User();


    @FXML
    private void onAddButton() {
        String name = ""; // Inicjalizujemy zmienną name na pustą.

        // Jeśli użytkownik wprowadza nazwę projektu
        if (!projectNameTextField.getText().isEmpty()) {
            name = projectNameTextField.getText();  // Pobieramy tekst z pola
            handleAddProject(name);
            projectNameTextField.clear();  // Czyścimy pole po dodaniu projektu

        } else if (!taskNameTextField.getText().isEmpty() && selectedTitlePane != null) {
            // Jeśli wprowadzono nazwę zadania i wybrano projekt
            name = taskNameTextField.getText();
            handleAddTask(name);
            taskNameTextField.clear();  // Czyścimy pole po dodaniu zadania
        }

        // Jeśli pole jest puste, wyświetlamy alert
        if (name.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Proszę podać nazwę projektu lub zadania.", ButtonType.OK);
            alert.show();
            return;
        }
    }

    private String handleDateAction(DatePicker datePicker) {
        // Pobierz wybraną datę
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate != null) {
            int day = selectedDate.getDayOfMonth(); // Dzień
            int month = selectedDate.getMonthValue(); // Miesiąc
            int year = selectedDate.getYear(); // Rok

            return day + "." + month + "." + year;
        } else {
            System.out.println("Nie wybrano daty!");
            return null;
        }
    }


    private void handleAddProject(String projectName) {
        //tworzymy nowy projekt
        var new_Project = new TitledPane();

        //dodanie nazwy z checkbox'em
        CheckBox projectCheckBox = new CheckBox(projectNameTextField.getText());
        new_Project.setGraphic(projectCheckBox);

        var project_Accordion = new Accordion();
        new_Project.setContent(project_Accordion);

        //dodanie opisu do projektu
        var pane = new AnchorPane();

        var due_Date = new Label("Due Date");
        due_Date.setLayoutX(10);
        due_Date.setLayoutY(10);

        var dueDate = new TextField();
        dueDate.setLayoutX(70);
        dueDate.setLayoutY(5);
        dueDate.setPromptText(handleDateAction(projectDueDateDatePicker));
        dueDate.setEditable(false);

        var start_Date = new Label("Start Date");
        start_Date.setLayoutX(10);
        start_Date.setLayoutY(50);

        var startDate = new TextField();
        startDate.setLayoutX(70);
        startDate.setLayoutY(45);
        startDate.setPromptText(handleDateAction(projectStartDateDatePicker));

        //dodanie dat
        pane.getChildren().addAll(due_Date, dueDate, start_Date, startDate);

        TitledPane about_Project = new TitledPane();
        about_Project.setText("About Project");
        project_Accordion.getPanes().add(about_Project);
        about_Project.setContent(pane);

        //dodanie projektu do Vbox'a
        projectContainer.getChildren().add(new_Project);

        //dodanie projektu do listy użytkownika
        user.addToDoProject(projectName, getLocalDateTime(projectStartDateDatePicker), getLocalDateTime(projectDueDateDatePicker));

        //żeby można było wybierać ten projekt
        new_Project.setOnMouseClicked(event -> handleSelectProject(new_Project));

        projectDueDateDatePicker.setValue(null);
    }

    private LocalDateTime getLocalDateTime(DatePicker datePicker) {
        LocalDate datePickerValue = datePicker.getValue();
        LocalDateTime dateTime = datePickerValue.atTime(LocalTime.of(0, 0));
        return dateTime;
    }

    ;

    private void handleSelectProject(TitledPane titlePane) {
        // Ustawiamy wybrany TitlePane (do którego będziemy dodawać zadania)
        selectedTitlePane = titlePane;
    }

    private void handleAddTask(String taskName) {
        if (selectedTitlePane.isExpanded()) {
            // Tworzymy nowe zadanie (TitledPane)

            TitledPane newTask = new TitledPane(taskName, new Label("Opis zadania"));

            Accordion accordion = (Accordion) selectedTitlePane.getContent();

            // Dodajemy zadanie do Accordion
            accordion.getPanes().add(newTask);
        }
    }

//    @FXML
//    private void onDeleteButton() {
//        TitledPane expandedPane = accordion.getExpandedPane();
//
//        accordion.getPanes().remove(expandedPane);
//    }
}