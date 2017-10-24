package com.exampl.hamdi.businesscard;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.exampl.hamdi.businesscard.DataBaseHandler.DBHandler;
import com.exampl.hamdi.businesscard.Model.BusinessCard;
import com.exampl.hamdi.businesscard.Model.IBusinessCard;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.File;

import static android.R.attr.width;
import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class BusinessCardActivity extends Activity implements View.OnClickListener {
    TextView tv_name, tv_mobile, tv_mail, tv_address, tv_function;
    ImageView myImg, qrImg;
    IBusinessCard bc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_card);

        DBHandler db = new DBHandler(this);
        Intent i = getIntent();
        bc = db.getBusinessCard(((int) i.getLongExtra("bc_id", 0)));

        tv_name = (TextView) findViewById(R.id.textView_name);
        tv_mobile = (TextView) findViewById(R.id.textView_mobile);
        tv_mobile.setOnClickListener(this);
        tv_mail = (TextView) findViewById(R.id.textView_mail);
        tv_mail.setOnClickListener(this);
        tv_address = (TextView) findViewById(R.id.textView_address);
        tv_address.setOnClickListener(this);
        tv_function = (TextView) findViewById(R.id.textView_function);
        tv_function.setOnClickListener(this);
        myImg = (ImageView) findViewById(R.id.imageView);
        qrImg = (ImageView) findViewById(R.id.qrCode);



        tv_name.setText(bc.getFirstName());
        tv_mobile.setText(bc.getNumbers());
        tv_mail.setText(bc.getMail());
        tv_address.setText(bc.getAddress());
        tv_function.setText(bc.getFunction());
        //displaying avatar
        File imgFile = new  File("/data/user/0/com.exampl.hamdi.businesscard/app_hello/img"+String.valueOf(bc.getId())+".jpg");
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            myImg.setImageBitmap(myBitmap);
        }else {

        }
        //displaying QR Code
        try {
            Gson gson = new Gson();
            String json = gson.toJson(bc);
            Log.d("JSON ",json);
            Bitmap bitmap = encodeAsBitmap(json);
            qrImg.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textView_mobile : {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + bc.getNumbers()));
                startActivity(intent);
                break;
            }
            case R.id.textView_mail : {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, bc.getMail());
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                break;
            }
            case R.id.textView_address : {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("geo:0,0?q="+bc.getAddress().replace(" ","+")));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                break;
            }
            case R.id.textView_function : {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.google.com/#q="+bc.getFunction()));
                startActivity(intent);
                break;
            }
        }
    }


    public static Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, 500,500, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, 500, 0, 0, w, h);
        return bitmap;
    }
}
