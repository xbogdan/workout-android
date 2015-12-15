package com.boamfa.workout.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.boamfa.workout.R;
import com.boamfa.workout.activities.InfoActivity;
import com.boamfa.workout.classes.AppTask;
import com.boamfa.workout.classes.Exercise;
import com.boamfa.workout.classes.TrackDayExercise;
import com.boamfa.workout.classes.TrackDayExerciseSet;
import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by bogdan on 30/11/15.
 */
public class TrackDayExerciseAdapter extends BaseExpandableListAdapter {

    private InfoActivity context;
    private Activity activity;
    private ArrayList<TrackDayExercise> groups;

    public TrackDayExerciseAdapter(Context context, ArrayList<TrackDayExercise> groups) {
        this.context = (InfoActivity) context;
        this.activity = (Activity) context;
        this.groups = groups;
    }

    public void addItem(TrackDayExerciseSet item, TrackDayExercise group) {
        if (!groups.contains(group)) {
            groups.add(group);
        }
        int index = groups.indexOf(group);
        ArrayList<TrackDayExerciseSet> ch = groups.get(index).sets;
        ch.add(item);
        groups.get(index).setItems(ch);
    }

    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<TrackDayExerciseSet> chList = groups.get(groupPosition).sets;
        return chList.get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
        TrackDayExerciseSet child = (TrackDayExerciseSet) getChild(groupPosition, childPosition);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.expandlist_set, null);
        }

        final SwipeLayout swipeLayout = (SwipeLayout) view.findViewById(R.id.swipe);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, view.findViewById(R.id.bottom_wrapper));
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        view.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeLayout.close(false);

            }
        });

        TextView tv = (TextView) view.findViewById(R.id.surface_text_view);
        tv.setText(child.reps + " reps  x  " + child.weight + " kg");

        return view;
    }

    public int getChildrenCount(int groupPosition) {
        ArrayList<TrackDayExerciseSet> chList = groups.get(groupPosition).sets;

        return chList.size();

    }

    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    public int getGroupCount() {
        return groups.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(final int groupPosition, boolean isLastChild, View view, ViewGroup parent) {
        TrackDayExercise group = (TrackDayExercise) getGroup(groupPosition);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.expandlist_exercise, null);
        }

        final SwipeLayout swipeLayout = (SwipeLayout) view.findViewById(R.id.swipe);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, view.findViewById(R.id.bottom_wrapper));
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        view.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeLayout.close(false);
                (new DeleteTrackDayExerciseTask(groupPosition)).execute();
            }
        });

        view.findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeLayout.close(false);
                final PopupWindow exercisesPopup = context.getExercisePopup();
                exercisesPopup.showAtLocation(context.getLayout(), Gravity.CENTER, 0, 0);
                context.getExerciseListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        exercisesPopup.dismiss();
                        Exercise selectedExercise = context.getExerciseAdapter().getItem(position);
                        groups.get(groupPosition).name = selectedExercise.name;
                        notifyDataSetChanged();
                        (new UpdateTrackDayExerciseTask(groupPosition, selectedExercise.id)).execute();
                    }
                });
            }
        });

        TextView tv = (TextView) view.findViewById(R.id.surface_text_view);
        tv.setText(group.name);

        return view;
    }

    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return true;
    }

    public boolean isChildSelectable(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return true;
    }

    public class DeleteTrackDayExerciseTask extends AppTask {
        int position;

        public DeleteTrackDayExerciseTask(int position) {
            super((Context) context, context.getUserLocalStore());
            this.position = position;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            response = context.getService().deleteTrackDayExercise(context.getCurrentUser().auth_token, groups.get(position).id);
            return true;
        }

        @Override
        public void onSuccess(String response) {
            groups.remove(position);
            notifyDataSetChanged();
        }
    }

    public class UpdateTrackDayExerciseTask extends AppTask {
        int position;
        int exercise_id;

        public UpdateTrackDayExerciseTask(int position, int exercise_id) {
            super((Context) context, context.getUserLocalStore());
            this.position = position;
            this.exercise_id = exercise_id;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            HashMap<String, String> postParams = new HashMap<String, String>();
            postParams.put("id", groups.get(position).id + "");
            postParams.put("exercise_id", exercise_id + "");
            response = context.getService().updateTrackDayExercise(context.getCurrentUser().auth_token, postParams);
            return true;
        }

        @Override
        public void onSuccess(String response) {
        }
    }

}


