package ankit.com.timetable;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by khach on 16-07-2017.
 */

public class Preference {
    Context s_context;
    protected SharedPreferences.Editor editor;
    protected SharedPreferences pref;

    public Preference(Context context) {

        pref = context.getSharedPreferences("Pref_Time_Table", MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setSession(boolean v, String s) {
        editor.putBoolean("First_Startup", v);
        editor.putString("Batch", s);
        editor.commit();
    }

    public void setFirstStartup(boolean val) {
        editor.putBoolean("First_Startup", val);
        editor.commit();
    }

    public void setBatch(String batch) {
        editor.putString("Batch", batch);
        editor.commit();
    }

    public void setDataSynced(boolean a) {
        editor.putBoolean("Data_Synced", a);
        editor.commit();
    }

    public boolean getDataSynced() {
        boolean val = pref.getBoolean("Data_Synced", false);
        return val;
    }

    public boolean getFirstStarupFlag() {
        boolean val = pref.getBoolean("Data_Synced", false);
        return val;
    }

    public String getBatch() {
        String s = pref.getString("Batch", "");
        return s;
    }
}