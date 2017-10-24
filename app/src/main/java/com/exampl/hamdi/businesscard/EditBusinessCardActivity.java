package com.exampl.hamdi.businesscard;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ViewUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.exampl.hamdi.businesscard.DataBaseHandler.DBHandler;
import com.exampl.hamdi.businesscard.Model.BusinessCard;
import com.exampl.hamdi.businesscard.Model.IBusinessCard;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.width;
import static android.R.id.input;
import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;
import static java.net.Proxy.Type.HTTP;

public class EditBusinessCardActivity extends AppCompatActivity implements View.OnClickListener {
    IBusinessCard bc;
    EditText et_name, et_function, et_number, et_mail, et_address;
    ImageView myImage;
    DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_business_card);
        et_name = (EditText) findViewById(R.id.editText_name);
        et_function = (EditText) findViewById(R.id.editText_funtion);
        et_number = (EditText) findViewById(R.id.editText_number);
        et_mail = (EditText) findViewById(R.id.editText_mail);
        et_address = (EditText) findViewById(R.id.editText_address);
        myImage = (ImageView) findViewById(R.id.imageView);

        db = new DBHandler(this);
        Intent i = getIntent();
        bc = db.getBusinessCard(((int) i.getLongExtra("bc_id", 0)));
        et_name.setText(bc.getFirstName());
        et_function.setText(bc.getFunction());
        et_number.setText(bc.getNumbers());
        et_mail.setText(bc.getMail());
        et_address.setText(bc.getAddress());


        myImage.setImageBitmap(imageToBitmap("/data/user/0/com.exampl.hamdi.businesscard/app_hello/img"+String.valueOf(bc.getId())+".jpg"));


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.business_card_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_submit) {

            IBusinessCard bc1 = bc;
            //bc1.setImagePath(imgToString("/data/user/0/com.exampl.hamdi.businesscard/app_hello/img"+String.valueOf(bc.getId())+".jpg"));

           /* Gson gson = new Gson();
            String json = gson.toJson(bc1);
            Log.i("json",json);

            Intent intent = new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:"));
            intent.putExtra("sms_body", json);
            //intent.putExtra(Intent.EXTRA_STREAM, Uri.p(new File("/data/user/0/com.exampl.hamdi.businesscard/app_hello/img"+String.valueOf(bc.getId())+".jpg")));
            startActivity(Intent.createChooser(intent,"Send"));*/

            /*
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra("sms_body", "Hi how are you");
            intent.setType("image/gif");
            startActivity(Intent.createChooser(intent,"Send"));*/


            bc.setFirstName(et_name.getText().toString());
            bc.setFunction(et_function.getText().toString());
            bc.setNumbers(et_number.getText().toString());
            bc.setMail(et_mail.getText().toString());
            bc.setAddress(et_address.getText().toString());
            db.updateBusinessCard(bc);
            startActivity(new Intent(EditBusinessCardActivity.this,MainActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab: {
                pickImage();
                break;
            }
        }
    }
    int  PICK_PHOTO_FOR_AVATAR = 1;
    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            try {
                InputStream inputStream = getApplicationContext().getContentResolver()
                        .openInputStream(data.getData());
                //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
                try {

                    File file = new File(getDir("hello",MODE_PRIVATE), "img"
                            +String.valueOf(bc.getId())+".jpg");
                    Log.i("path",file.getAbsolutePath());
                    OutputStream output = new FileOutputStream(file);
                    try {
                        try {
                            byte[] buffer = new byte[4 * 1024]; // or other buffer size
                            int read;

                            while ((read = inputStream.read(buffer)) != -1) {
                                output.write(buffer, 0, read);
                            }
                            output.flush();
                        } finally {
                            output.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace(); // handle exception, define IOException and others
                    }
                } finally {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        //refresh
        finish();
        startActivity(getIntent());
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Here we need to check if the activity that was triggers was the Image Gallery.
        // If it is the requestCode will match the LOAD_IMAGE_RESULTS value.
        // If the resultCode is RESULT_OK and there is some data we know that an image was picked.
        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == RESULT_OK && data != null) {
            // Let's read picked image data - its URI
            Uri pickedImage = data.getData();
            // Let's read picked image path using content resolver
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

            // Do something with the bitmap


            // At the end remember to close the cursor or you will end with the RuntimeException!
            cursor.close();
        }
    }*/

    public String imgToString(String imagePath){
        Bitmap bm = BitmapFactory.decodeFile(imagePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    /*public String imgToString(String imagePath) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(new File(imagePath));//You can get an inputStream using any IO API
        byte[] bytes;
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        bytes = output.toByteArray();
        String encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);
        return encodedString;
    }*/

    public static Bitmap imageToBitmap(String filePath){
        File imgFile = new  File(filePath);
        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        return myBitmap;
    }

}
