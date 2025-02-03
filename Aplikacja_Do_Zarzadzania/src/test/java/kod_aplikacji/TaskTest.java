package kod_aplikacji;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    private Task task;
    private LocalDateTime date;

    @BeforeEach
    void setUp() {
        date = LocalDateTime.now();
        task = new Task("Test Task", false, date, date.plusDays(1), null, date.plusDays(5), "Test Description");
    }

    @Test
    void endTask() {
        task.endTask(date.plusDays(2));

        assertTrue(task.getState(), "Task state should be completed (true)");
        assertEquals(date.plusDays(2), task.getDate_end(), "Task end date should be updated correctly");
    }

    @Test
    void endTaskReversed() {
        task.endTask(date.plusDays(2)); // First, complete the task
        task.endTaskReversed(); // Then reverse completion

        assertFalse(task.getState(), "Task state should be reverted to not completed (false)");
        assertNull(task.getDate_end(), "Task end date should be null after reversing completion");
    }
}