package ankit.com.timetable.presenter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ankit.com.timetable.R;
import ankit.com.timetable.model.ScheduleData;


/**
 * Created by khach on 16-07-2017.
 */

public class ScheduleListAdapter extends BaseAdapter {
    private final Context context;
    private final List<ScheduleData> scheduleData;
    String TAG = "ScheduleListAdapter";


    public ScheduleListAdapter(Context context, List<ScheduleData> o) {
        this.context = context;
        this.scheduleData = o;
    }

    @SuppressLint("ResourceAsColor")
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder h = null;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null && mInflater != null) {
            convertView = mInflater.inflate(R.layout.home_list_row, parent, false);
            h = new ViewHolder();
            h.sideBar = convertView.findViewById(R.id.bar);
            h.time_tv = convertView.findViewById(R.id.time_tv);
            h.sub_tv = convertView.findViewById(R.id.Sub_tv);
            h.detail_tv = convertView.findViewById(R.id.detail_tv);
            convertView.setTag(h);
        } else {
            if (convertView != null)
                h = (ViewHolder) convertView.getTag();
        }
        ScheduleData item = (ScheduleData) getItem(position);
        if (item != null) {

            String time = item.getTime();
            String sub = item.getSubject();
            String rono = item.getRoomNumber();
            String teacher = item.getProfessor();

//            GradientDrawable bgShape = (GradientDrawable) h.sideBar.getBackground();
            if (h != null) {
                int color = 0;
                h.time_tv.setText(time);
                h.sub_tv.setText(sub);
                h.detail_tv.setText(String.format("PROF : %s | Room No : %s", teacher, rono));
                if (item.getType().equals("Break")) {
                    h.sideBar.setBackground(ContextCompat.getDrawable(context, R.drawable.sidebar_break));
                }
                if (item.getType().equals("Practical")) {
                    h.sideBar.setBackground(ContextCompat.getDrawable(context, R.drawable.sidebar_practical));
                }
                if (item.getType().equals("Lecture")) {
                    h.sideBar.setBackground(ContextCompat.getDrawable(context, R.drawable.sidebar_lecture));
                }
            }


        }

        return convertView;
    }

    @Override
    public int getCount() {
        return scheduleData.size();
    }

    @Override
    public Object getItem(int i) {
        return scheduleData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return scheduleData.indexOf(getItem(i));
    }

    private class ViewHolder {
        TextView time_tv;
        TextView sub_tv;
        TextView detail_tv;
        LinearLayout sideBar;
    }
}
