package com.app.my.firstapplication;

import android.content.DialogInterface;
import android.support.constraint.Group;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.*;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView zXingScannerView;
    private ZXingScannerView.ResultHandler zXingResultHandler;

    private BottomSheetDialog bottomDialog;
    private View bottomDialogView;
    private Group successGroup, failGroup;
    private TextView studentID_text, studentName_text, failMessage_txt;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private ValueEventListener checkIDListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        bottomDialog = new BottomSheetDialog(ScannerActivity.this);
        bottomDialogView = getLayoutInflater().inflate(R.layout.scan_bottom_dialog, null, false);

        successGroup = (Group) bottomDialogView.findViewById(R.id.success_group);
        studentID_text = (TextView) bottomDialogView.findViewById(R.id.studentID_txt);
        studentName_text = (TextView) bottomDialogView.findViewById(R.id.studentName_txt);

        failGroup = (Group) bottomDialogView.findViewById(R.id.fail_group);
        failMessage_txt = (TextView) bottomDialogView.findViewById(R.id.failMessage_txt);

        bottomDialog.setContentView(bottomDialogView);
        bottomDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                successGroup.setVisibility(View.GONE);
                failGroup.setVisibility(View.GONE);
            }
        });

        zXingScannerView = new ZXingScannerView(getApplicationContext());
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void handleResult(Result result) {
        final ZXingScannerView.ResultHandler resultHandler = this;
        final String resultString = result.getText();
        bottomDialog.setCanceledOnTouchOutside(true);

        if(resultString.matches("^[C][0-9]{7}$")) {
            databaseReference.child("users").child(resultString).child("enrolledSubjects").orderByChild("subjectCode").equalTo("MTH1008").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        databaseReference.child("users").child(resultString).child("fullname").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String studentName = dataSnapshot.getValue(String.class);

                                bottomDialog.setCanceledOnTouchOutside(false);

                                successGroup.setVisibility(View.VISIBLE);

                                studentID_text.setText(resultString);
                                studentName_text.setText(studentName);

                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    } else {
                        failGroup.setVisibility(View.VISIBLE);

                        failMessage_txt.setText("Student ID \"" + resultString + "\" is not available in this class.");
                    }

                    bottomDialog.show();
                    zXingScannerView.resumeCameraPreview(resultHandler);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            failGroup.setVisibility(View.VISIBLE);

            failMessage_txt.setText("\"" + resultString + "\" is not a valid identifier.");
            bottomDialog.show();
            zXingScannerView.resumeCameraPreview(resultHandler);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        zXingResultHandler = null;
        zXingScannerView.stopCameraPreview();
        zXingScannerView.stopCamera();
        zXingScannerView = null;
        super.onDestroy();
    }
}
