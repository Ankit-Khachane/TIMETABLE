package com.ankit.timetable.local;

import androidx.lifecycle.LiveData;

import com.parse.ParseObject;

import java.util.List;

public interface ISchedule {

    void insertSchedule(Schedule schedule);

    void updateSchedule(Schedule schedule);

    void deleteSchedule(Schedule schedule);

    LiveData<List<Schedule>> loadSchedulesFromDB();

    LiveData<List<ParseObject>> loadScheduleFromServer();

}
