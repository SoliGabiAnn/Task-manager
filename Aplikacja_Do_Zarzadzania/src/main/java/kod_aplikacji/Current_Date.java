package kod_aplikacji;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Current_Date {
    public static void main(String[] args) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println("Current Date and Time: " + dtf.format(now));
    }
}
