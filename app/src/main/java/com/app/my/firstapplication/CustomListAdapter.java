package com.app.my.firstapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<CourseSubject> {

    private ArrayList<CourseSubject> subjectList;
    private Context mContext;
    private int layout;

    public CustomListAdapter(ArrayList<CourseSubject> data, int RLayout, Context context) {
        super(context, RLayout, data);
        this.subjectList = data;
        this.mContext = context;
        this.layout = RLayout;
    }

    @Override
    public CourseSubject getItem(int position) {
        return subjectList.get(position);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // Get the data item for this position
        String itemName = subjectList.get(position).toString();

        LayoutInflater inflater = LayoutInflater.from(getContext());
        view = inflater.inflate(this.layout, parent, false);
        TextView itemName_txt = (TextView) view.findViewById(R.id.subjectText);
        itemName_txt.setText(itemName);

        // Return the completed view to render on screen
        return view;
    }
}