package com.app.my.firstapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.content.SharedPreferences;
import android.widget.TextView;

import java.util.ArrayList;

import com.google.firebase.database.*;

public class StudentEnroll extends AppCompatActivity {
    private Spinner spinner;
    private SpinnerAdapter spinnerAdapter;
    private Button addSubject_btn, dropSubject_btn, submit_btn;
    private ListView subject_listView;
    private ArrayList<String> subjectList, enrolledList;
    private ArrayAdapter<String> adapter;

    private AlertDialog.Builder dBuilder;
    private AlertDialog dialog;
    private View dView;
    private TextView dTitle;
    private TextView desc_txt;

    private SharedPreferences preferences;
    private FirebaseDatabase firebaseDatabase;

    private Student currentStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize dialog box components
        dBuilder = new AlertDialog.Builder(StudentEnroll.this);
        dView = getLayoutInflater().inflate(R.layout.box_dialog, null);
        dTitle = (TextView) dView.findViewById(R.id.dialog_titleTxt);
        desc_txt = (TextView) dView.findViewById(R.id.dialog_descTxt);
        dBuilder.setView(dView);
        dialog = dBuilder.create();

        firebaseDatabase = FirebaseDatabase.getInstance();
        preferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        firebaseDatabase.getReference("users").child(preferences.getString("KEY_ID", null)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentStudent = dataSnapshot.getValue(Student.class);

                if(currentStudent.getSubjectNumber() >= 1) {
                    dTitle.setText("Alert");
                    desc_txt.setText("You have already enrolled subjects for this semester.\nPlease contact administrator if any problem.");
                    dialog.show();

                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            startActivity(new Intent(StudentEnroll.this, NavActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dTitle.setText("Error");
                desc_txt.setText("Connection error");
                dialog.show();
            }
        });

        setContentView(R.layout.activity_student_enroll);

        spinner = (Spinner) findViewById(R.id.spinner);
        subjectList = new ArrayList<String>();
        subjectList.add("Web Java");
        subjectList.add("C++");
        subjectList.add("Java");
        subjectList.add("Java 2");
        subjectList.add("Operating System");

        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(StudentEnroll.this, android.R.layout.simple_dropdown_item_1line, subjectList);
        spinner.setAdapter(subjectAdapter);

        addSubject_btn = (Button) findViewById(R.id.addBtn);

        addSubject_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = spinner.getSelectedItem().toString();
                enrolledList.add(result);
            }
        });

        dropSubject_btn = (Button) findViewById(R.id.drpBtn);
        submit_btn = (Button) findViewById(R.id.smtBtn);
        subject_listView = (ListView) findViewById(R.id.sbjList);

        enrolledList = new ArrayList<String>();
        adapter = new CustomListAdapter(enrolledList, StudentEnroll.this);
        subject_listView.setAdapter(adapter);
    }
}