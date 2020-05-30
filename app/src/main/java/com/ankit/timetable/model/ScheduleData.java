package com.ankit.timetable.model;

/**
 * Created by khach on 18-07-2017.
 */

public class ScheduleData {

    private String time;
    private String roomNumber;
    private String subject;
    private String type;
    private String teacher;

    public ScheduleData() {
    }

    public ScheduleData(String time,
                        String roomNumber,
                        String subject,
                        String teacher,
                        String type) {
        this.time = time;
        this.roomNumber = roomNumber;
        this.subject = subject;
        this.teacher = teacher;
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
