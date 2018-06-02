package com.app.my.firstapplication;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class StudentChangPassword extends AppCompatActivity {
    private TextInputLayout textInputOldPassword;
    private TextInputLayout textInputNewPassword;
    private TextInputLayout textInputRetypeNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_chang_password);

        textInputOldPassword = findViewById(R.id.text_input_old_password);
        textInputNewPassword = findViewById(R.id.text_input_new_password);
        textInputRetypeNewPassword = findViewById(R.id.text_input_retype_new_password);

    }

    private boolean validateOldPassword()
    {
        String oldPassword = textInputOldPassword.getEditText().getText().toString().trim();
        if(oldPassword.isEmpty())
        {
            textInputOldPassword.setError("Field can't be empty");
            return false;}
            else if(!oldPassword.equals())
        {
            textInputOldPassword.setError("Wrong Enter old password");
            return false;
        }
        else
        {
            textInputOldPassword.setError(null);
            return true;
        }


    }

    private boolean validateNewPassword()
    {
        String newPassword = textInputNewPassword.getEditText().getText().toString().trim();
        if(newPassword.isEmpty())
        {
            textInputNewPassword.setError("Field can't be empty");
            return false;}
        else
        {
            textInputOldPassword.setError(null);
            return true;
        }


    }

    private boolean validaterNewPassword()
    {
        String RnewPassword = textInputRetypeNewPassword.getEditText().getText().toString().trim();
        if(RnewPassword.isEmpty())
        {
            textInputRetypeNewPassword.setError("Field can't be empty");
            return false;}
        else if(!RnewPassword.equals(textInputNewPassword))
        {
            textInputRetypeNewPassword.setError("Password not match");
            return false;
        }
        else
        {
            textInputRetypeNewPassword.setError(null);
            return true;
        }


    }



}
