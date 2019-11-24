package ankit.com.timetable.model;

/**
 * Created by khach on 18-07-2017.
 */

public class DataModel {
    private String time;
    private String roomNumber;
    private String subject;
    private String type;
    private String professor;

    public DataModel() {
    }

    public DataModel(String time, String roomNumber, String subject, String professor, String type) {
        this.time = time;
        this.roomNumber = roomNumber;
        this.subject = subject;
        this.professor = professor;
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

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
