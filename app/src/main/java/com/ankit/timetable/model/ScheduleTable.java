package com.ankit.timetable.model;

/**
 * Created by khach on 12-12-2017.
 */
//@Table(name = "scheduleTable")
public class ScheduleTable {

    private static final String TAG = "ScheduleTable";

    //  @Column(name = "seq")
    private int SEQ;

    //  @Column(name = "monday")
    private String MONDAY;

    //  @Column(name = "tuesday")
    private String TUESDAY;

    //  @Column(name = "wednesday")
    private String WEDNESDAY;

    //  @Column(name = "thursday")
    private String THURSDAY;

    //  @Column(name = "friday")
    private String FRIDAY;

    public ScheduleTable() {
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

    //  public List<ScheduleTable> getDaySchedule(String day_selected) {
    //    List<ScheduleTable> mDayData = null;
    //    if (day_selected.equals("MONDAY")) {
    //      mDayData = new Select("Id,monday").from(ScheduleTable.class).orderBy("Id ASC").execute();
    //      Timber.i("getDaySchedule: column Monday");
    //    }
    //    if (day_selected.equals("TUESDAY")) {
    //      mDayData = new Select("Id,tuesday").from(ScheduleTable.class).orderBy("Id ASC").execute();
    //      Timber.i("getDaySchedule: column Tuesday");
    //    }
    //    if (day_selected.equals("WEDNESDAY")) {
    //      mDayData = new Select("Id,wednesday").from(ScheduleTable.class).orderBy("Id ASC").execute();
    //      Timber.i("getDaySchedule: column Wednesday");
    //    }
    //    if (day_selected.equals("THURSDAY")) {
    //      mDayData = new Select("Id,thursday").from(ScheduleTable.class).orderBy("Id ASC").execute();
    //      Timber.i("getDaySchedule: column Thursday");
    //    }
    //    if (day_selected.equals("FRIDAY")) {
    //      mDayData = new Select("Id,friday").from(ScheduleTable.class).orderBy("Id ASC").execute();
    //      Timber.i("getDaySchedule: column Friday");
    //    }
    //    return mDayData;
    //  }
}
