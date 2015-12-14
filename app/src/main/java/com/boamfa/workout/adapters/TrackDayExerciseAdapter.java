package com.boamfa.workout.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.boamfa.workout.R;
import com.boamfa.workout.activities.BaseActivity;
import com.boamfa.workout.classes.AppTask;
import com.boamfa.workout.classes.TrackDayExercise;
import com.boamfa.workout.classes.TrackDayExerciseSet;
import com.boamfa.workout.classes.User;
import com.boamfa.workout.classes.UserLocalStore;
import com.boamfa.workout.utils.AppService;
import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;

/**
 * Created by bogdan on 30/11/15.
 */
public class TrackDayExerciseAdapter extends BaseExpandableListAdapter {

    private BaseActivity context;
    private ArrayList<TrackDayExercise> groups;
    private AppService service;
    private User currentUser;
    private UserLocalStore userLocalStore;

    public TrackDayExerciseAdapter(Context context, ArrayList<TrackDayExercise> groups, AppService service, User currentUser, UserLocalStore userLocalStore) {
        this.context = (BaseActivity) context;
        this.groups = groups;
        this.service = service;
        this.currentUser = currentUser;
        this.userLocalStore = userLocalStore;
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
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.expandlist_set, null);
        }

        final SwipeLayout swipeLayout = (SwipeLayout) view.findViewById(R.id.swipe);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, view.findViewById(R.id.bottom_wrapper));
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        view.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                swipeLayout.close(false);

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
            LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.expandlist_exercise, null);
        }

        final SwipeLayout swipeLayout = (SwipeLayout) view.findViewById(R.id.swipe);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, view.findViewById(R.id.bottom_wrapper));
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        view.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeLayout.close(false);
                (new DeleteExerciseTask(groupPosition)).execute();

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

    public class DeleteExerciseTask extends AppTask {
        int position;

        public DeleteExerciseTask(int position) {
            super(context, userLocalStore);
            this.position = position;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            response = service.deleteTrackDayExercise(currentUser.auth_token, groups.get(position).id);
            return true;
        }

        @Override
        public void onSuccess(String response) {
            groups.remove(position);
            notifyDataSetChanged();
        }
    }

}


