package com.app.my.helpcatapp;

import android.content.Context;
import android.view.*;
import android.widget.*;

import java.util.ArrayList;

// a custom array list adapter
// used in StudentEnrollActivity for select subject
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

    public int getLayout() {
        return this.layout;
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