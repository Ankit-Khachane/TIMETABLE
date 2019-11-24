package ankit.com.timetable.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by khach on 16-07-2017.
 */

public class Preference {
    private final SharedPreferences.Editor editor;
    private final SharedPreferences sharedPreferences;

    @SuppressLint("CommitPrefEdits")
    public Preference(Context context) {

        sharedPreferences = context.getSharedPreferences("Pref_Time_Table", MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setSession(boolean firstStartup, String batch) {
        editor.putBoolean("First_Startup", firstStartup).putString("Batch", batch);
        editor.commit();
    }

    public void setFirstStartup(boolean firstStartup) {
        editor.putBoolean("First_Startup", firstStartup);
        editor.commit();
    }

    public boolean getDataSynced() {
        return sharedPreferences.getBoolean("Data_Synced", false);
    }

    public void setDataSynced(boolean dataSynced) {
        editor.putBoolean("Data_Synced", dataSynced);
        editor.commit();
    }

    public boolean getFirstStartFlag() {
        return !sharedPreferences.getBoolean("Data_Synced", false);
    }

    public String getBatch() {
        return sharedPreferences.getString("Batch", "");
    }

    public void setBatch(String batch) {
        editor.putString("Batch", batch);
        editor.commit();
    }
}