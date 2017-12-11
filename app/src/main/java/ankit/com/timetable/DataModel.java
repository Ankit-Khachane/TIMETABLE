package ankit.com.timetable;

/**
 * Created by khach on 18-07-2017.
 */

public class DataModel {
    private String time;
    private String ro_no;
    private String sub;
    private String teacher;

    public DataModel() {
    }

    public DataModel(String time, String ro_no, String sub, String teacher, String s_type) {
        this.time = time;
        this.ro_no = ro_no;
        this.sub = sub;
        this.teacher = teacher;
        this.s_type = s_type;
    }

    public String getTime() {

        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRo_no() {
        return ro_no;
    }

    public void setRo_no(String ro_no) {
        this.ro_no = ro_no;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getS_type() {
        return s_type;
    }

    public void setS_type(String s_type) {
        this.s_type = s_type;
    }

    private String s_type;


}
