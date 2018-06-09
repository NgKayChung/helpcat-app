package com.app.my.firstapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import android.content.SharedPreferences;
import com.google.firebase.database.*;

public class StudentEnroll extends AppCompatActivity {
    private Spinner spinner;
    private Button addSubject_btn, submit_btn;
    private ListView subject_listView;

    private ArrayList<CourseSubject> subjectList, enrolledList;

    private AlertDialog.Builder dBuilder;
    private AlertDialog dialog;
    private View dView;
    private TextView dTitle;
    private TextView desc_txt;

    private SharedPreferences preferences;
    private FirebaseDatabase firebaseDatabase;

    private Student currentStudent;
    private ArrayList<CourseSubject> allsubjects;
    private SubjectEnrollment enrollment;

    private String enrollKey;

    private static final GenericTypeIndicator<ArrayList<CourseSubject>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<CourseSubject>>(){};

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

        subjectList = new ArrayList<CourseSubject>();
        subjectList.add(new CourseSubject("Select", ""));

        firebaseDatabase.getReference("subjects").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allsubjects = dataSnapshot.getValue(genericTypeIndicator);
                subjectList.addAll(allsubjects);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dTitle.setText("Error");
                desc_txt.setText("Connection error");
                dialog.show();
            }
        });

        firebaseDatabase.getReference("users").child(preferences.getString("KEY_ID", null)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentStudent = dataSnapshot.getValue(Student.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dTitle.setText("Error");
                desc_txt.setText("Connection error");
                dialog.show();
            }
        });

        enrollment = new SubjectEnrollment();
        enrollment.setStudentID(preferences.getString("KEY_ID", null));
        enrolledList = new ArrayList<CourseSubject>();

        enrollKey = preferences.getString("KEY_ENROLL", null);

        if(enrollKey == null) {
            DatabaseReference dref = firebaseDatabase.getReference("add_sub_approval").push();
            enrollKey = dref.getKey();
            preferences.edit().putString("KEY_ENROLL", enrollKey).apply();
            dref.setValue(enrollment);
        } else {
            firebaseDatabase.getReference("add_sub_approval").child(enrollKey).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    enrollment = dataSnapshot.getValue(SubjectEnrollment.class);
                    enrolledList.addAll(enrollment.getSubjectList());
                    subjectList.removeAll(enrolledList);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    dTitle.setText("Error");
                    desc_txt.setText("Connection error");
                    dialog.show();
                }
            });
        }

        setContentView(R.layout.activity_student_enroll);

        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CourseSubject> subjectAdapter = new CustomListAdapter(subjectList, R.layout.simple_list_item, StudentEnroll.this);
        spinner.setAdapter(subjectAdapter);

        addSubject_btn = (Button) findViewById(R.id.addBtn);
        addSubject_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spinner.getSelectedItemPosition() != 0) {
                    CourseSubject resultSubject = (CourseSubject) spinner.getSelectedItem();
                    enrollment.addSubject(resultSubject);
                    enrolledList.add(resultSubject);
                    spinner.setSelection(0);
                    subjectList.remove(resultSubject);

                    firebaseDatabase.getReference("add_sub_approval").child(enrollKey).child("subjectList").setValue(enrollment.getSubjectList());
                }
            }
        });

        subject_listView = (ListView) findViewById(R.id.sbjList);
        ArrayAdapter<CourseSubject> adapter = new CustomListAdapter(enrolledList, R.layout.custom_list_item, StudentEnroll.this);
        subject_listView.setAdapter(adapter);

        submit_btn = (Button) findViewById(R.id.smtBtn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enrollment.setStatus(100);
                enrollment.setSubmitted(true);

                firebaseDatabase.getReference("add_sub_approval").child(enrollKey).setValue(enrollment);

                dTitle.setText("Successful");
                desc_txt.setText("Subject(s) are enrolled successfully\nThe enrollment will send to administrator for approval");
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        StudentEnroll.super.onBackPressed();
                    }
                });
            }
        });
    }
}