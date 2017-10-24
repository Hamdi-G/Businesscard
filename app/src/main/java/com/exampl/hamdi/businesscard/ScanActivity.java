package com.exampl.hamdi.businesscard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.exampl.hamdi.businesscard.DataBaseHandler.DBHandler;
import com.exampl.hamdi.businesscard.Model.BusinessCard;
import com.exampl.hamdi.businesscard.Model.IBusinessCard;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.gson.Gson;
import com.google.zxing.WriterException;

import java.io.IOException;

public class ScanActivity extends AppCompatActivity {
    SurfaceView cameraView;
    TextView barcodeInfo;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    DBHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        db = new DBHandler(this);

        cameraView = (SurfaceView)findViewById(R.id.camera_view);
        barcodeInfo = (TextView)findViewById(R.id.code_info);

        //fetch a stream of images from the device’s camera and display them in the SurfaceView
        barcodeDetector = new BarcodeDetector.Builder(this)
                        .setBarcodeFormats(Barcode.QR_CODE).build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                      .setRequestedPreviewSize(640, 480).build();

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    //start drawing the preview frames
                    cameraSource.start(cameraView.getHolder());
                } catch (IOException ie) {
                    Log.e("CAMERA SOURCE", ie.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                //stop drawing the preview frames
                cameraSource.stop();
            }
        });

        //Call when BarcodeDetector detects a QR code
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() != 0) {
                    barcodeInfo.post(new Runnable() {    // Use the post method of the TextView
                        public void run() {
                            // Update the TextView
                            barcodeInfo.setText(barcodes.valueAt(0).displayValue);
                            Log.i("result",barcodes.valueAt(0).displayValue);
                            try {
                                //JSON to Java object, read it from a Json String.
                                Gson gson = new Gson();
                                IBusinessCard bc = gson.fromJson(barcodes.valueAt(0).displayValue, BusinessCard.class);
                                db.addBusinessCard(bc);
                                Intent i = new Intent(ScanActivity.this,EditBusinessCardActivity.class);
                                i.putExtra("bc_id", MainActivity.getIdLastBusinessCard(db));
                                startActivity(i);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

            }
        });
    }
}
