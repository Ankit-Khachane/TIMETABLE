package com.ankit.timetable.local;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "schedule_table")
public class Schedule {

    @PrimaryKey
    @ColumnInfo(name = "seq")
    private int seq;
    @ColumnInfo(name = "monday")
    private String MONDAY;
    @ColumnInfo(name = "tuesday")
    private String TUESDAY;
    @ColumnInfo(name = "wednesday")
    private String WEDNESDAY;
    @ColumnInfo(name = "thursday")
    private String THURSDAY;
    @ColumnInfo(name = "friday")
    private String FRIDAY;

    public Schedule() {
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getMONDAY() {
        return MONDAY;
    }

    public void setMONDAY(String MONDAY) {
        this.MONDAY = MONDAY;
    }

    public String getTUESDAY() {
        return TUESDAY;
    }

    public void setTUESDAY(String TUESDAY) {
        this.TUESDAY = TUESDAY;
    }

    public String getWEDNESDAY() {
        return WEDNESDAY;
    }

    public void setWEDNESDAY(String WEDNESDAY) {
        this.WEDNESDAY = WEDNESDAY;
    }

    public String getTHURSDAY() {
        return THURSDAY;
    }

    public void setTHURSDAY(String THURSDAY) {
        this.THURSDAY = THURSDAY;
    }

    public String getFRIDAY() {
        return FRIDAY;
    }

    public void setFRIDAY(String FRIDAY) {
        this.FRIDAY = FRIDAY;
    }
}
