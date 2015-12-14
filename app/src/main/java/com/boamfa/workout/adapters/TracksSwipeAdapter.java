package com.boamfa.workout.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.boamfa.workout.R;
import com.boamfa.workout.activities.BaseActivity;
import com.boamfa.workout.classes.AppTask;
import com.boamfa.workout.classes.Track;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.HashMap;
import java.util.List;

/**
 * Created by bogdan on 02/12/15.
 */
public class TracksSwipeAdapter extends BaseSwipeAdapter {
    private BaseActivity context;
    private List<Track> objects;
    private int resource;
    private int textViewResourceId;

    public TracksSwipeAdapter(Context context, int resource, int textViewResourceId, List<Track> objects) {
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

                DeleteTrackTask task = new DeleteTrackTask(objects.get(position).id);
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
                        String m_Text = input.getText().toString();
                        (new UpdateTrackNameTask(objects.get(position).id, m_Text)).execute();
                        objects.get(position).name = m_Text;
                        notifyDataSetChanged();
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
        textView.setText(objects.get(position).name);
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


    public class DeleteTrackTask extends AppTask {
        private int trackId;

        public DeleteTrackTask(int trackId) {
            super(context, context.userLocalStore);
            this.trackId = trackId;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            response = context.service.deleteTrack(context.currentUser.auth_token, trackId);
            return true;
        }

        @Override
        public void onSuccess(String response) {

        }
    }

    public class UpdateTrackNameTask extends AppTask {
        private int trackId;
        private String trackName;

        public UpdateTrackNameTask(int trackId, String trackName) {
            super(context, context.userLocalStore);
            this.trackId = trackId;
            this.trackName = trackName;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            HashMap<String, String> postParams = new HashMap<String, String>();
            postParams.put("track[id]", trackId + "");
            postParams.put("track[name]",trackName);
            response = context.service.updateTrack(context.currentUser.auth_token, postParams);
            return true;
        }

        @Override
        public void onSuccess(String response) {

        }
    }
}
