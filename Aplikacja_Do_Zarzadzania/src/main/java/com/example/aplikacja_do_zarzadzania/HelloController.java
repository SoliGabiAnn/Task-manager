package com.example.aplikacja_do_zarzadzania;

import javafx.scene.control.*;
import kod_aplikacji.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.fxml.FXML;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


public class HelloController {

    public Button AddButton;
    public Button DeleteButton;
    public VBox toDoProjectContainer;
    public VBox doingProjectContainer;
    public VBox doneProjectContainer;

    public TextField projectNameTextField;
    public DatePicker projectDueDateDatePicker;
    public DatePicker projectStartDateDatePicker;
    public TextField taskNameTextField;
    public DatePicker taskStartDateDatePicker;
    public DatePicker taskDueDateDatePicker;
    public TextArea taskDescriptionTextArea;

    private TitledPane selectedTitlePane;
    User user = new User();

    //potrzebne do dodawania zadań do projektów
    private ArrayList <Project> listOfToDoProjects = new ArrayList<Project>();
    private Map<TitledPane, Project> indexOfProject = new HashMap<>();

    //potrzebne do usuwania zdań
    private Map<TitledPane, LocalDateTime> indexOfTask = new HashMap<>();


    @FXML
    private void onAddButton() {
        String name = "";

        if (!projectNameTextField.getText().isEmpty()) {                                           // Jeśli użytkownik wprowadza nazwę projektu
            name = projectNameTextField.getText();
            addig_Project(name);
            projectNameTextField.clear();

        } else if (!taskNameTextField.getText().isEmpty() && selectedTitlePane != null) {           // Jeśli wprowadzono nazwę zadania i wybrano projekt
            name = taskNameTextField.getText();
            adding_Task(name);
            taskNameTextField.clear();
        }

        if (name.isEmpty()) {
            warning_Sign("Proszę podać nazwę projektu lub zadania.");
        }
    }

    private void addig_Project(String projectName) {
        //tworzymy nowy projekt
        var new_Project = new TitledPane();

        //dodanie nazwy z checkbox'em
        CheckBox projectCheckBox = new CheckBox(projectNameTextField.getText());
        new_Project.setGraphic(projectCheckBox);

        var project_Accordion = new Accordion();
        new_Project.setContent(project_Accordion);

        //dodanie opisu do projektu
        var contentOfProject = new AnchorPane();

        if(projectStartDateDatePicker.getValue().isBefore(projectDueDateDatePicker.getValue())) {

            ResulDates result = getResultDates(projectStartDateDatePicker,projectDueDateDatePicker);

            //dodanie dat
            contentOfProject.getChildren().addAll(result.start_Date(), result.startDate(), result.due_Date(), result.dueDate());

            //dodanie projektu do Vbox'a
            toDoProjectContainer.getChildren().add(new_Project);

            //dodanie projektu do listy użytkownika
            var project_to_add = new Project(projectName, false, LocalDateTime.now(), getLocalDateTime(projectStartDateDatePicker), null, getLocalDateTime(projectDueDateDatePicker));

            listOfToDoProjects.add(project_to_add);

            indexOfProject.put(new_Project, project_to_add);

            user.addToDoProject(projectName, getLocalDateTime(projectStartDateDatePicker), getLocalDateTime(projectDueDateDatePicker));

        }else{
            warning_Sign("Start Date cannot be after Due Date.");
        }

        //dodanie progressBar'u do About Project
        var progressBar = new ProgressBar();
        progressBar.setLayoutX(70);
        progressBar.setLayoutY(85);

        contentOfProject.getChildren().addAll(progressBar);

        TitledPane about_Project = new TitledPane();
        about_Project.setText("About Project");
        project_Accordion.getPanes().add(about_Project);
        about_Project.setContent(contentOfProject);

        //żeby można było wybierać ten projekt
        new_Project.setOnMouseClicked(event -> handleSelectProject(new_Project));

        //wyczyszczenie kalendarza
        projectDueDateDatePicker.setValue(null);
        projectStartDateDatePicker.setValue(null);
    }

    private void updateProgressBar(TitledPane projectPane) {
        if (projectPane == null) return;

        Accordion projectAccordion = (Accordion) projectPane.getContent();
        ProgressBar progressBar = getProgressBar(projectPane);

        if (progressBar != null) {
            int totalTasks = projectAccordion.getPanes().size() - 1;            // Pomijamy "About Project" - nie liczy się do zadań
            int completedTasks = 0;

            for (int i = 1; i < projectAccordion.getPanes().size(); i++) {
                TitledPane taskPane = projectAccordion.getPanes().get(i);
                CheckBox taskCheckBox = (CheckBox) taskPane.getGraphic();
                if (taskCheckBox != null && taskCheckBox.isSelected()) {
                    completedTasks++;
                }
            }

            if (totalTasks == 0) {
                progressBar.setProgress(0);
                return;
            }
            double progress = (double) completedTasks / totalTasks;
            progressBar.setProgress(progress);
        }
    }

    private ProgressBar getProgressBar(TitledPane projectPane) {
        AnchorPane projectDetailsPane = (AnchorPane) ((Accordion) projectPane.getContent())
                .getPanes().get(0).getContent();
        for (var node : projectDetailsPane.getChildren()) {
            if (node instanceof ProgressBar) {
                return (ProgressBar) node;
            }
        }
        return null;
    }

