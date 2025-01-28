package com.example.aplikacja_do_zarzadzania;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import kod_aplikacji.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.fxml.FXML;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.*;
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
    public TextField taskStartTimeTextField;
    public DatePicker taskDueDateDatePicker;
    public TextArea taskDescriptionTextArea;
    public Button sortByDueDateButton;
    public Button sortByStartDateButton;
    public Button GenerateReportButton;
    public ToggleButton editToggleButton;
    public ToggleButton okButton;
    public TextField taskDueTimeTextField;
    public TextField projectDueTimeTextField;
    public TextField projectStartTimeTextField;
    @FXML
    private ToggleGroup editToggleGroup;

    private TitledPane selectedTitlePane;
    User user = new User();
    DateTimeFormat dateTimeFormat=new DateTimeFormat();
    FileHandler handler;

    {
        try {
            handler = new FileHandler();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private final Map<TitledPane, Project> indexOfProject = new HashMap<>();
    private final Map<TitledPane, LocalDateTime> indexOfTaskDateAdded = new HashMap<>();
    private final Map<Task, TitledPane> indexOfTask = new HashMap<>();


    @FXML
    public void initialize() {
        timeline1.setCycleCount(Animation.INDEFINITE);
        timeline1.play();
        if (handler.getFile().length() > 0) {
            try {
                user = handler.readFromJsonFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        for (Project project : user.getListOfToDoProject()) {
            addProject(project);
            if(!project.getListOfTask().isEmpty()){
                for (Task task : project.getListOfTask()) {
                    addTask(task, project);
                }
            }

        }
        for (Project project : user.getListOfUnfinishedProject()) {
            addProject(project);

            if(!project.getListOfTask().isEmpty()){
                for (Task task : project.getListOfTask()) {
                    addTask(task, project);
                }
            }
        }
        for (Project project : user.getListOfFinishedProject()) {
            addProject(project);
            CheckBox graphicCheckBox = (CheckBox) reversedMap(project).getGraphic();
            graphicCheckBox.setSelected(true);

            if(!project.getListOfTask().isEmpty()){
                for (Task task : project.getListOfTask()) {
                    addTask(task, project);
                }
            }
        }

        projectStartDateDatePicker.setConverter(new StringConverter<LocalDate>() {
            String pattern = "yyyy-MM-dd";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            {
                projectStartDateDatePicker.setPromptText(pattern.toLowerCase());
            }

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
//        projectStartDateDatePicker.setOnAction(event -> {
//            try{
//                LocalDate selectedDate = projectStartDateDatePicker.getValue();
//
//            }catch (DateTimeParseException e){
//                createWarningSign("Valid date");
//            }
//        });
    }

    public void setStage(Stage stage) {
        stage.setOnCloseRequest(event -> {
            try {
                user.clearListOfIndexOfProjectToMove();
                handler.writeToJsonFile(user);
                System.out.println("User data saved successfully.");
            } catch (IOException e) {
                e.printStackTrace();
            }

            Platform.exit();
        });
    }

    @FXML
    private void onAddButton() {
        String name = "";
        if (!projectNameTextField.getText().isEmpty()) {
            name = projectNameTextField.getText();
            if (projectStartDateDatePicker.getValue() == null && projectDueDateDatePicker.getValue() == null) {
                createWarningSign("Please select project dates");
            } else if (projectStartTimeTextField.getText().isEmpty() || projectDueTimeTextField.getText().isEmpty()) {
                createWarningSign("Please select project time");
            }else {
                addProject(name);
            }
        } else if (!taskNameTextField.getText().isEmpty() && selectedTitlePane != null) {
            name = taskNameTextField.getText();
            if (taskStartDateDatePicker.getValue() == null && taskDueDateDatePicker.getValue() == null) {
                createWarningSign("Please select task dates");
            } else if (taskStartTimeTextField.getText().isEmpty() || taskDueTimeTextField.getText().isEmpty()) {
                createWarningSign("Please select task time");
            } else{
                addTask(name);
            }
        }

        if (name.isEmpty()) {
            createWarningSign("Please enter the name of the project or task.");
        }
    }

    private void checkIfValidDates() {
//        try{
//            projectStartDateDatePicker.isValid
//            tera.prase(projectStartDateDatePicker);
//            projectDueDateDatePicker.getValue();
//            taskStartDateDatePicker.getValue();
//            taskDueDateDatePicker.getValue();
//        }catch (DateTimeParseException e){
//            createWarningSign("Please select correct dates ");
//        }
    }

    private void addProject(String projectName) {
        boolean hasWarningOccurred = false;
        try {
            LocalDateTime dateAdded = LocalDateTime.now();
            LocalDateTime dateStart = dateTimeFormat.toLocalDateTime(projectStartDateDatePicker,projectStartTimeTextField);
            LocalDateTime deadline = dateTimeFormat.toLocalDateTime(projectDueDateDatePicker,projectDueTimeTextField);
            if(deadline==null || dateStart==null){
                createWarningSign("Time has wrong format. Right is (hh : mm)");
                hasWarningOccurred = true;
            }else if (!dateStart.isAfter(deadline)) {
                var projectToAdd = new Project(projectName, false, dateAdded, dateStart, null, deadline);
                var projectTitlePane = addProjectTitlePane(projectNameTextField, projectDueDateDatePicker,
                        projectDueTimeTextField, projectStartDateDatePicker,projectStartTimeTextField);
                indexOfProject.put(projectTitlePane, projectToAdd);

                if (dateStart.isBefore(dateAdded)) {
                    user.addUnfinishedProject(projectToAdd);
                    doingProjectContainer.getChildren().add(projectTitlePane);
                    projectTitlePane.getGraphic().setMouseTransparent(false);
                } else {
                    user.addToDoProject(projectToAdd);
                    toDoProjectContainer.getChildren().add(projectTitlePane);
                }
                projectTitlePane.setOnMouseClicked(event -> getSelectProject(projectTitlePane));
            } else {
                createWarningSign("Start date cannot be after due date.");
                hasWarningOccurred = true;
            }
        } catch (ProjectException e) {
            createAlertSign(e, "Project Error");
            hasWarningOccurred = true;
        } finally {
            if(!hasWarningOccurred){
                projectDueDateDatePicker.setValue(null);
                projectStartDateDatePicker.setValue(null);
                projectStartTimeTextField.clear();
                projectDueTimeTextField.clear();
                projectNameTextField.clear();
            }

        }
    }

    private void addProject(Project project) {
        var projectTitlePane = addProjectTitlePane(project);
        indexOfProject.put(projectTitlePane, project);

        if (project.getDate_end() != null) {
            doneProjectContainer.getChildren().add(projectTitlePane);
        } else if (project.getDate_start().isBefore(LocalDateTime.now())) {
            doingProjectContainer.getChildren().add(projectTitlePane);
            projectTitlePane.getGraphic().setMouseTransparent(false);
        } else {
            toDoProjectContainer.getChildren().add(projectTitlePane);
        }
        projectTitlePane.setOnMouseClicked(event -> getSelectProject(projectTitlePane));
    }

    private void addTask(String taskName) {
        boolean hasWarningOccurred = false;
        try {
            if (selectedTitlePane != null && selectedTitlePane.isExpanded()) {
                Accordion projectAccordion = (Accordion) selectedTitlePane.getContent();
                LocalDateTime dateAdded = LocalDateTime.now();
                LocalDateTime dateStart = dateTimeFormat.toLocalDateTime(taskStartDateDatePicker,taskStartTimeTextField);
                LocalDateTime deadline = dateTimeFormat.toLocalDateTime(taskDueDateDatePicker,taskDueTimeTextField);

                if(deadline==null || dateStart==null){
                    createWarningSign("Time was chosen wrongly");
                    hasWarningOccurred = true;
                } else if (!dateStart.isAfter(deadline)) {
                    var taskToAdd = new Task(taskName, false, dateAdded, dateStart, null, deadline, taskDescriptionTextArea.getText());
                    var newTask = new TitledPane();

                    var contentOfTask = new AnchorPane();
                    indexOfProject.get(selectedTitlePane).addTask(taskToAdd);
                    indexOfTask.put(taskToAdd, newTask);
                    indexOfTaskDateAdded.put(newTask, dateAdded);

                    newTask.setContent(contentOfTask);
                    var datePickers = getDatePicker(selectedTitlePane);
                    var timeTextField= getTextField(selectedTitlePane);
                    if (datePickers != null) {
                        addCheckBoxWithName(taskNameTextField, newTask, false, datePickers.getFirst(),timeTextField.getFirst());
                    }
                    ResulDates addedDates = addDates(taskStartDateDatePicker,taskStartTimeTextField,taskDueDateDatePicker,taskDueTimeTextField);
                    contentOfTask.getChildren().addAll(addedDates.startDate(), addedDates.newStartDatePicker(), addedDates.newStartTimeTextField(), addedDates.dueDate(),
                            addedDates.newDueDatePicker(),addedDates.newDueTimeTextFiled());
                    contentOfTask.getChildren().addAll(addTaskDescription().description(), addTaskDescription().descriptionText());

                    CheckBox taskCheckBox = (CheckBox) newTask.getGraphic();
                    if (taskCheckBox != null) {
                        taskCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> updateProgressBar(selectedTitlePane));
                    }
                    updateProgressBar(selectedTitlePane);
                    projectAccordion.getPanes().add(newTask);
                } else {
                    createWarningSign("Start date cannot be after due date.");
                    hasWarningOccurred=true;
                }
            } else {
                createWarningSign("No project selected");
                hasWarningOccurred=true;
            }
        } catch (Exception e) {
            createAlertSign(e, "Task Error");
            hasWarningOccurred=true;
        } finally {
            if(!hasWarningOccurred){
                taskDueDateDatePicker.setValue(null);
                taskDueTimeTextField.clear();
                taskStartDateDatePicker.setValue(null);
                taskStartTimeTextField.clear();
                taskDescriptionTextArea.clear();
                taskNameTextField.clear();
            }
        }
    }

    private void addTask(Task task, Project project) {
        try {
            TitledPane selectedPane = reversedMap(project);
            Accordion projectAccordion = (Accordion) selectedPane.getContent();
            var newTask = new TitledPane();
            var contentOfTask = new AnchorPane();
            indexOfTask.put(task, newTask);
            indexOfTaskDateAdded.put(newTask, task.getDate_added());

            newTask.setContent(contentOfTask);
            var datePickers = getDatePicker(selectedPane);
            var timeTextField= getTextField(selectedPane);
            if (datePickers != null) {
                TextField taskNameTextField = new TextField();
                taskNameTextField.setText(task.getName());
                addCheckBoxWithName(taskNameTextField, newTask, false, datePickers.getFirst(),timeTextField.getFirst());
            }
            DatePicker taskStartDateDatePicker = new DatePicker();
            taskStartDateDatePicker.setValue(LocalDate.from(task.getDate_start()));
            TextField taskStartTimeTextField = new TextField();
            taskStartTimeTextField.setText(dateTimeFormat.toTextField(task.getDate_start()));
            DatePicker taskDueDateDatePicker = new DatePicker();
            taskDueDateDatePicker.setValue(LocalDate.from(task.getDeadline()));
            TextField taskDueTimeTextField = new TextField();
            taskDueTimeTextField.setText(dateTimeFormat.toTextField(task.getDeadline()));
            ResulDates addedDates = addDates(taskStartDateDatePicker, taskStartTimeTextField,taskDueDateDatePicker,taskDueTimeTextField);
            contentOfTask.getChildren().addAll(addedDates.startDate(), addedDates.newStartDatePicker(), addedDates.newStartTimeTextField(),
                    addedDates.dueDate(), addedDates.newDueDatePicker(), addedDates.newDueTimeTextFiled());
            contentOfTask.getChildren().addAll(addTaskDescription(task).description(), addTaskDescription(task).descriptionText());

            CheckBox taskCheckBox = (CheckBox) newTask.getGraphic();
            if (taskCheckBox != null) {
                taskCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> updateProgressBar(selectedPane));
            }
            updateProgressBar(selectedPane);
            projectAccordion.getPanes().add(newTask);

        } catch (Exception e) {
            createAlertSign(e, "Task Error");
        }
    }

    @FXML
    private void onDeleteButton() {
        if (selectedTitlePane == null) {
            createWarningSign("No project or task selected to delete");
            return;
        }
        Accordion projectAccordion = (Accordion) selectedTitlePane.getContent();
        if (projectAccordion.getExpandedPane() != null) {
            if (projectAccordion.getExpandedPane() != projectAccordion.getPanes().getFirst()) {
                TitledPane selectedTask = projectAccordion.getExpandedPane();
                projectAccordion.getPanes().remove(selectedTask);
                indexOfProject.get(selectedTitlePane).deleteTask(indexOfTaskDateAdded.get(selectedTask));
                indexOfTaskDateAdded.remove(selectedTask);
            } else {
                createWarningSign("Cannot delete information about project");
            }

        } else {
            Project projectToRemove = indexOfProject.get(selectedTitlePane);
            if (user.getListOfToDoProject().contains(projectToRemove)) {
                user.deleteToDoProject(projectToRemove.getDate_added());
                toDoProjectContainer.getChildren().remove(selectedTitlePane);
            } else if (user.getListOfUnfinishedProject().contains(projectToRemove)) {
                user.deleteUnfinishedProject(projectToRemove.getDate_added());
                doingProjectContainer.getChildren().remove(selectedTitlePane);
            } else {
                user.deleteFinishedProject(projectToRemove.getDate_added());
                doneProjectContainer.getChildren().remove(selectedTitlePane);
            }
            indexOfProject.remove(selectedTitlePane);
            selectedTitlePane = null;
        }
    }

    public void onSortByStartDate() {
        if (!(selectedTitlePane == null) && selectedTitlePane.isExpanded()) {
            sortTaskInProject(indexOfProject.get(selectedTitlePane).getListOfTask());
        } else {
            sortProjectInVbox(user.getListOfToDoProject(), toDoProjectContainer);
            sortProjectInVbox(user.getListOfUnfinishedProject(), doingProjectContainer);
            sortProjectInVbox(user.getListOfFinishedProject(), doneProjectContainer);
        }
    }

    public void onSortByDueDate() {
        if (!(selectedTitlePane == null) && selectedTitlePane.isExpanded()) {
            sortTaskInProject(indexOfProject.get(selectedTitlePane).sortTask());
            sortTaskInProject(indexOfProject.get(selectedTitlePane).sortTask());
        } else {
            sortProjectInVbox(user.sortProject(user.getListOfToDoProject()), toDoProjectContainer);
            sortProjectInVbox(user.sortProject(user.getListOfFinishedProject()), doingProjectContainer);
            sortProjectInVbox(user.sortProject(user.getListOfFinishedProject()), doneProjectContainer);
        }
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
                user.deleteToDoProject(projectToMove.getDate_added());
                var titlePaneToMove = reversedMap.get(projectToMove);
                toDoProjectContainer.getChildren().remove(titlePaneToMove);
                doingProjectContainer.getChildren().add(titlePaneToMove);
                titlePaneToMove.getGraphic().setMouseTransparent(false);

            }
            user.clearListOfIndexOfProjectToMove();
        }

    }

    private void moveProjectFromDoingToToDo() {
        var listOfProjectsToMove = user.getListOfIndexOfProjectToMove();
        var index = listOfProjectsToMove.size();
        if (index > 0) {
            HashMap<Project, TitledPane> reversedMap = new HashMap<>();
            for (Map.Entry<TitledPane, Project> entry : indexOfProject.entrySet()) {
                reversedMap.put(entry.getValue(), entry.getKey());
            }
            for (int i = 0; i < index; i++) {
                var projectToMove = user.getListOfUnfinishedProject().get(listOfProjectsToMove.get(i));
                user.deleteUnfinishedProject(user.getListOfUnfinishedProject().get(i).getDate_added());
                var titlePaneToMove = reversedMap.get(projectToMove);
                doingProjectContainer.getChildren().remove(titlePaneToMove);
                toDoProjectContainer.getChildren().add(titlePaneToMove);
                titlePaneToMove.getGraphic().setMouseTransparent(true);
            }
            user.clearListOfIndexOfProjectToMove();
        }

    }

//    private void checkIfAfterDeadline(){
//        var listOfProjectToMarkRed = user.getListOfUnfinishedProject();
//        HashMap<Project, TitledPane> reversedMap = new HashMap<>();
//        for (Map.Entry<TitledPane, Project> entry : indexOfProject.entrySet()) {
//            reversedMap.put(entry.getValue(), entry.getKey());
//        }
//
//        for (Project project : listOfProjectToMarkRed) {
//            if(project.getDeadline().isBefore(LocalDateTime.now())){
//                var projectTitlePane = reversedMap.get(project);
//                Node titleNode = projectTitlePane.lookup(".title");
//                if (titleNode != null) {
////                    var resource = getClass().getResource("/style.css");
////
////                    projectTitlePane.setStyle(String.valueOf(resource));
//                }
////                selectedTitlePane.setStyle("-fx-background-color: #CD8079; -fx-text-fill: white;");
////                projectTitlePane.lookup(".title").setStyle("-fx-background-color: #D98782;");
//            }
//        }
//    }

    private void moveProjectToDone(TitledPane projectTitlePane) {
        doingProjectContainer.getChildren().remove(projectTitlePane);
        doneProjectContainer.getChildren().add(projectTitlePane);

        setCheckBoxes(projectTitlePane, true);
    }
    private void moveProjectFromDoneToDoing(TitledPane projectTitledPane){
        doneProjectContainer.getChildren().remove(projectTitledPane);
        doingProjectContainer.getChildren().add(projectTitledPane);
        indexOfProject.get(projectTitledPane).setDate_end(null);
    }

    private void setCheckBoxes(TitledPane projectTitlePane, boolean b) {
        CheckBox projectCheckBox = (CheckBox) projectTitlePane.getGraphic();

        var projectContent = (Accordion) projectTitlePane.getContent();
        for (int i = 1; i < projectContent.getPanes().size(); i++) {
            TitledPane taskPane = projectContent.getPanes().get(i);
            CheckBox taskCheckBox = (CheckBox) taskPane.getGraphic();
            taskCheckBox.setMouseTransparent(b);
        }
        if (projectCheckBox != null) {
            projectCheckBox.setMouseTransparent(b);
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

    private TitledPane addProjectTitlePane(TextField projectNameTextField, DatePicker projectDueDateDatePicker, TextField projectDueTimeTextField,DatePicker projectStartDateDatePicker,TextField projectStartTimeTextField) {
        var newProject = new TitledPane();
        addCheckBoxWithName(projectNameTextField, newProject, true, projectStartDateDatePicker, projectStartTimeTextField);
        var projectAccordion = new Accordion();
        newProject.setContent(projectAccordion);
        var contentOfProject = new AnchorPane();
        ResulDates addedDates = addDates(projectStartDateDatePicker, projectStartTimeTextField,
                projectDueDateDatePicker, projectDueTimeTextField);
        contentOfProject.getChildren().addAll(addedDates.startDate(), addedDates.newStartDatePicker(),
                addedDates.newStartTimeTextField(), addedDates.dueDate(), addedDates.newDueDatePicker(),
                addedDates.newDueTimeTextFiled());
        contentOfProject.getChildren().addAll(addProgressBar());
        TitledPane aboutProject = new TitledPane();
        aboutProject.setText("About Project");
        projectAccordion.getPanes().add(aboutProject);
        aboutProject.setContent(contentOfProject);
        return newProject;
    }

    private TitledPane addProjectTitlePane(Project project) {
        var newProject = new TitledPane();
        var projectNameTextField = new TextField();
        projectNameTextField.setText(project.getName());
        var projectStartDateDatePicker = new DatePicker();
        projectStartDateDatePicker.setValue(LocalDate.from(project.getDate_start()));
        var projectStartTimeTextField = new TextField();
        projectStartTimeTextField.setText(dateTimeFormat.toTextField(project.getDate_start()));
        var projectDueDateDatePicker = new DatePicker();
        projectDueDateDatePicker.setValue(LocalDate.from(project.getDeadline()));
        var projectDueTimeTextField = new TextField();
        projectDueTimeTextField.setText(dateTimeFormat.toTextField(project.getDeadline()));
        addCheckBoxWithName(projectNameTextField, newProject, true, projectStartDateDatePicker,projectStartTimeTextField);
        var projectAccordion = new Accordion();
        newProject.setContent(projectAccordion);
        var contentOfProject = new AnchorPane();
        ResulDates addedDates = addDates(projectStartDateDatePicker, projectStartTimeTextField, projectDueDateDatePicker,projectDueTimeTextField);
        contentOfProject.getChildren().addAll(addedDates.startDate(), addedDates.newStartDatePicker(),
                addedDates.newStartTimeTextField(), addedDates.dueDate(), addedDates.newDueDatePicker(),
                addedDates.newDueTimeTextFiled());
        contentOfProject.getChildren().addAll(addProgressBar());
        var aboutProject = new TitledPane();
        aboutProject.setText("About Project");
        projectAccordion.getPanes().add(aboutProject);
        aboutProject.setContent(contentOfProject);
        return newProject;
    }


    private void addCheckBoxWithName(TextField nameTextField, TitledPane newTitledPane, boolean isProject, DatePicker starDatePicker, TextField startTime) {
        CheckBox checkBox = new CheckBox(nameTextField.getText());
        newTitledPane.setGraphic(checkBox);
        LocalDateTime startDateTime = dateTimeFormat.toLocalDateTime(starDatePicker,startTime);
        checkBox.setMouseTransparent(!startDateTime.isBefore(LocalDateTime.now()));

        if (isProject) {
            checkBox.setOnAction(event -> {
                if (checkBox.isSelected()) {
                    indexOfProject.get(newTitledPane).endProject(LocalDateTime.now());
                    user.ifFinished();
                    if (checkIfTaskAreFinished(newTitledPane)) {
                        moveProjectToDone(newTitledPane);
                    }
                    checkBox.setMouseTransparent(false);
                }
                if(!checkBox.isSelected()){
                    moveProjectFromDoneToDoing(newTitledPane);
                    checkBox.setMouseTransparent(false);
                }
            });
        } else {
            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> updateProgressBar(selectedTitlePane));
        }
    }

    private ResulDates addDates(DatePicker startDatePicker,TextField startTime, DatePicker dueDatePicker,TextField DueTime) {
        var startDate = new Label("Start Date");
        startDate.setLayoutX(10);
        startDate.setLayoutY(10);

        var newStartDatePicker = new DatePicker();
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
        dueDate.setLayoutX(90);
        dueDate.setLayoutY(45);
        dueDate.setValue(dueDatePicker.getValue());
        dueDate.setMouseTransparent(true);

        var dueTime = new TextField();
        dueTime.setLayoutX(280);
        dueTime.setLayoutY(45);
        dueTime.setText(DueTime.getText());
        dueTime.setMouseTransparent(true);

        return new ResulDates(startDate, newStartDatePicker, newStartTimeTextField, due_Date, dueDate,dueTime);
    }


    private TitledPane reversedMap(Project project) {
        HashMap<Project, TitledPane> reversedMap = new HashMap<>();
        for (Map.Entry<TitledPane, Project> entry : indexOfProject.entrySet()) {
            reversedMap.put(entry.getValue(), entry.getKey());
        }
        return reversedMap.get(project);
    }

    public void onEditButton() {
        setTransparentToChangeDate(false);
        refresh();
    }

    public void onOkToggleButton() {
        setTransparentToChangeDate(true);
        refresh();
    }

    public void setTransparentToChangeDate(boolean transparent) {
        var projectTitlePane = selectedTitlePane;
        if (projectTitlePane != null) {
            ArrayList<DatePicker> datePickers;
            datePickers = getDatePicker(projectTitlePane);
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

    public void refresh() {
        if (selectedTitlePane != null) {
            ArrayList<DatePicker> datePickers;
            datePickers = getDatePicker(selectedTitlePane);
            indexOfProject.get(selectedTitlePane).setDateStart(getLocalDateTime(datePickers.get(0)));
            indexOfProject.get(selectedTitlePane).setDeadline(getLocalDateTime(datePickers.get(1)));
        }
    }


    private record ResulDates(Label startDate, DatePicker newStartDatePicker,TextField newStartTimeTextField, Label dueDate,
                              DatePicker newDueDatePicker, TextField newDueTimeTextFiled){
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

    private taskDescription addTaskDescription(Task task) {
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
    }

    private ArrayList<TextField> getTextField(TitledPane projectPane) {
        AnchorPane projectDetailsPane = (AnchorPane) ((Accordion) projectPane.getContent()).getPanes().getFirst().getContent();
        ArrayList<TextField> timeTextField = new ArrayList<>();
        for (var node : projectDetailsPane.getChildren()) {
            if (node instanceof TextField) {
                if(((TextField) node).getText().matches("\\d{2} : \\d{2}")){
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

    private void getSelectProject(TitledPane titlePane) {
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

    public void createWarningSign(String s) {
        Alert alert = new Alert(Alert.AlertType.WARNING, s, ButtonType.OK);
        alert.show();
    }

    final Timeline timeline1 = new Timeline(
            new KeyFrame(
                    Duration.seconds(0.01),
                    event -> {
                        //long start = System.currentTimeMillis();
                        user.ifStarted();
                        moveProjectToDoing();
                        try {
                            user.ifStartedReverse();
                        } catch (ProjectException e) {

                            throw new RuntimeException(e);
                        }
                        checkIfValidDates();
                        moveProjectFromDoingToToDo();
                        //System.err.println("Finished after " + (System.currentTimeMillis() - start) + "ms");
                    }
            )
    );
}

