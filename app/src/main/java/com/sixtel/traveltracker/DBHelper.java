package com.sixtel.traveltracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by branden on 7/28/16.
 */
public class DBHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "traveltracker.db";
    private static final String MEMORIES_TABLE = "memories";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";
    private static final String COLUMN_CITY = "city";
    private static final String COLUMN_COUNTRY = "country";
    private static final String COLUMN_NOTES = "notes";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE "+ MEMORIES_TABLE
                + " ("+COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_LATITUDE +" DOUBLE, "
                + COLUMN_LONGITUDE + " DOUBLE, "
                + COLUMN_CITY + " VARCHAR(255), "
                + COLUMN_COUNTRY + " VARCHAR(255), "
                + COLUMN_NOTES + " TEXT"
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
