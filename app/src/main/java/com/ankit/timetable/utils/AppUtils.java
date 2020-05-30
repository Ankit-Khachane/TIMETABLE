package com.ankit.timetable.utils;

import com.ankit.timetable.local.Schedule;
import com.ankit.timetable.model.ScheduleData;
import com.ankit.timetable.utils.AppConstants.JsonTags;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AppUtils {

    public static String getScheduleByDay(String currentDay,
                                          Schedule schedule) {
        String timestamp = "";
        switch (currentDay) {
            case "MONDAY":
                timestamp = schedule.getMONDAY();
                break;
            case "TUESDAY":
                timestamp = schedule.getTUESDAY();
                break;
            case "WEDNESDAY":
                timestamp = schedule.getWEDNESDAY();
                break;
            case "THURSDAY":
                timestamp = schedule.getTHURSDAY();
                break;
            case "FRIDAY":
                timestamp = schedule.getFRIDAY();
                break;
        }
        return timestamp;
    }

    public static String getCurrentDay() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE",
                Locale.US);
        return dateFormat.format(date)
                .toUpperCase();
    }

    public static boolean isDayExist(String currentDay) {
        return currentDay.equals("MONDAY") || currentDay.equals("TUESDAY")
                || currentDay.equals("WEDNESDAY") || currentDay.equals("THURSDAY")
                || currentDay.equals("FRIDAY");
    }

    public static ScheduleData convertJsonToData(String stringJson) {
        ScheduleData data = new ScheduleData();

        JSONObject node = null;
        try {
            node = new JSONObject(stringJson);
            data.setTime(node.getString(JsonTags.TIME));
            data.setRoomNumber(node.getString(JsonTags.ROOM_NO));
            data.setTeacher(node.getString(JsonTags.TEACHER));
            data.setType(node.getString(JsonTags.TYPE));
            data.setSubject(node.getString(JsonTags.SUBJECT));
            return data;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }
}
