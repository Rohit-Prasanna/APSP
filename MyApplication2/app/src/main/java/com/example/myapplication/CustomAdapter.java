
package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CustomAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;
    private final Class next;

    public CustomAdapter(Context context, String[] values, Class next) {
        super(context, R.layout.custom_grid_item, values);
        this.context = context;
        this.values = values;
        this.next = next ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;
        if (convertView == null) {
            gridView = inflater.inflate(R.layout.custom_grid_item, null);
            Button button = gridView.findViewById(R.id.gridItemButton);
            button.setOnClickListener(view -> {
                Intent intent = new Intent(context,next);
                context.startActivity(intent);

            });
            button.setText(values[position]);
        } else {
            gridView = convertView;
        }
        return gridView;
    }
}