package com.exampl.hamdi.businesscard.DataBaseHandler;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.exampl.hamdi.businesscard.Model.BusinessCard;
import com.exampl.hamdi.businesscard.Model.IBusinessCard;
import com.exampl.hamdi.businesscard.Util.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hamdi on 26/04/2017.
 */

public class DBHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "myDataBase";
    // table name
    private static final String TABLE_BUSINESS_CARD = "businessCard";
    // businessCard Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_NUMBERS = "numbers";
    private static final String KEY_MAIL = "mail";
    private static final String KEY_ADDRESS  = "address";
    private static final String KEY_FUNCTION = "function";
    private static final String KEY_IMAGE_PATH = "imagePath";


    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BUSINESS_CARD_TABLE = "CREATE TABLE " + TABLE_BUSINESS_CARD + "("
        + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FIRST_NAME + " TEXT,"
        + KEY_LAST_NAME + " TEXT," + KEY_NUMBERS + " TEXT,"
        + KEY_MAIL + " TEXT," + KEY_ADDRESS + " TEXT,"
        + KEY_FUNCTION + " TEXT," + KEY_IMAGE_PATH + " TEXT" + ")";
        db.execSQL(CREATE_BUSINESS_CARD_TABLE);

        String INSERT_ADMIN_BUSINESS_CARD = "INSERT INTO " + TABLE_BUSINESS_CARD + " ( "
                + KEY_ID + " , " + KEY_FIRST_NAME + " , "
                + KEY_LAST_NAME + " , " + KEY_NUMBERS + " , "
                + KEY_MAIL + " , " + KEY_ADDRESS + " , "
                + KEY_FUNCTION + " , " + KEY_IMAGE_PATH  + " ) " + " VALUES ( "
                + "'1' , 'admin' ,'' ,'' ,'' ,'' ,'' ,'' )";
        db.execSQL(INSERT_ADMIN_BUSINESS_CARD);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUSINESS_CARD);
        // Creating tables again
        onCreate(db);
    }

    // Adding new business card
    public void addBusinessCard(IBusinessCard bc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FIRST_NAME,bc.getFirstName());
        values.put(KEY_LAST_NAME,bc.getLastName());
        values.put(KEY_NUMBERS,bc.getNumbers());
        values.put(KEY_MAIL,bc.getMail());
        values.put(KEY_ADDRESS,bc.getAddress());
        values.put(KEY_FUNCTION,bc.getFunction());
        values.put(KEY_IMAGE_PATH,bc.getImagePath());

        // Inserting Row
        db.insert(TABLE_BUSINESS_CARD, null, values);
        db.close(); // Closing database connection
    }

    // Getting one business card
    public IBusinessCard getBusinessCard(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BUSINESS_CARD, new String[] { KEY_ID,
                        KEY_FIRST_NAME, KEY_LAST_NAME, KEY_NUMBERS, KEY_MAIL,
                        KEY_ADDRESS, KEY_FUNCTION, KEY_IMAGE_PATH }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        IBusinessCard bc = new BusinessCard(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5), cursor.getString(6),
                cursor.getString(7));
    // return business card
        return bc;
    }

    // Getting All business cards
    public List<IBusinessCard> getAllBusinessCards() {
        List<IBusinessCard> bCList = new ArrayList<IBusinessCard>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_BUSINESS_CARD;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                IBusinessCard bc = new BusinessCard();
                bc.setId(Integer.parseInt(cursor.getString(0)));
                bc.setFirstName(cursor.getString(1));
                bc.setLastName(cursor.getString(2));
                bc.setNumbers(cursor.getString(3));
                bc.setMail(cursor.getString(4));
                bc.setAddress(cursor.getString(5));
                bc.setFunction(cursor.getString(6));
                bc.setImagePath(cursor.getString(7));
                // Adding contact to list
                bCList.add(bc);
            } while (cursor.moveToNext());
        }
        // return contact list
        return bCList;
    }

    // Getting business cards Count
    public int getBusinessCardsCount() {
        String countQuery = "SELECT * FROM " + TABLE_BUSINESS_CARD;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        // return count
        return cursor.getCount();
    }

    // Updating a business card
    public int updateBusinessCard(IBusinessCard bc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FIRST_NAME,bc.getFirstName());
        values.put(KEY_LAST_NAME,bc.getLastName());
        values.put(KEY_NUMBERS,bc.getNumbers());
        values.put(KEY_MAIL,bc.getMail());
        values.put(KEY_ADDRESS,bc.getAddress());
        values.put(KEY_FUNCTION,bc.getFunction());
        values.put(KEY_IMAGE_PATH,bc.getImagePath());
        // updating row
        return db.update(TABLE_BUSINESS_CARD, values, KEY_ID + " = ?",
                new String[]{String.valueOf(bc.getId())});
    }

    // Deleting a business card
    public void deleteBusinessCard(IBusinessCard bc) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BUSINESS_CARD, KEY_ID + " = ?",
                new String[] { String.valueOf(bc.getId()) });
        db.close();
    }


}
