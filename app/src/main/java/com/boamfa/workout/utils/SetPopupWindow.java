package com.boamfa.workout.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.boamfa.workout.R;

/**
 *
 * Created by bogdan on 11/02/16.
 */
public class SetPopupWindow {
    EditText weightField;
    EditText repsField;
    AlertDialog dialog;
    AlertDialog.Builder builder;
    Button okButton=null;

    public SetPopupWindow(Context context) {
        builder = new AlertDialog.Builder(context);

        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        // Set other dialog properties
        builder.setTitle("Add new set");
        RelativeLayout view = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.set_popup2, null, false);
        builder.setView(view);

        // Create the AlertDialog
        dialog = builder.create();

        weightField = (EditText) view.findViewById(R.id.set_weight);
        repsField = (EditText) view.findViewById(R.id.set_reps);
    }

    public void setPositiveButtonOnClickListener(View.OnClickListener listener) {
        okButton.setOnClickListener(listener);
    }

    public void show() {
        dialog.show();
        if (okButton == null) {
            okButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        }
    }

    public void hide() {
        dialog.hide();
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
