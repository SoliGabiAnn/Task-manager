package kod_aplikacji;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Project extends Basic_Info {
    @JsonProperty("listOfTask")
    protected ArrayList<Task> listOfTask = new ArrayList<Task>();
    @JsonProperty("isTaskSortingUpToDate")
    Boolean isTaskSortingUpToDate = false;

    @JsonCreator
    public Project(@JsonProperty("name") String name,@JsonProperty("state") Boolean state,@JsonProperty("date_added") LocalDateTime date_added,
                   @JsonProperty("date_start")LocalDateTime date_start,@JsonProperty("date_end") LocalDateTime date_end,@JsonProperty("deadline") LocalDateTime deadline) {
        super(name, state, date_added, date_start, date_end, deadline);
    }

//    public void addTask(String name, LocalDateTime date_added, LocalDateTime date_start, LocalDateTime deadline, String description) throws TaskException {
//        if (date_start.isAfter(this.date_start)) {
//            if (deadline.isBefore(this.deadline)) {
//                Boolean state = false;
//                LocalDateTime date_end = null;
//                listOfTask.add(new Task(name, state, date_added, date_start, date_end, deadline, description));
//                isTaskSortingUpToDate = false;
//            } else {
//                throw new TaskException("Task's due date cannot be after project's due date");
//            }
//        } else {
//            throw new TaskException("Task's start date cannot be before project's start date");
//        }
//    }

    public void addTask(Task task) throws TaskException {
        if (getDate_start().isBefore(task.getDate_start())) {
            if (getDeadline().isAfter(task.getDeadline())) {
                listOfTask.add(task);
                isTaskSortingUpToDate = false;
            } else {
                throw new TaskException("Task's due date cannot be after project's due date");
            }
        } else {
            throw new TaskException("Task's start date cannot be before project's start date");
        }
    }

    public void deleteTask(LocalDateTime date_added_to_del) {
        if (!listOfTask.isEmpty()) listOfTask.removeIf(Task -> Task.getDate_added().equals(date_added_to_del));
        isTaskSortingUpToDate = false;
        System.out.printf("Usunięte zadanie");
    }

    public ArrayList<Task> sortTask() {
        ArrayList<Task> tmpList = new ArrayList<Task>(listOfTask);
        ArrayList<Task> sortedList = new ArrayList<Task>();
        while (!tmpList.isEmpty()) {
            LocalDateTime date_min = tmpList.get(0).getDeadline();
            int min_index = 0;
            for (int i = 1; i < tmpList.size(); i++) {
                if (tmpList.get(i).getDeadline().isBefore(date_min)) {
                    date_min = tmpList.get(i).getDeadline();
                    min_index = i;
                }
            }
            sortedList.add(tmpList.get(min_index));
            tmpList.remove(min_index);
        }
        isTaskSortingUpToDate = true;
        return sortedList;
    }

    public Boolean checkIfTasksAreFinished() {
        if (!listOfTask.isEmpty()) {
            for (Task task : listOfTask) {
                if (!task.getState()) return false;
            }
            return true;
        }
        return true;
    }
    public void endProject(LocalDateTime end){
        this.setState(true);
        this.setDate_end(end);
    }

    public ArrayList<Task> getListOfTask() {
        return listOfTask;
    }

    public void setListOfTask(ArrayList<Task> listOfTask) {
        this.listOfTask = listOfTask;
    }
}
