package com.example.aplikacja_do_zarzadzania;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

public class DateTimeFormat {
    public LocalDateTime toLocalDateTime(DatePicker datePicker, TextField time) {
        if(time.getText().matches("\\d{2} : \\d{2}")){
            Scanner scanner = new Scanner(time.getText());
            int hour = scanner.nextInt();
            scanner.next();
            int minute = scanner.nextInt();
            if(0<=hour && hour<24 &&0<=minute && minute<60 ){
                LocalDate date = datePicker.getValue();
                return date.atTime(hour, minute);
            }
        }
        return null;
    }

    public String toTextField(LocalDateTime localDateTime) {
        int hour = localDateTime.getHour();
        int minute = localDateTime.getMinute();
        return String.format("%02d : %02d", hour, minute);
    }

    public LocalDate toDatePicker(LocalDateTime localDateTime) {
        LocalDate localDate = localDateTime.toLocalDate();
        return localDate;
    }
}
