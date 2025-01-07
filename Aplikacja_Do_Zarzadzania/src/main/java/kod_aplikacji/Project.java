package kod_aplikacji;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Project extends Basic_Info{
    protected ArrayList<Task> listOfTask = new ArrayList<Task>();
    Boolean isTaskSortingUpToDate = false;

    public Project(String name, Boolean state, LocalDateTime date_added, LocalDateTime date_start, LocalDateTime date_end, LocalDateTime deadline) {
        super(name, state, date_added, date_start, date_end, deadline);
    }
    public void addTask(String name,LocalDateTime date_added, LocalDateTime date_start, LocalDateTime deadline, String description) throws TaskException {
        if (date_start.isAfter(this.date_start)) {
            if (deadline.isBefore(this.deadline)) {
                Boolean state = false;
                LocalDateTime date_end = null;
                listOfTask.add(new Task(name, state, date_added, date_start, date_end, deadline, description));
                isTaskSortingUpToDate = false;
            } else {
                throw new TaskException("Task's due date cannot be after project's due date");
            }
        } else {
            throw new TaskException("Task's start date cannot be before project's start date");
        }
    }

    public void deleteTask(LocalDateTime date_added_to_del){
        if(!listOfTask.isEmpty()) listOfTask.removeIf(Task -> Task.getDate_added().equals(date_added_to_del));
        isTaskSortingUpToDate = false;
    }
    public ArrayList<Task> sortTask(){
        ArrayList<Task> tmpList=new ArrayList<Task>(listOfTask);
        ArrayList<Task>sortedList=new ArrayList<Task>();
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
        isTaskSortingUpToDate = true;
        return sortedList;
    }
    public Boolean checkIfTasksAreFinished(){
        if(!listOfTask.isEmpty()){
            for(Task task:listOfTask){
                if(!task.getState()) return false;
            }
            return true;
        }
        return true;
    }

    public ArrayList<Task> getListOfTask() {
        return listOfTask;
    }

    public void setListOfTask(ArrayList<Task> listOfTask) {
        this.listOfTask = listOfTask;
    }
}
