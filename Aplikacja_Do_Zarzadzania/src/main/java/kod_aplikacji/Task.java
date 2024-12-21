package kod_aplikacji;

import java.util.Date;

public class Task extends Basic_Info{
    String description;

    public Task(String name, Boolean state, Date date_added, Date date_start, Date date_end, Date deadline, String description) {
        super(name, state, date_added, date_start, date_end, deadline);
        this.description = description;
    }

}
