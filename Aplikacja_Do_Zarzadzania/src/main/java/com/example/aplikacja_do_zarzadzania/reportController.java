package com.example.aplikacja_do_zarzadzania;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import kod_aplikacji.*;

import java.util.ArrayList;

public class reportController {

    @FXML
    private Label projectToDoLabel;
    @FXML
    private Label projectDoingLabel;
    @FXML
    private Label projectDoneLabel;
    @FXML
    private Label tasksToDoLabel;
    @FXML
    private Label tasksDoneLabel;
    @FXML
    private Label freqOfAddingTasksLabel;
    @FXML
    private Label averageTimeOfDoingTasksLabel;
    private GenerateReport generateReport;
    private HelloController helloController;

    @FXML
    public void fillReport() {
        ArrayList<Float> report = generateReport.generate();
        projectToDoLabel.setText(String.valueOf( report.get(0)));
        projectDoingLabel.setText(String.valueOf(report.get(1)));
        projectDoneLabel.setText(String.valueOf(report.get(2)));
        tasksToDoLabel.setText(String.valueOf(report.get(3)));
        tasksDoneLabel.setText(String.valueOf(report.get(4)));
        freqOfAddingTasksLabel.setText(String.valueOf(report.get(5)));
        averageTimeOfDoingTasksLabel.setText(String.valueOf(report.get(6)));

    }

    public void setGenerateReport(User user) {
        generateReport = new GenerateReport(user);
    }

}
