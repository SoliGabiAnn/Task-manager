package kod_aplikacji;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class User {
    String username;
    public ArrayList<Project> listOfProject=new ArrayList<Project>();
    public void setUsername(String username) {
        this.username = username;
    }
    public void addProject(String name, LocalDateTime date_start, LocalDateTime deadline) {
        Boolean state = false;
        LocalDateTime date_added= new Date_Instances().getNow();
        LocalDateTime date_end= new Date_Instances().getEnd();
        listOfProject.add(new Project(name, state, date_added, date_start, date_end, deadline));
    }
    public void deleteProject(LocalDateTime date_added_to_del){
        if(!listOfProject.isEmpty()) listOfProject.removeIf(Project -> Project.getDate_added().equals(date_added_to_del));
    }
    public Project sortProject(){
        return null;
    }
    public static void main(String args[]){
        User u = new User();
        u.setUsername("admin");
        u.addProject("projekt1", LocalDateTime.of(2024,3,5,16,5), LocalDateTime.of(2024,4,5,6,7));
        u.listOfProject.get(0).addTask("pranie",LocalDateTime.of(2024,3,5,16,5), LocalDateTime.of(2024,4,5,6,7),"blabla");
    }
}
