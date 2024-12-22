package kod_aplikacji;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Project extends Basic_Info{
    public ArrayList<Task> listOfTask = new ArrayList<Task>();
    public Project(String name, Boolean state, LocalDateTime date_added, LocalDateTime date_start, LocalDateTime date_end, LocalDateTime deadline) {
        super(name, state, date_added, date_start, date_end, deadline);
    }
    public void addTask(String name, LocalDateTime date_start, LocalDateTime deadline, String description){
        Boolean state = false;
        LocalDateTime date_added= new Date_Instances().getNow();
        LocalDateTime date_end= new Date_Instances().getEnd();
        listOfTask.add(new Task(name, state, date_added, date_start, date_end, deadline, description));
    }
    public void deleteTask(LocalDateTime date_added_to_del){
        if(!listOfTask.isEmpty()) listOfTask.removeIf(Task -> Task.getDate_added().equals(date_added_to_del));
    }
    public Task sortTask(){
        return null;
    }
}
