package kod_aplikacji;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class generateReport extends User{
    public ArrayList<Float> generate(){
        ArrayList<Float> report = new ArrayList<>();
        report.add((float) listOfToDoProject.size());
        report.add(counterTasks(listOfToDoProject));
        report.add((float)listOfUnfinishedProject.size());
        report.add(counterTasks(listOfUnfinishedProject));
        report.add((float)listOfFinishedProject.size());
        report.add(counterTasks(listOfFinishedProject));
        report.add(averageFreqOfTasks());
        report.add(averageTimeOfCompletingTasks());
        return report;
    }

    public Float averageFreqOfTasks(){
        LocalDateTime first;
        LocalDateTime last;
        if(earliestDate(listOfToDoProject).isBefore(earliestDate(listOfUnfinishedProject))){
            first=earliestDate(listOfToDoProject);
        } else {
            first=earliestDate(listOfUnfinishedProject);
        }
        if(first.isAfter(earliestDate(listOfFinishedProject))){
            first=earliestDate(listOfFinishedProject);
        }
        if(latestDate(listOfToDoProject).isAfter(latestDate(listOfUnfinishedProject))){
            last=latestDate(listOfToDoProject);
        } else {
            last=earliestDate(listOfUnfinishedProject);
        }
        if(last.isBefore(earliestDate(listOfFinishedProject))){
            last=earliestDate(listOfFinishedProject);
        }
        long timeframe=ChronoUnit.DAYS.between(first, last);
        float totalCount=counterTasks(listOfUnfinishedProject)+counterTasks(listOfFinishedProject)+counterTasks(listOfToDoProject);
        return (totalCount / timeframe);
    }
    public LocalDateTime earliestDate(ArrayList<Project> listOfProject){
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
    public LocalDateTime latestDate(ArrayList<Project> listOfProject){
        LocalDateTime last = listOfProject.getFirst().getListOfTask().getFirst().getDate_added();
        for (Project project : listOfProject) {
            for (int j = 0; j < project.getListOfTask().size(); j++) {
                if (project.getListOfTask().get(j).getDate_added().isAfter(last)) {
                    last = project.getListOfTask().get(j).getDate_added();
                }
            }
        }
        return last;
    }
    public float counterTasks(ArrayList<Project> listOfProject){
        int count=0;
        for (Project project : listOfProject) {
            count += project.getListOfTask().size();
        }
        return count;
    }
    public Float averageTimeOfCompletingTasks(){
        long sum=0;
        for (Project project : listOfFinishedProject) {
            for (int j = 0; j < project.getListOfTask().size(); j++) {
                sum += ChronoUnit.DAYS.between(project.getListOfTask().get(j).getDate_start(), project.getListOfTask().get(j).getDate_end());
            }
        }
        return (float) sum/counterTasks(listOfFinishedProject);
    }

}
