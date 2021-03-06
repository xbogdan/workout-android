package com.boamfa.workout.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.boamfa.workout.R;
import com.boamfa.workout.activities.TracksActivity;

/**
 * Created by bogdan on 23/11/15.
 */
public class DrawerAdapter extends BaseAdapter {
    private Context context;
    private String[] items;

    public DrawerAdapter(Context context) {
        this.context = context;
        this.items = context.getResources().getStringArray( R.array.drawer_items );
    }

    @Override
    public int getCount() {
        return this.items.length;
    }

    @Override
    public Object getItem(int position) {
        return this.items[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.drawer_item, parent, false);
        } else {
            row = convertView;
        }
        TextView itemTitle = (TextView) row.findViewById(R.id.textView);
        itemTitle.setText(this.items[position]);
        if (this.items[position].equals("Tracker")) {
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, TracksActivity.class);
                    context.startActivity(i);
                }
            });
        }
        return row;
    }
}
