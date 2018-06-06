package com.app.my.firstapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<String> {

    private ArrayList<String> subjectList;
    Context mContext;

    public CustomListAdapter(ArrayList<String> data, Context context) {
        super(context, R.layout.custom_list_item, data);
        this.subjectList = data;
        this.mContext=context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // Get the data item for this position
        String subjectName = getItem(position);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        view = inflater.inflate(R.layout.custom_list_item, parent, false);
        TextView subjectName_txt = (TextView) view.findViewById(R.id.subjectTitle);
        subjectName_txt.setText(subjectName);

        // Return the completed view to render on screen
        return view;
    }
}