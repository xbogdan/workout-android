package com.boamfa.workout.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.boamfa.workout.R;
import com.boamfa.workout.classes.TrackDayExercise;
import com.boamfa.workout.classes.TrackDayExerciseSet;
import com.daimajia.swipe.SwipeLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Adapter for track day exercises + sets
 * Created by bogdan on 30/11/15.
 */
public class TrackDayExerciseAdapter extends BaseExpandableListAdapter {

    public interface ExpandableListActions {
        void deleteGroup(int groupPosition);
        void editGroup(int groupPosition);

        void addChild(int groupPosition, ImageView groupIndicator);
        void deleteChild(int groupPosition, int childPosition);
        void editChild(int groupPosition, int childPosition);

        void showSetPopup();
        void closeSetPopup();
    }

    private ArrayList<TrackDayExercise> groups;
    private ExpandableListActions actions;
    private Context context;

    public TrackDayExerciseAdapter(ExpandableListActions actions, ArrayList<TrackDayExercise> groups) {
        this.actions = actions;
        this.context = (Context) actions;
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

    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View view, ViewGroup parent) {
        TrackDayExerciseSet child = (TrackDayExerciseSet) getChild(groupPosition, childPosition);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.expandlist_set, null);
        }

        final SwipeLayout swipeLayout = (SwipeLayout) view.findViewById(R.id.swipe);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, view.findViewById(R.id.bottom_wrapper));
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        view.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeLayout.close(false);
                actions.deleteChild(groupPosition, childPosition);
            }
        });

        view.findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeLayout.close(false);
                actions.editChild(groupPosition, childPosition);
            }
        });

        TextView tv = (TextView) view.findViewById(R.id.surface_text_view);
        DecimalFormat df = new DecimalFormat("###.#");
        tv.setText(df.format(child.weight) + " kg  x  " + child.reps + " reps");

        return view;
    }

    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).sets.size();
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
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.expandlist_exercise, null);
        }

        final ImageView groupIndicator = (ImageView) view.findViewById(R.id.group_indicator);
        if (groupIndicator.getDrawable() == null) {
            groupIndicator.setImageDrawable(context.getResources().getDrawable(R.drawable.arrow_right));
            if (groups.get(groupPosition).sets.size() == 0) {
                groupIndicator.setImageAlpha(0); // TODO fix version compatibility
            }
        }

        final SwipeLayout swipeLayout = (SwipeLayout) view.findViewById(R.id.swipe);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, view.findViewById(R.id.bottom_wrapper));
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        view.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeLayout.close(false);
                actions.deleteGroup(groupPosition);
            }
        });

        view.findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeLayout.close(false);
                actions.editGroup(groupPosition);
            }
        });

        TextView tv = (TextView) view.findViewById(R.id.surface_text_view);
        tv.setText(group.name);

        Button addSetbutton = (Button) view.findViewById(R.id.add_set);
        addSetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actions.addChild(groupPosition, groupIndicator);
            }
        });

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
}


