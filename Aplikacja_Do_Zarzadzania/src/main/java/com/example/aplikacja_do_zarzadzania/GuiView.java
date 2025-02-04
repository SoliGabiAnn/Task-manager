package com.example.aplikacja_do_zarzadzania;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import kod_aplikacji.Project;
import kod_aplikacji.Task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class GuiView {

    Boolean checkIfTaskAreFinished( TitledPane projectPane) {
        if (projectPane == null) return false;
        var projectAccordion = (Accordion) projectPane.getContent();
        boolean allTasksCompleted = true;
        for (int i = 1; i < projectAccordion.getPanes().size(); i++) {
            var taskPane = projectAccordion.getPanes().get(i);
            var taskCheckBox = (CheckBox) taskPane.getGraphic();
            if (taskCheckBox != null && !taskCheckBox.isSelected()) {
                allTasksCompleted = false;
                break;
            }
        }
        return allTasksCompleted;
    }

    public void setTransparentToChangeDate(HelloController controller, boolean transparent, TitledPane titledPane) {
        if (titledPane != null) {
            ArrayList<DatePicker> datePickers;
            datePickers = getDatePicker(titledPane);
            assert datePickers != null;
            if (!datePickers.isEmpty()) {
                for (DatePicker datePicker : datePickers) {
                    datePicker.setMouseTransparent(transparent);
                }
            }
        } else {
            createWarningSign("No project selected");
        }
    }

    ArrayList<DatePicker> getDatePicker(TitledPane projectPane) {
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
    }

    public TextArea getTaskDescription(TitledPane taskTitledPane) {
        if (taskTitledPane.getContent() instanceof AnchorPane) {
            AnchorPane anchorPane = (AnchorPane) taskTitledPane.getContent();

            for (Node node : anchorPane.getChildren()) {
                if (node instanceof TextArea) {
                    TextArea textArea = (TextArea) node;
                    return textArea;
                }
            }
        }
        return null;
    }

    ArrayList<DatePicker> getTaskDatePickers(HelloController controller, TitledPane taskPane) {
        ArrayList<DatePicker> datePickers = new ArrayList<>();

        if (taskPane == null || taskPane.getContent() == null) {
            createWarningSign("Task pane is empty or null");
            return null;
        }

        AnchorPane content = (AnchorPane) taskPane.getContent();
        for (Node node : content.getChildren()) {
            if (node instanceof DatePicker) {
                datePickers.add((DatePicker) node);
            }
        }

        return datePickers;
    }

    public void setTransparentToChangeDateTask(HelloController controller, boolean transparent, TitledPane titledPane) {
        if (titledPane != null) {
            ArrayList<DatePicker> datePickers;
            datePickers = getTaskDatePickers(controller, titledPane);
            assert datePickers != null;
            if (!datePickers.isEmpty()) {
                for (DatePicker datePicker : datePickers) {
                    datePicker.setMouseTransparent(transparent);
                }
            }
        } else {
            createWarningSign("No project selected");
        }
    }

    TitledPane addProjectTitledPane(HelloController controller, TextField projectNameTextField, DatePicker projectDueDateDatePicker, TextField projectDueTimeTextField, DatePicker projectStartDateDatePicker, TextField projectStartTimeTextField) {
        var newProject = new TitledPane();
        newProject.setStyle("-fx-border-color: #7b8bac; " + "-fx-border-width: 5px;");
        controller.addCheckBoxWithName(projectNameTextField, newProject, true, projectStartDateDatePicker, projectStartTimeTextField);
        var projectAccordion = new Accordion();
        newProject.setContent(projectAccordion);
        var contentOfProject = new AnchorPane();
        ResulDates addedDates = addDates(controller, projectStartDateDatePicker, projectStartTimeTextField,
                projectDueDateDatePicker, projectDueTimeTextField);
        contentOfProject.getChildren().addAll(addedDates.startDate(), addedDates.newStartDatePicker(),
                addedDates.newStartTimeTextField(), addedDates.dueDate(), addedDates.newDueDatePicker(),
                addedDates.newDueTimeTextFiled());
        contentOfProject.getChildren().addAll(addProgressBar());
        var projectLabel = new Label();
        projectLabel.setText("Progress");
        projectLabel.setLayoutX(10);
        projectLabel.setLayoutY(85);
        TitledPane aboutProject = new TitledPane();
        contentOfProject.getChildren().add(projectLabel);
        aboutProject.setText("About Project");
        projectAccordion.getPanes().add(aboutProject);
        aboutProject.setContent(contentOfProject);
        return newProject;
    }

    TitledPane addTaskTitledPane(HelloController controller, TitledPane selectedTitlePane, TextField taskNameTextField, TextArea taskDescriptionTextArea, DatePicker taskStartDateDatePicker, TextField taskStartTimeTextField, DatePicker taskDueDateDatePicker, TextField taskDueTimeTextField) {
        var newTaskTitledPane = new TitledPane();
        var contentOfTask = new AnchorPane();

        newTaskTitledPane.setContent(contentOfTask);
        var datePickers = getDatePicker(selectedTitlePane);
        var timeTextField = getProjectTextField(selectedTitlePane);
        if (datePickers != null) {
            controller.addCheckBoxWithName(taskNameTextField, newTaskTitledPane, false, datePickers.getFirst(), timeTextField.getFirst());
        }
        var addedDates = addDates(controller, taskStartDateDatePicker, taskStartTimeTextField, taskDueDateDatePicker, taskDueTimeTextField);
        contentOfTask.getChildren().addAll(addedDates.startDate(), addedDates.newStartDatePicker(), addedDates.newStartTimeTextField(), addedDates.dueDate(),
                addedDates.newDueDatePicker(), addedDates.newDueTimeTextFiled());
        contentOfTask.getChildren().addAll(addTaskDescription(taskDescriptionTextArea).description(), addTaskDescription(taskDescriptionTextArea).descriptionText());

        CheckBox taskCheckBox = (CheckBox) newTaskTitledPane.getGraphic();
        if (taskCheckBox != null) {
            taskCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> updateProgressBar(selectedTitlePane));
        }
        updateProgressBar(selectedTitlePane);

        return newTaskTitledPane;
    }

    TitledPane addTaskTitledPane(HelloController controller,Task task,Project project, Accordion projectAccordion) {
        var newTask = new TitledPane();
        var contentOfTask = new AnchorPane();

        newTask.setContent(contentOfTask);
        var datePickers = getDatePicker(controller.reversedMap(project));
        var timeTextField = getProjectTextField(controller.reversedMap(project));
        if (datePickers != null) {
            TextField taskNameTextField = new TextField();
            taskNameTextField.setText(task.getName());
            controller.addCheckBoxWithName(taskNameTextField, newTask, false, datePickers.getFirst(), timeTextField.getFirst());
        }
        DatePicker taskStartDateDatePicker = new DatePicker();
        taskStartDateDatePicker.setValue(LocalDate.from(task.getDate_start()));
        TextField taskStartTimeTextField = new TextField();
        taskStartTimeTextField.setText(controller.dateTimeFormat.toTextField(task.getDate_start()));
        DatePicker taskDueDateDatePicker = new DatePicker();
        taskDueDateDatePicker.setValue(LocalDate.from(task.getDeadline()));
        TextField taskDueTimeTextField = new TextField();
        taskDueTimeTextField.setText(controller.dateTimeFormat.toTextField(task.getDeadline()));
        var addedDates = addDates(controller, taskStartDateDatePicker, taskStartTimeTextField, taskDueDateDatePicker, taskDueTimeTextField);
        contentOfTask.getChildren().addAll(addedDates.startDate(), addedDates.newStartDatePicker(), addedDates.newStartTimeTextField(),
                addedDates.dueDate(), addedDates.newDueDatePicker(), addedDates.newDueTimeTextFiled());
        contentOfTask.getChildren().addAll(addTaskDescription(task).description(), addTaskDescription(task).descriptionText());

        CheckBox taskCheckBox = (CheckBox) newTask.getGraphic();
        taskCheckBox.setSelected(task.getState());
        taskCheckBox.setMouseTransparent(true);
        if (taskCheckBox != null) {
            taskCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> updateProgressBar(controller.reversedMap(project)));
        }
        updateProgressBar(controller.reversedMap(project));
        projectAccordion.getPanes().add(newTask);
        return newTask;
    }

    TitledPane addProjectTitledPane(HelloController controller, Project project) {
        var newProject = new TitledPane();
        newProject.setStyle("-fx-border-color: #7b8bac; " + "-fx-border-width: 5px;");
        var projectNameTextField = new TextField();
        projectNameTextField.setText(project.getName());
        var projectStartDateDatePicker = new DatePicker();
        projectStartDateDatePicker.setValue(LocalDate.from(project.getDate_start()));
        var projectStartTimeTextField = new TextField();
        projectStartTimeTextField.setText(controller.dateTimeFormat.toTextField(project.getDate_start()));
        var projectDueDateDatePicker = new DatePicker();
        projectDueDateDatePicker.setValue(LocalDate.from(project.getDeadline()));
        var projectDueTimeTextField = new TextField();
        projectDueTimeTextField.setText(controller.dateTimeFormat.toTextField(project.getDeadline()));
        controller.addCheckBoxWithName(projectNameTextField, newProject, true, projectStartDateDatePicker, projectStartTimeTextField);
        var projectAccordion = new Accordion();
        newProject.setContent(projectAccordion);
        var contentOfProject = new AnchorPane();
        ResulDates addedDates = addDates(controller, projectStartDateDatePicker, projectStartTimeTextField, projectDueDateDatePicker, projectDueTimeTextField);
        contentOfProject.getChildren().addAll(addedDates.startDate(), addedDates.newStartDatePicker(),
                addedDates.newStartTimeTextField(), addedDates.dueDate(), addedDates.newDueDatePicker(),
                addedDates.newDueTimeTextFiled());
        contentOfProject.getChildren().addAll(addProgressBar());
        var projectLabel = new Label();
        projectLabel.setText("Progress");
        projectLabel.setLayoutX(10);
        projectLabel.setLayoutY(85);
        var aboutProject = new TitledPane();
        aboutProject.setText("About Project");
        projectAccordion.getPanes().add(aboutProject);
        contentOfProject.getChildren().addAll(projectLabel);
        aboutProject.setContent(contentOfProject);
        return newProject;
    }

    ResulDates addDates(HelloController controller, DatePicker startDatePicker, TextField startTime, DatePicker dueDatePicker, TextField DueTime) {
        var startDate = new Label("Start Date");
        startDate.setLayoutX(10);
        startDate.setLayoutY(10);


        var newStartDatePicker = new DatePicker();
        newStartDatePicker.setConverter(controller.localDateStringConverter);

        newStartDatePicker.setLayoutX(90);
        newStartDatePicker.setLayoutY(5);
        newStartDatePicker.setValue(startDatePicker.getValue());
        newStartDatePicker.setMouseTransparent(true);

        var newStartTimeTextField = new TextField();
        newStartTimeTextField.setLayoutX(280);
        newStartTimeTextField.setLayoutY(5);
        newStartTimeTextField.setText(startTime.getText());
        newStartTimeTextField.setMouseTransparent(true);

        var due_Date = new Label("Due Date");
        due_Date.setLayoutX(10);
        due_Date.setLayoutY(50);

        var dueDate = new DatePicker();

        dueDate.setConverter(controller.localDateStringConverter);

        dueDate.setLayoutX(90);
        dueDate.setLayoutY(45);
        dueDate.setValue(dueDatePicker.getValue());
        dueDate.setMouseTransparent(true);

        var dueTime = new TextField();
        dueTime.setLayoutX(280);
        dueTime.setLayoutY(45);
        dueTime.setText(DueTime.getText());
        dueTime.setMouseTransparent(true);

        return new ResulDates(startDate, newStartDatePicker, newStartTimeTextField, due_Date, dueDate, dueTime);
    }

    private ProgressBar addProgressBar() {
        var progressBar = new ProgressBar();
        progressBar.setLayoutX(90);
        progressBar.setLayoutY(85);
        return progressBar;
    }

    protected record ResulDates(Label startDate, DatePicker newStartDatePicker, TextField newStartTimeTextField,
                                Label dueDate,
                                DatePicker newDueDatePicker, TextField newDueTimeTextFiled) {
    }

    ArrayList<TextField> getProjectTextField(TitledPane projectPane) {
        AnchorPane projectDetailsPane = (AnchorPane) ((Accordion) projectPane.getContent()).getPanes().getFirst().getContent();
        ArrayList<TextField> timeTextField = new ArrayList<>();
        for (var node : projectDetailsPane.getChildren()) {
            if (node instanceof TextField) {
                if (((TextField) node).getText().matches("\\d{2} : \\d{2}")) {
                    timeTextField.add((TextField) node);
                }
            }
        }
        if (timeTextField.isEmpty()) {
            return null;
        } else {
            return timeTextField;
        }
    }

    void updateProgressBar(TitledPane projectPane) {
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

    private taskDescription addTaskDescription(TextArea taskDescriptionTextArea) {
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

    public taskDescription addTaskDescription(Task task) {
        var description = new Label("Description");
        description.setLayoutX(10);
        description.setLayoutY(90);

        var descriptionText = new TextArea();
        descriptionText.setText(task.getDescription());
        descriptionText.setLayoutX(90);
        descriptionText.setLayoutY(90);
        descriptionText.setEditable(false);

        return new taskDescription(description, descriptionText);
    }

    public record taskDescription(Label description, TextArea descriptionText) {
    }

    void sortProjectInVbox(HelloController controller, ArrayList<Project> userProjects, VBox vbox) {
        vbox.getChildren().clear();
        for (Project project : userProjects) {
            vbox.getChildren().add(controller.reversedMap(project));
        }
    }

    void sortTaskInProject(HelloController controller, ArrayList<Task> listOfTasks, TitledPane selectedTitlePane) {
        var projectAccordion = (Accordion) selectedTitlePane.getContent();

        if (projectAccordion.getPanes().size() > 1) {
            TitledPane firstPane = projectAccordion.getPanes().getFirst();
            projectAccordion.getPanes().clear();
            projectAccordion.getPanes().add(firstPane);
        }
        for (Task task : listOfTasks) {
            projectAccordion.getPanes().add(controller.indexOfTask.get(task));
        }
    }

    void createAlertSign(Exception e, String title) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(e.getMessage());
        alert.showAndWait();
    }

    public void createWarningSign(String s) {
        Alert alert = new Alert(Alert.AlertType.WARNING, s, ButtonType.OK);
        alert.show();
    }

    boolean confirmTaskWithoutDescription() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Brak opisu zadania");
        alert.setHeaderText(null);
        alert.setContentText("Nie dodałeś opisu zadania. Czy na pewno chcesz kontynuować?");

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
    ArrayList<TextField> getTaskTextField(TitledPane taskPane) {
        if (taskPane == null || taskPane.getContent() == null) {
            createWarningSign("No task selected.");
            return null;
        }

        AnchorPane taskDetailsPane = (AnchorPane) taskPane.getContent();
        ArrayList<TextField> timeTextFields = new ArrayList<>();

        for (Node node : taskDetailsPane.getChildren()) {
            if (node instanceof TextField textField) {
                if (textField.getText().matches("\\d{2} : \\d{2}")) { // Format godziny hh : mm
                    timeTextFields.add(textField);
                } else if (!textField.getText().isEmpty()) { // Dodajemy też nazwę zadania, jeśli nie jest pusta
                    timeTextFields.add(textField);
                }
            }
        }

        return timeTextFields.isEmpty() ? null : timeTextFields;
    }
}
