package com.app.my.helpcatapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.google.firebase.database.*;

public class ForgotPasswordActivity extends AppCompatActivity {
    private TextInputLayout forgotLoginID_txt;
    private Button resetPassword_btn;

    private AlertDialog.Builder dBuilder;
    private AlertDialog dialog;
    private View dView;
    private TextView dTitle;
    private TextView desc_txt;

    private FirebaseDatabase firebase;

    private boolean isSubmitted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        firebase = FirebaseDatabase.getInstance();

        //initialize alert dialog components
        dBuilder = new AlertDialog.Builder(ForgotPasswordActivity.this);
        dView = getLayoutInflater().inflate(R.layout.box_dialog, null);
        dTitle = (TextView) dView.findViewById(R.id.dialog_titleTxt);
        desc_txt = (TextView) dView.findViewById(R.id.dialog_descTxt);
        dBuilder.setView(dView);
        dialog = dBuilder.create();

        // initialize layout components
        forgotLoginID_txt = findViewById(R.id.forgot_loginid_txt);
        resetPassword_btn = findViewById(R.id.resetPassword_btn);

        // set click event for reset password button
        // to submit a reset password request for user
        resetPassword_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get login ID from ID text field
                final String loginID = forgotLoginID_txt.getEditText().getText().toString().trim();

                if(loginID.isEmpty()) {
                    forgotLoginID_txt.setError("This field cannot be empty");
                    return;
                } else if(loginID.equals("superadmin")) {
                    forgotLoginID_txt.setError("Administrator should not forget his/her password");
                    return;
                }

                // search existing login ID
                firebase.getReference("users").orderByChild("ID").equalTo(loginID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // if no snapshot is returned
                        if(!dataSnapshot.exists()) {
                            forgotLoginID_txt.setError("Login ID not found");
                        }
                        else {
                            ResetPasswordSubmission resetSub = new ResetPasswordSubmission(loginID, 100);
                            firebase.getReference("reset_password_approval").child(loginID).setValue(resetSub);

                            dTitle.setText("Success");
                            desc_txt.setText("Reset password for login ID " + loginID + " is submitted successfully.\nPlease contact administrator to complete your password reset.");
                            dialog.show();

                            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    dialog.dismiss();
                                    dialog.cancel();
                                    startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                    finish();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        dTitle.setText("Error");
                        desc_txt.setText("Error : " + databaseError.getMessage());
                        dialog.show();
                    }
                });

                forgotLoginID_txt.setError("");
            }
        });
    }
}
