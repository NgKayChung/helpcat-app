package com.app.my.helpcatapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;

// a custom array list adapter with button which inherits CustomListAdapter
// used in StudentEnrollActivity for displaying list of added subjects for enrolled
// button for remove subject from enrollment list
public class CustomListWithButtonAdapter extends CustomListAdapter {
    private CustomClickListener btnClickListener;

    public CustomListWithButtonAdapter(ArrayList<CourseSubject> data, int RLayout, Context context, CustomClickListener btnClickListener) {
        super(data, RLayout, context);
        this.btnClickListener = btnClickListener;
    }

    @Override
    public CourseSubject getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = super.getView(position, view, parent);

        ImageButton removeButton = (ImageButton) view.findViewById(R.id.removeSubjectButton);
        removeButton.setTag(position);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnClickListener != null)
                    btnClickListener.onClick((Integer) v.getTag());
            }
        });

        // Return the completed view to render on screen
        return view;
    }
}
