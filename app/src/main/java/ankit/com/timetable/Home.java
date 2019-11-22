package ankit.com.timetable;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ankit.com.timetable.orm.TimeTable;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

@SuppressWarnings("ALL")
public class Home extends AppCompatActivity {

    private final String TAG = "Home";
    private boolean isOnline;
    private Preference p;
    private ListView HomeList;
    private TextView day_tv;
    private ScheduleAdapter sc;
    private ImageView iv;
    private ProgressBar pb;
    private ImageButton next;
    private ImageButton prev;
    private LayoutAnimationController controller;
    private String time;
    private String techr;
    private String sub;
    private String s_type;
    private String ro_no;
    private String day_name;
    private List<DataModel> dataset;
    private List<String> days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        days = Arrays.asList("SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY");

        Toolbar tb = findViewById(R.id.tb_home);
        iv = findViewById(R.id.empty_view);
        iv = findViewById(R.id.empty_view);
        next = findViewById(R.id.next);
        prev = findViewById(R.id.previous);
        day_tv = findViewById(R.id.dayname);
        pb = findViewById(R.id.progressBar);
        pb.setVisibility(View.VISIBLE);

        setSupportActionBar(tb);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("EEEE", Locale.US);
        day_name = f.format(date).toUpperCase();
        day_tv.setText(day_name);

