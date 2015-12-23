package com.boamfa.workout.activities;

import android.support.v4.widget.DrawerLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.boamfa.workout.adapters.ExercisesAdapter;
import com.boamfa.workout.classes.User;
import com.boamfa.workout.classes.UserLocalStore;
import com.boamfa.workout.database.DatabaseHandler;
import com.boamfa.workout.utils.AppService;

public interface InfoActivity {
    ExercisesAdapter getExerciseAdapter();

    DrawerLayout getLayout();

    PopupWindow getExercisePopup();

    ListView getExerciseListView();

    DatabaseHandler getDBHandler();
}
