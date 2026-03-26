package taskmanager.util;

import taskmanager.model.Project;
import taskmanager.model.User;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Locale;

public class GenerateReport {
    private User user;

    public GenerateReport(User user) {
        this.user = user;
    }

    public ArrayList<Float> generate() {
        ArrayList<Float> report = new ArrayList<>();
        report.add((float) user.getListOfToDoProject().size());
        report.add((float) user.getListOfUnfinishedProject().size());
        report.add((float) user.getListOfFinishedProject().size());
        report.add(counterUnfinishedTasks(user.getListOfToDoProject()) + counterUnfinishedTasks(user.getListOfUnfinishedProject()));
        report.add(counterDoneTasks(user.getListOfToDoProject())+counterDoneTasks(user.getListOfFinishedProject()) + counterDoneTasks(user.getListOfUnfinishedProject()));
        report.add(averageFreqOfTasks());
        report.add(averageTimeOfCompletingTasks());
        return report;
    }

    private Float averageFreqOfTasks() {
        var first = earliestOfAll();
        var last = lastOfAll();
        if (first != null && last != null) {
            long timeframe = ChronoUnit.MINUTES.between(first, last);
            float totalCount = counterTasks(user.getListOfUnfinishedProject()) + counterTasks(user.getListOfFinishedProject()) + counterTasks(user.getListOfToDoProject());
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
            DecimalFormat decimalFormat = new DecimalFormat("#.####",symbols);
            float result = totalCount / timeframe;
            return Float.parseFloat(decimalFormat.format(result));
        }
        return (float) 0;
    }

    public LocalDateTime earliestDateOfList(ArrayList<Project> listOfProject) {
        LocalDateTime first = LocalDateTime.now();
        for (Project project : listOfProject) {
            for (int j = 0; j < project.getListOfTask().size(); j++) {
                if (project.getListOfTask().get(j).getDate_added().isBefore(first)) {
                    first = project.getListOfTask().get(j).getDate_added();
                }
            }
        }
        return first;

    }

    public LocalDateTime latestDateOfList(ArrayList<Project> listOfProject) {
        if (!listOfProject.isEmpty()) {
            LocalDateTime last = listOfProject.getFirst().getDate_added();
            for (Project project : listOfProject) {
                for (int j = 0; j < project.getListOfTask().size(); j++) {
                    if (project.getListOfTask().get(j).getDate_added().isAfter(last)) {
                        last = project.getListOfTask().get(j).getDate_added();
                    }
                }
            }
            return last;
        }
        return null;
    }

    private LocalDateTime earliestOfAll() {//if all lists are empty null is returned
        LocalDateTime first = LocalDateTime.now();
        if (!user.getListOfToDoProject().isEmpty() && !user.getListOfUnfinishedProject().isEmpty()) {
            if (earliestDateOfList(user.getListOfToDoProject()).isBefore(earliestDateOfList(user.getListOfUnfinishedProject()))) {
                first = earliestDateOfList(user.getListOfToDoProject());
            } else {
                first = earliestDateOfList(user.getListOfUnfinishedProject());
            }
        } else if (!user.getListOfToDoProject().isEmpty()) {
            first = earliestDateOfList(user.getListOfToDoProject());
        } else if (!user.getListOfUnfinishedProject().isEmpty()) {
            first = earliestDateOfList(user.getListOfUnfinishedProject());
        }
        if (first != null && !user.getListOfFinishedProject().isEmpty()) {
            if (first.isAfter(earliestDateOfList(user.getListOfFinishedProject()))) {
                first = earliestDateOfList(user.getListOfFinishedProject());
            }
        } else if (!user.getListOfFinishedProject().isEmpty()) {
            first = earliestDateOfList(user.getListOfFinishedProject());
        }
        return first;
    }


    private LocalDateTime lastOfAll() {//if all lists are empty null is returned
        LocalDateTime last = null;
        if (!user.getListOfToDoProject().isEmpty() && !user.getListOfUnfinishedProject().isEmpty()) {
            if (latestDateOfList(user.getListOfToDoProject()).isAfter(latestDateOfList(user.getListOfUnfinishedProject()))) {
                last = latestDateOfList(user.getListOfToDoProject());
            } else {
                last = latestDateOfList(user.getListOfUnfinishedProject());
            }
        } else if (!user.getListOfToDoProject().isEmpty()) {
            last = latestDateOfList(user.getListOfToDoProject());
        } else if (!user.getListOfUnfinishedProject().isEmpty()) {
            last = latestDateOfList(user.getListOfUnfinishedProject());
        }
        if (last != null && !user.getListOfFinishedProject().isEmpty()) {
            if (last.isBefore(latestDateOfList(user.getListOfFinishedProject()))) {
                last = latestDateOfList(user.getListOfFinishedProject());
            }
        } else if (!user.getListOfFinishedProject().isEmpty()) {
            last = latestDateOfList(user.getListOfFinishedProject());
        }

        return last;
    }

    private float counterTasks(ArrayList<Project> listOfProject) {
        int count = 0;
        for (Project project : listOfProject) {
            count += project.getListOfTask().size();
        }
        return count;
    }

    private float counterDoneTasks(ArrayList<Project> listOfProject) {
        int count = 0;
        for (Project project : listOfProject) {
            for (int i = 0; i < project.getListOfTask().size(); i++) {
                if (project.getListOfTask().get(i).getState()) {
                    count += 1;
                }
            }
        }
        return count;
    }

    private float counterUnfinishedTasks(ArrayList<Project> listOfProject) {
        int count = 0;
        for (Project project : listOfProject) {
            for (int i = 0; i < project.getListOfTask().size(); i++) {
                if (!project.getListOfTask().get(i).getState()) {
                    count += 1;
                }
            }
        }
        return count;
    }

    private Float averageTimeOfCompletingTasks() {
        long sum = 0;
        for (Project project : user.getListOfFinishedProject()) {
            for (int j = 0; j < project.getListOfTask().size(); j++) {
                sum += ChronoUnit.MINUTES.between(project.getListOfTask().get(j).getDate_start(), project.getListOfTask().get(j).getDate_end());
            }
        }
        return (float) sum / counterTasks(user.getListOfFinishedProject());
    }

}
