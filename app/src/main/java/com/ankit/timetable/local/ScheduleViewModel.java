package com.ankit.timetable.local;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ankit.timetable.utils.AppUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import timber.log.Timber;

public class ScheduleViewModel extends AndroidViewModel implements ISchedule {

    private static final List<String> DAYS = Arrays.asList("SUNDAY",
            "MONDAY",
            "TUESDAY",
            "WEDNESDAY",
            "THURSDAY",
            "FRIDAY",
            "SATURDAY");

    private ScheduleRepository mRepository;
    private boolean isOnline;

    private String currentDay;
    private LiveData<List<Schedule>> mLocalData;
    private LiveData<List<ParseObject>> mServerData;

    public ScheduleViewModel(@NonNull Application application) {
        super(application);
        mRepository = new ScheduleRepository(application);
        isOnline = NetworkUtils.isWifiConnected() || NetworkUtils.isMobileData();
        currentDay = AppUtils.getCurrentDay();
        Timber.i("ScheduleViewModel Constructor initialized");
    }

    public void onNextLicked() {

    }

    public void onPreviousClicked() {

    }

    @Override
    public void insertSchedule(Schedule schedule) {
        mRepository.insertSchedule(schedule);
    }

    @Override
    public void updateSchedule(Schedule schedule) {
        mRepository.updateSchedule(schedule);
    }

    @Override
    public void deleteSchedule(Schedule schedule) {
        mRepository.deleteSchedule(schedule);
    }

    @Override
    public LiveData<List<Schedule>> loadSchedulesFromDB() {
        return mRepository.loadSchedulesFromDB();
    }

    @Override
    public LiveData<List<ParseObject>> loadScheduleFromServer() {
        return mRepository.loadScheduleFromServer();
    }


    public LiveData<List<Schedule>> getLocalData() {
        if (mLocalData == null) {
            mLocalData = new MutableLiveData<>(new ArrayList<>());
            mLocalData = loadSchedulesFromDB();
        }
        return mLocalData;
    }

    public LiveData<List<ParseObject>> getServerData() {
        if (mServerData == null) {
            mServerData = new MutableLiveData<>(new ArrayList<>());
        }
        return mServerData;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public String getCurrentDay() {
        return currentDay;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
