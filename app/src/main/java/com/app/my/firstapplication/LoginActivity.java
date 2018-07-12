package com.app.my.firstapplication;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.*;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.*;

public class LoginActivity extends AppCompatActivity {
    private TextView id_lbl, idErr_txt, pass_lbl, passErr_txt;
    private EditText id_txt, pass_txt;
    private Button loginButton;
    private TextView forgotPassword_txt;

    private FirebaseDatabase firebase;
    private DatabaseReference database;

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //find view components
        id_lbl = (TextView) findViewById(R.id.idLbl);
        idErr_txt = (TextView) findViewById(R.id.idErrTxt);
        pass_lbl = (TextView) findViewById(R.id.passLbl);
        passErr_txt = (TextView) findViewById(R.id.passwordErrTxt);
        id_txt = (EditText) findViewById(R.id.idTxt);
        pass_txt = (EditText) findViewById(R.id.passTxt);
        forgotPassword_txt = (TextView) findViewById(R.id.forgotText);
        loginButton = (Button) findViewById(R.id.loginBtn);

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

                idErr_txt.setText("");
                passErr_txt.setText("");

                if (login_id.equals("") && login_password.equals("")) {
                    idErr_txt.setText("Login ID is required");
                    passErr_txt.setText("Password is required");
                } else if (login_id.equals("")) {
                    idErr_txt.setText("Login ID is required");
                } else if (login_password.equals("")) {
                    passErr_txt.setText("Password is required");
                } else {
                    database = firebase.getReference("users");

                    database.child(login_id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
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
                                    passErr_txt.setText("Incorrect ID or Password");
                                }
                            } else {
                                passErr_txt.setText("Incorrect ID or Password");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println(databaseError.getMessage());
                        }
                    });
                }
            }
        });
    }
}