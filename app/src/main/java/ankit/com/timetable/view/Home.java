package ankit.com.timetable.view;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.blankj.utilcode.util.NetworkUtils;
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
import java.util.Objects;

import ankit.com.timetable.R;
import ankit.com.timetable.model.ApiHelper;
import ankit.com.timetable.model.PreferenceService;
import ankit.com.timetable.model.ScheduleData;
import ankit.com.timetable.model.ScheduleTable;
import ankit.com.timetable.presenter.ScheduleListAdapter;
import ankit.com.timetable.utils.BasicUtils;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Home extends AppCompatActivity implements AdapterView.OnItemClickListener {
    // TODO: 24-11-2019 Refactor Project to Android MVP Pattern.

    private static final List<String> DAYS = Arrays.asList("SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY");
    private final String TAG = getClass().getSimpleName();
    private boolean isOnline;
    private PreferenceService mPreferenceService;
    private TextView mDayTv;
    private ImageView mEmptyPlaceHolderView;
    private ProgressBar mProgressView;
    private ListView mListView;
    private ImageButton mNextBtn, mPreviousBtn;
    private ScheduleListAdapter mAdapter;
    private LayoutAnimationController mAnimationController;
    private String time, teacher, subject, type, room_no, current_day_name;
    private List<ScheduleData> mDataSet;
    private ScheduleTable mScheduleTable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);


        Toolbar toolbar = findViewById(R.id.tb_home);
        mEmptyPlaceHolderView = findViewById(R.id.empty_view);
        mNextBtn = findViewById(R.id.next);
        mPreviousBtn = findViewById(R.id.previous);
        mDayTv = findViewById(R.id.dayname);
        mProgressView = findViewById(R.id.progressBar);
        mProgressView.setVisibility(View.VISIBLE);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.US);
        current_day_name = dateFormat.format(date).toUpperCase();
        mDayTv.setText(current_day_name);

        mDataSet = new ArrayList<>();
        mAdapter = new ScheduleListAdapter(this, mDataSet);
        mListView = findViewById(R.id.main_list);
        mAnimationController = AnimationUtils.loadLayoutAnimation(this, R.anim.list_row_anime);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mPreferenceService = new PreferenceService(this);
        //if (isNetworkConnected() && isInternetWorking())
        if (NetworkUtils.isConnected()) {
            //On Internet Connection
            isOnline = true;
            if (mPreferenceService.getFirstStartFlag() && mPreferenceService.getBatch().equals("") && !mPreferenceService.getDataSynced()) {
                //if this is first launch
                BatchDialog();
                Timber.tag(TAG).i("Yes Internet Connection : " + isOnline + " - is in First Lunch Block ");
            } else {
                //this is not first launch
                if (mPreferenceService.getDataSynced()) {
                    //if this is normal launch then fetch data
                    loadFromSyncedData(current_day_name);
                    Timber.tag(TAG).i("Yes Internet Connection : " + isOnline + " - is in Normal SQL Block ");
                } else {
                    //load data from server via internet
                    loadFromServerData(current_day_name);
                    Timber.tag(TAG).i("Yes Internet Connection : " + isOnline + " - is in Normal Server Fetch Block ");
                }
            }
            Toast.makeText(this, "Internet Connection Detected", Toast.LENGTH_SHORT).show();
        } else {
            //No network connection
            isOnline = false;
            if (mPreferenceService.getFirstStartFlag() && mPreferenceService.getBatch().equals("") && !mPreferenceService.getDataSynced()) {
                //if this is first launch
                //guide user to connect to Internet at least One Time
                Timber.tag(TAG).i("No Internet Connection : " + isOnline + " - is in First Lunch Block ");
            } else {
                //if this is normal launch
                if (mPreferenceService.getDataSynced()) {
                    //load data from sqlite if data is synced.
                    loadFromSyncedData(current_day_name);
                    Timber.tag(TAG).i("No Internet Connection : " + isOnline + " - is in Normal SQL Block ");
                } else {
                    //tell no data is fetched
                    //suggest to connect to Internet and follow
                    Timber.tag(TAG).i("No Internet Connection : " + isOnline + " - is in No Data Stored Block ");
                }
            }
            Toast.makeText(this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchAndSyncDataFromServer() {
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery(ApiHelper.TABLE_NAME);
        parseQuery.orderByAscending(ApiHelper.Table.SEQUENCE).findInBackground((scheduleList, e) -> {
            if (e == null) {
                int scheduleListSize = scheduleList.size();
                for (int i = 0; i < scheduleListSize; i++) {
                    mScheduleTable = new ScheduleTable();
                    ParseObject scheduleItem = scheduleList.get(i);
                    if (scheduleItem.has(DAYS.get(1))) {
                        JSONObject mondayJson = scheduleItem.getJSONObject(DAYS.get(1));
                        if (mondayJson != null)
                            mScheduleTable.setMONDAY(mondayJson.toString());
                    }
                    if (scheduleItem.has(DAYS.get(2))) {
                        JSONObject tuesdayJson = scheduleItem.getJSONObject(DAYS.get(2));
                        if (tuesdayJson != null)
                            mScheduleTable.setTUESDAY(tuesdayJson.toString());
                    }
                    if (scheduleItem.has(DAYS.get(3))) {
                        JSONObject wednesdayJson = scheduleItem.getJSONObject(DAYS.get(3));
                        if (wednesdayJson != null)
                            mScheduleTable.setWEDNESDAY(wednesdayJson.toString());
                    }
                    if (scheduleItem.has(DAYS.get(4))) {
                        JSONObject thursdayJson = scheduleItem.getJSONObject(DAYS.get(4));
                        if (thursdayJson != null)
                            mScheduleTable.setTHURSDAY(thursdayJson.toString());
                    }
                    if (scheduleItem.has(DAYS.get(5))) {
                        JSONObject fridayJson = scheduleItem.getJSONObject(DAYS.get(5));
                        if (fridayJson != null)
                            mScheduleTable.setFRIDAY(fridayJson.toString());
                    }
                    mScheduleTable.save();
                    Timber.tag(TAG).i("method list: " + "main list size " + mScheduleTable.toString() + " seq. =" + scheduleItem.get("sequence"));
                }
//                    set data fetched pref
                mPreferenceService.setDataSynced(true);
            }
        });
    }

    private void loadFromSyncedData(String currentDay) {
        if (BasicUtils.getDayBy(currentDay)) {
            mDataSet.clear();
            String time, nextTime, room_no, subject, teacher, type;
            if (mDataSet.isEmpty()) {
                mProgressView.setVisibility(View.GONE);
                //fetch data from SQLite data pass it to Adapter
                List<ScheduleTable> schedules = mScheduleTable.getDaySchedule(currentDay);
                int size = schedules.size();
                for (int i = 0; i < schedules.size(); i++) {
                    try {
                        ScheduleTable scheduleTable = schedules.get(i);
                        String todayScheduleData = BasicUtils.getTableByCurrentDay(currentDay, scheduleTable);
                        JSONObject todaySchedule = new JSONObject(todayScheduleData);
                        if (i < size - 1) {
                            ScheduleTable nextSchedule = schedules.get(i + 1);
                            String nextScheduleData = BasicUtils.getTableByCurrentDay(currentDay, nextSchedule);
                            JSONObject nextDaySchedule = new JSONObject(nextScheduleData);
                            time = todaySchedule.getString("time");
                            nextTime = nextDaySchedule.getString("time");
                            room_no = todaySchedule.getString("room_no");
                            subject = todaySchedule.getString("subject");
                            teacher = todaySchedule.getString("teacher");
                            type = todaySchedule.getString("type");
                            if (!time.equals(nextTime) && !type.equals("--")) {
                                ScheduleData dm = new ScheduleData(time, room_no, subject, teacher, type);
                                mDataSet.add(dm);
                                mAdapter.notifyDataSetChanged();
                                mProgressView.setVisibility(View.GONE);
                                mListView.setLayoutAnimation(mAnimationController);
                                mNextBtn.setEnabled(true);
                                mPreviousBtn.setEnabled(true);
                                Timber.tag(TAG).i("Data - %s", scheduleTable.getWEDNESDAY());
                            }
                        }
                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                    }
                }
                Timber.tag(TAG).i("SQL fetched list Size: -%s", schedules.size());
            }
        } else {
            mEmptyPlaceHolderView.setVisibility(View.VISIBLE);
            mProgressView.setVisibility(View.GONE);
        }
    }

    private void loadFromServerData(final String currentDay) {
        if (BasicUtils.getDayBy(currentDay)) {
//            progressBar.setVisibility(View.GONE);
            mDataSet.clear();
            if (mDataSet.isEmpty()) {
                ParseQuery<ParseObject> schedule = ParseQuery.getQuery(ApiHelper.TABLE_NAME);
                schedule.orderByAscending(ApiHelper.Table.SEQUENCE)
                        .findInBackground((list, e) -> {
                            if (e == null) {
                                int size = list.size();
                                // get output list and add it to adapter list parameter list
                                for (int i = 0; i < list.size(); i++) {
                                    ParseObject currentDayJson = list.get(i);
                                    if (i < size - 1) {
                                        ParseObject nextDayJson = list.get(i + 1);
                                        try {
                                            if (currentDayJson != null) {

                                                JSONObject currentDayData = Objects.requireNonNull(currentDayJson.getJSONObject(currentDay));
                                                JSONObject nextDayData = Objects.requireNonNull(nextDayJson.getJSONObject(currentDay));

                                                String nextObjectTime = nextDayData.getString("time");
                                                time = currentDayData.getString("time");
                                                type = currentDayData.getString("type");
                                                subject = currentDayData.getString("subject");
                                                room_no = currentDayData.getString("room_no");
                                                teacher = currentDayData.getString("teacher");
                                                if (!type.equals("--") && !time.equals(nextObjectTime)) {
                                                    /*Add dataModel To ListView adapter only of it's s_type is not "--"
                                                     & current object time key value is not equal to nextObject time key value*/
                                                    ScheduleData dm = new ScheduleData(time, room_no, subject, teacher, type);
                                                    mDataSet.add(dm);
                                                    mAdapter.notifyDataSetChanged();
                                                    mProgressView.setVisibility(View.GONE);
                                                    mListView.setLayoutAnimation(mAnimationController);
                                                    mNextBtn.setEnabled(true);
                                                    mPreviousBtn.setEnabled(true);
                                                }
                                            } else {
                                                Timber.tag(TAG).d("done: Object o = null !");
                                            }
                                        } catch (JSONException jsonException) {
                                            jsonException.printStackTrace();
                                        }
                                    }
                                }
                            } else {
                                //data fetch failed
                                Timber.tag(TAG).d("done: Data Fetched Failed !");
                            }
                        });
            }
        } else {
            mEmptyPlaceHolderView.setVisibility(View.VISIBLE);
            mProgressView.setVisibility(View.GONE);
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
                .setItems(adapter, (position, item) -> {
                    mPreferenceService = new PreferenceService(getApplicationContext());
                    mPreferenceService.setBatch(item);
                    mPreferenceService.setFirstStartup(true);
                    fetchAndSyncDataFromServer();
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(Home.this, " Selected Batch" + item, Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    public void onNextClick(View v) {
        String nextDay, currentDay;
        mProgressView.setVisibility(View.VISIBLE);
        mEmptyPlaceHolderView.setVisibility(View.GONE);
        mNextBtn.setEnabled(false);
        mPreviousBtn.setEnabled(false);
        if (DAYS.contains(current_day_name)) {
            if (BasicUtils.getDayBy(current_day_name)) {
                int i = DAYS.indexOf(current_day_name);
                currentDay = current_day_name;
                nextDay = DAYS.get(i + 1);
                current_day_name = nextDay;
                mDayTv.setText(nextDay);
                if (isOnline) {
                    loadFromServerData(nextDay);
                } else {
                    loadFromSyncedData(nextDay);
                }
                Timber.tag(TAG).i("onNextClick: From" + currentDay + " To " + nextDay);
            }
            if (current_day_name.equals("SATURDAY")) {
                currentDay = current_day_name;
                nextDay = DAYS.get(0);
                current_day_name = nextDay;
                mDayTv.setText(nextDay);
                mDataSet.clear();
                Timber.tag(TAG).i("onNextClick: From(Saturday)" + currentDay + " To " + nextDay);
            }
            if (current_day_name.equals("SUNDAY")) {
                currentDay = current_day_name;
                nextDay = DAYS.get(1);
                current_day_name = nextDay;
                mDayTv.setText(nextDay);
                mDataSet.clear();
                if (isOnline) {
                    loadFromServerData(nextDay);
                } else {
                    loadFromSyncedData(nextDay);
                }
                Timber.tag(TAG).i("onNextClick: From(Sunday)" + currentDay + " To " + nextDay);
            }
        }
    }

    public void onPrevClick(View v) {
        String previousDay, currentDay;
        mProgressView.setVisibility(View.VISIBLE);
        mEmptyPlaceHolderView.setVisibility(View.GONE);
        mNextBtn.setEnabled(false);
        mPreviousBtn.setEnabled(false);
        if (DAYS.contains(current_day_name)) {
            if (BasicUtils.getDayBy(current_day_name)) {
                int currentDayIndex = DAYS.indexOf(current_day_name);
                currentDay = current_day_name;
                if (currentDayIndex > 1) {
                    previousDay = DAYS.get(currentDayIndex - 1);
                    current_day_name = previousDay;
                    mDayTv.setText(previousDay);
                    if (isOnline) {
                        loadFromServerData(previousDay);
                    } else {
                        loadFromSyncedData(previousDay);
                    }
                    Timber.tag(TAG).i("onPrevClick: From " + currentDay + " To " + previousDay);
                } else {
                    previousDay = DAYS.get(5);
                    current_day_name = previousDay;
                    mDayTv.setText(previousDay);
                    mDataSet.clear();
                    if (isOnline) {
                        loadFromServerData(previousDay);
                    } else {
                        loadFromSyncedData(previousDay);
                    }
                    Timber.tag(TAG).i("onPrevClick: Day Saturday is set !");
                }
            }
            if (current_day_name.equals("SUNDAY")) {
                currentDay = current_day_name;
                previousDay = DAYS.get(6);
                current_day_name = previousDay;
                mDayTv.setText(previousDay);
                mDataSet.clear();
                Timber.tag(TAG).i("onPrevClick: From (Sunday)" + currentDay + " To " + previousDay);
            }
            if (current_day_name.equals("SATURDAY")) {
                currentDay = current_day_name;
                previousDay = DAYS.get(5);
                current_day_name = previousDay;
                mDayTv.setText(previousDay);
                mDataSet.clear();
                if (isOnline) {
                    loadFromServerData(previousDay);
                } else {
                    loadFromSyncedData(previousDay);
                }
                Timber.tag(TAG).i("onPrevClick: From (Saturday)" + currentDay + " To " + previousDay);
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
            BatchDialog();
            Timber.i("Setting is clicked");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}