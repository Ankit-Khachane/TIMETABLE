package com.ankit.timetable.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Schedule.class, version = 1, exportSchema = false)
public abstract class ScheduleDatabase extends RoomDatabase {

    private static volatile ScheduleDatabase INSTANCE;

    public static synchronized ScheduleDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    ScheduleDatabase.class,
                    "schedule_table")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public abstract ScheduleDAO scheduleDAO();

}
