package com.ankit.timetable;

import android.app.Application;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.os.StrictMode.VmPolicy;

import com.parse.Parse;

import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by khach on 15-07-2017.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            StrictMode.setThreadPolicy(
                    new ThreadPolicy.Builder()
                            .detectDiskReads()
                            .detectDiskWrites()
                            .detectNetwork()
                            .penaltyLog()
                            .build());
            StrictMode.setVmPolicy(
                    new VmPolicy.Builder()
                            .detectLeakedSqlLiteObjects()
                            .detectLeakedClosableObjects()
                            .penaltyLog()
                            .build());

        }
        super.onCreate();

    /*ActiveAndroid.initialize(
        new Configuration.Builder(this).setDatabaseName("table.db").create(), true);*/

        CalligraphyConfig.initDefault(
                new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Montserrat-Medium.otf")
                        .setFontAttrId(R.attr.fontPath)
                        .build());

        Parse.initialize(
                new Parse.Configuration.Builder(this)
                        .applicationId("FIOZLYupdLWlWN0rhno72jhiKjIIGWHObF4HoUcq")
                        .clientKey("b5RJAqZ8Cs2VoxBY7UvbJ8lWO6h7MfZy6x7tSlUq")
                        .server("https://parseapi.back4app.com/")
                        .build());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Timber.d("Timetable App Terminated");
    }
}
