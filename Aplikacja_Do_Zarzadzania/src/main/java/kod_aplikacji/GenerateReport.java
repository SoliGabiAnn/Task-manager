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
        var first=earliestOfAll();
        var last=lastOfAll();
        if(first!=null && last!=null){
            long timeframe=ChronoUnit.DAYS.between(first, last);
            float totalCount=counterTasks(listOfUnfinishedProject)+counterTasks(listOfFinishedProject)+counterTasks(listOfToDoProject);
            return (totalCount / timeframe);
        }
        return (float) 0;
    }
    private LocalDateTime earliestDateOfList(ArrayList<Project> listOfProject){
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
    private LocalDateTime latestDateOfList(ArrayList<Project> listOfProject){
        if(!listOfProject.isEmpty()){
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
        return null;
    }
    private LocalDateTime earliestOfAll(){//if all lists are empty null is returned
        LocalDateTime first=LocalDateTime.now();
        if(!listOfToDoProject.isEmpty() && !listOfUnfinishedProject.isEmpty()){
            if(earliestDateOfList(listOfToDoProject).isBefore(earliestDateOfList(listOfUnfinishedProject))){
                first= earliestDateOfList(listOfToDoProject);
            } else {
                first= earliestDateOfList(listOfUnfinishedProject);
            }
        } else if (!listOfToDoProject.isEmpty() ) {
            first= earliestDateOfList(listOfToDoProject);
        } else if (!listOfUnfinishedProject.isEmpty()) {
            first=earliestDateOfList(listOfUnfinishedProject);
        }
        if(first!=null && !listOfFinishedProject.isEmpty()){
            if(first.isAfter(earliestDateOfList(listOfFinishedProject))){
                first= earliestDateOfList(listOfFinishedProject);
            }
            } else if (!listOfFinishedProject.isEmpty()) {
                first=earliestDateOfList(listOfFinishedProject);
            }
        return first;
    }




    private LocalDateTime lastOfAll(){//if all lists are empty null is returned
        LocalDateTime last=null;
        if(!listOfToDoProject.isEmpty() && !listOfUnfinishedProject.isEmpty()){
            if(latestDateOfList(listOfToDoProject).isAfter(latestDateOfList(listOfUnfinishedProject))){
                last= latestDateOfList(listOfToDoProject);
            } else {
                last = earliestDateOfList(listOfUnfinishedProject);
            }
        } else if (!listOfToDoProject.isEmpty() ) {
            last= latestDateOfList(listOfToDoProject);
        } else if (!listOfUnfinishedProject.isEmpty()) {
            last=latestDateOfList(listOfUnfinishedProject);
        }
        if(last!=null && !listOfFinishedProject.isEmpty()){
            if(last.isBefore(earliestDateOfList(listOfFinishedProject))){
                last= latestDateOfList(listOfFinishedProject);
        } else if (!listOfFinishedProject.isEmpty()) {
                last=latestDateOfList(listOfFinishedProject);
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
