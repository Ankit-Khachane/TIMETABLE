package ankit.com.timetable;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;


/**
 * Created by khach on 16-07-2017.
 */

public class ScheduleAdapter extends BaseAdapter {
    Context c;
    List<DataModel> modelset;
    GradientDrawable bgShape;
    String TAG = "Adapter", time, sub, rono, teacher, stype;


    public ScheduleAdapter(Context context, List<DataModel> o) {
        this.c = context;
        this.modelset = o;
    }

    private class ViewHolder {
        TextView time_tv;
        TextView sub_tv;
        TextView detail_tv;
        LinearLayout bar;
    }

    @SuppressWarnings("ResourceAsColor")
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder h = null;
        LayoutInflater mInflater = (LayoutInflater) c.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.home_list_row, null);
            h = new ViewHolder();
            h.bar = (LinearLayout) convertView.findViewById(R.id.bar);
            h.time_tv = (TextView) convertView.findViewById(R.id.time_tv);
            h.sub_tv = (TextView) convertView.findViewById(R.id.Sub_tv);
            h.detail_tv = (TextView) convertView.findViewById(R.id.detail_tv);
            convertView.setTag(h);
        } else {
            h = (ViewHolder) convertView.getTag();
        }
        DataModel sc = (DataModel) getItem(position);
        if (sc != null) {

            time = sc.getTime();
            sub = sc.getSub();
            rono = sc.getRo_no();
            teacher = sc.getTeacher();
            stype = sc.getS_type();

            h.time_tv.setText(time);
            h.sub_tv.setText(sub);
            h.detail_tv.setText("PROF : " + teacher + " | Room No : " + rono);

            bgShape = (GradientDrawable) h.bar.getBackground();
            if (sc.getS_type().equals("Break")) {
                bgShape.setColor(Color.parseColor("#4CAF50"));
            }
            if (sc.getS_type().equals("practicle")) {
                bgShape.setColor(Color.parseColor("#C900FF"));
            }
            if (sc.getS_type().equals("lecture")) {
                bgShape.setColor(Color.parseColor("#ff0074"));
            }
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return modelset.size();
    }

    @Override
    public Object getItem(int i) {
        return modelset.get(i);
    }

    @Override
    public long getItemId(int i) {
        return modelset.indexOf(getItem(i));
    }
}
