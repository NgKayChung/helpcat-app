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

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private TextView id_lbl, pass_lbl;
    private EditText id_txt, pass_txt;
    private Button loginButton;

    private AlertDialog.Builder dBuilder;
    private AlertDialog dialog;
    private View dView;
    private TextView dTitle;
    private TextView desc_txt;

    private FirebaseDatabase firebase;
    private DatabaseReference database;

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //find view components
        id_lbl = (TextView) findViewById(R.id.idLbl);
        pass_lbl = (TextView) findViewById(R.id.passLbl);
        id_txt = (EditText) findViewById(R.id.idTxt);
        pass_txt = (EditText) findViewById(R.id.passTxt);
        loginButton = (Button) findViewById(R.id.loginBtn);

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
                final String login_id = id_txt.getText().toString();
                final String login_password = pass_txt.getText().toString();

                if (login_id.equals("") && login_password.equals("")) {
                    dTitle.setText("Error");
                    desc_txt.setText("Please insert your login ID and Password");
                    dialog.show();
                } else if (login_id.equals("")) {
                    dTitle.setText("Error");
                    desc_txt.setText("Please insert your login ID");
                    dialog.show();
                } else if (login_password.equals("")) {
                    dTitle.setText("Error");
                    desc_txt.setText("Please insert your Password");
                    dialog.show();
                } else {
                    database = firebase.getReference("users");

                    database.child(login_id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                HashMap<String, Object> userdata = (HashMap<String, Object>) dataSnapshot.getValue();

                                if (login_password.equals((String)userdata.get("password"))) {
                                    String userID = (String) userdata.get("ID");
                                    String user_fullname = (String) userdata.get("fullname");
                                    String loginType = "";

                                    dTitle.setText("Successful");
                                    desc_txt.setText(user_fullname);
                                    dialog.show();

                                    if(userID.startsWith("C"))
                                        loginType = "student";
                                    else if(userID.startsWith("I"))
                                        loginType = "lecturer";
                                    else
                                        loginType = userID;

                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("KEY_ID", userID);
                                    editor.putString("KEY_NAME", user_fullname);
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
                                    desc_txt.setText("Incorrect ID or Password");
                                    dialog.show();
                                }
                            } catch (Exception ex) {
                                dTitle.setText("Error");
                                desc_txt.setText("Incorrect ID or Password");
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
        });
    }
}