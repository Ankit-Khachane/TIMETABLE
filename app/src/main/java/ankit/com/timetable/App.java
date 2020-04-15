package ankit.com.timetable;


import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.activeandroid.app.Application;
import com.parse.Parse;

import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by khach on 15-07-2017.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            Timber.i("Debug Timber is Planted");
        }
        Configuration configuration = new Configuration.Builder(this).setDatabaseName("table.db").create();
        ActiveAndroid.initialize(configuration, true);
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
