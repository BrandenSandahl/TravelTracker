package com.sixtel.traveltracker;

import android.content.ContentValues;
import android.content.Context;

/**
 * DAO
 *
 * Created by branden on 8/2/16.
 */
public class MemoriesDataSource {

    private DBHelper mDBHelper;

    public MemoriesDataSource(Context context) {
        mDBHelper = DBHelper.getInstance(context);
    }



    public void createMemory(Memory memory) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_NOTES, memory.notes);
        values.put(DBHelper.COLUMN_CITY, memory.city);
        values.put(DBHelper.COLUMN_COUNTRY, memory.country);
        values.put(DBHelper.COLUMN_LATITUDE, memory.latitude);
        values.put(DBHelper.COLUMN_LONGITUDE, memory.longitute);

        mDBHelper.getWritableDatabase().insert(DBHelper.MEMORIES_TABLE, null, values);

    }


}
