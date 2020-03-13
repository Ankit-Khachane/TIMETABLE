package ankit.com.timetable.view;

import android.content.Context;
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

import ankit.com.timetable.R;
import ankit.com.timetable.model.ApiHelper;
import ankit.com.timetable.model.DatabaseService;
import ankit.com.timetable.model.PreferenceService;
import ankit.com.timetable.model.ScheduleData;
import ankit.com.timetable.presenter.ScheduleListAdapter;
import ankit.com.timetable.utils.NetworkUtil;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Home extends AppCompatActivity {
    // TODO: 24-11-2019 Refractor Project to Android MVP Pattern.

    private static final List<String> DAYS = Arrays.asList("SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY");
    private final String TAG = getClass().getSimpleName();
    private boolean isOnline;
    private PreferenceService preferenceService;
    private TextView day_tv;
    private ScheduleListAdapter scheduleListAdapter;
    private ListView ScheduleListView;
    private ImageView empty_iv;
    private ProgressBar progressBar;
    private LayoutAnimationController controller;
    private ImageButton next_ibtn, previous_ibtn;
    private String time, techr, sub, s_type, ro_no, day_name;
    private List<ScheduleData> dataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);


        Toolbar toolbar = findViewById(R.id.tb_home);
        empty_iv = findViewById(R.id.empty_view);
        next_ibtn = findViewById(R.id.next);
        previous_ibtn = findViewById(R.id.previous);
        day_tv = findViewById(R.id.dayname);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.US);
        day_name = dateFormat.format(date).toUpperCase();
        day_tv.setText(day_name);

        dataset = new ArrayList<>();
        scheduleListAdapter = new ScheduleListAdapter(this, dataset);
        ScheduleListView = findViewById(R.id.main_list);
        controller = AnimationUtils.loadLayoutAnimation(this, R.anim.list_row_anime);
        ScheduleListView.setAdapter(scheduleListAdapter);
        preferenceService = new PreferenceService(this);
        //if (isNetworkConnected() && isInternetWorking())
        if (NetworkUtil.isNetworkConnected(getApplicationContext())) {
            //On Internet Connection
            isOnline = true;
            if (preferenceService.getFirstStartFlag() && preferenceService.getBatch().equals("") && !preferenceService.getDataSynced()) {
                //if this is first launch
                // call -> Dialog call -> 1.FetchAndSync() + 2.laodSyncedData();
                BatchDialog();
                Log.i(TAG, "Yes Internet Conenction : " + isOnline + " - is in First Lunch Block ");
            } else {
                //this is not first launch
                if (preferenceService.getDataSynced()) {
                    //if this is normal launch then fetch data w.r.t pref> datasynced and laodSyncedData().
                    loadFromSyncedData(day_name);
                    Log.i(TAG, "Yes Internet Conenction : " + isOnline + " - is in Normal SQL Block ");
                } else {
                    //load data from server via internet
                    loadFromServerData(day_name);
                    Log.i(TAG, "Yes Internet Conenction : " + isOnline + " - is in Normal Server Fetch Block ");
                }
            }
            Toast.makeText(this, "Internet Connection Detected", Toast.LENGTH_SHORT).show();
        } else {
            //No network connection
            isOnline = false;
            if (preferenceService.getFirstStartFlag() && preferenceService.getBatch().equals("") && !preferenceService.getDataSynced()) {
                //if this is first launch
                //guide user to connect to Intenet Atleast One Time
                Log.i(TAG, "No Internet Conenction : " + isOnline + " - is in First Lunch Block ");
            } else {
                //if this is normal launch
                if (preferenceService.getDataSynced()) {
                    //load data from sqlite if data is synced.
                    loadFromSyncedData(day_name);
                    Log.i(TAG, "No Internet Conenction : " + isOnline + " - is in Normal SQL Block ");
                } else {
                    //tell no data is fetched
                    //suggest to connect to Internet and follow
                    //checkinternet -> FetchAndLoadData() -> checksynched ->loadSynchedData()
                    Log.i(TAG, "No Internet Conenction : " + isOnline + " - is in No Data Stored Block ");
                }
            }
            Toast.makeText(this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchAndSyncDataFromServer() {
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery(ApiHelper.TABLE_NAME);
        parseQuery.orderByAscending(ApiHelper.Table.SEQUENCE).findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> scheduleList, ParseException e) {
                if (e == null) {
                    int scheduleListSize = scheduleList.size();
                    for (int i = 0; i < scheduleListSize; i++) {
                        DatabaseService databaseService = new DatabaseService();
                        ParseObject scheduleItem = scheduleList.get(i);
                        if (scheduleItem.has(DAYS.get(1))) {
                            JSONObject job = scheduleItem.getJSONObject(DAYS.get(1));
                            if (job != null)
                                databaseService.setMONDAY(job.toString());
                        }
                        if (scheduleItem.has(DAYS.get(2))) {
                            JSONObject job = scheduleItem.getJSONObject(DAYS.get(2));
                            if (job != null)
                                databaseService.setTUESDAY(job.toString());
                        }
                        if (scheduleItem.has(DAYS.get(3))) {
                            JSONObject job = scheduleItem.getJSONObject(DAYS.get(3));
                            if (job != null)
                                databaseService.setWEDNESDAY(job.toString());
                        }
                        if (scheduleItem.has(DAYS.get(4))) {
                            JSONObject job = scheduleItem.getJSONObject(DAYS.get(4));
                            if (job != null)
                                databaseService.setTHURSDAY(job.toString());
                        }
                        if (scheduleItem.has(DAYS.get(5))) {
                            JSONObject job = scheduleItem.getJSONObject(DAYS.get(5));
                            if (job != null)
                                databaseService.setFRIDAY(job.toString());
                        }
                        databaseService.save();
                        Log.i(TAG, "method list: " + "main list size " + databaseService.toString() + " seq. =" + scheduleItem.get("sequence"));
                    }
//                    set data fetched pref
                    preferenceService.setDataSynced(true);
                }
            }
        });
    }

    private void loadFromSyncedData(String currentDay) {
        if (currentDay.equals("MONDAY")
                || currentDay.equals("TUESDAY") || currentDay.equals("WEDNESDAY")
                || currentDay.equals("THURSDAY") || currentDay.equals("FRIDAY")) {
            dataset.clear();
            String time, ftime, ro_no, Sub, Techer, s_type;
            if (dataset.isEmpty()) {
                progressBar.setVisibility(View.GONE);
                //fetch data from SQLite data pass it to Adapter
                List<DatabaseService> schedules = new DatabaseService().getDaySchedule(currentDay);
                int size = schedules.size();
                for (int i = 0; i < schedules.size(); i++) {
                    try {
                        DatabaseService databaseService = schedules.get(i);
                        String time1 = "";
                        switch (currentDay) {
                            case "MONDAY":
                                time1 = databaseService.getMONDAY();
                                break;
                            case "TUESDAY":
                                time1 = databaseService.getTUESDAY();
                                break;
                            case "WEDNESDAY":
                                time1 = databaseService.getWEDNESDAY();
                                break;
                            case "THURSDAY":
                                time1 = databaseService.getTHURSDAY();
                                break;
                            case "FRIDAY":
                                time1 = databaseService.getFRIDAY();
                                break;
                        }
                        JSONObject firstjo = new JSONObject(time1);
                        if (i < size - 1) {
                            DatabaseService databaseService1 = schedules.get(i + 1);
                            String time2 = "";
//                            time2 = databaseService1.getWEDNESDAY();
                            switch (currentDay) {
                                case "MONDAY":
                                    time2 = databaseService1.getMONDAY();
                                    break;
                                case "TUESDAY":
                                    time2 = databaseService1.getTUESDAY();
                                    break;
                                case "WEDNESDAY":
                                    time2 = databaseService1.getWEDNESDAY();
                                    break;
                                case "THURSDAY":
                                    time2 = databaseService1.getTHURSDAY();
                                    break;
                                case "FRIDAY":
                                    time2 = databaseService1.getFRIDAY();
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
                                ScheduleData dm = new ScheduleData(time, ro_no, Sub, Techer, s_type);
                                dataset.add(dm);
                                scheduleListAdapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
                                ScheduleListView.setLayoutAnimation(controller);
                                next_ibtn.setEnabled(true);
                                previous_ibtn.setEnabled(true);
                                Log.i(TAG, "Data - " + databaseService.getWEDNESDAY());
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i(TAG, "SQl_fetchd list Size: -" + schedules.size());
            }
        } else {
            empty_iv.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    private void loadFromServerData(final String cday) {
        if (cday.equals("MONDAY")
                || cday.equals("TUESDAY") || cday.equals("WEDNESDAY")
                || cday.equals("THURSDAY") || cday.equals("FRIDAY")) {
//            progressBar.setVisibility(View.GONE);
            dataset.clear();
            if (dataset.isEmpty()) {
                ParseQuery<ParseObject> schedule = ParseQuery.getQuery(ApiHelper.TABLE_NAME);
                schedule.orderByAscending(ApiHelper.Table.SEQUENCE)
                        .findInBackground(new FindCallback<ParseObject>() {
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
                                                        ScheduleData dm = new ScheduleData(time, ro_no, sub, techr, s_type);
                                                        dataset.add(dm);
                                                        scheduleListAdapter.notifyDataSetChanged();
                                                        progressBar.setVisibility(View.GONE);
                                                        ScheduleListView.setLayoutAnimation(controller);
                                                        next_ibtn.setEnabled(true);
                                                        previous_ibtn.setEnabled(true);

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
            empty_iv.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    private void BatchDialog() {
        String[] list = {"COMP-B1", "COMP-B2", "COMP-B3", "COMP-B4"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dialog_row, R.id.dialog_list_row, list);
        new LovelyChoiceDialog(this)
                .setTopColorRes(R.color.colorAccent)
                .setTitle("Select Your Batch")
                .setCancelable(false)
                .setIcon(R.drawable.dialog_header_icon)
                .setMessage("Selected Batch Will Be Default")
                .setItems(adapter, new LovelyChoiceDialog.OnItemSelectedListener<String>() {
                    @Override
                    public void onItemSelected(int position, String item) {
                        preferenceService = new PreferenceService(getApplicationContext());
                        preferenceService.setBatch(item);
                        preferenceService.setFirstStartup(true);
                        fetchAndSyncDataFromServer();
                        Toast.makeText(Home.this, " Selected Batch" + item, Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    public void onNextClick(View v) {
        String nextDay, currentDay;
        progressBar.setVisibility(View.VISIBLE);
        empty_iv.setVisibility(View.GONE);
        next_ibtn.setEnabled(false);
        previous_ibtn.setEnabled(false);
        if (DAYS.contains(day_name)) {
            if (day_name.equals("MONDAY")
                    || day_name.equals("TUESDAY")
                    || day_name.equals("WEDNESDAY")
                    || day_name.equals("THURSDAY")
                    || day_name.equals("FRIDAY")) {
                int i = DAYS.indexOf(day_name);
                currentDay = day_name;
                nextDay = DAYS.get(i + 1);
                day_name = nextDay;
                day_tv.setText(nextDay);
                if (isOnline) {
                    loadFromServerData(nextDay);
                } else {
                    loadFromSyncedData(nextDay);
                }
                Log.i(TAG, "onNextClick: From" + currentDay + " To " + nextDay);
            }
            if (day_name.equals("SATURDAY")) {
                currentDay = day_name;
                nextDay = DAYS.get(0);
                day_name = nextDay;
                day_tv.setText(nextDay);
                dataset.clear();
//                loadFromServerData(nextday);
                Log.i(TAG, "onNextClick: From(Saturday)" + currentDay + " To " + nextDay);
            }
            if (day_name.equals("SUNDAY")) {
                currentDay = day_name;
                nextDay = DAYS.get(1);
                day_name = nextDay;
                day_tv.setText(nextDay);
                dataset.clear();
                if (isOnline) {
                    loadFromServerData(nextDay);
                } else {
                    loadFromSyncedData(nextDay);
                }
                Log.i(TAG, "onNextClick: From(Sunday)" + currentDay + " To " + nextDay);
            }
        }
    }

    public void onPrevClick(View v) {
        String previousDay, currentDay;
        progressBar.setVisibility(View.VISIBLE);
        empty_iv.setVisibility(View.GONE);
        next_ibtn.setEnabled(false);
        previous_ibtn.setEnabled(false);
        if (DAYS.contains(day_name)) {
            if (day_name.equals("MONDAY")
                    || day_name.equals("TUESDAY")
                    || day_name.equals("WEDNESDAY")
                    || day_name.equals("THURSDAY")
                    || day_name.equals("FRIDAY")) {
                int i = DAYS.indexOf(day_name);
                currentDay = day_name;
                if (i > 1) {
                    previousDay = DAYS.get(i - 1);
                    day_name = previousDay;
                    day_tv.setText(previousDay);
                    if (isOnline) {
                        loadFromServerData(previousDay);
                    } else {
                        loadFromSyncedData(previousDay);
                    }
                    Log.i(TAG, "onPrevClick: From " + currentDay + " To " + previousDay);
                } else {
                    previousDay = DAYS.get(5);
                    day_name = previousDay;
                    day_tv.setText(previousDay);
                    dataset.clear();
                    if (isOnline) {
                        loadFromServerData(previousDay);
                    } else {
                        loadFromSyncedData(previousDay);
                    }
                    Log.i(TAG, "onPrevClick: Day Saturday is set !");
                }
            }
            if (day_name.equals("SUNDAY")) {
                currentDay = day_name;
                previousDay = DAYS.get(6);
                day_name = previousDay;
                day_tv.setText(previousDay);
                dataset.clear();
//                loadFromServerData(nextday);
                Log.i(TAG, "onPrevClick: From (Sunday)" + currentDay + " To " + previousDay);
            }
            if (day_name.equals("SATURDAY")) {
                currentDay = day_name;
                previousDay = DAYS.get(5);
                day_name = previousDay;
                day_tv.setText(previousDay);
                dataset.clear();
                if (isOnline) {
                    loadFromServerData(previousDay);
                } else {
                    loadFromSyncedData(previousDay);
                }
                Log.i(TAG, "onPrevClick: From (Saturday)" + currentDay + " To " + previousDay);
            }
        }
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