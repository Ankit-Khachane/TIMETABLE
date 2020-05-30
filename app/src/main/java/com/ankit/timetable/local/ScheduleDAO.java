package com.ankit.timetable.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ScheduleDAO {

    @Insert
    void insertSchedules(Schedule schedule);

    @Update
    void updateSchedule(Schedule schedule);

    @Delete
    void deleteSchedule(Schedule schedule);

    @Query("SELECT * FROM schedule_table ORDER BY seq ASC")
    LiveData<List<Schedule>> getAllSchedulesFromDB();

}
