package weaver;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        String date1 = "2023-01-15";
        String date2 = "2024-04-20";

        ArrayList<String> monthYearList = getMonthsAndYears(date1, date2);
        for (String monthYear : monthYearList) {
            System.out.println(monthYear);
        }
    }

    public static ArrayList<String> getMonthsAndYears(String date1, String date2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(date1, formatter);
        LocalDate endDate = LocalDate.parse(date2, formatter);

        ArrayList<String> monthYearList = new ArrayList<>();
        while (!startDate.isAfter(endDate)) {
            String monthYear = startDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            monthYearList.add(monthYear);
            startDate = startDate.plusMonths(1);
        }

        return monthYearList;
    }
}
