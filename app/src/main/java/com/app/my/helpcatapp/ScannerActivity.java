package com.app.my.helpcatapp;

import android.app.AlertDialog;
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

    private AlertDialog.Builder dBuilder;
    private AlertDialog dialog;
    private View dialogView;
    private TextView title_txt, desc_txt;

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

    private ValueEventListener attendanceListListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // retrieve the parameter passed from LecturerAttendanceActivity
        Bundle extras = getIntent().getExtras();
        subjectID = extras.getString("subjectID");

        // initialize dialog components
        dBuilder = new AlertDialog.Builder(ScannerActivity.this);
        dialogView = getLayoutInflater().inflate(R.layout.box_dialog, null);
        title_txt = dialogView.findViewById(R.id.dialog_titleTxt);
        desc_txt = dialogView.findViewById(R.id.dialog_descTxt);
        dBuilder.setView(dialogView);
        dialog = dBuilder.create();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        // get and format current date as String
        currentDate = DateFormat.getDateInstance().format(Calendar.getInstance().getTime()).replaceAll("/", "-");

        // retrieve all students
        databaseReference.child("users").orderByChild("ID").startAt("C").endAt("C~").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot usersSnapshot) {
                // iterate students snapshot
                for(final DataSnapshot studentSnapshot : usersSnapshot.getChildren()) {
                    // determine student is taking current subject
                    studentSnapshot.child("enrolledSubjects").getRef().orderByChild("subjectCode").equalTo(subjectID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot studEnrolledSnapshot) {
                            if(studEnrolledSnapshot.exists()) { // if the student is taking the subject
                                final Map<String, Object> attendanceMap = new HashMap<>();
                                attendanceMap.put("studentID", (String)studentSnapshot.child("ID").getValue());
                                attendanceMap.put("attended", false);

                                // get current subject snapshot
                                databaseReference.child("subjects").orderByChild("subjectCode").equalTo(subjectID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot subjectSnapshot) {
                                        // get index of the subject in the list
                                        subjectIndex = subjectSnapshot.getChildren().iterator().next().getKey();

                                        // add or fetch attendance list of current subject
                                        attendanceListListener = databaseReference.child("subjects").child(subjectIndex).child("attendanceList").child(currentDate).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(final DataSnapshot attendListSnapshot) {
                                                // check attendance list
                                                attendListSnapshot.getRef().orderByChild("attended").equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot attendedSnapshot) {
                                                        // if all students are attended to current class
                                                        if(attendListSnapshot.getChildrenCount() == attendedSnapshot.getChildrenCount()) {
                                                            failGroup.setVisibility(View.VISIBLE);

                                                            failMessage_txt.setText("All students are attended to the class");

                                                            // display dialog, back to previous page
                                                            bottomDialog.show();
                                                            bottomDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                                @Override
                                                                public void onDismiss(DialogInterface dialog) {
                                                                    zXingScannerView.stopCamera();
                                                                    zXingScannerView.stopCameraPreview();
                                                                    bottomDialog.dismiss();
                                                                    onBackPressed();
                                                                }
                                                            });
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                        // get current student in attendance
                                        DataSnapshot snap = subjectSnapshot.child(subjectIndex).child("attendanceList").child(currentDate).child((String)studentSnapshot.child("ID").getValue());

                                        // if student already added to the list
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

        // initialize bottom dialog components
        bottomDialog = new BottomSheetDialog(ScannerActivity.this);
        bottomDialogView = getLayoutInflater().inflate(R.layout.scan_bottom_dialog, null, false);

        successGroup = (Group) bottomDialogView.findViewById(R.id.success_group);
        studentID_text = (TextView) bottomDialogView.findViewById(R.id.studentID_txt);
        studentName_text = (TextView) bottomDialogView.findViewById(R.id.studentName_txt);
        confirm_btn = (Button) bottomDialogView.findViewById(R.id.confirm_btn);
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.dismiss();
                databaseReference.child("subjects").child(subjectIndex).child("attendanceList").child(currentDate).child(resultString).child("attended").setValue(true);
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

        // starts camera for scanning students' QR code
        // using external library - ZXingScannerView
        zXingScannerView = new ZXingScannerView(getApplicationContext());
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    // method to handle scanner result
    @Override
    public void handleResult(final Result result) {
        zXingResultHandler = this;

        // get QR code value as String
        resultString = result.getText();
        bottomDialog.setCanceledOnTouchOutside(true);

        // if the QR code value matches student ID format
        if(resultString.matches("^[C][0-9]{7}$")) {
            //check for the student in attendance list
            databaseReference.child("subjects").child(subjectIndex).child("attendanceList").child(currentDate).orderByChild("studentID").equalTo(resultString).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        // if student already attended
                        if((boolean) dataSnapshot.child(resultString).child("attended").getValue()) {
                            failGroup.setVisibility(View.VISIBLE);

                            failMessage_txt.setText("Student ID \"" + resultString + "\" already attended.");
                        } else {
                            // set student attended
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
                                    System.out.println(databaseError.getMessage());
                                }
                            });
                        }
                    } else { // student does not belong to current class
                        failGroup.setVisibility(View.VISIBLE);

                        failMessage_txt.setText("Student ID \"" + resultString + "\" is not available in this class.");
                    }

                    bottomDialog.show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println(databaseError.getMessage());
                }
            });
        } else { // invalid QR code value
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
        databaseReference.child("subjects").child(subjectIndex).child("attendanceList").child(currentDate).removeEventListener(attendanceListListener);
        zXingResultHandler = null;
        zXingScannerView.stopCameraPreview();
        zXingScannerView.stopCamera();
        zXingScannerView = null;
        super.onDestroy();
    }
}
