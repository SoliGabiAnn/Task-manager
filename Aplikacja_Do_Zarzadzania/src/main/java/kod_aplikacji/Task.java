package kod_aplikacji;

import java.time.LocalDateTime;
import java.util.Date;

public class Task extends Basic_Info{
    String description;

    public Task(String name, Boolean state, LocalDateTime date_added, LocalDateTime date_start, LocalDateTime date_end, LocalDateTime deadline, String description) {
        super(name, state, date_added, date_start, date_end, deadline);
        this.description = description;
    }

}
