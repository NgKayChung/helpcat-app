package com.app.my.firstapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRGenerate extends AppCompatActivity {
    private Button gen_btn;
    private ImageView image;
    private String text2Qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrgenerate);

        gen_btn = (Button)findViewById(R.id.gen_btn);
        image = (ImageView)findViewById(R.id.image);

        gen_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text2Qr = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE).getString("KEY_ID", "");
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

                try {
                    BitMatrix bitMatrix =multiFormatWriter.encode(text2Qr, BarcodeFormat.QR_CODE,200,200);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    image.setImageBitmap(bitmap);
                }
                catch (WriterException e)
                {
                    e.printStackTrace();
                }

            }
        });
    }
}