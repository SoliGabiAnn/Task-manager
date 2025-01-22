package kod_aplikacji;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

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

    @JsonProperty("date_added")
    public LocalDateTime getDate_added() {
        return date_added;
    }
    @JsonProperty("deadline")
    public LocalDateTime getDeadline() {return deadline;}
    @JsonProperty("date_start")
    public LocalDateTime getDate_start() {return date_start;}
    @JsonProperty("name")
    public String getName() {return name;}
    @JsonProperty("state")
    public Boolean getState() {return state;}
    @JsonProperty("date_end")
    public LocalDateTime getDate_end() {return date_end;}

    public void setState(Boolean state) {this.state = state;}
    public void setDate_end(LocalDateTime date_end) {this.date_end = date_end;}
    public void setDateStart(LocalDateTime date_start) {this.date_start = date_start;}
}
