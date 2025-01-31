package kod_aplikacji;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class User {
    @JsonProperty("username")
    private String username;
    @JsonProperty("listOfToDoProject")
    protected ArrayList<Project> listOfToDoProject = new ArrayList<Project>();
    @JsonProperty("listOfUnfinishedProject")
    protected ArrayList<Project> listOfUnfinishedProject = new ArrayList<Project>();
    @JsonProperty("listOfFinishedProject")
    protected ArrayList<Project> listOfFinishedProject = new ArrayList<Project>();
    @JsonProperty("isProjectSortingUptoDate")
    private Boolean isProjectSortingUptoDate = false;

    public void setUsername(String username) {
        this.username = username;
    }

    public void addToDoProject(String name, LocalDateTime date_added, LocalDateTime date_start, LocalDateTime deadline) throws ProjectException {
        if (date_start.isBefore(deadline)) {
            Boolean state = false;
            LocalDateTime date_end = null;
            listOfToDoProject.add(new Project(name, state, date_added, date_start, date_end, deadline));
            isProjectSortingUptoDate = false;
        } else {
            throw new ProjectException("Start date cannot be after due date");
        }

    }

    public void addToDoProject(Project project) throws ProjectException {
        if (project.getDate_start().isBefore(project.getDeadline())) {
            listOfToDoProject.add(project);
            isProjectSortingUptoDate = false;
        } else {
            throw new ProjectException("Start date cannot be after due date");
        }

    }

    public void addUnfinishedProject(String name, LocalDateTime date_added, LocalDateTime date_start, LocalDateTime deadline) {
        Boolean state = false;
        LocalDateTime date_end = null;
        listOfUnfinishedProject.add(new Project(name, state, date_added, date_start, date_end, deadline));
        isProjectSortingUptoDate = false;
    }

    public void addUnfinishedProject(Project project) {
        listOfUnfinishedProject.add(project);
        isProjectSortingUptoDate = false;
    }

    public void addFinishedProject(Project project) {
        listOfFinishedProject.add(project);
        project.setState(true);
        isProjectSortingUptoDate = false;
    }

    public void deleteToDoProject(LocalDateTime date_added_to_del) {
        if (!listOfToDoProject.isEmpty())
            listOfToDoProject.removeIf(Project -> Project.getDate_added().equals(date_added_to_del));
        isProjectSortingUptoDate = false;
    }

    public void deleteUnfinishedProject(LocalDateTime date_added_to_del) {
        if (!listOfUnfinishedProject.isEmpty())
            listOfUnfinishedProject.removeIf(Project -> Project.getDate_added().equals(date_added_to_del));
        isProjectSortingUptoDate = false;
    }

    public void deleteFinishedProject(LocalDateTime date_added_to_del) {
        if (!listOfFinishedProject.isEmpty())
            listOfFinishedProject.removeIf(Project -> Project.getDate_added().equals(date_added_to_del));
        isProjectSortingUptoDate = false;
    }

    public ArrayList<Project> sortProject(ArrayList<Project> listOfProject) {
        ArrayList<Project> tmpList = new ArrayList<Project>(listOfProject);
        ArrayList<Project> sortedList = new ArrayList<Project>();
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
        isProjectSortingUptoDate = true;
        return sortedList;
    }

    //lista indeksów projektów, które będą zmieniały stan
    ArrayList<Integer> listOfIndexOfProjectToMove = new ArrayList<>();

    public ArrayList<Integer> getListOfIndexOfProjectToMove() {
        return this.listOfIndexOfProjectToMove;
    }

    public void clearListOfIndexOfProjectToMove() {
        listOfIndexOfProjectToMove.clear();
    }

    public void ifStarted() {
        if (!listOfToDoProject.isEmpty()) {
            for (int i = 0; i < listOfToDoProject.size(); i++) {
                if (listOfToDoProject.get(i).getDate_start().isBefore(LocalDateTime.now())) {
                    this.addUnfinishedProject(listOfToDoProject.get(i));
                    //this.deleteToDoProject(listOfToDoProject.get(i).getDate_added());
                    listOfIndexOfProjectToMove.add(i);
                }
            }
        }
    }

    //lista indeksów projektów, które będą zmieniały stan
    ArrayList<Integer> listOfIndexOfProjectToMoveToDo = new ArrayList<>();

    public ArrayList<Integer> getListOfIndexOfProjectToMoveToDo() {
        return this.listOfIndexOfProjectToMoveToDo;
    }

    public void clearListOfIndexOfProjectToMoveToDo() {
        listOfIndexOfProjectToMoveToDo.clear();
    }

    public void ifStartedReverse() throws ProjectException {
        if (!listOfUnfinishedProject.isEmpty()) {
            for (int i = 0; i < listOfUnfinishedProject.size(); i++) {
                if (listOfUnfinishedProject.get(i).getDate_start().isAfter(LocalDateTime.now())) {
                    this.addToDoProject(listOfUnfinishedProject.get(i));
                    //this.deleteToDoProject(listOfToDoProject.get(i).getDate_added());
                    listOfIndexOfProjectToMoveToDo.add(i);
                }
            }
        }
    }

    public void ifFinished() {
        if (!listOfUnfinishedProject.isEmpty()) {
            for (int i = 0; i < listOfUnfinishedProject.size(); i++) {//in controlls it is necessary ot change state
                if (listOfUnfinishedProject.get(i).getState() && listOfUnfinishedProject.get(i).checkIfTasksAreFinished()) {
                    this.addFinishedProject(listOfUnfinishedProject.get(i));
                    this.deleteUnfinishedProject(listOfUnfinishedProject.get(i).getDate_added());
                }
            }
        }
    }
    public void ifFinishedReverse() {
        if (!listOfFinishedProject.isEmpty()) {
            for (int i = 0; i < listOfFinishedProject.size(); i++) {//in controlls it is necessary ot change state
                if (!listOfFinishedProject.get(i).getState()) {
                    this.addUnfinishedProject(listOfFinishedProject.get(i));
                    this.deleteFinishedProject(listOfFinishedProject.get(i).getDate_added());
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

    public void setListOfToDoProject(ArrayList<Project> listOfToDoProject) {
        this.listOfToDoProject = listOfToDoProject;
    }

    public void setListOfUnfinishedProject(ArrayList<Project> listOfUnfinishedProject) {
        this.listOfUnfinishedProject = listOfUnfinishedProject;
    }

    public void setListOfFinishedProject(ArrayList<Project> listOfFinishedProject) {
        this.listOfFinishedProject = listOfFinishedProject;
    }


    public Boolean getProjectSortingUptoDate() {
        return isProjectSortingUptoDate;
    }

    public void setProjectSortingUptoDate(Boolean projectSortingUptoDate) {
        isProjectSortingUptoDate = projectSortingUptoDate;
    }


    public void setListOfIndexOfProjectToMove(ArrayList<Integer> listOfIndexOfProjectToMove) {
        this.listOfIndexOfProjectToMove = listOfIndexOfProjectToMove;
    }

    public String getUsername() {
        return username;
    }
}
