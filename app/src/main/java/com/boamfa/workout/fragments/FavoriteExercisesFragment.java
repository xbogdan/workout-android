package com.boamfa.workout.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.boamfa.workout.R;
import com.boamfa.workout.adapters.ExercisesAdapter;
import com.boamfa.workout.classes.Exercise;
import com.boamfa.workout.database.DatabaseHandler;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FavoriteExercisesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FavoriteExercisesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteExercisesFragment extends Fragment implements ExercisesAdapter.OnAdapterInteractionListener {
    public static List<Exercise> exercisesList;
    public static ExercisesAdapter exercisesAdapter;

    private OnFragmentInteractionListener mListener;

    public FavoriteExercisesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param page int.
     * @return A new instance of fragment AllExercisesFragment.
     */
    public static Fragment newInstance(int page) {
        Bundle args = new Bundle();
        FavoriteExercisesFragment fragment = new FavoriteExercisesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListener = (OnFragmentInteractionListener) getActivity();
        if (getArguments() != null) {}

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_exercises, container, false);

        DatabaseHandler db = new DatabaseHandler(getActivity());
        exercisesList = db.getFavoriteExercises();
        exercisesAdapter = new ExercisesAdapter(getContext(), exercisesList, true, this, getActivity());
        ListView exercisesListView = (ListView) view.findViewById(R.id.favorite_exercises_list);
        exercisesListView.setAdapter(exercisesAdapter);

        EditText exerciseSearch = (EditText) view.findViewById(R.id.exercises_search);
        exerciseSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                exercisesAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return view;
    }

    public void setFavoriteExercise(Exercise exercise) {
        for (int i=0, n = exercisesList.size(); i < n; i++) {
            if (exercisesList.get(i).id == exercise.id) {
                exercisesList.get(i).favorite = true;
                exercisesAdapter.notifyDataSetChanged();
                return;
            }
        }

        exercise.favorite = true;
        exercisesList.add(exercise);
        exercisesAdapter.notifyDataSetChanged();
    }

    public void removeFavoriteExercise(Exercise exercise) {
        for (int i=0, n = exercisesList.size(); i < n; i++) {
            if (exercisesList.get(i).id == exercise.id) {
                exercisesList.remove(i);
                exercisesAdapter.notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    public void setFavorite(Exercise exercise) {}

    @Override
    public void removeFavorite(Exercise exercise) {
        mListener.removeFavoriteFavorites(exercise);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void removeFavoriteFavorites(Exercise exercise);
    }
}
