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
import android.widget.TextView;

import java.util.ArrayList;

import android.content.SharedPreferences;
import com.google.firebase.database.*;

public class StudentEnroll extends AppCompatActivity {
    private Spinner spinner;
    private Button addSubject_btn, submit_btn;
    private ListView subject_listView;

    private ArrayList<CourseSubject> subjectList, enrolledList;
    private ArrayAdapter<CourseSubject> spinnerAdapter, enrolledListAdapter;

    private AlertDialog.Builder dBuilder;
    private AlertDialog dialog;
    private View dView;
    private TextView dTitle;
    private TextView desc_txt;

    private SharedPreferences preferences;
    private FirebaseDatabase firebaseDatabase;

    //private Student currentStudent;
    private SubjectEnrollment enrollment;

    private ArrayList<CourseSubject> allsubjects;

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

        setContentView(R.layout.activity_student_enroll);

        subjectList = new ArrayList<CourseSubject>();
        subjectList.add(new CourseSubject("Select", ""));

        spinner = (Spinner) findViewById(R.id.spinner);
        spinnerAdapter = new CustomListAdapter(subjectList, R.layout.simple_list_item, StudentEnroll.this);
        spinner.setAdapter(spinnerAdapter);

        enrolledList = new ArrayList<CourseSubject>();

        subject_listView = (ListView) findViewById(R.id.sbjList);
        enrolledListAdapter = new CustomListWithButtonAdapter(enrolledList, R.layout.custom_list_item, StudentEnroll.this, new CustomClickListener() {
            @Override
            public void onClick(int position) {
                enrollment.removeSubject(enrolledList.get(position));
                spinner.setSelection(0);

                firebaseDatabase.getReference("add_sub_approval").child(preferences.getString("KEY_ID", null)).child("subjectList").setValue(enrollment.getSubjectList());
            }
        });
        subject_listView.setAdapter(enrolledListAdapter);

        firebaseDatabase.getReference("subjects").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allsubjects = dataSnapshot.getValue(genericTypeIndicator);
                subjectList.addAll(allsubjects);
                subjectList.removeAll(enrolledList);
                spinnerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dTitle.setText("Error");
                desc_txt.setText("Connection error");
                dialog.show();
            }
        });

//        firebaseDatabase.getReference("users").child(preferences.getString("KEY_ID", null)).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                currentStudent = dataSnapshot.getValue(Student.class);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                dTitle.setText("Error");
//                desc_txt.setText("Connection error");
//                dialog.show();
//            }
//        });

        enrollment = new SubjectEnrollment();
        enrollment.setStudentID(preferences.getString("KEY_ID", null));

        firebaseDatabase.getReference("add_sub_approval").orderByChild("studentID").equalTo(preferences.getString("KEY_ID", null)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() <= 0) {
                    DatabaseReference dref = firebaseDatabase.getReference("add_sub_approval").child(preferences.getString("KEY_ID", null));
                    dref.setValue(enrollment);
                }

                firebaseDatabase.getReference("add_sub_approval").child(preferences.getString("KEY_ID", null)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        enrollment = dataSnapshot.getValue(SubjectEnrollment.class);

                        enrolledList.clear();
                        enrolledList.addAll(enrollment.getSubjectList());

                        subjectList.clear();
                        subjectList.add(new CourseSubject("Select", ""));
                        if(allsubjects != null) {
                            subjectList.addAll(allsubjects);
                            subjectList.removeAll(enrolledList);
                        }
                        spinnerAdapter.notifyDataSetChanged();
                        enrolledListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        dTitle.setText("Error");
                        desc_txt.setText("Connection error");
                        dialog.show();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dTitle.setText("Error");
                desc_txt.setText("Error : " + databaseError.getMessage());
                dialog.show();
            }
        });

        addSubject_btn = (Button) findViewById(R.id.addBtn);
        addSubject_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spinner.getSelectedItemPosition() != 0) {
                    CourseSubject resultSubject = (CourseSubject) spinner.getSelectedItem();
                    enrollment.addSubject(resultSubject);

                    spinner.setSelection(0);

                    firebaseDatabase.getReference("add_sub_approval").child(preferences.getString("KEY_ID", null)).child("subjectList").setValue(enrollment.getSubjectList());
                }
            }
        });

        submit_btn = (Button) findViewById(R.id.smtBtn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enrollment.setStatus(100);
                enrollment.setSubmitted(true);

                firebaseDatabase.getReference("add_sub_approval").child(preferences.getString("KEY_ID", null)).setValue(enrollment);

                dTitle.setText("Successful");
                desc_txt.setText("Subject(s) are enrolled successfully\nThe enrollment will send to administrator for approval");
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        startActivity(new Intent(StudentEnroll.this, NavActivity.class));
                        finish();
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(StudentEnroll.this, NavActivity.class));
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}