        dataset = new ArrayList<>();
        sc = new ScheduleAdapter(this, dataset);
        HomeList = findViewById(R.id.main_list);
        controller = AnimationUtils.loadLayoutAnimation(this, R.anim.list_row_anime);
        HomeList.setAdapter(sc);
        p = new Preference(this);
        //if (isNetworkConnected() && isInternetWorking())
        if (isNetworkConnected()) {
            //On Internet Connection
            isOnline = true;
            if (p.getFirstStartFlag() && p.getBatch().equals("") && !p.getDataSynced()) {
                //if this is first launch
                // call -> Dialog call -> 1.FetchAndSync() + 2.laodSyncedData();
                BatchDialog();
                Log.i(TAG, "Yes Internet Conenction : " + isOnline + " - is in First Lunch Block ");
            } else {
                //this is not first launch
                if (p.getDataSynced()) {
                    //if this is normal launch then fetch data w.r.t pref> datasynced and laodSyncedData().
                    loadSyncedData(day_name);
                    Log.i(TAG, "Yes Internet Conenction : " + isOnline + " - is in Normal SQL Block ");
                } else {
                    //load data from server via internet
                    loadServerData(day_name);
                    Log.i(TAG, "Yes Internet Conenction : " + isOnline + " - is in Normal Server Fetch Block ");
                }
            }
            Toast.makeText(this, "Internet Connection Detected", Toast.LENGTH_SHORT).show();
        } else {
            //No network connection
            isOnline = false;
            if (p.getFirstStartFlag() && p.getBatch().equals("") && !p.getDataSynced()) {
                //if this is first launch
                //guide user to connect to Intenet Atleast One Time
                Log.i(TAG, "No Internet Conenction : " + isOnline + " - is in First Lunch Block ");
            } else {
                //if this is normal launch
                if (p.getDataSynced()) {
                    //load data from sqlite if data is synced.
                    loadSyncedData(day_name);
                    Log.i(TAG, "No Internet Conenction : " + isOnline + " - is in Normal SQL Block ");
                } else {
                    //tell no data is fetched
                    //suggest to connect to Internet and follow
                    //checkinternet -> FetchAndLoadData() -> checksynched ->loadSynchedData()
                    // TODO: 13-12-2017 Add Dialog To DO FirstLunch Routine & check internet before that.
                    Log.i(TAG, "No Internet Conenction : " + isOnline + " - is in No Data Stored Block ");
                }
            }
            Toast.makeText(this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    private void FetchAndStoreData() {
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("te_b");
        parseQuery.orderByAscending("sequence");
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> ttls, ParseException e) {
                if (e == null) {
                    int size = ttls.size();
                    for (int i = 0; i < size; i++) {
                        TimeTable tb = new TimeTable();
                        ParseObject po = ttls.get(i);
                        if (po.has(days.get(1))) {
                            JSONObject job = po.getJSONObject(days.get(1));
                            if (job != null)
                                tb.setMONDAY(job.toString());
                        }
                        if (po.has(days.get(2))) {
                            JSONObject job = po.getJSONObject(days.get(2));
                            if (job != null)
                                tb.setTUESDAY(job.toString());
                        }
                        if (po.has(days.get(3))) {
                            JSONObject job = po.getJSONObject(days.get(3));
                            if (job != null)
                                tb.setWEDNESDAY(job.toString());
                        }
                        if (po.has(days.get(4))) {
                            JSONObject job = po.getJSONObject(days.get(4));
                            if (job != null)
                                tb.setTHURSDAY(job.toString());
                        }
                        if (po.has(days.get(5))) {
                            JSONObject job = po.getJSONObject(days.get(5));
                            if (job != null)
                                tb.setFRIDAY(job.toString());
                        }
                        tb.save();
                        Log.i(TAG, "method list: " + "main list size " + tb.toString() + " seq. =" + po.get("sequence"));
                    }
//                    set data fetched pref
                    p.setDataSynced(true);
                }
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void loadSyncedData(String cday) {
        if (cday.equals("MONDAY") || cday.equals("TUESDAY") || cday.equals("WEDNESDAY") || cday.equals("THURSDAY") || cday.equals("FRIDAY")) {
            dataset.clear();
            String time, ftime, ro_no, Sub, Techer, s_type;
            if (dataset.isEmpty()) {
                pb.setVisibility(View.GONE);
                //fetch data from SQLite data pass it to Adapter
                List<TimeTable> daycol = new TimeTable().getDaySchedule(cday);
                int size = daycol.size();
                for (int i = 0; i < daycol.size(); i++) {
                    try {
                        TimeTable rowfirst = daycol.get(i);
                        String time1 = "";
                        switch (cday) {
                            case "MONDAY":
                                time1 = rowfirst.getMONDAY();
                                break;
                            case "TUESDAY":
                                time1 = rowfirst.getTUESDAY();
                                break;
                            case "WEDNESDAY":
                                time1 = rowfirst.getWEDNESDAY();
                                break;
                            case "THURSDAY":
                                time1 = rowfirst.getTHURSDAY();
                                break;
                            case "FRIDAY":
                                time1 = rowfirst.getFRIDAY();
                                break;
                        }
                        JSONObject firstjo = new JSONObject(time1);
                        if (i < size - 1) {
                            TimeTable rowsecond = daycol.get(i + 1);
                            String time2;
                            time2 = rowsecond.getWEDNESDAY();
                            switch (cday) {
                                case "MONDAY":
                                    time2 = rowsecond.getMONDAY();
                                    break;
                                case "TUESDAY":
                                    time2 = rowsecond.getTUESDAY();
                                    break;
                                case "WEDNESDAY":
                                    time2 = rowsecond.getWEDNESDAY();
                                    break;
                                case "THURSDAY":
                                    time2 = rowsecond.getTHURSDAY();
                                    break;
                                case "FRIDAY":
                                    time2 = rowsecond.getFRIDAY();
                                    break;
                            }
                            JSONObject secondjo = new JSONObject(time2);
                            time = firstjo.getString("time");
                            ftime = secondjo.getString("time");
                            ro_no = firstjo.getString("ro_no");
                            Sub = firstjo.getString("Sub");
                            Techer = firstjo.getString("Techer");
                            s_type = firstjo.getString("s_type");
                            if (!time.equals(ftime) && !s_type.equals("--")) {
                                DataModel dm = new DataModel(time, ro_no, Sub, Techer, s_type);
                                dataset.add(dm);
                                sc.notifyDataSetChanged();
                                pb.setVisibility(View.GONE);
                                HomeList.setLayoutAnimation(controller);
                                next.setEnabled(true);
                                prev.setEnabled(true);
                                Log.i(TAG, "Data - " + rowfirst.getWEDNESDAY());
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i(TAG, "SQl_fetchd list Size: -" + daycol.size());
            }
        } else {
            iv.setVisibility(View.VISIBLE);
            pb.setVisibility(View.GONE);
        }
    }


    private void loadServerData(final String cday) {
        if (cday.equals("MONDAY") || cday.equals("TUESDAY") || cday.equals("WEDNESDAY") || cday.equals("THURSDAY") || cday.equals("FRIDAY")) {
//            pb.setVisibility(View.GONE);
            dataset.clear();
            if (dataset.isEmpty()) {
                ParseQuery<ParseObject> schedule = ParseQuery.getQuery("te_b");
                schedule.orderByAscending("sequence");
                schedule.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        if (e == null) {
                            int n = list.size();
                            // get output list and add it to adapter list parameter list
                            for (int i = 0; i < list.size(); i++) {
                                ParseObject o = list.get(i);
                                if (i < n - 1) {
                                    ParseObject oo = list.get(i + 1);
                                    try {
                                        if (o != null) {
                                            time = o.getJSONObject(cday).getString("time");
                                            String nextObjectTime = oo.getJSONObject(cday).getString("time");
                                            s_type = o.getJSONObject(cday).getString("s_type");
                                            sub = o.getJSONObject(cday).getString("Sub");
                                            ro_no = o.getJSONObject(cday).getString("ro_no");
                                            techr = o.getJSONObject(cday).getString("Techer");
                                            if (!s_type.equals("--") && !time.equals(nextObjectTime)) {
//                                          Add dataModel To ListView adapter only of it's s_type is not "--"
//                                          & current object time key value is not equal to nextObject time key value
                                                DataModel dm = new DataModel(time, ro_no, sub, techr, s_type);
                                                dataset.add(dm);
                                                sc.notifyDataSetChanged();
                                                pb.setVisibility(View.GONE);
                                                HomeList.setLayoutAnimation(controller);
                                                next.setEnabled(true);
                                                prev.setEnabled(true);
//                                            Log.i(TAG, "Data ---++: " + o.getJSONObject(cday).get("Sub") + "- day position =" + o.getJSONObject(cday).toString());
                                            }
                                        } else {
                                            Log.d(TAG, "done: Object o = null !");
                                        }
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            }
                        } else {
                            //data fetch failed
                            Log.d(TAG, "done: Data Fetched Failed !");
                        }
                    }
                });
            }
        } else {
            iv.setVisibility(View.VISIBLE);
            pb.setVisibility(View.GONE);
        }
    }

    private void BatchDialog() {
        String[] list = {"COMP-B1", "COMP-B2", "COMP-B3", "COMP-B4"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dialog_row, R.id.dialog_list_row, list);
        new LovelyChoiceDialog(this)
                .setTopColorRes(R.color.colorAccent)
                .setTitle("Select Your Batch")
                .setCancelable(false)
                .setIcon(R.drawable.dialog_header_icon)
                .setMessage("Selected Batch Will Be Default")
                .setItems(adapter, new LovelyChoiceDialog.OnItemSelectedListener<String>() {
                    @Override
                    public void onItemSelected(int position, String item) {
                        p = new Preference(Home.this);
                        p.setBatch(item);
                        p.setFirstStartup(true);
                        FetchAndStoreData();
                        Toast.makeText(Home.this, " Selected Batch" + item, Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    public void onNextClick(View v) {
        String nextday, cday;
        pb.setVisibility(View.VISIBLE);
        iv.setVisibility(View.GONE);
        next.setEnabled(false);
        prev.setEnabled(false);
        if (days.contains(day_name)) {
            if (day_name.equals("MONDAY")
                    || day_name.equals("TUESDAY")
                    || day_name.equals("WEDNESDAY")
                    || day_name.equals("THURSDAY")
                    || day_name.equals("FRIDAY")) {
                int i = days.indexOf(day_name);
                cday = day_name;
                nextday = days.get(i + 1);
                day_name = nextday;
                day_tv.setText(nextday);
                if (isOnline) {
                    loadServerData(nextday);
                } else {
                    loadSyncedData(nextday);
                }
                Log.i(TAG, "onNextClick: From" + cday + " To " + nextday);
            }
            if (day_name.equals("SATURDAY")) {
                cday = day_name;
                nextday = days.get(0);
                day_name = nextday;
                day_tv.setText(nextday);
                dataset.clear();
//                loadServerData(nextday);
                Log.i(TAG, "onNextClick: From(Saturday)" + cday + " To " + nextday);
            }
            if (day_name.equals("SUNDAY")) {
                cday = day_name;
                nextday = days.get(1);
                day_name = nextday;
                day_tv.setText(nextday);
                dataset.clear();
                if (isOnline) {
                    loadServerData(nextday);
                } else {
                    loadSyncedData(nextday);
                }
                Log.i(TAG, "onNextClick: From(Sunday)" + cday + " To " + nextday);
            }
        }
    }

    public void onPrevClick(View v) {
        String prevday, cday;
        pb.setVisibility(View.VISIBLE);
        iv.setVisibility(View.GONE);
        next.setEnabled(false);
        prev.setEnabled(false);
        if (days.contains(day_name)) {
            if (day_name.equals("MONDAY")
                    || day_name.equals("TUESDAY")
                    || day_name.equals("WEDNESDAY")
                    || day_name.equals("THURSDAY")
                    || day_name.equals("FRIDAY")) {
                int i = days.indexOf(day_name);
                cday = day_name;
                if (i > 1) {
                    prevday = days.get(i - 1);
                    day_name = prevday;
                    day_tv.setText(prevday);
                    if (isOnline) {
                        loadServerData(prevday);
                    } else {
                        loadSyncedData(prevday);
                    }
                    Log.i(TAG, "onPrevClick: From " + cday + " To " + prevday);
                } else {
                    prevday = days.get(5);
                    day_name = prevday;
                    day_tv.setText(prevday);
                    dataset.clear();
                    if (isOnline) {
                        loadServerData(prevday);
                    } else {
                        loadSyncedData(prevday);
                    }
                    Log.i(TAG, "onPrevClick: Day Saturday is set !");
                }
            }
            if (day_name.equals("SUNDAY")) {
                cday = day_name;
                prevday = days.get(6);
                day_name = prevday;
                day_tv.setText(prevday);
                dataset.clear();
//                loadServerData(nextday);
                Log.i(TAG, "onPrevClick: From (Sunday)" + cday + " To " + prevday);
            }
            if (day_name.equals("SATURDAY")) {
                cday = day_name;
                prevday = days.get(5);
                day_name = prevday;
                day_tv.setText(prevday);
                dataset.clear();
                if (isOnline) {
                    loadServerData(prevday);
                } else {
                    loadSyncedData(prevday);
                }
                Log.i(TAG, "onPrevClick: From (Saturday)" + cday + " To " + prevday);
            }
        }
    }

    private boolean isNetworkConnected() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connectivityManager != null) {
            if (connectivityManager.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                    connectivityManager.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                    connectivityManager.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

                // if connected with internet

                Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
                return true;

            } else if (connectivityManager.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                    connectivityManager.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

                Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sett) {
            Toast.makeText(this, "It's Setting", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}