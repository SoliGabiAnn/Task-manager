package kod_aplikacji;

import java.time.LocalDateTime;
import java.util.Date;

public abstract class Basic_Info {
    String name;
    Boolean state; // state false means project is underway, true that is finished
    LocalDateTime date_added;//for raport
    LocalDateTime date_start;//by user
    LocalDateTime date_end;//for raport
    LocalDateTime deadline;//by user
    public Basic_Info(String name, Boolean state, LocalDateTime date_added, LocalDateTime date_start, LocalDateTime date_end, LocalDateTime deadline) {
        this.name = name;
        this.state = state;
        this.date_added = date_added;
        this.date_start = date_start;
        this.date_end = date_end;
        this.deadline = deadline;
    }

    public LocalDateTime getDate_added() {
        return date_added;
    }
}
