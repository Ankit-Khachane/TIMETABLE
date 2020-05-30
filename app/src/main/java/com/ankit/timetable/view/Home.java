package com.ankit.timetable.view;

import android.content.Context;
import android.os.Bundle;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.ankit.timetable.R;
import com.ankit.timetable.local.PreferenceHelper;
import com.ankit.timetable.local.Schedule;
import com.ankit.timetable.local.ScheduleViewModel;
import com.ankit.timetable.model.ScheduleData;
import com.ankit.timetable.utils.AppConstants;
import com.ankit.timetable.utils.AppUtils;
import com.ankit.timetable.utils.ScheduleListAdapter;
import com.blankj.utilcode.util.NetworkUtils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

@SuppressWarnings("ALL")
public class Home extends AppCompatActivity {

    private static final List<String> DAYS = Arrays.asList("SUNDAY",
            "MONDAY",
            "TUESDAY",
            "WEDNESDAY",
            "THURSDAY",
            "FRIDAY",
            "SATURDAY");
    private final String TAG = getClass().getSimpleName();
    private ScheduleViewModel viewModel;
    private PreferenceHelper mPreferenceHelper;
    private LayoutAnimationController mAnimationController;
    private ScheduleListAdapter mAdapter;

    private TextView mDayTV;
    private ListView mScheduleListView;
    private ImageView mPlaceHolderIV;
    private ProgressBar mProgressBar;
    private ImageButton mNextBtn, mPreviousBtn;

