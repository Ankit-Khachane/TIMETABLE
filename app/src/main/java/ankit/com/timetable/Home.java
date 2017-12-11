package ankit.com.timetable;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Home extends AppCompatActivity {

    String selectedBatch;
    Preference p;
    private String TAG = "Home";
    ListView HomeList;
    Toolbar tb;
    TextView mTitle, day_tv;
    ScheduleAdapter sc;
    ImageView iv;
    ProgressBar pb;
    ImageButton next, prev;
    Animation am;
    LayoutAnimationController controller;
    String time, techr, sub, s_type, ro_no, day_name;
    List<DataModel> dataset;
    List<String> days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        days = Arrays.asList("SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY");

        tb = (Toolbar) findViewById(R.id.tb_home);
        mTitle = tb.findViewById(R.id.title);
        iv = (ImageView) findViewById(R.id.empty_view);
        iv = (ImageView) findViewById(R.id.empty_view);
        next = (ImageButton) findViewById(R.id.next);
        prev = (ImageButton) findViewById(R.id.previous);
        day_tv = (TextView) findViewById(R.id.dayname);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setVisibility(View.VISIBLE);

        setSupportActionBar(tb);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("EEEE");
        day_name = f.format(date).toUpperCase();
        day_tv.setText(day_name);

        am = AnimationUtils.loadAnimation(this, R.anim.fadein);

        dataset = new ArrayList<DataModel>();
        sc = new ScheduleAdapter(this, dataset);
        HomeList = (ListView) findViewById(R.id.main_list);
        controller = AnimationUtils.loadLayoutAnimation(this, R.anim.list_row_anime);
        HomeList.setAdapter(sc);

        p = new Preference(this);

        if (!p.getFirstStarup() && p.getBatch() == "") {
            BatchDialog();
            Log.i(TAG, "onCreate:--New Preference " + selectedBatch);
        } else {
            if (!p.getBatch().equals("")) {
                //get batch column object from server and display details
                loadListData(day_name);
            }
            Log.i(TAG, "onCreate:--Preference Refereed");
        }
    }


    private void loadListData(final String dn) {
        if (dn.equals("MONDAY") || dn.equals("TUESDAY") || dn.equals("WEDNESDAY") || dn.equals("THURSDAY") || dn.equals("FRIDAY")) {
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
                                        time = o.getJSONObject(dn).getString("time").toString();
                                        String timeo = oo.getJSONObject(dn).getString("time").toString();
                                        s_type = o.getJSONObject(dn).getString("s_type");
                                        sub = o.getJSONObject(dn).getString("Sub");
                                        ro_no = o.getJSONObject(dn).getString("ro_no");
                                        techr = o.getJSONObject(dn).getString("Techer");
                                        if (!s_type.equals("--") && !time.equals(timeo)) {
                                            DataModel dm = new DataModel(time, ro_no, sub, techr, s_type);
                                            dataset.add(dm);
                                            sc.notifyDataSetChanged();
                                            pb.setVisibility(View.GONE);
                                            HomeList.setLayoutAnimation(controller);
                                            next.setEnabled(true);
                                            prev.setEnabled(true);
//                                            Log.i(TAG, "Data ---++: " + o.getJSONObject(dn).get("Sub"));
                                        }

                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                }


                            }

                        } else {
                            //data fetch failed
                        }
                    }
                });
            }
        } else {
            iv.setVisibility(View.VISIBLE);
            pb.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sett:
                Toast.makeText(this, "It's Setting", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void BatchDialog() {
        String list[] = {"COMP-B1", "COMP-B2", "COMP-B3", "COMP-B4"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.dialog_row, R.id.dialog_list_row, list);
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
                        loadListData(day_name);
//                        selectedBatch = item;
                        Toast.makeText(Home.this, " Selected Batch" + item, Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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
                loadListData(nextday);
                Log.i(TAG, "onNextClick: From" + cday + " To " + nextday);
            }
            if (day_name.equals("SATURDAY")) {
                cday = day_name;
                nextday = days.get(0);
                day_name = nextday;
                day_tv.setText(nextday);
                dataset.clear();
//                loadListData(nextday);
                Log.i(TAG, "onNextClick: From(Saturday)" + cday + " To " + nextday);
            }
            if (day_name.equals("SUNDAY")) {
                cday = day_name;
                nextday = days.get(1);
                day_name = nextday;
                day_tv.setText(nextday);
                dataset.clear();
                loadListData(nextday);
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
                    loadListData(prevday);
                    Log.i(TAG, "onPrevClick: From " + cday + " To " + prevday);
                } else {
                    prevday = days.get(5);
                    day_name = prevday;
                    day_tv.setText(prevday);
                    dataset.clear();
                    loadListData(prevday);
                    Log.i(TAG, "onPrevClick: Day Saturday is set !");
                }
            }
            if (day_name.equals("SUNDAY")) {
                cday = day_name;
                prevday = days.get(6);
                day_name = prevday;
                day_tv.setText(prevday);
                dataset.clear();
//                loadListData(nextday);
                Log.i(TAG, "onPrevClick: From (Sunday)" + cday + " To " + prevday);
            }
            if (day_name.equals("SATURDAY")) {
                cday = day_name;
                prevday = days.get(5);
                day_name = prevday;
                day_tv.setText(prevday);
                dataset.clear();
                loadListData(prevday);
                Log.i(TAG, "onPrevClick: From (Saturday)" + cday + " To " + prevday);
            }
        }
    }

}
