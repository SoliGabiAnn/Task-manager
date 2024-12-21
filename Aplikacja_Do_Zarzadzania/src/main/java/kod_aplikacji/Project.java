package kod_aplikacji;

import java.util.ArrayList;
import java.util.Date;

public class Project extends Basic_Info{
    ArrayList<Task> listOfTask = new ArrayList<Task>();
    public Project(String name, Boolean state, Date date_added, Date date_start, Date date_end, Date deadline) {
        super(name, state, date_added, date_start, date_end, deadline);
    }
    public void addTask(String name, Date date_added, Date date_end, Date deadline, String description){
        Boolean state = false;
        listOfTask.add(new Task(name, state, date_added, date_start, date_end, deadline, description));
    }
}
