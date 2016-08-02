package com.sixtel.traveltracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO
 *
 * Created by branden on 8/2/16.
 */
public class MemoriesDataSource {

    private DBHelper mDBHelper;
    private String[] allColumns = {
            DBHelper.COLUMN_ID, DBHelper.COLUMN_CITY,
            DBHelper.COLUMN_COUNTRY, DBHelper.COLUMN_LATITUDE,
            DBHelper.COLUMN_LONGITUDE, DBHelper.COLUMN_NOTES
    };

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

    public List<Memory> getAllMemories() {
        List<Memory> memories = new ArrayList<>();

        Cursor cursor = mDBHelper.getReadableDatabase().query(DBHelper.MEMORIES_TABLE, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Memory memory = cursorToMemory(cursor);
            memories.add(memory);
            cursor.moveToNext();
        }
        cursor.close();
        return memories;
    }


    private Memory cursorToMemory(Cursor cursor) {
        Memory memory = new Memory();
        memory.city = cursor.getString(1);
        memory.country = cursor.getString(2);
        memory.latitude = cursor.getDouble(3);
        memory.longitute = cursor.getDouble(4);
        memory.notes = cursor.getString(5);
        return  memory;
    }


}
