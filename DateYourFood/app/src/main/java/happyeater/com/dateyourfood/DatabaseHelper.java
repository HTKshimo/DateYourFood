package happyeater.com.dateyourfood;

//package com.example.steh.date_your_food.feature;

import android.content.Context;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ArrayList;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Food";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "FOOD_TABLE";
    private static final String COL_ID = "ID";
    private static final String COL_NAME = "NAME";
    private static final String COL_DATE = "EXPIRY_DATE";
    private static final String COL_DAYS = "DAYS_LEFT";

    SQLiteDatabase database;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        initialize();
    }

    // Check for the correct values of days_left attribute in the table
    public void initialize() {
        SQLiteDatabase db = this.getWritableDatabase();
        List<Food> food = new ArrayList<Food>();
        food = getListContents();
        for (Food f : food) {
            String expiryDateString = f.getExpiry_date();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            // Get Current Date in LocalData type
            LocalDate local_current_date = LocalDate.now();
            LocalDate local_expiry_date = LocalDate.parse(expiryDateString, formatter);
            // Get the difference between the two dates
            long difference = ChronoUnit.DAYS.between(local_current_date, local_expiry_date);
            int days_left = (int) difference;
            ContentValues cv = new ContentValues();
            int id = f.getID();
            String[] arg = new String[1];
            arg[0] = id + "";
            cv.put(COL_NAME, f.getName());
            cv.put(COL_DATE, f.getExpiry_date());
            cv.put(COL_DAYS, days_left);
            db.update(TABLE_NAME, cv, "ID = ?", arg);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table =
                "CREATE TABLE " + TABLE_NAME + "( " +
                        COL_ID + " INTEGER PRIMARY KEY, " +
                        COL_NAME + " TEXT, " +
                        COL_DATE + " TEXT, " +
                        COL_DAYS + " INTEGER " +
                        ");"
                ;
        db.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //addData
    public void insertFood(String name, String date, int days) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_DATE, date);
        values.put(COL_DAYS, days);
        database.insert(TABLE_NAME, null, values);
        database.close();

    }

    public List<Food> getListContents() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        List<Food> data = new ArrayList<Food>();
        if (c.moveToFirst()) {
            do {
                Food f = new Food(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3));
                data.add(f);

            } while (c.moveToNext());
        }

        db.close();

        return data;
    }

}

