package com.exampl.hamdi.businesscard;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.zxing.WriterException;

public class QRAppActivity extends AppCompatActivity {
    ImageView qrCodeImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrapp);
        qrCodeImg = (ImageView) findViewById(R.id.qr_code);
        try {
            qrCodeImg.setImageBitmap(BusinessCardActivity.encodeAsBitmap("azertyu"));
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }
}
