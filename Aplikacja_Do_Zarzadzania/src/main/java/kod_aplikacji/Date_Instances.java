package kod_aplikacji;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Date_Instances {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public LocalDateTime getNow() {
        return LocalDateTime.now();
    }
    public LocalDateTime getEnd() {
        return null;
    }
}
