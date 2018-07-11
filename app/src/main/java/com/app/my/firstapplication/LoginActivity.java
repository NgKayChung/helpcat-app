package com.app.my.firstapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.*;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.*;

public class LoginActivity extends AppCompatActivity {
    private TextView id_lbl, pass_lbl;
    private EditText id_txt, pass_txt;
    private Button loginButton;
    private TextView forgotPassword_txt;

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
        setContentView(R.layout.activity_login);

        //find view components
        id_lbl = (TextView) findViewById(R.id.idLbl);
        pass_lbl = (TextView) findViewById(R.id.passLbl);
        id_txt = (EditText) findViewById(R.id.idTxt);
        pass_txt = (EditText) findViewById(R.id.passTxt);
        forgotPassword_txt = (TextView) findViewById(R.id.forgotText);
        loginButton = (Button) findViewById(R.id.loginBtn);

        //initialize dialog box components
        dBuilder = new AlertDialog.Builder(LoginActivity.this);
        dView = getLayoutInflater().inflate(R.layout.box_dialog, null);
        dTitle = (TextView) dView.findViewById(R.id.dialog_titleTxt);
        desc_txt = (TextView) dView.findViewById(R.id.dialog_descTxt);
        dBuilder.setView(dView);
        dialog = dBuilder.create();

        forgotPassword_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

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
                                User user = dataSnapshot.getValue(User.class);

                                if (login_password.equals(user.getPassword())) {
                                    user.determineUserType();

                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("KEY_ID", user.getID());
                                    editor.putString("KEY_NAME", user.getFullname());
                                    editor.putString("KEY_EMAIL", user.getEmailAddress());
                                    editor.putString("KEY_TYPE", user.getUserLoginType());
                                    editor.apply();

                                    Toast.makeText(getApplicationContext(), "Logged in successfully!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
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