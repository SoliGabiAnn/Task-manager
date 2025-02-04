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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.LocalDateStringConverter;
import kod_aplikacji.*;
import javafx.scene.layout.VBox;
import javafx.fxml.FXML;

import java.io.IOException;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


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
    DateTimeFormat dateTimeFormat = new DateTimeFormat();
    FileHandler handler;
    GuiView view = new GuiView();

    {
        try {
            handler = new FileHandler();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private final Map<TitledPane, Project> indexOfProject = new HashMap<>();
    private final Map<TitledPane, LocalDateTime> indexOfTaskDateAdded = new HashMap<>();
    final Map<Task, TitledPane> indexOfTask = new HashMap<>();


    @FXML
    public void initialize() {
        timeline1.setCycleCount(Animation.INDEFINITE);
        timeline1.play();
        projectStartDateDatePicker.setConverter(localDateStringConverter);
        projectDueDateDatePicker.setConverter(localDateStringConverter);
        taskStartDateDatePicker.setConverter(localDateStringConverter);
        taskDueDateDatePicker.setConverter(localDateStringConverter);

        if (handler.getFile().length() > 0) {
            try {
                user = handler.readFromJsonFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        for (Project project : user.getListOfToDoProject()) {
            addProject(project);
            if (!project.getListOfTask().isEmpty()) {
                for (Task task : project.getListOfTask()) {
                    addTask(task, project);
                }
            }

        }
        for (Project project : user.getListOfUnfinishedProject()) {
            addProject(project);
            CheckBox graphicCheckBox = (CheckBox) reversedMap(project).getGraphic();
            graphicCheckBox.setSelected(false);

            if (!project.getListOfTask().isEmpty()) {
                for (Task task : project.getListOfTask()) {
                    addTask(task, project);
                }
            }
            setCheckBoxesSelected(reversedMap(project));
        }
        for (Project project : user.getListOfFinishedProject()) {
            addProject(project);
            if (!project.getListOfTask().isEmpty()) {
                for (Task task : project.getListOfTask()) {
                    addTask(task, project);
                }
            }
            setCheckBoxesSelected(reversedMap(project));
            CheckBox graphicCheckBox = (CheckBox) reversedMap(project).getGraphic();
            graphicCheckBox.setSelected(true);
        }
    }

    public void setStage(Stage stage) {
        stage.setOnCloseRequest(event -> {
            try {
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
                view.createWarningSign("Please select project dates");
            } else if (projectStartTimeTextField.getText().isEmpty() || projectDueTimeTextField.getText().isEmpty()) {
                view.createWarningSign("Please select project time");
            } else {
                addProject(name);
            }
        } else if (!taskNameTextField.getText().isEmpty() && selectedTitlePane != null) {
            name = taskNameTextField.getText();
            if (taskStartDateDatePicker.getValue() == null && taskDueDateDatePicker.getValue() == null) {
                view.createWarningSign("Please select task dates");
            } else if (taskStartTimeTextField.getText().isEmpty() || taskDueTimeTextField.getText().isEmpty()) {
                view.createWarningSign("Please select task time");
            } else {
                addTask(name);
            }
        }

        if (name.isEmpty()) {
            view.createWarningSign("Please enter the name of the project or task.");
        }
    }


    private void addProject(String projectName) {
        boolean hasWarningOccurred = false;
        try {
            LocalDateTime dateAdded = LocalDateTime.now();
            LocalDateTime dateStart = dateTimeFormat.toLocalDateTime(projectStartDateDatePicker, projectStartTimeTextField);
            LocalDateTime deadline = dateTimeFormat.toLocalDateTime(projectDueDateDatePicker, projectDueTimeTextField);

            if (deadline == null || dateStart == null) {
                view.createWarningSign("Time has wrong format. Right is (hh : mm)");
                hasWarningOccurred = true;
            } else if (!dateStart.isAfter(deadline)) {
                var projectToAdd = new Project(projectName, false, dateAdded, dateStart, null, deadline);
                var projectTitlePane = view.addProjectTitledPane(this,projectNameTextField, projectDueDateDatePicker,
                        projectDueTimeTextField, projectStartDateDatePicker, projectStartTimeTextField);
                indexOfProject.put(projectTitlePane, projectToAdd);

                if (dateStart.isBefore(dateAdded)) {
                    user.addUnfinishedProject(projectToAdd);
                    doingProjectContainer.getChildren().add(projectTitlePane);
                    projectTitlePane.getGraphic().setMouseTransparent(false);
                } else {
                    user.addToDoProject(projectToAdd);
                    toDoProjectContainer.getChildren().add(projectTitlePane);
                    projectTitlePane.getGraphic().setMouseTransparent(true);
                }
                projectTitlePane.setOnMouseClicked(event -> getSelectProject(projectTitlePane));
            } else {
                view.createWarningSign("Start date cannot be after due date.");
                hasWarningOccurred = true;
            }
        } catch (ProjectException e) {
            view.createAlertSign(e, "Project Error");
            hasWarningOccurred = true;
        } finally {
            if (!hasWarningOccurred) {
                projectDueDateDatePicker.setValue(null);
                projectStartDateDatePicker.setValue(null);
                projectStartTimeTextField.clear();
                projectDueTimeTextField.clear();
                projectNameTextField.clear();
            }

        }
    }

    private void addProject(Project project) {
        var projectTitlePane = view.addProjectTitledPane(this,project);
        indexOfProject.put(projectTitlePane, project);

        if (project.getDate_end() != null) {
            doneProjectContainer.getChildren().add(projectTitlePane);
            projectTitlePane.getGraphic().setMouseTransparent(false);

        } else if (project.getDate_start().isBefore(LocalDateTime.now())) {
            doingProjectContainer.getChildren().add(projectTitlePane);
            projectTitlePane.getGraphic().setMouseTransparent(false);
        } else {
            toDoProjectContainer.getChildren().add(projectTitlePane);
            projectTitlePane.getGraphic().setMouseTransparent(true);

        }
        projectTitlePane.setOnMouseClicked(event -> getSelectProject(projectTitlePane));
    }

    private void addTask(String taskName) {
        boolean hasWarningOccurred = false;
        try {
            if (selectedTitlePane != null && selectedTitlePane.isExpanded()) {
                Accordion projectAccordion = (Accordion) selectedTitlePane.getContent();
                LocalDateTime dateAdded = LocalDateTime.now();
                LocalDateTime dateStart = dateTimeFormat.toLocalDateTime(taskStartDateDatePicker, taskStartTimeTextField);
                LocalDateTime deadline = dateTimeFormat.toLocalDateTime(taskDueDateDatePicker, taskDueTimeTextField);

                if (deadline == null || dateStart == null) {
                    view.createWarningSign("Time was chosen wrongly");
                    hasWarningOccurred = true;
                } else if (!dateStart.isAfter(deadline)) {

                    if (taskDescriptionTextArea.getText().trim().isEmpty()) {
                        boolean confirmed = view.confirmTaskWithoutDescription();
                        if (!confirmed) {
                            return;
                        }
                    }
                    var taskToAdd = new Task(taskName, false, dateAdded, dateStart, null, deadline, taskDescriptionTextArea.getText());
                    var newTaskTitledPane = view.addTaskTitledPane(this,selectedTitlePane,taskNameTextField, taskDescriptionTextArea, taskStartDateDatePicker, taskStartTimeTextField, taskDueDateDatePicker, taskDueTimeTextField);
                    indexOfProject.get(selectedTitlePane).addTask(taskToAdd);
                    indexOfTask.put(taskToAdd, newTaskTitledPane);
                    indexOfTaskDateAdded.put(newTaskTitledPane, dateAdded);

                    projectAccordion.getPanes().add(newTaskTitledPane);
                } else {
                    view.createWarningSign("Start date cannot be after due date.");
                    hasWarningOccurred = true;
                }
            } else {
                view.createWarningSign("No project selected");
                hasWarningOccurred = true;
            }
        } catch (Exception e) {
            view.createAlertSign(e, "Task Error");
            hasWarningOccurred = true;
        } finally {
            if (!hasWarningOccurred) {
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
            var newTask = view.addTaskTitledPane(this, task, project, projectAccordion);
            indexOfTask.put(task, newTask);
            indexOfTaskDateAdded.put(newTask, task.getDate_added());
        } catch (Exception e) {
            view.createAlertSign(e, "Task Error");
        }
    }

    @FXML
    private void onDeleteButton() {
        if (selectedTitlePane == null) {
            view.createWarningSign("No project or task selected to delete");
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
                view.createWarningSign("Cannot delete information about project");
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
            view.sortTaskInProject(this,indexOfProject.get(selectedTitlePane).getListOfTask(), selectedTitlePane);
        } else {
            view.sortProjectInVbox(this, user.getListOfToDoProject(), toDoProjectContainer);
            view.sortProjectInVbox(this, user.getListOfUnfinishedProject(), doingProjectContainer);
            view.sortProjectInVbox(this, user.getListOfFinishedProject(), doneProjectContainer);
        }
    }

    public void onSortByDueDate() {
        if (!(selectedTitlePane == null) && selectedTitlePane.isExpanded()) {
            view.sortTaskInProject(this,indexOfProject.get(selectedTitlePane).sortTask(), selectedTitlePane);
            view.sortTaskInProject(this,indexOfProject.get(selectedTitlePane).sortTask(), selectedTitlePane);
        } else {
            view.sortProjectInVbox(this,user.sortProject(user.getListOfToDoProject()), toDoProjectContainer);
            view.sortProjectInVbox(this, user.sortProject(user.getListOfUnfinishedProject()), doingProjectContainer);
            view.sortProjectInVbox(this, user.sortProject(user.getListOfFinishedProject()), doneProjectContainer);
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

    private void moveProjectToDoing() {
        var listOfProjectsToMove = user.getListOfIndexOfProjectToMove();
        var index = listOfProjectsToMove.size();
        if (index > 0) {
            for (Integer integer : listOfProjectsToMove) {
                var projectToMove = user.getListOfToDoProject().get(integer);
                user.deleteToDoProject(projectToMove.getDate_added());
                var titlePaneToMove = reversedMap(projectToMove);
                toDoProjectContainer.getChildren().remove(titlePaneToMove);
                doingProjectContainer.getChildren().add(titlePaneToMove);
                titlePaneToMove.getGraphic().setMouseTransparent(false);

                for (int i = 0; i < projectToMove.getListOfTask().size(); i++) {
                    indexOfTask.get(projectToMove.getListOfTask().get(i)).getGraphic().setMouseTransparent(false);
                }
            }
            user.clearListOfIndexOfProjectToMove();
        }

    }

    private void moveProjectFromDoingToToDo() {
        var listOfProjectsToMove = user.getListOfIndexOfProjectToMoveToDo();
        var index = listOfProjectsToMove.size();
        if (index > 0) {
            for (Integer integer : listOfProjectsToMove) {
                var projectToMove = user.getListOfUnfinishedProject().get(integer);
                user.deleteUnfinishedProject(projectToMove.getDate_added());
                var titledPaneToMove = reversedMap(projectToMove);
                doingProjectContainer.getChildren().remove(titledPaneToMove);
                toDoProjectContainer.getChildren().add(titledPaneToMove);
                titledPaneToMove.getGraphic().setMouseTransparent(true);
                var listOfTask = indexOfProject.get(titledPaneToMove).getListOfTask();
                for (Task task : listOfTask) {
                    indexOfTask.get(task).getGraphic().setMouseTransparent(true);
                }
            }
            user.clearListOfIndexOfProjectToMoveToDo();
        }
    }

    private void moveProjectToDone(TitledPane projectTitlePane) {
        if(indexOfProject.get(projectTitlePane).checkIfTasksAreFinished()) {
            doingProjectContainer.getChildren().remove(projectTitlePane);
            doneProjectContainer.getChildren().add(projectTitlePane);
            projectTitlePane.setStyle("-fx-border-color: #7b8bac; " + "-fx-border-width: 5px;");

            indexOfProject.get(projectTitlePane).setState(true);
            setCheckBoxesSelected(projectTitlePane);

            var listOfTask = indexOfProject.get(projectTitlePane).getListOfTask();
            for (Task task : listOfTask) {
                indexOfTask.get(task).getGraphic().setMouseTransparent(true);
            }
        }
    }

    private void moveProjectFromDoneToDoing(TitledPane projectTitledPane) {
        if(doneProjectContainer.getChildren().contains(projectTitledPane)) {
            doneProjectContainer.getChildren().remove(projectTitledPane);
            doingProjectContainer.getChildren().add(projectTitledPane);
            indexOfProject.get(projectTitledPane).setDate_end(null);
            indexOfProject.get(projectTitledPane).setState(false);

            var listOfTask = indexOfProject.get(projectTitledPane).getListOfTask();
            for (Task task : listOfTask) {
                indexOfTask.get(task).getGraphic().setMouseTransparent(false);
            }
        }
    }

    private void setCheckBoxesSelected(TitledPane projectTitlePane) {
        CheckBox projectCheckBox = (CheckBox) projectTitlePane.getGraphic();

        var project = indexOfProject.get(projectTitlePane);
        for (int i = 0; i < project.getListOfTask().size(); i++) {
            TitledPane taskPane = indexOfTask.get(project.getListOfTask().get(i));
            if (project.getListOfTask().get(i).getState()) {
                CheckBox taskCheckBox = (CheckBox) taskPane.getGraphic();
                taskCheckBox.setSelected(true);
            }
        }
        if (project.getState()) {
            projectCheckBox.setSelected(true);
        }
    }

    private void checkIfTasksStartDateCame() {
        for (int i = 0; i < user.getListOfUnfinishedProject().size(); i++) {
            for (int j = 0; j < user.getListOfUnfinishedProject().get(i).getListOfTask().size(); j++) {
                var task = user.getListOfUnfinishedProject().get(i).getListOfTask().get(j);
                indexOfTask.get(task).getGraphic().setMouseTransparent(!task.getDate_start().isBefore(LocalDateTime.now()));
            }
        }
    }


    void addCheckBoxWithName(TextField nameTextField, TitledPane newTitledPane, boolean isProject, DatePicker starDatePicker, TextField startTime) {
        CheckBox checkBox = new CheckBox(nameTextField.getText());
        newTitledPane.setGraphic(checkBox);
        LocalDateTime startDateTime = dateTimeFormat.toLocalDateTime(starDatePicker, startTime);
        checkBox.setMouseTransparent(!startDateTime.isBefore(LocalDateTime.now()));

        if (isProject) {
            checkBox.setOnAction(event -> {
                if (checkBox.isSelected()) {
                    if (view.checkIfTaskAreFinished(newTitledPane)) {
                        moveProjectToDone(newTitledPane);
                        indexOfProject.get(newTitledPane).endProject(LocalDateTime.now());
                        user.ifFinished();
                    }
                }
                if (!checkBox.isSelected()) {
                    moveProjectFromDoneToDoing(newTitledPane);
                    indexOfProject.get(newTitledPane).endProjectReversed();
                    user.ifFinishedReverse();

                }
            });
        } else {
            checkBox.setOnAction(event -> {
                if (checkBox.isSelected()) {
                    reversedMapTask(newTitledPane).endTask(LocalDateTime.now());
                }
                if (!checkBox.isSelected()) {
                    reversedMapTask(newTitledPane).endTaskReversed();
                }
            });
            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> view.updateProgressBar(selectedTitlePane));
        }
    }

    TitledPane reversedMap(Project project) {
        HashMap<Project, TitledPane> reversedMap = new HashMap<>();
        for (Map.Entry<TitledPane, Project> entry : indexOfProject.entrySet()) {
            reversedMap.put(entry.getValue(), entry.getKey());
        }
        return reversedMap.get(project);
    }

    private Task reversedMapTask(TitledPane titledPane) {
        Map<TitledPane, Task> reversed = new HashMap<>();
        for (Map.Entry<Task, TitledPane> entry : indexOfTask.entrySet()) {
            reversed.put(entry.getValue(), entry.getKey());
        }
        return reversed.get(titledPane);
    }


    public void onOkToggleButton() {
        if (selectedTitlePane == null) {
            view.createWarningSign("No project or task selected.");
            return;
        }

        var project = indexOfProject.get(selectedTitlePane);

        view.setTransparentToChangeDate(this,true, selectedTitlePane);

        Accordion projectAccordion = (Accordion) selectedTitlePane.getContent();
        if (projectAccordion != null) {
            TitledPane selectedTask = projectAccordion.getExpandedPane();
            if (selectedTask != null) {
                view.setTransparentToChangeDateTask(this,true, selectedTask);
                var taskTimes = view.getTaskTextField(selectedTask);
                taskTimes.getFirst().setMouseTransparent(true);
                taskTimes.get(1).setMouseTransparent(true);
                if (!projectAccordion.getPanes().getFirst().isExpanded()) {
                    view.getTaskDescription(selectedTask).setMouseTransparent(true);

                }
            }
        }
        refresh();
    }

    public void refresh() {
        GenerateReport generate = new GenerateReport(user);
        if (selectedTitlePane != null) {
            ArrayList<DatePicker> datePickers = view.getDatePicker(selectedTitlePane);
            Accordion projectAccordion = (Accordion) selectedTitlePane.getContent();
            TitledPane expandedTask = projectAccordion.getExpandedPane();

            if (datePickers != null && datePickers.size() >= 2) {
                var projectTasks = view.getProjectTextField(selectedTitlePane);

                LocalDateTime startDate = dateTimeFormat.toLocalDateTime(datePickers.get(0), projectTasks.get(0));
                LocalDateTime deadline = dateTimeFormat.toLocalDateTime(datePickers.get(1), projectTasks.get(1));
                if(startDate!=null && deadline!=null) {
                    if (indexOfProject.containsKey(selectedTitlePane)) {
                        if (!startDate.isAfter(deadline)) {
                            if (!deadline.isBefore(startDate)) {
                                LocalDateTime last = indexOfProject.get(selectedTitlePane).latestDate();
                                LocalDateTime first = indexOfProject.get(selectedTitlePane).earliestDate();
                                if (last != null && first != null) {
                                    if (last.isBefore(deadline) && first.isAfter(startDate)) {
                                        indexOfProject.get(selectedTitlePane).setDateStart(startDate);
                                        indexOfProject.get(selectedTitlePane).setDeadline(deadline);
                                    } else {
                                        view.createWarningSign("Project timeframe must include tasks timeframe");
                                    }
                                } else {
                                    indexOfProject.get(selectedTitlePane).setDateStart(startDate);
                                    indexOfProject.get(selectedTitlePane).setDeadline(deadline);
                                }
                            } else {
                                view.createWarningSign("Due date cannot be before start date");
                            }
                        } else {
                            view.createWarningSign("Start date cannot be after due date");
                        }

                    }
                }else{
                    view.createWarningSign("Wrong time format (hh : mm)");
                }
            }


            if (expandedTask != null) {
                ArrayList<DatePicker> taskDatePickers = view.getTaskDatePickers(this,expandedTask);
                if (taskDatePickers != null && taskDatePickers.size() >= 2) {
                    if (indexOfTask.containsValue(expandedTask)) {
                        for (Map.Entry<Task, TitledPane> entry : indexOfTask.entrySet()) {
                            if (entry.getValue() == expandedTask) {
                                var taskTimes = view.getTaskTextField(expandedTask);

                                LocalDateTime taskStartDate = dateTimeFormat.toLocalDateTime(taskDatePickers.get(0), taskTimes.get(0));
                                LocalDateTime taskDeadline = dateTimeFormat.toLocalDateTime(taskDatePickers.get(1), taskTimes.get(1));

                                Task task = entry.getKey();
                                if(taskDeadline!=null && taskStartDate!=null) {
                                    if (!indexOfProject.get(selectedTitlePane).getDate_start().isAfter(taskStartDate)) {
                                        if (!taskStartDate.isAfter(taskDeadline)) {
                                            if (!indexOfProject.get(selectedTitlePane).getDeadline().isBefore(taskDeadline)) {
                                                if (!taskDeadline.isBefore(taskStartDate)) {
                                                    task.setDateStart(taskStartDate);
                                                    task.setDeadline(taskDeadline);
                                                } else {
                                                    view.createWarningSign("Due date cannot be before start date");
                                                }
                                            } else {
                                                view.createWarningSign("Task' due date cannot be after project start date");
                                            }

                                        } else {
                                            view.createWarningSign("Start date cannot be after due date");
                                        }
                                    } else {
                                        view.createWarningSign("Task's start date cannot be before project's start date");
                                    }
                                }else{
                                    view.createWarningSign("Wrong time format (hh : mm)");
                                }


                                var taskDescription = view.getTaskDescription(expandedTask);
                                reversedMapTask(expandedTask).setDescription(taskDescription.getText());

                                break;
                            }
                        }
                    }
                }
            }
        }
    }


    public void onEditButton() {
        if (selectedTitlePane == null) {
            view.createWarningSign("No project or task selected to edit.");
            return;
        }

        Accordion projectAccordion = (Accordion) selectedTitlePane.getContent();
        if (projectAccordion.getExpandedPane() != null) {
            TitledPane selectedTask = projectAccordion.getExpandedPane();
            view.setTransparentToChangeDateTask(this, false, selectedTask);
            var taskTimes = view.getTaskTextField(selectedTask);
            taskTimes.getFirst().setMouseTransparent(false);
            taskTimes.get(1).setMouseTransparent(false);
            if (!projectAccordion.getPanes().getFirst().isExpanded()) {
                var taskDescription = view.getTaskDescription(selectedTask);
                taskDescription.setEditable(true);
            }
        }
    }

    LocalDateStringConverter localDateStringConverter = new LocalDateStringConverter() {
        @Override
        public LocalDate fromString(String value) {
            try {
                return super.fromString(value);
            } catch (Exception e) {
                view.createAlertSign(e, "Date is in wrong format!");
                return LocalDate.now();
            }
        }

        @Override
        public String toString(LocalDate value) {
            return super.toString(value);
        }
    };

    private void getSelectProject(TitledPane titlePane) {
        selectedTitlePane = titlePane;
    }

    public void checkIfProjectIsBeforeDeadline() {
        for (int i = 0; i < user.getListOfUnfinishedProject().size(); i++) {
            if (user.getListOfUnfinishedProject().get(i).getDeadline().isBefore(LocalDateTime.now())) {
                reversedMap(user.getListOfUnfinishedProject().get(i)).setStyle("-fx-border-color: #9f5959; " + "-fx-border-width: 5px;");
            } else {
                reversedMap(user.getListOfUnfinishedProject().get(i)).setStyle("-fx-border-color: #7b8bac; " + "-fx-border-width: 5px;");
            }
        }
    }

    final Timeline timeline1 = new Timeline(
            new KeyFrame(
                    Duration.seconds(1),
                    event -> {
//                        long start = System.currentTimeMillis();
                        user.ifStarted();
                        moveProjectToDoing();
                        try {
                            user.ifStartedReverse();
                            moveProjectFromDoingToToDo();
                        } catch (ProjectException e) {
                            throw new RuntimeException(e);
                        }
                        checkIfTasksStartDateCame();
                        checkIfProjectIsBeforeDeadline();
//                        System.err.println("Finished after " + (System.currentTimeMillis() - start) + "ms");
                    }
            )
    );
}