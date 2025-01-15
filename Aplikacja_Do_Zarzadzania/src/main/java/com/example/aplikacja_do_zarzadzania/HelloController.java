package com.example.aplikacja_do_zarzadzania;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import kod_aplikacji.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.fxml.FXML;

import java.io.IOException;
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
    public Button sortByDueDateButton;
    public Button sortByStartDateButton;
    public Button GenerateReportButton;

    private TitledPane selectedTitlePane;
    User user = new User();

    private final Map<TitledPane, Project> indexOfProject = new HashMap<>();
    private final Map<TitledPane, LocalDateTime> indexOfTaskDateAdded = new HashMap<>();
    private final Map<Task, TitledPane> indexOfTask = new HashMap<>();

    @FXML
    public void initialize() {
        timeline1.setCycleCount(Animation.INDEFINITE);
        timeline1.play();
    }

    @FXML
    private void onAddButton() {
        String name = "";
        if (!projectNameTextField.getText().isEmpty()) {
            name = projectNameTextField.getText();
            if (projectStartDateDatePicker.getValue() == null && projectDueDateDatePicker.getValue() == null) {
                createWarningSign("Proszę wybrać daty");
            } else {
                addProject(name);
                projectNameTextField.clear();
            }
        } else if (!taskNameTextField.getText().isEmpty() && selectedTitlePane != null) {
            name = taskNameTextField.getText();
            if (taskStartDateDatePicker.getValue() == null && taskDueDateDatePicker.getValue() == null) {
                createWarningSign("Proszę wybrać daty");
            } else {
                addTask(name);
                taskNameTextField.clear();
            }
        }

        if (name.isEmpty()) {
            createWarningSign("Proszę podać nazwę projektu lub zadania.");
        }
    }

    private void addProject(String projectName) {
        try {
            LocalDateTime dateAdded = LocalDateTime.now();
            if (!getLocalDateTime(projectStartDateDatePicker).isAfter(getLocalDateTime(projectDueDateDatePicker))) {
                var projectToAdd = new Project(projectName, false, dateAdded, getLocalDateTime(projectStartDateDatePicker), null, getLocalDateTime(projectDueDateDatePicker));
                var projectTitlePane = addProjectTitlePane(projectNameTextField, projectDueDateDatePicker, projectStartDateDatePicker);
                indexOfProject.put(projectTitlePane, projectToAdd);

                if (getLocalDateTime(projectStartDateDatePicker).isBefore(dateAdded)) {
                    user.addUnfinishedProject(projectToAdd);
                } else {
                    user.addToDoProject(projectToAdd);
                }

                if (getLocalDateTime(projectStartDateDatePicker).isBefore(dateAdded)) {
                    doingProjectContainer.getChildren().add(projectTitlePane);
                    projectTitlePane.getGraphic().setMouseTransparent(false);
                } else {
                    toDoProjectContainer.getChildren().add(projectTitlePane);
                }
                projectTitlePane.setOnMouseClicked(event -> getSelectProject(projectTitlePane));
            } else {
                createWarningSign("Start date cannot be after due date.");
            }


        } catch (ProjectException e) {
            createAlertSign(e, "Project Error");
        } finally {
            projectDueDateDatePicker.setValue(null);
            projectStartDateDatePicker.setValue(null);
        }
    }

    private void addTask(String taskName) {
        try {
            if (selectedTitlePane != null && selectedTitlePane.isExpanded()) {
                Accordion projectAccordion = (Accordion) selectedTitlePane.getContent();
                LocalDateTime dateAdded = LocalDateTime.now();
                var taskToAdd = new Task(taskName, false, dateAdded, getLocalDateTime(taskStartDateDatePicker), null, getLocalDateTime(taskDueDateDatePicker), taskDescriptionTextArea.getText());
                var newTask = new TitledPane();
                var contentOfTask = new AnchorPane();
                indexOfProject.get(selectedTitlePane).addTask(taskToAdd);
                indexOfTask.put(taskToAdd, newTask);
                indexOfTaskDateAdded.put(newTask, dateAdded);

                newTask.setContent(contentOfTask);
                var datePickers = getDatePicker(selectedTitlePane);
                if (datePickers != null) {
                    addCheckBoxWithName(taskNameTextField, newTask, false, datePickers.getFirst());
                }
                contentOfTask.getChildren().addAll(addDates(taskStartDateDatePicker, taskDueDateDatePicker).startDate(), addDates(taskStartDateDatePicker, taskDueDateDatePicker).newStartDatePicker(), addDates(taskStartDateDatePicker, taskDueDateDatePicker).dueDate(), addDates(taskStartDateDatePicker, taskDueDateDatePicker).newDueDatePicker());
                contentOfTask.getChildren().addAll(addTaskDescription().description(), addTaskDescription().descriptionText());

                CheckBox taskCheckBox = (CheckBox) newTask.getGraphic();
                if (taskCheckBox != null) {
                    taskCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> updateProgressBar(selectedTitlePane));
                }
                updateProgressBar(selectedTitlePane);
                projectAccordion.getPanes().add(newTask);
            } else {
                createWarningSign("Najpierw wybierz projekt!");
            }
        } catch (Exception e) {
            createAlertSign(e, "Task Error");
        } finally {
            taskDueDateDatePicker.setValue(null);
            taskStartDateDatePicker.setValue(null);
            taskDescriptionTextArea.clear();
        }
    }

    @FXML
    private void onDeleteButton() {
        if (selectedTitlePane == null) {
            createWarningSign("Nie wybrano żadnego projektu ani zadania do usunięcia.");
            return;
        }
        Accordion projectAccordion = (Accordion) selectedTitlePane.getContent();
        if (projectAccordion.getExpandedPane() != null) {
            TitledPane selectedTask = projectAccordion.getExpandedPane();
            projectAccordion.getPanes().remove(selectedTask);
            indexOfProject.get(selectedTitlePane).deleteTask(indexOfTaskDateAdded.get(selectedTask));
            indexOfTaskDateAdded.remove(selectedTask);
        } else {
            Project projectToRemove = indexOfProject.get(selectedTitlePane);
            indexOfProject.remove(selectedTitlePane);
            if (user.getListOfToDoProject().contains(projectToRemove)) {
                user.deleteToDoProject(projectToRemove.getDate_added());
                toDoProjectContainer.getChildren().remove(selectedTitlePane);
            } else if (user.getListOfUnfinishedProject().contains(projectToRemove)) {
                user.deleteUnfinishedProject(projectToRemove.getDate_added());
                doingProjectContainer.getChildren().remove(selectedTitlePane);
            } else {
                user.deleteFinishedProject(projectToRemove.getDate_added());
            }
            selectedTitlePane = null;
        }
    }

    public void onSortByStartDate() {
        if (!(selectedTitlePane == null) && selectedTitlePane.isExpanded()) {
            sortTaskInProject(indexOfProject.get(selectedTitlePane).getListOfTask());
        }
        sortProjectInVbox(user.getListOfToDoProject(), toDoProjectContainer);
        sortProjectInVbox(user.getListOfUnfinishedProject(), doingProjectContainer);
        sortProjectInVbox(user.getListOfFinishedProject(), doneProjectContainer);
    }

    public void onSortByDueDate(){
        if (!(selectedTitlePane == null) && selectedTitlePane.isExpanded()) {
            sortTaskInProject(indexOfProject.get(selectedTitlePane).sortTask());sortTaskInProject(indexOfProject.get(selectedTitlePane).sortTask());
        }
        sortProjectInVbox(user.sortProject(user.getListOfToDoProject()), toDoProjectContainer);
        sortProjectInVbox(user.sortProject(user.getListOfFinishedProject()), doingProjectContainer);
        sortProjectInVbox(user.sortProject(user.getListOfFinishedProject()), doneProjectContainer);
    }


    public void onGenerateReportButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Report.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Raport postępów");
        reportController newReportController = loader.getController();
        newReportController.setGenerateReport(user);
        newReportController.fillReport();
        stage.show();
    }

    private void sortProjectInVbox(ArrayList<Project> userProjects, VBox vbox) {
        vbox.getChildren().clear();
        for (Project project : userProjects) {
            vbox.getChildren().add(reversedMap(project));
        }
    }

    private void sortTaskInProject(ArrayList<Task> listOfTasks) {
        var projectAccordion = (Accordion) selectedTitlePane.getContent();

        if (projectAccordion.getPanes().size() > 1) {
            TitledPane firstPane = projectAccordion.getPanes().get(0);
            projectAccordion.getPanes().clear();
            projectAccordion.getPanes().add(firstPane);
        }
        for (Task task : listOfTasks) {
            projectAccordion.getPanes().add(indexOfTask.get(task));
        }
    }

    private void moveProjectToDoing() {
        var listOfProjectsToMove = user.getListOfIndexOfProjectToMove();
        var index = listOfProjectsToMove.size();
        if (index > 0) {
            HashMap<Project, TitledPane> reversedMap = new HashMap<>();
            for (Map.Entry<TitledPane, Project> entry : indexOfProject.entrySet()) {
                reversedMap.put(entry.getValue(), entry.getKey());
            }
            for (int i = 0; i < index; i++) {
                var projectToMove = user.getListOfToDoProject().get(listOfProjectsToMove.get(i));
                var titlePaneToMove = reversedMap.get(projectToMove);
                toDoProjectContainer.getChildren().remove(titlePaneToMove);
                doingProjectContainer.getChildren().add(titlePaneToMove);
                titlePaneToMove.getGraphic().setMouseTransparent(false);
            }
        }
        user.clearListOfIndexOfProjectToMove();
    }

    private void moveProjectToDone(TitledPane projectTitlePane) {
        doingProjectContainer.getChildren().remove(projectTitlePane);
        doneProjectContainer.getChildren().add(projectTitlePane);
        CheckBox projectCheckBox = (CheckBox) projectTitlePane.getGraphic();
        var projectContent = (Accordion) projectTitlePane.getContent();
        for (int i = 1; i < projectContent.getPanes().size(); i++) {
            TitledPane taskPane = projectContent.getPanes().get(i);
            CheckBox taskCheckBox = (CheckBox) taskPane.getGraphic();
            taskCheckBox.setMouseTransparent(true);
        }
        if (projectCheckBox != null) {
            projectCheckBox.setMouseTransparent(true);
        }
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

    private TitledPane addProjectTitlePane(TextField projectNameTextField, DatePicker projectDueDateDatePicker, DatePicker projectStartDateDatePicker) {
        var newProject = new TitledPane();
        addCheckBoxWithName(projectNameTextField, newProject, true, projectStartDateDatePicker);
        var projectAccordion = new Accordion();
        newProject.setContent(projectAccordion);
        var contentOfProject = new AnchorPane();
        ResulDates addedDates = addDates(projectStartDateDatePicker, projectDueDateDatePicker);
        contentOfProject.getChildren().addAll(addedDates.startDate(), addedDates.newStartDatePicker(), addedDates.dueDate(), addedDates.newDueDatePicker());
        contentOfProject.getChildren().addAll(addProgressBar());
        TitledPane aboutProject = new TitledPane();
        aboutProject.setText("About Project");
        projectAccordion.getPanes().add(aboutProject);
        aboutProject.setContent(contentOfProject);
        return newProject;
    }

    private void addCheckBoxWithName(TextField nameTextField, TitledPane newTitledPane, boolean isProject, DatePicker starDatePicker) {
        CheckBox checkBox = new CheckBox(nameTextField.getText());
        newTitledPane.setGraphic(checkBox);
        checkBox.setMouseTransparent(!starDatePicker.getValue().isBefore(LocalDate.now()));
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
    }

    private ResulDates addDates(DatePicker startDatePicker, DatePicker dueDatePicker) {
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

        return new ResulDates(startDate, newStartDatePicker, due_Date, dueDate);
    }


    private TitledPane reversedMap(Project project) {
        HashMap<Project, TitledPane> reversedMap = new HashMap<>();
        for (Map.Entry<TitledPane, Project> entry : indexOfProject.entrySet()) {
            reversedMap.put(entry.getValue(), entry.getKey());
        }
        return reversedMap.get(project);
    }

    private record ResulDates(Label startDate, DatePicker newStartDatePicker, Label dueDate,
                              DatePicker newDueDatePicker) {
    }

    private taskDescription addTaskDescription() {
        var description = new Label("Description");
        description.setLayoutX(10);
        description.setLayoutY(90);

        var descriptionText = new TextArea();
        descriptionText.setText(taskDescriptionTextArea.getText());
        descriptionText.setLayoutX(90);
        descriptionText.setLayoutY(90);
        descriptionText.setEditable(false);

        return new taskDescription(description, descriptionText);
    }

    private record taskDescription(Label description, TextArea descriptionText) {
    }

    private void updateProgressBar(TitledPane projectPane) {
        if (projectPane == null) return;

        Accordion projectAccordion = (Accordion) projectPane.getContent();
        ProgressBar progressBar = getProgressBar(projectPane);

        if (progressBar != null) {
            int totalTasks = projectAccordion.getPanes().size() - 1;
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
                .getPanes().getFirst().getContent();
        for (var node : projectDetailsPane.getChildren()) {
            if (node instanceof ProgressBar) {
                return (ProgressBar) node;
            }
        }
        return null;
    }

    private ProgressBar addProgressBar() {
        var progressBar = new ProgressBar();
        progressBar.setLayoutX(70);
        progressBar.setLayoutY(85);
        return progressBar;
    }

    //dostanie się do DatePickers w Projekcie -> może się sprzydać do edycji
    private ArrayList<DatePicker> getDatePicker(TitledPane projectPane) {
        AnchorPane projectDetailsPane = (AnchorPane) ((Accordion) projectPane.getContent())
                .getPanes().getFirst().getContent();
        ArrayList<DatePicker> datePickers = new ArrayList<>();
        for (var node : projectDetailsPane.getChildren()) {
            if (node instanceof DatePicker) {
                datePickers.add((DatePicker) node);
            }
        }
        if (datePickers.isEmpty()) {
            return null;
        } else {
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

    private void getSelectProject(TitledPane titlePane) {
        // Ustawiamy wybrany TitlePane (do którego będziemy dodawać zadania)
        selectedTitlePane = titlePane;
        System.out.println("Wybrano projekt: " + titlePane.getText());

    }

    private LocalDateTime getLocalDateTime(DatePicker datePicker) {
        LocalDate datePickerValue = datePicker.getValue();
        return datePickerValue.atTime(LocalTime.of(0, 0));
    }

    private void createAlertSign(Exception e, String title) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(e.getMessage());
        alert.showAndWait();
    }

    private void createWarningSign(String s) {
        Alert alert = new Alert(Alert.AlertType.WARNING, s, ButtonType.OK);
        alert.show();
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
}

