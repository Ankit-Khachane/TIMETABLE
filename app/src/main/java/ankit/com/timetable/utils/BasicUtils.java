package ankit.com.timetable.utils;

import ankit.com.timetable.model.ScheduleTable;

public class BasicUtils {
    private static final String TAG = "BasicUtils";

    public static String getTableByCurrentDay(String currentDay, ScheduleTable scheduleTable) {
        String schedule_data = "";
        switch (currentDay) {
            case "MONDAY":
                schedule_data = scheduleTable.getMONDAY();
                break;
            case "TUESDAY":
                schedule_data = scheduleTable.getTUESDAY();
                break;
            case "WEDNESDAY":
                schedule_data = scheduleTable.getWEDNESDAY();
                break;
            case "THURSDAY":
                schedule_data = scheduleTable.getTHURSDAY();
                break;
            case "FRIDAY":
                schedule_data = scheduleTable.getFRIDAY();
                break;
        }
        return schedule_data;
    }

    public static boolean getDayBy(String currentDay) {
        return currentDay.equals("MONDAY")
                || currentDay.equals("TUESDAY")
                || currentDay.equals("WEDNESDAY")
                || currentDay.equals("THURSDAY")
                || currentDay.equals("FRIDAY");
    }
}
