package ankit.com.timetable;

import android.app.Application;

import com.parse.Parse;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by khach on 15-07-2017.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
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
