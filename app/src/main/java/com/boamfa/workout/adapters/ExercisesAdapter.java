package com.boamfa.workout.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import com.boamfa.workout.R;
import com.boamfa.workout.classes.Exercise;
import com.boamfa.workout.database.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bogdan on 13/12/15.
 * Exercises adapter
 */
public class ExercisesAdapter extends BaseAdapter implements Filterable {
    private List<Exercise> mObjects;
    private List<Exercise> mOriginalValues;
    private Context mContext;
    private boolean removeAction;

    OnAdapterActivityInteractionListener mActivityListener;
    OnAdapterInteractionListener mFragmentListener;

    public interface OnAdapterInteractionListener {
        void setFavorite(Exercise exercise);
        void removeFavorite(Exercise exercise);
    }

    public interface OnAdapterActivityInteractionListener {
        void onExerciseClick(Exercise exercise);
    }

    public ExercisesAdapter(Context context, List<Exercise> objects, boolean removeAction, Fragment fragmentListener, Activity activityListener) {
        this.mObjects = objects;
        this.mContext = context;
        this.removeAction = removeAction;
        this.mFragmentListener = (OnAdapterInteractionListener) fragmentListener;
        this.mActivityListener = (OnAdapterActivityInteractionListener) activityListener;
    }

    @Override
    public int getCount() {
        if (mObjects != null) {
            return mObjects.size();
        } else {
            return 0;
        }
    }

    @Override
    public Exercise getItem(int position) {
        return mObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.exercise_item, parent, false);
        }

        final Exercise exercise = getItem(position);

        TextView exerciseNameTextView = (TextView) convertView.findViewById(R.id.exercise_name);
        exerciseNameTextView.setText(exercise.name);
        exerciseNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivityListener.onExerciseClick(exercise);
            }
        });

        final ImageButton save = (ImageButton) convertView.findViewById(R.id.save);
        if (exercise.favorite) {
            save.setImageResource(R.drawable.star);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHandler db = new DatabaseHandler(mContext);
                    db.deleteFavoriteExercise(exercise.id);
                    save.setImageResource(R.drawable.star_outline);
                    mObjects.get(position).favorite = false;
                    if (removeAction) mObjects.remove(position);
                    notifyDataSetChanged();
                    mFragmentListener.removeFavorite(exercise);
                }
            });
        } else {
            save.setImageResource(R.drawable.star_outline);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHandler db = new DatabaseHandler(mContext);
                    db.addFavoriteExercise(exercise.id);
                    save.setImageResource(R.drawable.star);
                    mObjects.get(position).favorite = true;
                    notifyDataSetChanged();
                    mFragmentListener.setFavorite(exercise);
                }
            });
        }

        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                ArrayList<Exercise> tempList = new ArrayList<Exercise>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<Exercise>(mObjects);
                }

                if (constraint == null || constraint.length() == 0) {
                    // set the Original result to return
                    if (mOriginalValues != null) {
                        filterResults.count = mOriginalValues.size();
                    } else {
                        filterResults.count = 0;
                    }
                    filterResults.values = mOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0, length = mOriginalValues.size(); i < length; i++) {
                        Exercise item = mOriginalValues.get(i);
                        if (item.name.toLowerCase().contains(constraint.toString())) {
                            tempList.add(item);
                        }
                    }
                    filterResults.values = tempList;
                    filterResults.count = tempList.size();
                }
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence contraint, FilterResults results) {
                mObjects = (ArrayList<Exercise>) results.values;
                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return myFilter;
    }
}
