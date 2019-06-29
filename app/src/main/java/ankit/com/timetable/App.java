package ankit.com.timetable;


import android.os.StrictMode;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.activeandroid.app.Application;
import com.parse.Parse;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by khach on 15-07-2017.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectNetwork()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .penaltyDialog()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectActivityLeaks()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .build());
        }
        Configuration dbconf = new Configuration.Builder(this).setDatabaseName("table.db").create();
        ActiveAndroid.initialize(dbconf, true);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Montserrat-Medium.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("FIOZLYupdLWlWN0rhno72jhiKjIIGWHObF4HoUcq")
                .clientKey("b5RJAqZ8Cs2VoxBY7UvbJ8lWO6h7MfZy6x7tSlUq")
                .server("https://parseapi.back4app.com/").build()
        );
    }
}
