package com.app.my.firstapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.*;

public class MainActivity extends AppCompatActivity {
    private Button loginButton;
    private EditText studid_txt, studPass_txt;

    private AlertDialog.Builder dBuilder;
    private AlertDialog dialog;
    private View dView;
    private TextView dTitle;
    private TextView desc_txt;

    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //find view components
        loginButton = (Button) findViewById(R.id.loginBtn);
        studid_txt = (EditText) findViewById(R.id.stud_idTxt);
        studPass_txt = (EditText) findViewById(R.id.stud_passTxt);

        //initialize dialog box components
        dBuilder = new AlertDialog.Builder(MainActivity.this);
        dView = getLayoutInflater().inflate(R.layout.box_dialog, null);
        dTitle = (TextView) dView.findViewById(R.id.dialog_titleTxt);
        desc_txt = (TextView) dView.findViewById(R.id.dialog_descTxt);
        dBuilder.setView(dView);
        dialog = dBuilder.create();

        database = FirebaseDatabase.getInstance().getReference("students");

        //set login button event
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String studentID = studid_txt.getText().toString();
                final String studentPassword = studPass_txt.getText().toString();

                if(studentID.equals("") && studentPassword.equals("")){
                    dTitle.setText("Error");
                    desc_txt.setText("Please insert your Student ID and Password");
                    dialog.show();
                }else if(studentID.equals("")){
                    dTitle.setText("Error");
                    desc_txt.setText("Please insert your Student ID");
                    dialog.show();
                }else if(studentPassword.equals("")){
                    dTitle.setText("Error");
                    desc_txt.setText("Please insert your Password");
                    dialog.show();
                }else {
                    database.child(studentID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                Student loginStudent = dataSnapshot.getValue(Student.class);

                                if (studentPassword.equals(loginStudent.getStudentPassword())) {
                                    dTitle.setText("Successful");
                                    desc_txt.setText(loginStudent.toString());
                                    dialog.show();

                                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialog) {
                                            startActivity(new Intent(MainActivity.this, NavActivity.class));
                                        }
                                    });
                                } else {
                                    dTitle.setText("Error");
                                    desc_txt.setText("Incorrect Student ID or Password");
                                    dialog.show();
                                }
                            }
                            catch (Exception ex) {
                                dTitle.setText("Error");
                                desc_txt.setText("Incorrect Student ID or Password");
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

                    /*database.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(idExists) {
                                Student loginStudent = dataSnapshot.getValue(Student.class);

                                if(studentPassword.equals(loginStudent.getStudentPassword())) {
                                    dTitle.setText("Successful");
                                    desc_txt.setText(loginStudent.toString());
                                    dialog.show();

                                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialog) {
                                            startActivity(new Intent(MainActivity.this, NavActivity.class));
                                        }
                                    });
                                } else {
                                    dTitle.setText("Error");
                                    desc_txt.setText("Incorrect Student ID or Password");
                                    dialog.show();
                                }
                            } else {
                                dTitle.setText("Error");
                                desc_txt.setText("Incorrect Student ID or Password");
                                dialog.show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            dTitle.setText("Error");
                            desc_txt.setText("Failed to read value");
                            dialog.show();
                        }
                    });*/
                }
            }
        });
    }
}