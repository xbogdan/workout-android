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
import com.boamfa.workout.classes.Track;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.List;

/**
 * Swipe adapter
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

                context.db.markDeleteTrack(objects.get(position).id);

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
                        objects.get(position).name = m_Text;

                        context.db.updateTrack(objects.get(position));

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
}
