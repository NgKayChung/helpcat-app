package com.app.my.firstapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.google.zxing.integration.android.IntentIntegrator;

public class LecturerAttendanceActivity extends AppCompatActivity {
    private Button attendanceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        attendanceButton = (Button) findViewById(R.id.attendance_btn);
        attendanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LecturerAttendanceActivity.this, ScannerActivity.class);
                intent.putExtra("classObject", "CSC1015");
                startActivityForResult(intent, IntentIntegrator.REQUEST_CODE);
            }
        });
    }
}