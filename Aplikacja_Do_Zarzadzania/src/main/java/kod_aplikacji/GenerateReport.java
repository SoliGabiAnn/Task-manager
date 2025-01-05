package kod_aplikacji;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class GenerateReport extends User{
    public ArrayList<Float> generate(){
        ArrayList<Float> report = new ArrayList<>();
        report.add((float) listOfToDoProject.size());
        report.add((float)listOfUnfinishedProject.size());
        report.add((float)listOfFinishedProject.size());
        report.add(counterUnfinishedTasks(listOfToDoProject)+counterUnfinishedTasks(listOfUnfinishedProject));
        report.add(counterDoneTasks(listOfFinishedProject)+counterDoneTasks(listOfUnfinishedProject));
        report.add(averageFreqOfTasks());
        report.add(averageTimeOfCompletingTasks());
        return report;
    }

    private Float averageFreqOfTasks(){
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
    private LocalDateTime earliestDate(ArrayList<Project> listOfProject){
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
    private LocalDateTime latestDate(ArrayList<Project> listOfProject){
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
    private float counterTasks(ArrayList<Project> listOfProject) {
        int count = 0;
        for (Project project : listOfProject) {
            count += project.getListOfTask().size();
        }
        return count;
    }
    private float counterDoneTasks(ArrayList<Project> listOfProject){
        int count=0;
        for (Project project : listOfProject) {
            for(int i=0;i<project.getListOfTask().size();i++){
                if(project.getListOfTask().get(i).getState()){
                    count += 1;
                }
            }
        }
        return count;
    }
    private float counterUnfinishedTasks(ArrayList<Project> listOfProject){
        int count=0;
        for (Project project : listOfProject) {
            for(int i=0;i<project.getListOfTask().size();i++){
                if(!project.getListOfTask().get(i).getState()){
                    count += 1;
                }
            }
        }
        return count;
    }
    private Float averageTimeOfCompletingTasks(){
        long sum=0;
        for (Project project : listOfFinishedProject) {
            for (int j = 0; j < project.getListOfTask().size(); j++) {
                sum += ChronoUnit.DAYS.between(project.getListOfTask().get(j).getDate_start(), project.getListOfTask().get(j).getDate_end());
            }
        }
        return (float) sum/counterTasks(listOfFinishedProject);
    }

}
