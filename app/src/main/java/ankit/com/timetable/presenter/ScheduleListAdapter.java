package ankit.com.timetable.presenter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.List;

import ankit.com.timetable.R;
import ankit.com.timetable.model.ScheduleData;


/**
 * Created by khach on 16-07-2017.
 */

public class ScheduleListAdapter extends BaseAdapter {
    private final Context mContext;
    private final List<ScheduleData> data;
    String TAG = "ScheduleListAdapter";


    public ScheduleListAdapter(Context mContext, List<ScheduleData> scheduleData) {
        this.mContext = mContext;
        this.data = scheduleData;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null && mInflater != null) {
            convertView = mInflater.inflate(R.layout.home_list_row, parent, false);
            holder = new ViewHolder();
            holder.sideBar = convertView.findViewById(R.id.bar);
            holder.timeTv = convertView.findViewById(R.id.time_tv);
            holder.subTv = convertView.findViewById(R.id.Sub_tv);
            holder.detailTv = convertView.findViewById(R.id.detail_tv);
            convertView.setTag(holder);
        } else {
            if (convertView != null)
                holder = (ViewHolder) convertView.getTag();
        }
        ScheduleData item = (ScheduleData) getItem(position);
        if (item != null) {
            String time = item.getTime();
            String subject = item.getSubject();
            String room_no = item.getRoomNumber();
            String teacher = item.getTeacher();
            if (holder != null) {
                int color = 0;
                holder.timeTv.setText(time);
                holder.subTv.setText(subject);
                holder.detailTv.setText(String.format("PROF : %s | Room No : %s", teacher, room_no));
                if (item.getType().equals("Break")) {
                    holder.sideBar.setBackground(ContextCompat.getDrawable(mContext, R.drawable.sidebar_break));
                }
                if (item.getType().equals("Practical")) {
                    holder.sideBar.setBackground(ContextCompat.getDrawable(mContext, R.drawable.sidebar_practical));
                }
                if (item.getType().equals("Lecture")) {
                    holder.sideBar.setBackground(ContextCompat.getDrawable(mContext, R.drawable.sidebar_lecture));
                }
            }
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return data.indexOf(getItem(i));
    }

    private static class ViewHolder {
        TextView timeTv;
        TextView subTv;
        TextView detailTv;
        LinearLayout sideBar;
    }
}
