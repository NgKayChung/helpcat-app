package com.app.my.firstapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.api.services.calendar.Calendar;

import java.util.ArrayList;

public class AdminChangePass extends AppCompatActivity {
    private ListView cpl;
    ArrayList<String> std = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_change_pass);

        cpl=(ListView) findViewById(R.id.changePassList);
        String[] stdID={"1","2","3","4"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.pas_list_row,R.id.stdID,stdID);
        cpl.setAdapter(adapter);


    }
}
