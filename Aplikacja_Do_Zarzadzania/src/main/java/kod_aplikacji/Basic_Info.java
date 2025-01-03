package kod_aplikacji;

import java.time.LocalDateTime;
import java.util.Date;

public abstract class Basic_Info {
    protected String name;
    protected Boolean state; // state false means project is underway, true that is finished
    protected LocalDateTime date_added;//for raport
    protected LocalDateTime date_start;//by user
    protected LocalDateTime date_end;//for raport
    protected LocalDateTime deadline;//by user

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
    public LocalDateTime getDeadline() {return deadline;}
    public LocalDateTime getDate_start() {return date_start;}
    public String getName() {return name;}
    public Boolean getState() {return state;}
    public LocalDateTime getDate_end() {return date_end;}

    public void setState(Boolean state) {this.state = state;}
    public void setDate_end(LocalDateTime date_end) {this.date_end = date_end;}
}
