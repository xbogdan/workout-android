package com.boamfa.workout.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.boamfa.workout.fragments.AllExercisesFragment;
import com.boamfa.workout.fragments.FavoriteExercisesFragment;
import com.boamfa.workout.fragments.UserExercisesFragment;

/**
 * Created by bogdan on 08/02/16.
 * Page adapter for managing tab layouts
 */
public class ExercisesPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "Exercises", "Favorites", "My exercises" };
    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();


    public ExercisesPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return AllExercisesFragment.newInstance(position + 1);
            case 1:
                return FavoriteExercisesFragment.newInstance(position + 1);
            case 2:
                return UserExercisesFragment.newInstance(position + 1);
            default:
                return AllExercisesFragment.newInstance(position + 1);
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}