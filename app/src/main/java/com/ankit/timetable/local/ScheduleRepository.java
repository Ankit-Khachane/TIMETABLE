package com.ankit.timetable.local;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.ankit.timetable.network.ApiHelper;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ThreadUtils.SimpleTask;
import com.parse.ParseObject;

import java.util.List;

public class ScheduleRepository implements ISchedule {

    private ScheduleDAO mDAO;
    private ApiHelper mApiHelper;

    ScheduleRepository(Application application) {
        ScheduleDatabase database = ScheduleDatabase.getInstance(application);
        mDAO = database.scheduleDAO();
    }

    @Override
    public void insertSchedule(Schedule schedule) {
        ThreadUtils.executeByIo(new SimpleTask<Schedule>() {
            @Override
            public Schedule doInBackground() throws Throwable {
                mDAO.insertSchedules(schedule);
                return null;
            }

            @Override
            public void onSuccess(Schedule result) {

            }
        });
    }

    @Override
    public void updateSchedule(Schedule schedule) {
        ThreadUtils.executeByIo(new SimpleTask<Schedule>() {
            @Override
            public Schedule doInBackground() throws Throwable {
                mDAO.updateSchedule(schedule);
                return null;
            }

            @Override
            public void onSuccess(Schedule result) {

            }
        });

    }

    @Override
    public void deleteSchedule(Schedule schedule) {
        ThreadUtils.executeByIo(new SimpleTask<Schedule>() {
            @Override
            public Schedule doInBackground() throws Throwable {
                mDAO.deleteSchedule(schedule);
                return null;
            }

            @Override
            public void onSuccess(Schedule result) {

            }
        });
    }

    @Override
    public LiveData<List<Schedule>> loadSchedulesFromDB() {
        return mDAO.getAllSchedulesFromDB();
    }

    @Override
    public LiveData<List<ParseObject>> loadScheduleFromServer() {
        return mApiHelper.loadDataFromServer();
    }


}
