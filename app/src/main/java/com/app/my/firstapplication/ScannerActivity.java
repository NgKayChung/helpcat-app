package com.app.my.firstapplication;

import android.content.DialogInterface;
import android.support.constraint.Group;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.*;
import com.google.zxing.Result;

import java.text.DateFormat;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView zXingScannerView;
    private ZXingScannerView.ResultHandler zXingResultHandler;

    private BottomSheetDialog bottomDialog;
    private View bottomDialogView;
    private Group successGroup, failGroup;
    private TextView studentID_text, studentName_text, failMessage_txt;
    private Button confirm_btn, cancel_btn, dismiss_btn;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private String subjectID;

    private String currentDate;
    private String subjectIndex;
    private String resultString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        subjectID = extras.getString("subjectID");

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        currentDate = DateFormat.getDateInstance().format(Calendar.getInstance().getTime()).replaceAll("/", "-");

        databaseReference.child("users").orderByChild("ID").startAt("C").endAt("C~").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot usersSnapshot) {
                for(final DataSnapshot studentSnapshot : usersSnapshot.getChildren()) {
                    studentSnapshot.child("enrolledSubjects").getRef().orderByChild("subjectCode").equalTo(subjectID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot studEnrolledSnapshot) {
                            if(studEnrolledSnapshot.exists()) {
                                final Map<String, Object> attendanceMap = new HashMap<>();
                                attendanceMap.put("studentID", (String)studentSnapshot.child("ID").getValue());
                                attendanceMap.put("attended", false);

                                databaseReference.child("subjects").orderByChild("subjectCode").equalTo(subjectID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot subjectSnapshot) {
                                        subjectIndex = subjectSnapshot.getChildren().iterator().next().getKey();
                                        DataSnapshot snap = subjectSnapshot.child(subjectIndex).child("attendanceList").child(currentDate).child((String)studentSnapshot.child("ID").getValue());
                                        if(snap.exists()) {
                                            if (!snap.child("attended").getValue(Boolean.class)) {
                                                snap.getRef().setValue(attendanceMap);
                                            }
                                        } else {
                                            snap.getRef().setValue(attendanceMap);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        bottomDialog = new BottomSheetDialog(ScannerActivity.this);
        bottomDialogView = getLayoutInflater().inflate(R.layout.scan_bottom_dialog, null, false);

        successGroup = (Group) bottomDialogView.findViewById(R.id.success_group);
        studentID_text = (TextView) bottomDialogView.findViewById(R.id.studentID_txt);
        studentName_text = (TextView) bottomDialogView.findViewById(R.id.studentName_txt);
        confirm_btn = (Button) bottomDialogView.findViewById(R.id.confirm_btn);
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("subjects").child(subjectIndex).child("attendanceList").child(currentDate).child(resultString).child("attended").setValue(true);
                bottomDialog.dismiss();
            }
        });
        cancel_btn = (Button) bottomDialogView.findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.dismiss();
            }
        });

        failGroup = (Group) bottomDialogView.findViewById(R.id.fail_group);
        failMessage_txt = (TextView) bottomDialogView.findViewById(R.id.failMessage_txt);
        dismiss_btn = (Button) bottomDialogView.findViewById(R.id.dismiss_btn);
        dismiss_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.dismiss();
            }
        });

        bottomDialog.setContentView(bottomDialogView);
        bottomDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                successGroup.setVisibility(View.GONE);
                failGroup.setVisibility(View.GONE);

                zXingScannerView.resumeCameraPreview(zXingResultHandler);
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
    public void handleResult(final Result result) {
        zXingResultHandler = this;
        resultString = result.getText();
        bottomDialog.setCanceledOnTouchOutside(true);

        if(resultString.matches("^[C][0-9]{7}$")) {
            databaseReference.child("subjects").child(subjectIndex).child("attendanceList").child(currentDate).orderByChild("studentID").equalTo(resultString).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        // if student already attended
                        if((boolean) dataSnapshot.child(resultString).child("attended").getValue()) {
                            failGroup.setVisibility(View.VISIBLE);

                            failMessage_txt.setText("Student ID \"" + resultString + "\" already attended.");
                        } else {
                            databaseReference.child("users").child(resultString).child("fullname").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String studentName = dataSnapshot.getValue(String.class);

                                    bottomDialog.setCanceledOnTouchOutside(false);

                                    successGroup.setVisibility(View.VISIBLE);

                                    studentID_text.setText(resultString);
                                    studentName_text.setText(studentName);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    } else {
                        failGroup.setVisibility(View.VISIBLE);

                        failMessage_txt.setText("Student ID \"" + resultString + "\" is not available in this class.");
                    }

                    bottomDialog.show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            failGroup.setVisibility(View.VISIBLE);

            failMessage_txt.setText("\"" + resultString + "\" is not a valid identifier.");
            bottomDialog.show();
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
