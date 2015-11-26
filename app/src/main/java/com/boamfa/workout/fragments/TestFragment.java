package com.boamfa.workout.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.boamfa.workout.R;

/**
 * Created by bogdan on 24/11/15.
 */
public class TestFragment extends android.support.v4.app.Fragment {

    private EditText text;
    private Button button;

    MyListener listener;

    public interface MyListener {
        public void createMeme(String text);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (MyListener) ((Activity) context);
        } catch(ClassCastException e) {
            throw new ClassCastException(e.toString());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);

        text = (EditText) view.findViewById(R.id.text_view);
        button = (Button) view.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClicked(v);
            }
        });

        return view;
    }

    private void buttonClicked(View v) {
        listener.createMeme(text.getText().toString());
    }
}
