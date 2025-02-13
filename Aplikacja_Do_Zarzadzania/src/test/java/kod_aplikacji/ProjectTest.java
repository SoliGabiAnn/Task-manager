package kod_aplikacji;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;



class ProjectTest {

    private Project project;
    private Task task1;
    private Task task2;
    private final LocalDateTime date=LocalDateTime.now();

    @BeforeEach
    void setUp() {
        // Setting up mock tasks and project
        project = new Project("Project A", false, date, date, date.plusDays(10), date.plusDays(15));
        task1 = new Task("Task 1", false, date, date.plusDays(1), null, date.plusDays(5), "Description of Task 1");
        task2 = new Task("Task 2", false, date, date.plusDays(3), null, date.plusDays(8), "Description of Task 2");
    }

    @Test
    void addTask() {
        try {
            project.addTask(task1);
            assertTrue(project.getListOfTask().contains(task1), "Task should be added to the list");
        } catch (TaskException e) {
            fail("TaskException was thrown unexpectedly");
        }
    }

    @Test
    void deleteTask() {
        try {
            project.addTask(task1);
            project.deleteTask(task1.getDate_added());
            assertFalse(project.getListOfTask().contains(task1), "Task should be removed from the list");
        } catch (TaskException e) {
            fail("TaskException was thrown unexpectedly");
        }
    }

    @Test
    void sortTask() {
        try {
            project.addTask(task1);
            project.addTask(task2);

            // Sort tasks by deadline
            var sortedList = project.sortTask();

            assertEquals(task1, sortedList.get(0), "The task with the earlier deadline should be first");
            assertEquals(task2, sortedList.get(1), "The task with the later deadline should be second");

        } catch (TaskException e) {
            fail("TaskException was thrown unexpectedly");
        }
    }

    @Test
    void checkIfTasksAreFinished() {
        try {
            task1.setState(true);  // mark task1 as finished
            task2.setState(true);  // mark task2 as finished

            project.addTask(task1);
            project.addTask(task2);

            assertTrue(project.checkIfTasksAreFinished(), "All tasks should be finished");

            // Now make one task unfinished
            task2.setState(false);
            assertFalse(project.checkIfTasksAreFinished(), "Not all tasks are finished");
        } catch (TaskException e) {
            fail("TaskException was thrown unexpectedly");
        }
    }

    @Test
    void endProject() {
        LocalDateTime endDate = date.plusDays(5);
        project.endProject(endDate);

        assertTrue(project.getState(), "Project state should be true (finished)");
        assertEquals(endDate, project.getDate_end(), "Project end date should be set correctly");
    }

    @Test
    void endProjectReversed() {
        project.endProjectReversed();

        assertFalse(project.getState(), "Project state should be false (not finished)");
        assertNull(project.getDate_end(), "Project end date should be null");
    }
}
