package ankit.com.timetable.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by khach on 16-07-2017.
 */

public class PreferenceService {
    private static final String PREFERENCE_TIMETABLE = "PREFERENCE_TIMETABLE";
    private static final String KEY_FIRST_STARTUP = "FIRST_STARTUP";
    private static final String KEY_DATA_SYNCED = "DATA_SYNCED";
    private static final String KEY_BATCH = "BATCH";

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;


    @SuppressLint("CommitPrefEdits")
    public PreferenceService(Context context) {

        sharedPreferences = context.getSharedPreferences(PREFERENCE_TIMETABLE, MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setSessionAndBatch(boolean firstStartup, String batch) {
        editor.putBoolean(KEY_FIRST_STARTUP, firstStartup).putString(KEY_BATCH, batch).commit();
    }

    public void setFirstStartup(boolean firstStartup) {
        editor.putBoolean(KEY_FIRST_STARTUP, firstStartup).commit();
    }

    public boolean getFirstStartFlag() {
        return !sharedPreferences.getBoolean(KEY_DATA_SYNCED, false);
    }

    public boolean getDataSynced() {
        return sharedPreferences.getBoolean(KEY_DATA_SYNCED, false);
    }

    public void setDataSynced(boolean dataSynced) {
        editor.putBoolean(KEY_DATA_SYNCED, dataSynced).commit();
    }

    public String getBatch() {
        return sharedPreferences.getString(KEY_BATCH, "");
    }

    public void setBatch(String batch) {
        editor.putString(KEY_BATCH, batch).commit();
    }
}