package kod_aplikacji;

import java.util.Date;

public abstract class Basic_Info {
    String name;
    Boolean state; // state false means project is underway, true that is finished
    Date date_added;
    Date date_start;
    Date date_end;
    Date deadline;
    public Basic_Info(String name, Boolean state, Date date_added, Date date_start, Date date_end, Date deadline) {
        this.name = name;
        this.state = state;
        this.date_added = date_added;
        this.date_start = date_start;
        this.date_end = date_end;
        this.deadline = deadline;
    }
}
