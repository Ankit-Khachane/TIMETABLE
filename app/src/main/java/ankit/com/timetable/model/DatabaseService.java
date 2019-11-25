package ankit.com.timetable.model;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by khach on 12-12-2017.
 */
@Table(name = "scheduleTable")
public class DatabaseService extends Model {
    @Column(name = "seq")
    private int SEQ;
    @Column(name = "monday")
    private String MONDAY;
    @Column(name = "tuesday")
    private String TUESDAY;
    @Column(name = "wednesday")
    private String WEDNESDAY;
    @Column(name = "thursday")
    private String THURSDAY;
    @Column(name = "friday")
    private String FRIDAY;

    public DatabaseService() {
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
            this.FRIDAY = FRIDAY;
        }
    }

    public List<DatabaseService> getDaySchedule(String day_selected) {
        List<DatabaseService> col = null;
        String raw;
        if (day_selected.equals("MONDAY")) {
            col = new Select("Id,monday").from(DatabaseService.class).orderBy("Id ASC").execute();
            Log.i("DatabaseService", "getDaySchedule: column Monday");
        }
        if (day_selected.equals("TUESDAY")) {
            col = new Select("Id,tuesday").from(DatabaseService.class).orderBy("Id ASC").execute();
            Log.i("DatabaseService", "getDaySchedule: column Tuesday");
        }
        if (day_selected.equals("WEDNESDAY")) {
            col = new Select("Id,wednesday").from(DatabaseService.class).orderBy("Id ASC").execute();
            Log.i("DatabaseService", "getDaySchedule: column Wednesday");
        }
        if (day_selected.equals("THURSDAY")) {
            col = new Select("Id,thursday").from(DatabaseService.class).orderBy("Id ASC").execute();
            Log.i("DatabaseService", "getDaySchedule: column Thursday");
        }
        if (day_selected.equals("FRIDAY")) {
            col = new Select("Id,friday").from(DatabaseService.class).orderBy("Id ASC").execute();
            Log.i("DatabaseService", "getDaySchedule: column Friday");
        }
        return col;
    }

}
