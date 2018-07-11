package com.app.my.firstapplication;

import android.content.*;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import com.google.firebase.database.*;

public class UserChangePasswordActivity extends AppCompatActivity {
    private TextInputLayout textInputOldPassword, textInputNewPassword, textInputRetypeNewPassword;
    private Button changePassword_btn;

    private FirebaseDatabase firebase;
    private DatabaseReference databaseReference;

    private SharedPreferences pref;

    private String currentPassword;
    private String newPassword;

    private ValueEventListener passwordListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_change_password);

        textInputOldPassword = findViewById(R.id.text_input_old_password);
        textInputNewPassword = findViewById(R.id.text_input_new_password);
        textInputRetypeNewPassword = findViewById(R.id.text_input_retype_new_password);

        firebase = FirebaseDatabase.getInstance();
        databaseReference = firebase.getReference();

        pref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        passwordListener = databaseReference.child("users").child(pref.getString("KEY_ID", null)).child("password").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentPassword = (String) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getMessage());
            }
        });

        changePassword_btn = (Button) findViewById(R.id.changePassword_btn);

        changePassword_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateChangePassword()) {
                    firebase.getReference("users").child(pref.getString("KEY_ID", null)).child("password").setValue(newPassword);
                    Toast.makeText(getApplicationContext(), "Password is successfully updated", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    private boolean validateChangePassword() {
        String oldPassword = textInputOldPassword.getEditText().getText().toString().trim();
        newPassword = textInputNewPassword.getEditText().getText().toString().trim();
        String retypePassword = textInputRetypeNewPassword.getEditText().getText().toString().trim();
        boolean success = true;

        if(oldPassword.isEmpty()) {
            textInputOldPassword.setError("Field cannot be empty");
            success = false;
        }
        else if(!oldPassword.equals(currentPassword)) {
            textInputOldPassword.setError("Not match with old password");
            success = false;
        }
        else {
            textInputOldPassword.setError("");
        }

        if (newPassword.isEmpty()) {
            textInputNewPassword.setError("Field cannot be empty");
            success = false;
        } else if(newPassword.length() < 8) {
            textInputNewPassword.setError("Password should consists of 8 characters");
            success = false;
        } else {
            textInputNewPassword.setError("");
        }

        if(retypePassword.isEmpty()) {
            textInputRetypeNewPassword.setError("Field cannot be empty");
            success = false;
        }
        else if(!retypePassword.equals(newPassword)) {
            textInputRetypeNewPassword.setError("Not match with new password");
            success = false;
        }
        else {
            textInputRetypeNewPassword.setError("");
        }

        return success;
    }

    @Override
    public void onDestroy() {
        databaseReference.child("users").child(pref.getString("KEY_ID", null)).child("password").removeEventListener(passwordListener);
        super.onDestroy();
    }
}