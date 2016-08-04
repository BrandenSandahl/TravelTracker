package com.sixtel.traveltracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by branden on 7/28/16.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "traveltracker.db";
    public static final String MEMORIES_TABLE = "memories";
    private static final int DATABASE_VERSION = 1;

    private static DBHelper singleton = null;


    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_COUNTRY = "country";
    public static final String COLUMN_NOTES = "notes";



    //synchronized forces getInstance to be called by only one thing at a time
    public synchronized static DBHelper getInstance(Context context) {
        if (singleton == null) {
            singleton = new DBHelper(context.getApplicationContext());
        }
        return  singleton;

    }

    private DBHelper(Context context) {
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
        //for test purposes only, this will kill all of your data
        db.execSQL("DROP TABLE IF EXISTS " + MEMORIES_TABLE);
        onCreate(db);
    }
}
