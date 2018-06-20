package com.app.my.firstapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;

import java.util.ArrayList;

public class RegisterNewStudentActivity extends AppCompatActivity {
    private Spinner genderSpinner, nationalitySpinner, programmeSpinner, intakeMonthSpinner, intakeYearSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new_student);

        genderSpinner = (Spinner) findViewById(R.id.gender_spinner);
        nationalitySpinner = (Spinner) findViewById(R.id.nationality_spinner);
        programmeSpinner = (Spinner) findViewById(R.id.programme_spinner);
        intakeMonthSpinner = (Spinner) findViewById(R.id.intakeMonth_spinner);
        intakeYearSpinner = (Spinner) findViewById(R.id.intakeYear_spinner);

        ArrayList<String> genderList = new ArrayList<>();
        genderList.add("Select");
        genderList.add("Male");
        genderList.add("Female");

        genderSpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, genderList));

        ArrayList<String> nationalityList = new ArrayList<>();
        nationalityList.add("Select");
        nationalityList.add("Malaysia");
        nationalityList.add("Indonesia");
        nationalityList.add("Thailand");
        nationalityList.add("Laos");
        nationalityList.add("Pakistan");
        nationalityList.add("Bangladesh");

        nationalitySpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, nationalityList));

        ArrayList<String> programmeList = new ArrayList<>();
        programmeList.add("Select");
        programmeList.add("Diploma in Computer Science");
        programmeList.add("Diploma in Business Information System");

        programmeSpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, programmeList));

        // varies by programme
        ArrayList<String> intakeMonthList = new ArrayList<>();
        intakeMonthList.add("Select");
        intakeMonthList.add("January");
        intakeMonthList.add("April");
        intakeMonthList.add("August");

        intakeMonthSpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, intakeMonthList));

        ArrayList<String> intakeYearList = new ArrayList<>();
        intakeYearList.add("Select");
        intakeYearList.add("2018");
        intakeYearList.add("2019");
        intakeYearList.add("2020");

        intakeYearSpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, intakeYearList));
    }
}