    private boolean isOnline;
    private String time, teacher, subject, type, room_no, currentDayValue;
    private List<ScheduleData> mDataSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        Toolbar toolbar = findViewById(R.id.tb_home);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        mPlaceHolderIV = findViewById(R.id.empty_view);
        mNextBtn = findViewById(R.id.next);
        mPreviousBtn = findViewById(R.id.previous);
        mDayTV = findViewById(R.id.dayname);
        mProgressBar = findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.VISIBLE);

        viewModel = new ViewModelProvider(this).get(ScheduleViewModel.class);
        Date date = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE",
                Locale.US);
        currentDayValue = dateFormat.format(date)
                .toUpperCase();
        mDayTV.setText(currentDayValue);

        mDataSet = new ArrayList<>();
        mAdapter = new ScheduleListAdapter(this,
                mDataSet);
        mScheduleListView = findViewById(R.id.main_list);
        mAnimationController = AnimationUtils.loadLayoutAnimation(this,
                R.anim.list_row_anime);
        mScheduleListView.setAdapter(mAdapter);
        mPreferenceHelper = new PreferenceHelper(this);
        // if (isNetworkConnected() && isInternetWorking())
        if (NetworkUtils.isWifiConnected() || NetworkUtils.isMobileData()) {
            // On Internet Connection
            isOnline = true;
            if (mPreferenceHelper.getFirstStartFlag() && mPreferenceHelper.getBatch()
                    .equals("")
                    && !mPreferenceHelper.getDataSynced()) {
                // if this is first launch
                // call -> Dialog call -> 1.FetchAndSync() + 2.laodSyncedData();
                BatchDialog();
                Timber.tag(TAG)
                        .d("Yes Internet Conenction : %s - is in First Lunch Block",
                                isOnline);
            } else {
                // this is not first launch
                if (mPreferenceHelper.getDataSynced()) {
                    // if this is normal launch then fetch data w.r.t pref> datasynced and laodSyncedData().
                    loadFromSyncedData(currentDayValue);
                    Timber.tag(TAG)
                            .d("Yes Internet Conenction : %s - is in Normal SQL Block ",
                                    isOnline);

                } else {
                    // load data from server via internet
                    loadFromServerData(currentDayValue);
                    Timber.tag(TAG)
                            .d("Yes Internet Conenction : %s - is in Normal Server Fetch Block ",
                                    isOnline);
                }
            }
            Toast.makeText(this,
                    "Internet Connection Detected",
                    Toast.LENGTH_SHORT)
                    .show();
        } else {
            // No network connection
            isOnline = false;
            if (mPreferenceHelper.getFirstStartFlag() && mPreferenceHelper.getBatch()
                    .equals("")
                    && !mPreferenceHelper.getDataSynced()) {
                // if this is first launch
                // guide user to connect to Intenet Atleast One Time
                Timber.tag(TAG)
                        .d("No Internet Conenction : %s - is in First Lunch Block ",
                                isOnline);
            } else {
                // if this is normal launch
                if (mPreferenceHelper.getDataSynced()) {
                    // load data from sqlite if data is synced.
                    loadFromSyncedData(currentDayValue);
                    Timber.tag(TAG)
                            .d("No Internet Conenction : %s - is in Normal SQL Block ",
                                    isOnline);
                } else {
                    // tell no data is fetched
                    // suggest to connect to Internet and follow
                    // checkinternet -> FetchAndLoadData() -> checksynched ->loadSynchedData()
                    Timber.tag(TAG)
                            .d("No Internet Conenction : %s - is in No Data Stored Block ",
                                    isOnline);
                }
            }
            Toast.makeText(this,
                    "No Internet Connection !",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

    //rememebr to remeove this method
    private void fetchAndSyncDataFromServer() {
        if (!mPreferenceHelper.getDataSynced()) {
            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery(AppConstants.TABLE_NAME);
            parseQuery.orderByAscending(AppConstants.Table.SEQUENCE);
            parseQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> scheduleList,
                                 ParseException e) {
                    if (e == null) {
                        int scheduleListSize = scheduleList.size();
                        for (int i = 0; i < scheduleListSize; i++) {
                            ParseObject scheduleItem = scheduleList.get(i);
                            if (scheduleItem.has(DAYS.get(1))) {
                                JSONObject job = scheduleItem.getJSONObject(DAYS.get(1));
                                if (job != null) {
                                    //                      scheduleTable.setMONDAY(job.toString());
                                    Schedule monday = new Schedule();
                                    monday.setMONDAY(job.toString());
                                    viewModel.insertSchedule(monday);
                                }
                            }
                            if (scheduleItem.has(DAYS.get(2))) {
                                JSONObject job = scheduleItem.getJSONObject(DAYS.get(2));
                                if (job != null) {
                                    //                      scheduleTable.setTUESDAY(job.toString());
                                    Schedule tuesday = new Schedule();
                                    tuesday.setTUESDAY(job.toString());
                                    viewModel.insertSchedule(tuesday);
                                }
                            }
                            if (scheduleItem.has(DAYS.get(3))) {
                                JSONObject job = scheduleItem.getJSONObject(DAYS.get(3));
                                if (job != null) {
                                    //                      scheduleTable.setWEDNESDAY(job.toString());
                                    Schedule wednesday = new Schedule();
                                    wednesday.setWEDNESDAY(job.toString());
                                    viewModel.insertSchedule(wednesday);
                                }
                            }
                            if (scheduleItem.has(DAYS.get(4))) {
                                JSONObject job = scheduleItem.getJSONObject(DAYS.get(4));
                                if (job != null) {
                                    //                      scheduleTable.setTHURSDAY(job.toString());
                                    Schedule thursday = new Schedule();
                                    thursday.setTHURSDAY(job.toString());
                                    viewModel.insertSchedule(thursday);
                                }
                            }
                            if (scheduleItem.has(DAYS.get(5))) {
                                JSONObject job = scheduleItem.getJSONObject(DAYS.get(5));
                                if (job != null) {
                                    //                      scheduleTable.setFRIDAY(job.toString());
                                    Schedule friday = new Schedule();
                                    friday.setFRIDAY(job.toString());
                                    viewModel.insertSchedule(friday);
                                }
                            }
                            // scheduleTable.save();
                        }
                        mPreferenceHelper.setDataSynced(true);
                    }
                }
            });
        } else {
            Toast.makeText(this,
                    "Data Already Synched",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void loadFromSyncedData(String currentDay) {
    /*if (BasicAppUtils.isDayExist(currentDay)) {
      mDataSet.clear();
      String currentDayTime, nextDayTime, room_no, subject, teacher, type;
      if (mDataSet.isEmpty()) {
        mProgressBar.setVisibility(View.GONE);
        // fetch data from SQLite data pass it to Adapter
        List<ScheduleTable> schedules = new ScheduleTable().getDaySchedule(currentDay);
        int size = schedules.size();
        for (int i = 0; i < size; i++) {
          try {
            ScheduleTable currentDayTable = schedules.get(i);
            String timestampone = BasicAppUtils.getScheduleByDay(currentDay, currentDayTable);
            JSONObject currentDayJsonData = new JSONObject(timestampone);
            if (i < size - 1) {
              ScheduleTable nextDayTable = schedules.get(i + 1);
              String timestamptwo = BasicAppUtils.getScheduleByDay(currentDay, nextDayTable);
              JSONObject nextdayJsonData = new JSONObject(timestamptwo);

              currentDayTime = currentDayJsonData.getString("time");
              nextDayTime = nextdayJsonData.getString("time");
              room_no = currentDayJsonData.getString("room_no");
              subject = currentDayJsonData.getString("subject");
              teacher = currentDayJsonData.getString("teacher");
              type = currentDayJsonData.getString("type");
              if (!currentDayTime.equals(nextDayTime) && !type.equals("--")) {
                ScheduleData dm = new ScheduleData(currentDayTime, room_no, subject, teacher, type);
                mDataSet.add(dm);
                mAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.GONE);
                mScheduleListView.setLayoutAnimation(mAnimationController);
                mNextBtn.setEnabled(true);
                mPreviousBtn.setEnabled(true);
              }
            }
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }
        Timber.tag(TAG).d("SQl_fetchd list Size: - %s", schedules.size());
      }
    } else {
      mPlaceHolderIV.setVisibility(View.VISIBLE);
      mProgressBar.setVisibility(View.GONE);
    }*/
    }

    private void loadFromServerData(final String currentDay) {
        //if (AppUtils.isDayExist(currentDay)) {
        //  mDataSet.clear();
        //  if (mDataSet.isEmpty()) {
        //    ParseQuery<ParseObject> schedule = ParseQuery.getQuery("te_b");
        //    schedule.orderByAscending("sequence");
        //    schedule.findInBackground(new FindCallback<ParseObject>() {
        //      @Override
        //      public void done(List<ParseObject> list,
        //                       ParseException parseException) {
        //        if (parseException == null && !list.isEmpty()) {
        //          int size = list.size();
        //          // get output list and add it to adapter list parameter list
        //          for (int i = 0; i < size; i++) {
        //            ParseObject currentDayParseJson = list.get(i);
        //            if (i < size - 1) {
        //              ParseObject nextDayParseJson = list.get(i + 1);
        //              try {
        //                if (currentDayParseJson != null) {
        //
        //                  JSONObject currentDayJsonData = currentDayParseJson.getJSONObject(currentDay);
        //                  JSONObject nextDayJsonData = nextDayParseJson.getJSONObject(currentDay);
        //
        //                  time = currentDayJsonData.getString("time");
        //                  String nextDayTime = nextDayJsonData.getString("time");
        //                  type = currentDayJsonData.getString("type");
        //                  subject = currentDayJsonData.getString("subject");
        //                  room_no = currentDayJsonData.getString("room_no");
        //                  teacher = currentDayJsonData.getString("teacher");
        //                  if (!type.equals("--") && !time.equals(nextDayTime)) {
        //                    //                                          Add dataModel To ListView
        //                    // adapter only of it's s_type is not "--"
        //                    //                                          & current object time key
        //                    // value is not equal to nextObject time key value
        //                    ScheduleData dm = new ScheduleData(time,
        //                                                       room_no,
        //                                                       subject,
        //                                                       teacher,
        //                                                       type);
        //                    mDataSet.add(dm);
        //                    mAdapter.notifyDataSetChanged();
        //                    mProgressBar.setVisibility(View.GONE);
        //                    mScheduleListView.setLayoutAnimation(mAnimationController);
        //                    mNextBtn.setEnabled(true);
        //                    mPreviousBtn.setEnabled(true);
        //                  }
        //                }
        //              } catch (JSONException jsonException) {
        //                jsonException.printStackTrace();
        //              }
        //            }
        //          }
        //        } else {
        //          // data fetch failed
        //          Timber.tag(TAG)
        //                .d("Data Fetch Failed");
        //        }
        //      }
        //    });
        //  }
        //} else {
        //  mPlaceHolderIV.setVisibility(View.VISIBLE);
        //  mProgressBar.setVisibility(View.GONE);
        //}
    }

    private void BatchDialog() {
        //    String[] list = {"COMP-B1", "COMP-B2", "COMP-B3", "COMP-B4"};
        String[] list = {"COMP-B4"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                R.layout.dialog_row,
                R.id.dialog_list_row,
                list);
        new LovelyChoiceDialog(this).setTopColorRes(R.color.colorAccent)
                .setTitle("Select Your Batch")
                .setCancelable(false)
                .setIcon(R.drawable.dialog_header_icon)
                .setMessage("Selected Batch Will Be Default")
                .setItems(adapter,
                        new LovelyChoiceDialog.OnItemSelectedListener<String>() {
                            @Override
                            public void onItemSelected(int position,
                                                       String item) {
                                mPreferenceHelper = new PreferenceHelper(getApplicationContext());
                                mPreferenceHelper.setBatch(item);
                                mPreferenceHelper.setFirstStartup(true);
                                fetchAndSyncDataFromServer();
                                Toast.makeText(Home.this,
                                        " Selected Batch" + item,
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                        })
                .show();
    }

    public void onNextClick(View v) {
        String nextDay, currentDay;
        mProgressBar.setVisibility(View.VISIBLE);
        mPlaceHolderIV.setVisibility(View.GONE);
        mNextBtn.setEnabled(false);
        mPreviousBtn.setEnabled(false);
        if (DAYS.contains(currentDayValue)) {
            if (AppUtils.isDayExist(currentDayValue)) {
                int i = DAYS.indexOf(currentDayValue);
                currentDay = currentDayValue;
                nextDay = DAYS.get(i + 1);
                currentDayValue = nextDay;
                mDayTV.setText(nextDay);
                if (isOnline) {
                    loadFromServerData(nextDay);
                } else {
                    loadFromSyncedData(nextDay);
                }
                Timber.tag(TAG)
                        .d("onNextClick: From" + currentDay + " To " + nextDay);
            }
            if (currentDayValue.equals("SATURDAY")) {
                currentDay = currentDayValue;
                nextDay = DAYS.get(0);
                currentDayValue = nextDay;
                mDayTV.setText(nextDay);
                mDataSet.clear();
                //                loadFromServerData(nextday);
                Timber.tag(TAG)
                        .d("onNextClick: From(Saturday)" + currentDay + " To " + nextDay);
            }
            if (currentDayValue.equals("SUNDAY")) {
                currentDay = currentDayValue;
                nextDay = DAYS.get(1);
                currentDayValue = nextDay;
                mDayTV.setText(nextDay);
                mDataSet.clear();
                if (isOnline) {
                    loadFromServerData(nextDay);
                } else {
                    loadFromSyncedData(nextDay);
                }
                Timber.tag(TAG)
                        .d("onNextClick: From(Sunday)" + currentDay + " To " + nextDay);
            }
        }
    }

    public void onPrevClick(View v) {
        String previousDay, currentDay;
        mProgressBar.setVisibility(View.VISIBLE);
        mPlaceHolderIV.setVisibility(View.GONE);
        mNextBtn.setEnabled(false);
        mPreviousBtn.setEnabled(false);
        if (DAYS.contains(currentDayValue)) {
            if (AppUtils.isDayExist(currentDayValue)) {
                int i = DAYS.indexOf(currentDayValue);
                currentDay = currentDayValue;
                if (i > 1) {
                    previousDay = DAYS.get(i - 1);
                    currentDayValue = previousDay;
                    mDayTV.setText(previousDay);
                    if (isOnline) {
                        loadFromServerData(previousDay);
                    } else {
                        loadFromSyncedData(previousDay);
                    }
                    Timber.tag(TAG)
                            .d("onPrevClick: From " + currentDay + " To " + previousDay);
                } else {
                    previousDay = DAYS.get(5);
                    currentDayValue = previousDay;
                    mDayTV.setText(previousDay);
                    mDataSet.clear();
                    if (isOnline) {
                        loadFromServerData(previousDay);
                    } else {
                        loadFromSyncedData(previousDay);
                    }
                    Timber.tag(TAG)
                            .d("onPrevClick: Day Saturday is set !");
                }
            }
            if (currentDayValue.equals("SUNDAY")) {
                currentDay = currentDayValue;
                previousDay = DAYS.get(6);
                currentDayValue = previousDay;
                mDayTV.setText(previousDay);
                mDataSet.clear();
                //                loadFromServerData(nextday);
                Timber.tag(TAG)
                        .d("onPrevClick: From (Sunday)" + currentDay + " To " + previousDay);
            }
            if (currentDayValue.equals("SATURDAY")) {
                currentDay = currentDayValue;
                previousDay = DAYS.get(5);
                currentDayValue = previousDay;
                mDayTV.setText(previousDay);
                mDataSet.clear();
                if (isOnline) {
                    loadFromServerData(previousDay);
                } else {
                    loadFromSyncedData(previousDay);
                }
                Timber.tag(TAG)
                        .d("onPrevClick: From (Saturday)" + currentDay + " To " + previousDay);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu,
                menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sett) {
            BatchDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
