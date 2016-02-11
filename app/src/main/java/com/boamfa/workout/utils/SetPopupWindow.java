package com.boamfa.workout.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.boamfa.workout.R;

/**
 *
 * Created by bogdan on 11/02/16.
 */
public class SetPopupWindow extends PopupWindow {
    EditText weightField;
    EditText repsField;
    Button closeButton;
    public Button okButton;

    public SetPopupWindow(Context context, int width, int height) {
        super(width, height);

        setFocusable(true);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        RelativeLayout setPopupBg = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.set_popup, null, false);
        setPopupBg.getBackground().setAlpha(400); // Dim the background color
        setContentView(setPopupBg);

        weightField = (EditText) getContentView().findViewById(R.id.set_weight);
        repsField = (EditText) getContentView().findViewById(R.id.set_reps);


        // Close popup button
        closeButton = (Button) setPopupBg.findViewById(R.id.set_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // Close popup button
        okButton = (Button) setPopupBg.findViewById(R.id.set_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void showCenter(View v) {
        showAtLocation(v, Gravity.CENTER, 0, 0);
    }

    public double getWeight() {
        return Double.parseDouble(weightField.getText().toString());
    }

    public int getReps() {
        return Integer.parseInt(repsField.getText().toString());
    }

    public void setReps(int reps) {
        repsField.setText(reps + "");
    }

    public void setWeight(double weight) {
        weightField.setText(weight + "");
    }
}
