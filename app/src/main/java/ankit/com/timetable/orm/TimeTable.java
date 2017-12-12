package ankit.com.timetable.orm;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by khach on 12-12-2017.
 */
@Table(name = "ankittb")
public class TimeTable extends Model {
    @Column(name = "seq")
    public int SEQ;
    @Column(name = "monday")
    public String MONDAY;
    @Column(name = "tuesday")
    public String TUESDAY;
    @Column(name = "wednesday")
    public String WEDNESDAY;
    @Column(name = "thursday")
    public String THURSDAY;
    @Column(name = "friday")
    public String FRIDAY;

    public TimeTable() {
        super();
    }

    public int getSEQ() {
        return SEQ;
    }

    public void setSEQ(int SEQ) {
        this.SEQ = SEQ;
    }

    public String getMONDAY() {
        return MONDAY;
    }

    public void setMONDAY(String MONDAY) {
        if (!MONDAY.equals("")) {
            this.MONDAY = MONDAY;
        }
    }

    public String getTUESDAY() {
        return TUESDAY;
    }

    public void setTUESDAY(String TUESDAY) {
        if (!TUESDAY.equals("")) {
            this.TUESDAY = TUESDAY;
        }
    }

    public String getWEDNESDAY() {
        return WEDNESDAY;
    }

    public void setWEDNESDAY(String WEDNESDAY) {
        if (!WEDNESDAY.equals("")) {
            this.WEDNESDAY = WEDNESDAY;
        }
    }

    public String getTHURSDAY() {
        return THURSDAY;
    }

    public void setTHURSDAY(String THURSDAY) {
        if (!THURSDAY.equals("")) {
            this.THURSDAY = THURSDAY;
        }
    }

    public String getFRIDAY() {
        return FRIDAY;
    }

    public void setFRIDAY(String FRIDAY) {
        if (!FRIDAY.equals("")) {
        }
        this.FRIDAY = FRIDAY;
    }
}
