package kod_aplikacji;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class GenerateReport {
    private User user;

    public GenerateReport(User user) {
        this.user = user;
    }

    public ArrayList<Float> generate() {
        ArrayList<Float> report = new ArrayList<>();
        report.add((float) user.listOfToDoProject.size());
        report.add((float) user.listOfUnfinishedProject.size());
        report.add((float) user.listOfFinishedProject.size());
        report.add(counterUnfinishedTasks(user.listOfToDoProject) + counterUnfinishedTasks(user.listOfUnfinishedProject));
        report.add(counterDoneTasks(user.listOfFinishedProject) + counterDoneTasks(user.listOfUnfinishedProject));
        report.add(averageFreqOfTasks());
        report.add(averageTimeOfCompletingTasks());
        return report;
    }

    private Float averageFreqOfTasks() {
        var first = earliestOfAll();
        var last = lastOfAll();
        if (first != null && last != null) {
            long timeframe = ChronoUnit.MINUTES.between(first, last);
            float totalCount = counterTasks(user.listOfUnfinishedProject) + counterTasks(user.listOfFinishedProject) + counterTasks(user.listOfToDoProject);
            DecimalFormat decimalFormat = new DecimalFormat("#.####");
            float result = totalCount / timeframe;
            return Float.parseFloat(decimalFormat.format(result));
        }
        return (float) 0;
    }

    private LocalDateTime earliestDateOfList(ArrayList<Project> listOfProject) {
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

    private LocalDateTime latestDateOfList(ArrayList<Project> listOfProject) {
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
        if (!user.listOfToDoProject.isEmpty() && !user.listOfUnfinishedProject.isEmpty()) {
            if (earliestDateOfList(user.listOfToDoProject).isBefore(earliestDateOfList(user.listOfUnfinishedProject))) {
                first = earliestDateOfList(user.listOfToDoProject);
            } else {
                first = earliestDateOfList(user.listOfUnfinishedProject);
            }
        } else if (!user.listOfToDoProject.isEmpty()) {
            first = earliestDateOfList(user.listOfToDoProject);
        } else if (!user.listOfUnfinishedProject.isEmpty()) {
            first = earliestDateOfList(user.listOfUnfinishedProject);
        }
        if (first != null && !user.listOfFinishedProject.isEmpty()) {
            if (first.isAfter(earliestDateOfList(user.listOfFinishedProject))) {
                first = earliestDateOfList(user.listOfFinishedProject);
            }
        } else if (!user.listOfFinishedProject.isEmpty()) {
            first = earliestDateOfList(user.listOfFinishedProject);
        }
        return first;
    }


    private LocalDateTime lastOfAll() {//if all lists are empty null is returned
        LocalDateTime last = null;
        if (!user.listOfToDoProject.isEmpty() && !user.listOfUnfinishedProject.isEmpty()) {
            if (latestDateOfList(user.listOfToDoProject).isAfter(latestDateOfList(user.listOfUnfinishedProject))) {
                last = latestDateOfList(user.listOfToDoProject);
            } else {
                last = latestDateOfList(user.listOfUnfinishedProject);
            }
        } else if (!user.listOfToDoProject.isEmpty()) {
            last = latestDateOfList(user.listOfToDoProject);
        } else if (!user.listOfUnfinishedProject.isEmpty()) {
            last = latestDateOfList(user.listOfUnfinishedProject);
        }
        if (last != null && !user.listOfFinishedProject.isEmpty()) {
            if (last.isBefore(latestDateOfList(user.listOfFinishedProject))) {
                last = latestDateOfList(user.listOfFinishedProject);
            }
        } else if (!user.listOfFinishedProject.isEmpty()) {
            last = latestDateOfList(user.listOfFinishedProject);
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
        for (Project project : user.listOfFinishedProject) {
            for (int j = 0; j < project.getListOfTask().size(); j++) {
                sum += ChronoUnit.MINUTES.between(project.getListOfTask().get(j).getDate_start(), project.getListOfTask().get(j).getDate_end());
            }
        }
        return (float) sum / counterTasks(user.listOfFinishedProject);
    }

}
