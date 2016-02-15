package com.boamfa.workout.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.boamfa.workout.R;
import com.boamfa.workout.adapters.ExercisesAdapter;
import com.boamfa.workout.adapters.TrackDayExerciseAdapter;
import com.boamfa.workout.classes.Exercise;
import com.boamfa.workout.classes.TrackDayExercise;
import com.boamfa.workout.classes.TrackDayExerciseSet;
import com.boamfa.workout.fragments.AllExercisesFragment;
import com.boamfa.workout.fragments.ExercisesFragment;
import com.boamfa.workout.fragments.FavoriteExercisesFragment;
import com.boamfa.workout.utils.SetPopupWindow;

import java.util.ArrayList;

public class TrackDaysActivity extends BaseActivity implements TrackDayExerciseAdapter.ExpandableListActions,
        AllExercisesFragment.OnFragmentInteractionListener, FavoriteExercisesFragment.OnFragmentInteractionListener,
        ExercisesAdapter.OnAdapterActivityInteractionListener {

    private long trackDayId;
    private ArrayList<TrackDayExercise> trackDayExercises;

    private SetPopupWindow stw;

    private ExpandableListView trackDayExercisesListView;
    private TrackDayExerciseAdapter trackDayExercisesAdapter;

    private View contentView;

    private FloatingActionButton floatingActionButton;

    private Integer editingGroup = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.activity_track_days, null, false);
        drawerLayout.addView(contentView, 0);

        trackDayId = getIntent().getLongExtra("track_day_id", -1);
        trackDayExercises = db.getTrackDayExercises(trackDayId);

        trackDayExercisesListView = (ExpandableListView) findViewById(R.id.expandableList);
        trackDayExercisesAdapter = new TrackDayExerciseAdapter(TrackDaysActivity.this, trackDayExercises);
        trackDayExercisesListView.setAdapter(trackDayExercisesAdapter);
        trackDayExercisesListView.setGroupIndicator(null);
        trackDayExercisesListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });
        trackDayExercisesListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                ImageView groupIndicator = (ImageView) v.findViewById(R.id.group_indicator);
                if (trackDayExercises.get(groupPosition).sets.size() > 0) {
                    groupIndicator.setImageAlpha(1000); // TODO fix version compatibility
                    if (parent.isGroupExpanded(groupPosition)) {
                        groupIndicator.setImageDrawable(getResources().getDrawable(R.drawable.arrow_right));
                    } else {
                        groupIndicator.setImageDrawable(getResources().getDrawable(R.drawable.arrow_down));
                    }
                }
                return false;
            }
        });

        hideExercises();

        // Open pupup button
        floatingActionButton = (FloatingActionButton) contentView.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExercises();
                floatingActionButton.hide();
            }
        });

        // Create popup window
        stw = new SetPopupWindow(this, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
    }

    private void showExercises() {
        FragmentManager fm = getSupportFragmentManager();
        final Fragment f = fm.findFragmentById(R.id.fragment);
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.addToBackStack("");
        ft.show(f);
        ft.commit();
        drawerListener.setDrawerIndicatorEnabled(false);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    private void hideExercises() {
        FragmentManager fm = getSupportFragmentManager();
        final Fragment f = fm.findFragmentById(R.id.fragment);
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        fm.popBackStack();
        ft.hide(f);
        ft.commit();
        getSupportActionBar().show();
        drawerListener.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public void deleteGroup(int groupPosition) {
        db.deleteTrackDayExercise(trackDayExercises.get(groupPosition).id);
        trackDayExercises.remove(groupPosition);
        trackDayExercisesAdapter.notifyDataSetChanged();
    }

    @Override
    public void editGroup(final int groupPosition) {
        showExercises();
        floatingActionButton.hide();
        editingGroup = groupPosition;
    }

    @Override
    public void addChild(final int groupPosition, final ImageView groupIndicator) {
        stw.showCenter(drawerLayout);
        stw.okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stw.dismiss();

                TrackDayExerciseSet trackDayExerciseSet = new TrackDayExerciseSet(stw.getReps(), stw.getWeight(), trackDayExercises.get(groupPosition).id);
                trackDayExerciseSet.id = db.addTrackDayExerciseSet(trackDayExerciseSet);
                trackDayExercises.get(groupPosition).sets.add(trackDayExerciseSet);
                trackDayExercisesAdapter.notifyDataSetChanged();
                groupIndicator.setImageAlpha(1000); // TODO fix version compatibility
            }
        });
    }

    @Override
    public void deleteChild(int groupPosition, int childPosition) {
        db.deleteTrackDayExerciseSet(trackDayExercises.get(groupPosition).sets.get(childPosition).id);
        trackDayExercises.get(groupPosition).sets.remove(childPosition);
        trackDayExercisesAdapter.notifyDataSetChanged();
    }

    @Override
    public void editChild(final int groupPosition, final int childPosition) {
        stw.showCenter(drawerLayout);
        final TrackDayExerciseSet trackDayExerciseSet = trackDayExercises.get(groupPosition).sets.get(childPosition);

        stw.setWeight(trackDayExerciseSet.weight);
        stw.setReps(trackDayExerciseSet.reps);

        stw.okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stw.dismiss();

                trackDayExerciseSet.reps = stw.getReps();
                trackDayExerciseSet.weight = stw.getWeight();
                db.updateTrackDayExerciseSet(trackDayExerciseSet);
                trackDayExercisesAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onExerciseClick(final Exercise exercise) {
        if (editingGroup != null) {
            TrackDayExercise trackDayExercise = trackDayExercises.get(editingGroup);
            trackDayExercise.name = exercise.name;
            trackDayExercise.exerciseId = exercise.id;
            db.updateTrackDayExercise(trackDayExercise);
            trackDayExercisesAdapter.notifyDataSetChanged();
            editingGroup = null;
            hideExercises();
            floatingActionButton.show();
        } else {
            stw.showCenter(drawerLayout);
            floatingActionButton.hide();
            stw.okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Add track day exercise
                    TrackDayExercise trackDayExercise = new TrackDayExercise(exercise.name, exercise.id, trackDayId);
                    trackDayExercise.id = db.addTrackDayExercise(trackDayExercise);

                    // Add track day exercise set
                    TrackDayExerciseSet trackDayExerciseSet = new TrackDayExerciseSet(stw.getReps(), stw.getWeight(), trackDayExercise.id);
                    trackDayExercise.sets.add(trackDayExerciseSet);
                    db.addTrackDayExerciseSet(trackDayExerciseSet);

                    trackDayExercises.add(trackDayExercise);
                    trackDayExercisesAdapter.notifyDataSetChanged();
                    stw.dismiss();

                    // Hide fragment
                    hideExercises();
                    floatingActionButton.show();
                }
            });
        }
    }

    @Override
    public void setFavoriteAll(Exercise exercise) {
        ExercisesFragment ef = (ExercisesFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        ((FavoriteExercisesFragment) ef.pagerAdapter.getRegisteredFragment(1)).setFavoriteExercise(exercise);
    }

    @Override
    public void removeFavoriteAll(Exercise exercise) {
        ExercisesFragment ef = (ExercisesFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        ((FavoriteExercisesFragment) ef.pagerAdapter.getRegisteredFragment(1)).removeFavoriteExercise(exercise);
    }

    @Override
    public void removeFavoriteFavorites(Exercise exercise) {
        ExercisesFragment ef = (ExercisesFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        ((AllExercisesFragment) ef.pagerAdapter.getRegisteredFragment(0)).removeFavoriteExercise(exercise);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            floatingActionButton.show();
            drawerListener.setDrawerIndicatorEnabled(true);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }
}
