package com.boamfa.workout.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.boamfa.workout.R;
import com.boamfa.workout.activities.BaseActivity;
import com.boamfa.workout.classes.AppTask;
import com.boamfa.workout.classes.TrackDay;
import com.boamfa.workout.utils.AppService;
import com.boamfa.workout.utils.User;
import com.boamfa.workout.utils.UserLocalStore;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private int trackId;
    private int textViewResourceId;

    public TrackSwipeAdapter(Context context, int resource, int textViewResourceId, int trackId, List<TrackDay> objects) {
        this.trackId = trackId;
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

                DeleteTask task = new DeleteTask(trackId, objects.get(position).id);
                task.execute();

                objects.remove(position);
                notifyDataSetChanged();
            }
        });
        v.findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeLayout.close(false);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Track name");

                // Set up the input
                final EditText input = new EditText(context);
                input.setTextColor(context.getResources().getColor(R.color.black));
                input.setText(objects.get(position).name);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        String m_Text = input.getText().toString();
//                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {
        TextView textView = (TextView) convertView.findViewById(R.id.surface_text_view);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        Date date = null;
        try {
            date = format.parse(objects.get(position).date);
        } catch (ParseException e) {
            e.printStackTrace();
            // TODO Handle exception
        }

        DateFormat format2 = new SimpleDateFormat("dd MMMM, yyyy", Locale.US);
        String newDate = format2.format(date);
        textView.setText(newDate);
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

    public class DeleteTask extends AppTask {
        private int trackId;
        private int trackDayId;

        public DeleteTask(int trackId, int trackDayId) {
            super(context, context.userLocalStore);
            this.trackId = trackId;
            this.trackDayId = trackDayId;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            response = context.service.deleteTrackDay(context.currentUser.auth_token, trackId, trackDayId);
            return true;
        }

        @Override
        public void onSuccess(String response) {

        }
    }
}
