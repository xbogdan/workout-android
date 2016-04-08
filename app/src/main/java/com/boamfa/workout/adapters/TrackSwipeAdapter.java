package com.boamfa.workout.adapters;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.boamfa.workout.R;
import com.boamfa.workout.activities.BaseActivity;
import com.boamfa.workout.classes.TrackDay;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by bogdan on 02/12/15.
 */
public class TrackSwipeAdapter extends BaseSwipeAdapter {
    private BaseActivity context;
    private List<TrackDay> objects;
    private int resource;
    private int textViewResourceId;

    public TrackSwipeAdapter(Context context, int resource, int textViewResourceId, List<TrackDay> objects) {
        this.context = (BaseActivity) context;
        this.objects = objects;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return textViewResourceId;
    }

    @Override
    public View generateView(final int position, ViewGroup parent) {
        View v = LayoutInflater.from(context).inflate(resource, null);
        final SwipeLayout swipeLayout = (SwipeLayout) v.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, v.findViewById(R.id.bottom_wrapper));
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        v.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeLayout.close(false);

                context.db.markDeleteTrackDay(objects.get(position).id);

                objects.remove(position);
                notifyDataSetChanged();
            }
        });
        v.findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeLayout.close(false);
                final Calendar myCalendar = Calendar.getInstance();

                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                        String newDate = format.format(myCalendar.getTime());

                        objects.get(position).date = newDate;
                        context.db.updateTrackDay(objects.get(position));
                        notifyDataSetChanged();
                    }
                };

                new DatePickerDialog(
                        context,
                        date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });
        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {
        TextView textView = (TextView) convertView.findViewById(R.id.surface_text_view);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        Date date = null;
        String newDate;
        try {
            date = format.parse(objects.get(position).date);
            DateFormat format2 = new SimpleDateFormat("dd MMMM, yyyy", Locale.US);
            newDate = format2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            newDate = objects.get(position).date;
        }
        // TODO review
        TextView muscleGroupsTextView = (TextView) convertView.findViewById(R.id.groups);
        muscleGroupsTextView.setText(newDate);

        if (muscleGroupsTextView != null) {
            String groupsString = "";
            ArrayList<Pair<String, String>> muscleGroups = objects.get(position).muscleGroups;
            if (muscleGroups != null) {
                for (int i = 0, n = muscleGroups.size(); i < n; i++) {
                    groupsString += muscleGroups.get(i).first;
                    if (i < n - 1) {
                        groupsString += ", ";
                    }
                }
                textView.setText(groupsString);
            }
        }
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}