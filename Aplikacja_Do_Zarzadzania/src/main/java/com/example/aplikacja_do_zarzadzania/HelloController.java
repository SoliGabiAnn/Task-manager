package com.example.aplikacja_do_zarzadzania;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.*;
import javafx.util.Duration;
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

    public Button addButton;
    public Button deleteButton;
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
    private final ArrayList<Project> listOfToDoProjects = new ArrayList<Project>();
    private final Map<TitledPane, Project> indexOfProject = new HashMap<>();

    //potrzebne do usuwania zdań
    private final Map<TitledPane, LocalDateTime> indexOfTask = new HashMap<>();

    private final ArrayList<Project> listOfDoingProjects = new ArrayList<Project>();
    private final ArrayList<Project> listOfDoneProjects = new ArrayList<Project>();


    @FXML
    private void onAddButton() {
        String name = "";

        if (!projectNameTextField.getText().isEmpty()) {                                           // Jeśli użytkownik wprowadza nazwę projektu
            name = projectNameTextField.getText();
            addProject(name);
            projectNameTextField.clear();

        } else if (!taskNameTextField.getText().isEmpty() && selectedTitlePane != null) {           // Jeśli wprowadzono nazwę zadania i wybrano projekt
            name = taskNameTextField.getText();
            addTask(name);
            taskNameTextField.clear();
        }

        if (name.isEmpty()) {
            createWarningSign("Proszę podać nazwę projektu lub zadania.");
        }
    }
    
    

    private TitledPane addProjectTitlePane(TextField projectNameTextField, DatePicker projectDueDateDatePicker, DatePicker projectStartDateDatePicker) {
        //tworzymy nowy projekt
        var newProject = new TitledPane();

        addCheckBoxWithName(projectNameTextField, newProject, true);

        //żeby można było dodawać i rozwijać zadania w projekcie
        var projectAccordion = new Accordion();
        newProject.setContent(projectAccordion);

        //dodanie opisu do projektu
        var contentOfProject = new AnchorPane();

        ResulDates result = getResultDates(projectStartDateDatePicker, projectDueDateDatePicker);

        //dodanie dat
        contentOfProject.getChildren().addAll(result.startDate(), result.newStartDatePicker(), result.dueDate(), result.newDueDatePicker());

        //dodanie progressBar'u do About Project
        var progressBar = new ProgressBar();
        progressBar.setLayoutX(70);
        progressBar.setLayoutY(85);

        contentOfProject.getChildren().addAll(progressBar);

        TitledPane aboutProject = new TitledPane();
        aboutProject.setText("About Project");
        projectAccordion.getPanes().add(aboutProject);
        aboutProject.setContent(contentOfProject);

        return newProject;
    }

    private void addCheckBoxWithName(TextField nameTextField, TitledPane newTitledPane, boolean isProject) {
        CheckBox checkBox = new CheckBox(nameTextField.getText());
        newTitledPane.setGraphic(checkBox);
        checkBox.setMouseTransparent(true);


        if (isProject) {
            checkBox.setOnAction(event -> {
                if (checkBox.isSelected()) {
                    user.ifFinished();
                    if (checkIfTaskAreFinished(newTitledPane)) {
                        moveProjectToDone(newTitledPane);
                    }
                }
            });
        }
//        }else{
//            Accordion parentAccordion = (Accordion) newTitledPane.getParent();
//            TitledPane parentTitlePane = (TitledPane) parentAccordion.getPanes().get(0);
//
//            if(parenetAccordion)
//        }
    }



    private void addProject(String projectName) {
        try {
            LocalDateTime dateAdded = LocalDateTime.now();

            //dodanie do listy usera
            if(getLocalDateTime(projectStartDateDatePicker).isBefore(dateAdded)) {
                user.addUnfinishedProject(projectName, dateAdded, getLocalDateTime(projectStartDateDatePicker), getLocalDateTime(projectDueDateDatePicker));
            }else{
                user.addToDoProject(projectName, dateAdded, getLocalDateTime(projectStartDateDatePicker), getLocalDateTime(projectDueDateDatePicker));
            }

            var projectTitlePane = addProjectTitlePane(projectNameTextField, projectDueDateDatePicker, projectStartDateDatePicker);
            var projectToAdd = new Project(projectName, false, dateAdded, getLocalDateTime(projectStartDateDatePicker), null, getLocalDateTime(projectDueDateDatePicker));

            if(getLocalDateTime(projectStartDateDatePicker).isBefore(dateAdded)) {
                doingProjectContainer.getChildren().add(projectTitlePane);
                listOfDoingProjects.add(projectToAdd);
                projectTitlePane.getGraphic().setMouseTransparent(false);
            }else{
                toDoProjectContainer.getChildren().add(projectTitlePane);
                listOfToDoProjects.add(projectToAdd);
            }

            indexOfProject.put(projectTitlePane, projectToAdd);

            //żeby można było wybierać ten projekt
            projectTitlePane.setOnMouseClicked(event -> handleSelectProject(projectTitlePane));
        } catch (ProjectException e) {
            createAlertSign(e, "Project Error");
        } finally {
            //wyczyszczenie kalendarza
            projectDueDateDatePicker.setValue(null);
            projectStartDateDatePicker.setValue(null);
        }
    }

    private void moveProjectToDoing() {
        //numery indeksów projektów, które będą przenoszone
        var listOfProjectsToMove = user.getListOfIndex();
        var index = listOfProjectsToMove.size();

        if (index > 0) {
            //do odwracania hashmapy, żeby można było się przedostać do TitlePane
            HashMap<Project, TitledPane> reversedMap = new HashMap<>();
            for (Map.Entry<TitledPane, Project> entry : indexOfProject.entrySet()) {
                reversedMap.put(entry.getValue(), entry.getKey());
            }

            for (int i = 0; i < index; i++) {
                //wyznaczamy projekt, który chcemy przenieść
                var projectToMove = listOfToDoProjects.get(listOfProjectsToMove.get(i));

                //wyznaczamy titlepane, który chcemy przenieść
                var titlePaneToMove = reversedMap.get(projectToMove);

                //przeniesienie w kontrolerze
                listOfToDoProjects.remove(projectToMove);
                listOfDoingProjects.add(projectToMove);

                // Projekt do usunięcia
                toDoProjectContainer.getChildren().remove(titlePaneToMove);
                doingProjectContainer.getChildren().add(titlePaneToMove);

                //ustawienie, że można zaznaczać projekt jako wykonany
                titlePaneToMove.getGraphic().setMouseTransparent(false);
            }
        }
        // czyszczenie za każdym razem listy do indeksów
        user.clearListOfToDoProject();

    }

    private void moveProjectToDone(TitledPane projectTitlePane) {
        Project projectToMove = indexOfProject.get(projectTitlePane);

        listOfDoingProjects.remove(projectToMove);
        doingProjectContainer.getChildren().remove(projectTitlePane);

        listOfDoneProjects.add(projectToMove);
        doneProjectContainer.getChildren().add(projectTitlePane);

        // Ustaw, aby checkbox projektu nie był już klikalny
        CheckBox projectCheckBox = (CheckBox) projectTitlePane.getGraphic();
        if (projectCheckBox != null) {
            projectCheckBox.setMouseTransparent(true);
        }

        //System.out.println("Projekt został przeniesiony do Done.");
    }

    private Boolean checkIfTaskAreFinished(TitledPane projectPane) {
        if (projectPane == null) return false;

        Accordion projectAccordion = (Accordion) projectPane.getContent();

        boolean allTasksCompleted = true;

        for (int i = 1; i < projectAccordion.getPanes().size(); i++) {
            TitledPane taskPane = projectAccordion.getPanes().get(i);
            CheckBox taskCheckBox = (CheckBox) taskPane.getGraphic();

            if (taskCheckBox != null && !taskCheckBox.isSelected()) {
                allTasksCompleted = false;
                break;
            }
        }
        return allTasksCompleted;
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

    //dostanie się do DatePickers w Projekcie -> może się sprzydać do edycji
    private ArrayList<DatePicker> getDatePicker(TitledPane projectPane) {
        AnchorPane projectDetailsPane = (AnchorPane) ((Accordion) projectPane.getContent())
                .getPanes().get(0).getContent();
        ArrayList<DatePicker> datePickers = new ArrayList<>();
        for (var node : projectDetailsPane.getChildren()) {
            if (node instanceof DatePicker) {
                datePickers.add((DatePicker) node);
            }
        }
        if (datePickers.size() == 0) {
            return null;
        }else{
            return datePickers;
        }

        //Przykład użycia
//        ArrayList<DatePicker> datePickers;
//        datePickers = getDatePicker(projectTitlePane);
//        if(datePickers.size() > 0) {
//            for(int j = 0; j < datePickers.size(); j++){
//                datePickers.get(j).setMouseTransparent(false);
//            }
//        }
    }

    private void addTask(String taskName) {
        try {
            if (selectedTitlePane != null && selectedTitlePane.isExpanded()) {
                // selectedTitlePane.getGraphic().setMouseTransparent(false); -> do odblokowania żeby kliknąć w checkbox'a

                Accordion projectAccordion = (Accordion) selectedTitlePane.getContent();

                LocalDateTime dateAdded = LocalDateTime.now();

                if (listOfToDoProjects.contains(indexOfProject.get(selectedTitlePane))) {
                    user.getListOfToDoProject().get(listOfToDoProjects.indexOf(indexOfProject.get(selectedTitlePane))).addTask(taskName, dateAdded, getLocalDateTime(taskStartDateDatePicker), getLocalDateTime(taskDueDateDatePicker), taskDescriptionTextArea.getText());
                } else {
                    user.getListOfUnfinishedProject().get(listOfDoingProjects.indexOf(indexOfProject.get(selectedTitlePane))).addTask(taskName, dateAdded, getLocalDateTime(taskStartDateDatePicker), getLocalDateTime(taskDueDateDatePicker), taskDescriptionTextArea.getText());
                }

                var newTask = new TitledPane();
                var contentOfTask = new AnchorPane();

                indexOfTask.put(newTask, dateAdded);

                newTask.setContent(contentOfTask);

                addCheckBoxWithName(taskNameTextField, newTask, false);

                //dodanie nazw
                ResulDates result = getResultDates(taskStartDateDatePicker, taskDueDateDatePicker);

                contentOfTask.getChildren().addAll(result.startDate(), result.newStartDatePicker(), result.dueDate(), result.newDueDatePicker());

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


                CheckBox taskCheckBox = (CheckBox) newTask.getGraphic();
                if (taskCheckBox != null) {
                    taskCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> updateProgressBar(selectedTitlePane));
                }

                // Aktualizuj wskaźnik postępu po dodaniu nowego zadania
                updateProgressBar(selectedTitlePane);

                projectAccordion.getPanes().add(newTask);
//                    System.out.printf(""+listOfToDoProjects.indexOf(indexOfProject.get(selectedTitlePane)));
//                    System.out.println(user.getListOfToDoProject().get(listOfToDoProjects.indexOf(indexOfProject.get(selectedTitlePane))).getListOfTask());
            } else {
                createWarningSign("Najpierw wybierz projekt!");
            }
        } catch (Exception e) {
            createAlertSign(e, "Task Error");
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
            createWarningSign("Nie wybrano żadnego projektu ani zadania do usunięcia.");
            return;
        }

        // Sprawdź, czy kliknięto projekt
        Accordion projectAccordion = (Accordion) selectedTitlePane.getContent();
        if (projectAccordion.getExpandedPane() != null) {

            // Zadanie do usunięcia
            TitledPane selectedTask = projectAccordion.getExpandedPane();
            projectAccordion.getPanes().remove(selectedTask);

            //Usuń zadanie z modelu danych
            if (listOfToDoProjects.contains(indexOfProject.get(selectedTitlePane))) {
                user.getListOfToDoProject().get(listOfToDoProjects.indexOf(indexOfProject.get(selectedTitlePane))).deleteTask(indexOfTask.get(selectedTask));
            }else if (listOfDoingProjects.contains(indexOfProject.get(selectedTitlePane))){
                user.getListOfUnfinishedProject().get(listOfToDoProjects.indexOf(indexOfProject.get(selectedTitlePane))).deleteTask(indexOfTask.get(selectedTask));
            }else {
                user.getListOfFinishedProject().get(listOfToDoProjects.indexOf(indexOfProject.get(selectedTitlePane))).deleteTask(indexOfTask.get(selectedTask));
            }

            indexOfTask.remove(selectedTask);

            updateProgressBar(selectedTitlePane);

        } else {
            // Usuń projekt z listy i mapy
            Project projectToRemove = indexOfProject.remove(selectedTitlePane);

            if (listOfToDoProjects.contains(indexOfProject.get(selectedTitlePane))) {
                user.deleteToDoProject(projectToRemove.getDate_added());
                listOfToDoProjects.remove(projectToRemove);
                // Projekt do usunięcia
                toDoProjectContainer.getChildren().remove(selectedTitlePane);
            }else if (listOfDoingProjects.contains(indexOfProject.get(selectedTitlePane))){
                user.deleteUnfinishedProject(projectToRemove.getDate_added());
                listOfDoingProjects.remove(projectToRemove);
                doingProjectContainer.getChildren().remove(selectedTitlePane);
            }else {
                user.deleteFinishedProject(projectToRemove.getDate_added());
            }

            // Wyzeruj zaznaczenie
            selectedTitlePane = null;
        }
    }


    private ResulDates getResultDates(DatePicker startDatePicker, DatePicker dueDatePicker) {
        var startDate = new Label("Start Date");
        startDate.setLayoutX(10);
        startDate.setLayoutY(10);

        var newStartDatePicker = new DatePicker();
        newStartDatePicker.setLayoutX(90);
        newStartDatePicker.setLayoutY(5);
        newStartDatePicker.setValue(startDatePicker.getValue());
        newStartDatePicker.setMouseTransparent(true);

        var due_Date = new Label("Due Date");
        due_Date.setLayoutX(10);
        due_Date.setLayoutY(50);

        var dueDate = new DatePicker();
        dueDate.setLayoutX(90);
        dueDate.setLayoutY(45);
        dueDate.setValue(dueDatePicker.getValue());
        dueDate.setMouseTransparent(true);

        ResulDates resulDates = new ResulDates(startDate, newStartDatePicker, due_Date, dueDate);
        return resulDates;
    }

    private record ResulDates(Label startDate, DatePicker newStartDatePicker, Label dueDate, DatePicker newDueDatePicker) {
    }

    private void createWarningSign(String s) {
        Alert alert = new Alert(Alert.AlertType.WARNING, s, ButtonType.OK);
        alert.show();
    }

    private LocalDateTime getLocalDateTime(DatePicker datePicker) {
        LocalDate datePickerValue = datePicker.getValue();
        LocalDateTime dateTime = datePickerValue.atTime(LocalTime.of(0, 0));
        return dateTime;
    }


    //chyba ostatecznie nie potrzebne
