package com.app.my.firstapplication;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.*;

public class StudentChangPassword extends AppCompatActivity {
    private TextInputLayout textInputOldPassword, textInputNewPassword, textInputRetypeNewPassword;
    private Button changePassword_btn;

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
        setContentView(R.layout.activity_student_chang_password);

        //initialize dialog box components
        dBuilder = new AlertDialog.Builder(StudentChangPassword.this);
        dView = getLayoutInflater().inflate(R.layout.box_dialog, null);
        dTitle = (TextView) dView.findViewById(R.id.dialog_titleTxt);
        desc_txt = (TextView) dView.findViewById(R.id.dialog_descTxt);
        dBuilder.setView(dView);
        dialog = dBuilder.create();

        textInputOldPassword = findViewById(R.id.text_input_old_password);
        textInputNewPassword = findViewById(R.id.text_input_new_password);
        textInputRetypeNewPassword = findViewById(R.id.text_input_retype_new_password);

        changePassword_btn = (Button) findViewById(R.id.changePassword_btn);

        changePassword_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateChangePassword()) {
                    dTitle.setText("Successful");
                    desc_txt.setText("Password is updated");
                    dialog.show();
                }
            }
        });
    }

    private boolean validateChangePassword() {
        String oldPassword = textInputOldPassword.getEditText().getText().toString().trim();
        String newPassword = textInputNewPassword.getEditText().getText().toString().trim();
        String retypePassword = textInputRetypeNewPassword.getEditText().getText().toString().trim();
        boolean success = true;

        if(oldPassword.isEmpty()) {
            textInputOldPassword.setError("Field cannot be empty");
            success = false;
        }
        else if(!oldPassword.equals("1234")) {
            textInputOldPassword.setError("Not match with old password");
            success = false;
        }
        else {
            textInputOldPassword.setError("");
        }

        if (newPassword.isEmpty()) {
            textInputNewPassword.setError("Field cannot be empty");
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
}