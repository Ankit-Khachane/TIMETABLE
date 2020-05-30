package com.ankit.timetable.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.ankit.timetable.R;
import com.ankit.timetable.model.ScheduleData;

import java.util.List;


/**
 * Created by khach on 16-07-2017.
 */

public class ScheduleListAdapter extends BaseAdapter {

    private static final String TAG = "ScheduleListAdapter";
    private final Context mContext;
    private final List<ScheduleData> mData;


    public ScheduleListAdapter(Context mContext,
                               List<ScheduleData> scheduleDataList) {
        this.mContext = mContext;
        this.mData = scheduleDataList;
    }

    @SuppressLint("ResourceAsColor")
    public View getView(int position,
                        View convertView,
                        ViewGroup parent) {
        ViewHolder viewHolder = null;
        LayoutInflater mInflater = (LayoutInflater) mContext
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null && mInflater != null) {
            convertView = mInflater.inflate(R.layout.home_list_row,
                    parent,
                    false);
            viewHolder = new ViewHolder();
            viewHolder.mSideBar = convertView.findViewById(R.id.bar);
            viewHolder.mTimeTv = convertView.findViewById(R.id.time_tv);
            viewHolder.mSubjectTV = convertView.findViewById(R.id.Sub_tv);
            viewHolder.mDetailTv = convertView.findViewById(R.id.detail_tv);
            convertView.setTag(viewHolder);
        } else {
            if (convertView != null) {
                viewHolder = (ViewHolder) convertView.getTag();
            }
        }
        ScheduleData item = (ScheduleData) getItem(position);
        if (item != null) {

            String time = item.getTime();
            String subject = item.getSubject();
            String roomNumber = item.getRoomNumber();
            String teacher = item.getTeacher();

//            GradientDrawable bgShape = (GradientDrawable) h.sideBar.getBackground();
            if (viewHolder != null) {
                int color = 0;
                viewHolder.mTimeTv.setText(time);
                viewHolder.mSubjectTV.setText(subject);
                viewHolder.mDetailTv
                        .setText(String.format("PROF : %s | Room No : %s",
                                teacher,
                                roomNumber));
                if (item.getType().equals("Break")) {
                    viewHolder.mSideBar
                            .setBackground(ContextCompat.getDrawable(mContext,
                                    R.drawable.sidebar_break));
                }
                if (item.getType().equals("Practical")) {
                    viewHolder.mSideBar
                            .setBackground(ContextCompat.getDrawable(mContext,
                                    R.drawable.sidebar_practical));
                }
                if (item.getType().equals("Lecture")) {
                    viewHolder.mSideBar
                            .setBackground(ContextCompat.getDrawable(mContext,
                                    R.drawable.sidebar_lecture));
                }
            }


        }

        return convertView;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mData.indexOf(getItem(i));
    }

    private static class ViewHolder {

        TextView mTimeTv;
        TextView mSubjectTV;
        TextView mDetailTv;
        LinearLayout mSideBar;
    }
}
