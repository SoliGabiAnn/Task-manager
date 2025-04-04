package kod_aplikacji;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class Task extends Basic_Info{
    @JsonProperty("description")
    String description;
    @JsonCreator
    public Task(@JsonProperty("name") String name,@JsonProperty("state") Boolean state,@JsonProperty("date_added") LocalDateTime date_added,
                @JsonProperty("date_start") LocalDateTime date_start,@JsonProperty("date_end") LocalDateTime date_end,@JsonProperty("deadline") LocalDateTime deadline,
                @JsonProperty("description") String description) {
        super(name, state, date_added, date_start, date_end, deadline);
        this.description = description;
    }
    public void endTask(LocalDateTime end){
        this.setState(true);
        this.setDate_end(end);
    }
    public void endTaskReversed(){
        this.setState(false);
        this.setDate_end(null);
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}