package kod_aplikacji;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
class GenerateReportTest {
    private User user;
    private LocalDateTime date;

    @BeforeEach
    void setUp() throws TaskException, ProjectException {
        date = LocalDateTime.now();
        user = new User();

        Project toDoProject = new Project("ToDo Project", false, date, date.plusDays(1), null, date.plusDays(10));
        Project unfinishedProject = new Project("Unfinished Project", false, date, date.plusDays(1), null, date.plusDays(5));
        Project finishedProject = new Project("Finished Project", true, date, date.plusDays(1), date.plusDays(5), date.plusDays(7));

        Task task1 = new Task("Task 1", false, date, date.plusDays(1),null,date.plusDays(4),"");
        Task task2 = new Task("Task 2", true, date.plusMinutes(2), date.plusDays(3),date.plusDays(3).plusMinutes(3),date.plusDays(4),"");
        unfinishedProject.addTask(task1);
        finishedProject.addTask(task2);

        user.addToDoProject(toDoProject);
        user.addUnfinishedProject(unfinishedProject);
        user.addFinishedProject(finishedProject);
    }

    @Test
    void generate() {
        GenerateReport reportGenerator = new GenerateReport(user);
        ArrayList<Float> report = reportGenerator.generate();

        assertEquals(1.0f, report.get(0)); // ToDo Projects
        assertEquals(1.0f, report.get(1)); // Unfinished Projects
        assertEquals(1.0f, report.get(2)); // Finished Projects
        assertEquals(1.0f, report.get(3)); // Unfinished Tasks
        assertEquals(1.0f, report.get(4)); // Completed Tasks
        assertEquals(1.0f, report.get(5));
        assertEquals(3.0f, report.get(6)); // Avg Completion Time of Tasks (task completed in 2 days)
    }
}