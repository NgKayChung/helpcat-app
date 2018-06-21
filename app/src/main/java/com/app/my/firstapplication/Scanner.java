package com.app.my.firstapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.*;

public class Scanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView zXingScannerView;

    private Button attendanceButton;

    private BottomSheetDialog dialog;
    private View dView;
    private TextView dTitle;
    private TextView desc_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        //initialize dialog box components
        dialog = new BottomSheetDialog(Scanner.this);
        dView = getLayoutInflater().inflate(R.layout.box_dialog, null);
        dTitle = (TextView) dView.findViewById(R.id.dialog_titleTxt);
        desc_txt = (TextView) dView.findViewById(R.id.dialog_descTxt);
        dialog.setContentView(dView);

        attendanceButton = (Button) findViewById(R.id.attendance_btn);
        attendanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan(v);
            }
        });
    }

    public void scan(View view){
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
            dTitle.setText("Scan Result");
            desc_txt.setText(result.getText());
            dialog.show();
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    zXingScannerView.removeAllViews();
                    zXingScannerView.stopCamera();
                    setContentView(R.layout.activity_scanner);
                }
            });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}