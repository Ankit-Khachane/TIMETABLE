package ankit.com.timetable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by khach on 16-07-2017.
 */

class Preference {
    Context s_context;
    private final SharedPreferences.Editor editor;
    private final SharedPreferences pref;

    @SuppressLint("CommitPrefEdits")
    Preference(Context context) {

        pref = context.getSharedPreferences("Pref_Time_Table", MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setSession(boolean v, String s) {
        editor.putBoolean("First_Startup", v);
        editor.putString("Batch", s);
        editor.commit();
    }

    void setFirstStartup(boolean val) {
        editor.putBoolean("First_Startup", val);
        editor.commit();
    }

    boolean getDataSynced() {
        return pref.getBoolean("Data_Synced", false);
    }

    void setDataSynced(boolean a) {
        editor.putBoolean("Data_Synced", a);
        editor.commit();
    }

    boolean getFirstStartFlag() {
        return !pref.getBoolean("Data_Synced", false);
    }

    String getBatch() {
        return pref.getString("Batch", "");
    }

    void setBatch(String batch) {
        editor.putString("Batch", batch);
        editor.commit();
    }
}