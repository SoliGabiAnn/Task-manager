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
import java.util.function.DoubleFunction;


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
    private ArrayList<Project> listOfToDoProjects = new ArrayList<Project>();
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
        try {
            LocalDateTime date_added = LocalDateTime.now();
            user.addToDoProject(projectName, date_added, getLocalDateTime(projectStartDateDatePicker), getLocalDateTime(projectDueDateDatePicker));

            //tworzymy nowy projekt
            var new_Project = new TitledPane();

            //dodanie nazwy z checkbox'em
            CheckBox projectCheckBox = new CheckBox(projectNameTextField.getText());
            new_Project.setGraphic(projectCheckBox);
            //projectCheckBox.setDisable(true); -> jak zrobić tak, aby checkboxa nie można było zaznaczyć w to do


            var project_Accordion = new Accordion();
            new_Project.setContent(project_Accordion);

            //dodanie opisu do projektu
            var contentOfProject = new AnchorPane();

            ResulDates result = getResultDates(projectStartDateDatePicker, projectDueDateDatePicker);
            var dueDate = new TextField();
            dueDate.setLayoutX(90);
            dueDate.setLayoutY(45);
            dueDate.setText(handleDateAction(projectStartDateDatePicker));

            //dodanie dat
            contentOfProject.getChildren().addAll(result.start_Date(), result.startDate(), result.due_Date(), result.dueDate(), dueDate);

            //dodanie projektu do Vbox'a
            toDoProjectContainer.getChildren().add(new_Project);

            //dodanie projektu do listy użytkownika
            var project_to_add = new Project(projectName, false, date_added, getLocalDateTime(projectStartDateDatePicker), null, getLocalDateTime(projectDueDateDatePicker));

            listOfToDoProjects.add(project_to_add);
            indexOfProject.put(new_Project, project_to_add);

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

        } catch (ProjectException e) {
            alert_sign(e, "Project Error");
        } finally {
            //wyczyszczenie kalendarza
            projectDueDateDatePicker.setValue(null);
            projectStartDateDatePicker.setValue(null);
        }

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
        try {
            if (selectedTitlePane != null && selectedTitlePane.isExpanded()) {
                Accordion projectAccordion = (Accordion) selectedTitlePane.getContent();

                LocalDateTime date_added = LocalDateTime.now();

                user.getListOfToDoProject().get(listOfToDoProjects.indexOf(indexOfProject.get(selectedTitlePane))).addTask(taskName, date_added, getLocalDateTime(taskStartDateDatePicker), getLocalDateTime(taskDueDateDatePicker), taskDescriptionTextArea.getText());

                var newTask = new TitledPane();
                var contentOfTask = new AnchorPane();

                indexOfTask.put(newTask, date_added);

                newTask.setContent(contentOfTask);

                //dodanie nazwy z checkbox'em
                CheckBox taskCheckBox = new CheckBox(taskNameTextField.getText());
                newTask.setGraphic(taskCheckBox);
                projectAccordion.getPanes().add(newTask);

                //dodanie nazw
                ResulDates result = getResultDates(taskStartDateDatePicker, taskDueDateDatePicker);

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

//                    System.out.printf(""+listOfToDoProjects.indexOf(indexOfProject.get(selectedTitlePane)));
//                    System.out.println(user.getListOfToDoProject().get(listOfToDoProjects.indexOf(indexOfProject.get(selectedTitlePane))).getListOfTask());
            } else {
                warning_Sign("Najpierw wybierz projekt!");
            }
        } catch (Exception e) {
            alert_sign(e, "Task Error");
        } finally {
            //wyczyszczenie kalendarza
            projectDueDateDatePicker.setValue(null);
            projectStartDateDatePicker.setValue(null);
            taskDescriptionTextArea.clear();
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

            indexOfTask.remove(selectedTask);

            updateProgressBar(selectedTitlePane);

        } else {
            // Usuń projekt z listy i mapy
            Project projectToRemove = indexOfProject.remove(selectedTitlePane);
            user.deleteToDoProject(projectToRemove.getDate_added());
            listOfToDoProjects.remove(projectToRemove);

            // Projekt do usunięcia
            toDoProjectContainer.getChildren().remove(selectedTitlePane);

            // Wyzeruj zaznaczenie
            selectedTitlePane = null;
        }
    }


    private ResulDates getResultDates(DatePicker startDatePicker, DatePicker dueDatePicker) {
        var start_Date = new Label("Start Date");
        start_Date.setLayoutX(10);
        start_Date.setLayoutY(10);

        var startDate = new DatePicker();
        startDate.setLayoutX(90);
        startDate.setLayoutY(5);
        startDate.setValue(startDatePicker.getValue());
        startDate.setEditable(false);

        var due_Date = new Label("Due Date");
        due_Date.setLayoutX(10);
        due_Date.setLayoutY(50);

        var dueDate = new DatePicker();
        dueDate.setLayoutX(90);
        dueDate.setLayoutY(45);
        dueDate.setValue(dueDatePicker.getValue());

        dueDate.setEditable(false);
        dueDate.setDisable(true);

        ResulDates resulDates = new ResulDates(start_Date, startDate, due_Date, dueDate);
        return resulDates;
    }

    private record ResulDates(Label start_Date, DatePicker startDate, Label due_Date, DatePicker dueDate) {
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

    private void alert_sign(Exception e, String title) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(e.getMessage());
        alert.showAndWait();
    }

    private void handleSelectProject(TitledPane titlePane) {
        // Ustawiamy wybrany TitlePane (do którego będziemy dodawać zadania)
        selectedTitlePane = titlePane;
    }
}