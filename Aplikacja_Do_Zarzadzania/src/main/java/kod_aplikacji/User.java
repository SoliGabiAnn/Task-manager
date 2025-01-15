package kod_aplikacji;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class User {
    String username;
    protected ArrayList<Project> listOfToDoProject=new ArrayList<Project>();
    protected ArrayList<Project> listOfUnfinishedProject=new ArrayList<Project>();
    protected ArrayList<Project> listOfFinishedProject=new ArrayList<Project>();
    Boolean isProjectSortingUptoDate=false;

    public void setUsername(String username) {
        this.username = username;
    }

    public void addToDoProject(String name,LocalDateTime date_added, LocalDateTime date_start, LocalDateTime deadline) throws ProjectException {
        if(date_start.isBefore(deadline)) {
            Boolean state = false;
            LocalDateTime date_end= null;
            listOfToDoProject.add(new Project(name, state, date_added, date_start, date_end, deadline));
            isProjectSortingUptoDate=false;
        }else{
            throw new ProjectException("Start date cannot be after due date");
        }

    }
    public void addToDoProject(Project project) throws ProjectException {
        if(project.getDate_start().isBefore(project.getDeadline())) {
            listOfToDoProject.add(project);
            isProjectSortingUptoDate=false;
        }else{
            throw new ProjectException("Start date cannot be after due date");
        }

    }
    public void addUnfinishedProject(String name,LocalDateTime date_added, LocalDateTime date_start, LocalDateTime deadline) {
        Boolean state = false;
        LocalDateTime date_end= null;
        listOfUnfinishedProject.add(new Project(name, state, date_added, date_start, date_end, deadline));
        isProjectSortingUptoDate=false;
    }
    public void addUnfinishedProject(Project project) {
        listOfUnfinishedProject.add(project);
        isProjectSortingUptoDate=false;
    }

    public void addFinishedProject(Project project) {
        listOfFinishedProject.add(project);
        isProjectSortingUptoDate=false;
    }
    public void deleteToDoProject(LocalDateTime date_added_to_del) {
        if(!listOfToDoProject.isEmpty()) listOfToDoProject.removeIf(Project -> Project.getDate_added().equals(date_added_to_del));
        isProjectSortingUptoDate=false;
    }
    public void deleteUnfinishedProject(LocalDateTime date_added_to_del) {
        if(!listOfUnfinishedProject.isEmpty()) listOfUnfinishedProject.removeIf(Project -> Project.getDate_added().equals(date_added_to_del));
        isProjectSortingUptoDate=false;
    }
    public void deleteFinishedProject(LocalDateTime date_added_to_del) {
        if(!listOfFinishedProject.isEmpty()) listOfFinishedProject.removeIf(Project -> Project.getDate_added().equals(date_added_to_del));
        isProjectSortingUptoDate=false;
    }
    public ArrayList<Project> sortProject(ArrayList<Project> listOfProject) {
        ArrayList<Project> tmpList=new ArrayList<Project>(listOfProject);
        ArrayList<Project>sortedList=new ArrayList<Project>();
        while(!tmpList.isEmpty()){
            LocalDateTime date_min=tmpList.get(0).getDeadline();
            int min_index=0;
            for(int i=1;i<tmpList.size();i++){
                if(tmpList.get(i).getDeadline().isBefore(date_min)){
                    date_min=tmpList.get(i).getDeadline();
                    min_index=i;
                }
            }
            sortedList.add(tmpList.get(min_index));
            tmpList.remove(min_index);
        }
        isProjectSortingUptoDate=true;
        return sortedList;
    }

    //lista indeksów projektów, które będą zmieniały stan
    ArrayList <Integer> listOfIndex=new ArrayList<>();

    public ArrayList<Integer> getListOfIndex(){
        return listOfIndex;
    }
    public void clearListOfToDoProject(){
        listOfIndex.clear();
    }

    public void ifStarted() {
        if(!listOfToDoProject.isEmpty()){
            for(int i=0;i<listOfToDoProject.size();i++){
                if(listOfToDoProject.get(i).getDate_start().isBefore(LocalDateTime.now())){
                    this.addUnfinishedProject(listOfToDoProject.get(i));
                    this.deleteToDoProject(listOfToDoProject.get(i).getDate_added());
                    listOfIndex.add(i);
                }
            }
        }
    }

    public void ifFinished(){
        if(!listOfUnfinishedProject.isEmpty()){
            for(int i=0;i<listOfUnfinishedProject.size();i++){//in controlls it is necessary ot change state
                if(listOfUnfinishedProject.get(i).getState() && listOfUnfinishedProject.get(i).checkIfTasksAreFinished()){
                    this.addFinishedProject(listOfUnfinishedProject.get(i));
                    this.deleteUnfinishedProject(listOfUnfinishedProject.get(i).getDate_added());
                }
            }
        }
    }
    //gettery i settery
    public ArrayList<Project> getListOfToDoProject() {
        return listOfToDoProject;
    }

    public ArrayList<Project> getListOfUnfinishedProject() {
        return listOfUnfinishedProject;
    }

    public ArrayList<Project> getListOfFinishedProject() {
        return listOfFinishedProject;
    }



//    public static void main(String args[]){
//        User u = new User();
//        u.setUsername("admin");
//        u.addToDoProject("projekt1", LocalDateTime.of(2025,3,5,16,5), LocalDateTime.of(2025,4,7,6,7));
//        u.getListOfToDoProject().get(0).addTask("pranie",LocalDateTime.of(2025,3,5,16,5), LocalDateTime.of(2025,4,5,6,7),"blabla");
//        u.getListOfToDoProject().get(0).addTask("składanie",LocalDateTime.of(2025,3,5,16,5), LocalDateTime.of(2025,4,6,7,4),"W kostkę");
//        u.getListOfToDoProject().get(0).addTask("suszenie",LocalDateTime.of(2025,3,5,16,5), LocalDateTime.of(2025,4,5,9,54),"pralkosuszarką");
//        try {
//            Thread.sleep(10);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        u.addToDoProject("projekt0", LocalDateTime.of(2025,3,5,16,5), LocalDateTime.of(2025,4,5,6,7));
//        u.getListOfToDoProject().get(1).addTask("pranie",LocalDateTime.of(2025,3,5,16,5), LocalDateTime.of(2025,4,2,6,7),"blabla");
//        u.getListOfToDoProject().get(1).addTask("składanie",LocalDateTime.of(2025,3,5,16,5), LocalDateTime.of(2025,4,4,7,4),"W kostkę");
//        u.getListOfToDoProject().get(1).addTask("suszenie",LocalDateTime.of(2025,3,5,16,5), LocalDateTime.of(2025,4,3,9,54),"pralkosuszarką");
//
//        try {
//            Thread.sleep(10);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//        u.addToDoProject("projekt3", LocalDateTime.of(2025,1,2,16,5), LocalDateTime.of(2025,4,7,6,7));
//        u.getListOfToDoProject().get(2).addTask("pranie",LocalDateTime.of(2025,1,2,16,5), LocalDateTime.of(2025,4,5,6,7),"blabla");
//
//        try {
//            Thread.sleep(10);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//        u.addToDoProject("projekt2", LocalDateTime.of(2025,1,1,16,5), LocalDateTime.of(2025,1,3,6,7));
//        u.getListOfToDoProject().get(3).addTask("pranie",LocalDateTime.of(2025,1,2,16,5), LocalDateTime.of(2025,1,3,6,7),"blabla");
//
//        u.ifStarted();
//        u.getListOfUnfinishedProject().getFirst().getListOfTask().getFirst().endTask();
//        u.getListOfUnfinishedProject().getFirst().setState(true);
//        u.ifFinished();
//
//        ArrayList<Project> sortedProjectList=u.sortProject(u.getListOfToDoProject());
//        ArrayList<Task> sortedTaskList0=u.getListOfToDoProject().get(0).sortTask();
//        ArrayList<Task> sortedTaskList1=u.getListOfToDoProject().get(1).sortTask();
//        u.getListOfToDoProject().get(1).deleteTask(u.getListOfToDoProject().get(1).listOfTask.get(0).date_added);
//    }
}