//    private String handleDateAction(DatePicker datePicker) {
//        // Pobierz wybraną datę
//        LocalDate selectedDate = datePicker.getValue();
//        if (selectedDate != null) {
//            int day = selectedDate.getDayOfMonth(); // Dzień
//            int month = selectedDate.getMonthValue(); // Miesiąc
//            int year = selectedDate.getYear(); // Rok
//            return day + "." + month + "." + year;
//        } else {
//            warning_Sign("No date was selected!");
//            return null;
//
//        }
//    }

    private void createAlertSign(Exception e, String title) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(e.getMessage());
        alert.showAndWait();
    }

    private void handleSelectProject(TitledPane titlePane) {
        // Ustawiamy wybrany TitlePane (do którego będziemy dodawać zadania)
        selectedTitlePane = titlePane;
        System.out.println("Wybrano projekt: " + titlePane.getText());

    }


    final Timeline timeline1 = new Timeline(
            new KeyFrame(
                    Duration.seconds(1),
                    event -> {
                        //long start = System.currentTimeMillis();
                        user.ifStarted();
                        moveProjectToDoing();
                        //System.err.println("Finished after " + (System.currentTimeMillis() - start) + "ms");
                    }
            )
    );

    @FXML
    public void initialize() {
        timeline1.setCycleCount(Animation.INDEFINITE);
        timeline1.play();
    }
}