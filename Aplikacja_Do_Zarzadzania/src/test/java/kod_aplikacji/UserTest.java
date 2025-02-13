package kod_aplikacji;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
class UserTest {
    User user;
    Project project1;
    Project project2;
    LocalDateTime date=LocalDateTime.now();
    @BeforeEach
    void setUp() {
        user = new User();
        project1 = new Project("Project 1", false, date, date.plusDays(1), null, date.plusDays(5));
        project2 = new Project("Project 2", false, date, date.plusDays(2), null, date.plusDays(6));
    }

    @Test
    void addToDoProject() {
        try {
            user.addToDoProject(project1);
            assertTrue(user.getListOfToDoProject().contains(project1), "Project should be added to the To-Do list");

            // Test adding by parameters
            user.addToDoProject("Project 3", date, date.plusDays(3), date.plusDays(7));
            assertEquals(2, user.getListOfToDoProject().size(), "There should be 2 projects in the To-Do list");
        } catch (ProjectException e) {
            fail("ProjectException was thrown unexpectedly");
        }
    }

    @Test
    void addUnfinishedProject() {
        user.addUnfinishedProject(project1);
        assertTrue(user.getListOfUnfinishedProject().contains(project1), "Project should be added to the Unfinished list");
        user.addUnfinishedProject("Project 4", date, date.plusDays(4), date.plusDays(8));
        assertEquals(2, user.getListOfUnfinishedProject().size(), "There should be 1 project in the Unfinished list");
    }

    @Test
    void deleteToDoProject() throws ProjectException {
        user.addToDoProject(project1);
        user.deleteToDoProject(project1.getDate_added());
        assertFalse(user.getListOfToDoProject().contains(project1), "Project should be removed from the To-Do list");
    }

    @Test
    void deleteUnfinishedProject() {
        user.addUnfinishedProject(project1);
        user.deleteUnfinishedProject(project1.getDate_added());
        assertFalse(user.getListOfUnfinishedProject().contains(project1), "Project should be removed from the Unfinished list");
    }

    @Test
    void deleteFinishedProject() {
        user.addFinishedProject(project1);
        user.deleteFinishedProject(project1.getDate_added());
        assertFalse(user.getListOfFinishedProject().contains(project1), "Project should be removed from the Finished list");
    }

    @Test
    void sortProject() {
        user.addUnfinishedProject(project1);
        user.addUnfinishedProject(project2);

        var sortedList = user.sortProject(user.getListOfUnfinishedProject());
        assertEquals(project1, sortedList.get(0), "The project with the earlier deadline should be first");
        assertEquals(project2, sortedList.get(1), "The project with the later deadline should be second");
    }
}