    private void adding_Task(String taskName) {
        if (selectedTitlePane != null && selectedTitlePane.isExpanded()) {

            Accordion projectAccordion = (Accordion) selectedTitlePane.getContent();

            if (projectAccordion != null) {
                var newTask = new TitledPane();
                var contentOfTask = new AnchorPane();

                indexOfTask.put(newTask, LocalDateTime.now());

                newTask.setContent(contentOfTask);

                //dodanie nazwy z checkbox'em
                CheckBox taskCheckBox = new CheckBox(taskNameTextField.getText());
                newTask.setGraphic(taskCheckBox);
                projectAccordion.getPanes().add(newTask);

                //dodanie nazw
                ResulDates result = getResultDates(taskStartDateDatePicker,taskDueDateDatePicker);

                contentOfTask.getChildren().addAll(result.start_Date(), result.startDate(), result.due_Date(), result.dueDate());

                //dodanie opisu
                var description = new Label("Description");
                description.setLayoutX(10);
                description.setLayoutY(90);

                var descriptionText = new TextArea();
                descriptionText.setText(taskDescriptionTextArea.getText());
                descriptionText.setLayoutX(90);
                descriptionText.setLayoutY(90);
                descriptionText.setEditable(false);

                contentOfTask.getChildren().addAll(description, descriptionText);

                taskCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> updateProgressBar(selectedTitlePane));

                // Aktualizuj wskaźnik postępu po dodaniu nowego zadania
                updateProgressBar(selectedTitlePane);

                System.out.printf(""+listOfToDoProjects.indexOf(indexOfProject.get(selectedTitlePane)));

                user.getListOfToDoProject().get(listOfToDoProjects.indexOf(indexOfProject.get(selectedTitlePane))).addTask(taskName, getLocalDateTime(taskStartDateDatePicker), getLocalDateTime(taskDueDateDatePicker), taskDescriptionTextArea.getText());

            } else {
                System.out.println("Błąd: Brak Accordion w wybranym projekcie!");
            }
        } else {
            warning_Sign("Najpierw wybierz projekt!");
        }
    }


    @FXML
    private void onDeleteButton() {
        if (selectedTitlePane == null) {
            warning_Sign("Nie wybrano żadnego projektu ani zadania do usunięcia.");
            return;
        }

        // Sprawdź, czy kliknięto projekt
        Accordion projectAccordion = (Accordion) selectedTitlePane.getContent();
        if (projectAccordion.getExpandedPane() != null) {

            // Zadanie do usunięcia
            TitledPane selectedTask = projectAccordion.getExpandedPane();
            projectAccordion.getPanes().remove(selectedTask);

            //Usuń zadanie z modelu danych


            user.getListOfToDoProject().get(listOfToDoProjects.indexOf(indexOfProject.get(selectedTitlePane))).deleteTask(indexOfTask.get(selectedTask));

            System.out.printf(""+indexOfTask.get(selectedTask));

            System.out.printf(""+listOfToDoProjects.indexOf(indexOfProject.get(selectedTitlePane)));

            System.out.printf(""+user.getListOfToDoProject().get(listOfToDoProjects.indexOf(indexOfProject.get(selectedTitlePane))).getListOfTask());

            indexOfTask.remove(selectedTask);

            // Zaktualizuj wskaźnik postęp
            updateProgressBar(selectedTitlePane);

        } else {
            // Projekt do usunięcia
            toDoProjectContainer.getChildren().remove(selectedTitlePane);

            // Usuń projekt z listy i mapy
            Project projectToRemove = indexOfProject.remove(selectedTitlePane);
            listOfToDoProjects.remove(projectToRemove);

            // Usuń projekt z modelu danych użytkownika
            user.getListOfToDoProject().remove(projectToRemove);


            System.out.printf(""+user.getListOfToDoProject());

            // Wyzeruj zaznaczenie
            selectedTitlePane = null;
        }
    }


    private ResulDates getResultDates(DatePicker startDatePicker, DatePicker dueDatePicker) {
        var start_Date = new Label("Start Date");
        start_Date.setLayoutX(10);
        start_Date.setLayoutY(10);

        var startDate = new TextField();
        startDate.setLayoutX(90);
        startDate.setLayoutY(5);
        startDate.setText(handleDateAction(startDatePicker));
        startDate.setEditable(false);

        var due_Date = new Label("Due Date");
        due_Date.setLayoutX(10);
        due_Date.setLayoutY(50);

        var dueDate = new TextField();
        dueDate.setLayoutX(90);
        dueDate.setLayoutY(45);
        dueDate.setText(handleDateAction(dueDatePicker));
        dueDate.setEditable(false);

        ResulDates resulDates = new ResulDates(start_Date, startDate, due_Date, dueDate);
        return resulDates;
    }

    private record ResulDates(Label start_Date, TextField startDate, Label due_Date, TextField dueDate) {
    }

    private void warning_Sign(String s) {
        Alert alert = new Alert(Alert.AlertType.WARNING, s, ButtonType.OK);
        alert.show();
    }

    private LocalDateTime getLocalDateTime(DatePicker datePicker) {
        LocalDate datePickerValue = datePicker.getValue();
        LocalDateTime dateTime = datePickerValue.atTime(LocalTime.of(0, 0));
        return dateTime;
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
            warning_Sign("No date was selected!");
            return null;
        }
    }

    private void handleSelectProject(TitledPane titlePane) {
        // Ustawiamy wybrany TitlePane (do którego będziemy dodawać zadania)
        selectedTitlePane = titlePane;
    }
}