package com.app.my.firstapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.*;

public class MainActivity extends AppCompatActivity {
    private ToggleButton studentToggBtn, lecturerToggBtn;
    private TextView id_lbl, pass_lbl;
    private EditText id_txt, pass_txt;
    private Button loginButton;

    private AlertDialog.Builder dBuilder;
    private AlertDialog dialog;
    private View dView;
    private TextView dTitle;
    private TextView desc_txt;

    private boolean isStudent;

    private FirebaseDatabase firebase;
    private DatabaseReference database;

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //find view components
        studentToggBtn = (ToggleButton) findViewById(R.id.studentToggle);
        lecturerToggBtn = (ToggleButton) findViewById(R.id.lecturerToggle);
        id_lbl = (TextView) findViewById(R.id.idLbl);
        pass_lbl = (TextView) findViewById(R.id.passLbl);
        id_txt = (EditText) findViewById(R.id.idTxt);
        pass_txt = (EditText) findViewById(R.id.passTxt);
        loginButton = (Button) findViewById(R.id.loginBtn);

        //initialize student login toggle button to checked
        isStudent = true;
        studentToggBtn.setChecked(isStudent);
        studentToggBtn.setBackgroundResource(R.drawable.login_toggle_selected);

        //add click listener to student login toggle button
        studentToggBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.studentToggle) {
                    if (!isStudent) {
                        isStudent = true;
                        studentToggBtn.setChecked(isStudent);
                        studentToggBtn.setBackgroundResource(R.drawable.login_toggle_selected);

                        id_lbl.setText(R.string.studentid_text);

                        lecturerToggBtn.setChecked(!isStudent);
                        lecturerToggBtn.setBackgroundResource(R.drawable.login_toggle_unselected);
                    }
                }
            }
        });

        //set ID label to student's
        id_lbl.setText(R.string.studentid_text);

        //initialize lecturer login toggle button to unchecked
        lecturerToggBtn.setChecked(!isStudent);
        lecturerToggBtn.setBackgroundResource(R.drawable.login_toggle_unselected);

        //add click listener to student login toggle button
        lecturerToggBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.lecturerToggle) {
                    if (isStudent) {
                        isStudent = false;
                        lecturerToggBtn.setChecked(!isStudent);
                        lecturerToggBtn.setBackgroundResource(R.drawable.login_toggle_selected);

                        id_lbl.setText(R.string.lecturerid_text);

                        studentToggBtn.setChecked(isStudent);
                        studentToggBtn.setBackgroundResource(R.drawable.login_toggle_unselected);
                    }
                }
            }
        });

        //initialize dialog box components
        dBuilder = new AlertDialog.Builder(MainActivity.this);
        dView = getLayoutInflater().inflate(R.layout.box_dialog, null);
        dTitle = (TextView) dView.findViewById(R.id.dialog_titleTxt);
        desc_txt = (TextView) dView.findViewById(R.id.dialog_descTxt);
        dBuilder.setView(dView);
        dialog = dBuilder.create();

        firebase = FirebaseDatabase.getInstance();

        pref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        //set login button event
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.loginBtn) {
                    final String login_id = id_txt.getText().toString();
                    final String login_password = pass_txt.getText().toString();
                    final String loginType = (isStudent) ? "Student" : "Lecturer";

                    database = firebase.getReference(isStudent ? "students" : "lecturers");

                    if (login_id.equals("") && login_password.equals("")) {
                        dTitle.setText("Error");
                        desc_txt.setText("Please insert your " + loginType + " ID and Password");
                        dialog.show();
                    } else if (login_id.equals("")) {
                        dTitle.setText("Error");
                        desc_txt.setText("Please insert your " + loginType + " ID");
                        dialog.show();
                    } else if (login_password.equals("")) {
                        dTitle.setText("Error");
                        desc_txt.setText("Please insert your Password");
                        dialog.show();
                    } else {
                        database.child(login_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                try {
                                    if(loginType == "Student") {
                                        Student loginStudent = dataSnapshot.getValue(Student.class);

                                        if (login_password.equals(loginStudent.getStudentPassword())) {
                                            dTitle.setText("Successful");
                                            desc_txt.setText(loginStudent.toString());
                                            dialog.show();

                                            SharedPreferences.Editor editor = pref.edit();
                                            editor.putString("KEY_ID", loginStudent.getStudentID());
                                            editor.putString("KEY_NAME", loginStudent.getStudentName());
                                            editor.putString("KEY_TYPE", loginType);
                                            editor.apply();

                                            Log.d("Login Student", loginStudent.getStudentID() + " , " + loginStudent.getStudentName() + " , " + loginStudent.getStudentPassword());

                                            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                @Override
                                                public void onDismiss(DialogInterface dialog) {
                                                    startActivity(new Intent(MainActivity.this, Scanner.class));
                                                }
                                            });
                                        } else {
                                            dTitle.setText("Error");
                                            desc_txt.setText("Incorrect " + loginType + " ID or Password");
                                            dialog.show();
                                        }
                                    } else if(loginType == "Lecturer") {
                                        Lecturer loginLecturer = dataSnapshot.getValue(Lecturer.class);

                                        if (login_password.equals(loginLecturer.getLecturerPassword())) {
                                            dTitle.setText("Successful");
                                            desc_txt.setText(loginLecturer.toString());
                                            dialog.show();

                                            SharedPreferences.Editor editor = pref.edit();
                                            editor.putString("KEY_ID", loginLecturer.getLecturerID());
                                            editor.putString("KEY_NAME", loginLecturer.getLecturerName());
                                            editor.putString("KEY_TYPE", loginType);
                                            editor.apply();

                                            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                @Override
                                                public void onDismiss(DialogInterface dialog) {
                                                    startActivity(new Intent(MainActivity.this, NavActivity.class));
                                                }
                                            });
                                        } else {
                                            dTitle.setText("Error");
                                            desc_txt.setText("Incorrect " + loginType + " ID or Password");
                                            dialog.show();
                                        }
                                    }
                                } catch (Exception ex) {
                                    dTitle.setText("Error");
                                    desc_txt.setText("Incorrect " + loginType + " ID or Password");
                                    dialog.show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                dTitle.setText("Error");
                                desc_txt.setText("Failed to read value");
                                dialog.show();
                            }
                        });
                    }
                }
            }
        });
    }
}