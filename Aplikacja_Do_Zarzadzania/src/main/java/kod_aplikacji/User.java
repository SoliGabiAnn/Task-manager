package kod_aplikacji;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class User {
    String username;
    public ArrayList<Project> listOfProject=new ArrayList<Project>();
    Boolean isProjectSortingUptoDate=false;
    public void setUsername(String username) {
        this.username = username;
    }
    public void addProject(String name, LocalDateTime date_start, LocalDateTime deadline) {
        Boolean state = false;
        LocalDateTime date_added= new Date_Instances().getNow();
        LocalDateTime date_end= new Date_Instances().getEnd();
        listOfProject.add(new Project(name, state, date_added, date_start, date_end, deadline));
        isProjectSortingUptoDate=false;
    }
    public void deleteProject(LocalDateTime date_added_to_del){
        if(!listOfProject.isEmpty()) listOfProject.removeIf(Project -> Project.getDate_added().equals(date_added_to_del));
        isProjectSortingUptoDate=false;
    }
    public ArrayList<Project> sortProject(){
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
    public static void main(String args[]){
        User u = new User();
        u.setUsername("admin");
        u.addProject("projekt1", LocalDateTime.of(2024,3,5,16,5), LocalDateTime.of(2024,4,7,6,7));
        u.listOfProject.get(0).addTask("pranie",LocalDateTime.of(2024,3,5,16,5), LocalDateTime.of(2024,4,5,6,7),"blabla");
        u.listOfProject.get(0).addTask("składanie",LocalDateTime.of(2024,3,5,16,5), LocalDateTime.of(2024,4,6,7,4),"W kostkę");
        u.listOfProject.get(0).addTask("suszenie",LocalDateTime.of(2024,3,5,16,5), LocalDateTime.of(2024,4,5,9,54),"pralkosuszarką");

        u.addProject("projekt0", LocalDateTime.of(2024,3,5,16,5), LocalDateTime.of(2024,4,5,6,7));
        u.listOfProject.get(1).addTask("pranie",LocalDateTime.of(2024,3,5,16,5), LocalDateTime.of(2024,4,2,6,7),"blabla");
        u.listOfProject.get(1).addTask("składanie",LocalDateTime.of(2024,3,5,16,5), LocalDateTime.of(2024,4,4,7,4),"W kostkę");
        u.listOfProject.get(1).addTask("suszenie",LocalDateTime.of(2024,3,5,16,5), LocalDateTime.of(2024,4,3,9,54),"pralkosuszarką");
        ArrayList<Project> sortedProjectList=u.sortProject();
        ArrayList<Task> sortedTaskList0=u.listOfProject.get(0).sortTask();
        ArrayList<Task> sortedTaskList1=u.listOfProject.get(1).sortTask();
        u.listOfProject.get(1).deleteTask(u.listOfProject.get(1).listOfTask.get(0).date_added);
    }
}
