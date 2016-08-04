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

        memory.id = mDBHelper.getWritableDatabase().insert(DBHelper.MEMORIES_TABLE, null, values);
    }

    public List<Memory> getAllMemories() {
        Cursor cursor = allMemoriesCursor();
        return cursorToMemories(cursor);
    }


    /**
     *
     * @return cursor containing all memories in DB
     */
    public Cursor allMemoriesCursor() {
        return mDBHelper.getReadableDatabase().query(DBHelper.MEMORIES_TABLE, allColumns, null, null, null, null, null);

    }

    /**
     *
     * @param cursor cursor containing all memories in DB
     * @return list of Memories in DB
     */
    public List<Memory> cursorToMemories(Cursor cursor) {
        List<Memory> memories = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Memory memory = cursorToMemory(cursor);
            memories.add(memory);
            cursor.moveToNext();
        }
        return memories;
    }


    public void updateMemory(Memory memory) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_NOTES, memory.notes);
        values.put(DBHelper.COLUMN_CITY, memory.city);
        values.put(DBHelper.COLUMN_COUNTRY, memory.country);
        values.put(DBHelper.COLUMN_LATITUDE, memory.latitude);
        values.put(DBHelper.COLUMN_LONGITUDE, memory.longitute);

        String[] whereArgs = {String.valueOf(memory.id)};

        mDBHelper.getWritableDatabase().update(
                mDBHelper.MEMORIES_TABLE,
                values,
                mDBHelper.COLUMN_ID + "=?",
                whereArgs
        );
    }

    public void deleteMemory(Memory memory) {
        String[] whereArgs = {String.valueOf(memory.id)};

        mDBHelper.getWritableDatabase().delete(
                mDBHelper.MEMORIES_TABLE,
                mDBHelper.COLUMN_ID + "=?",
                whereArgs
        );
    }

    private Memory cursorToMemory(Cursor cursor) {
        Memory memory = new Memory();
        memory.id = cursor.getLong(0);
        memory.city = cursor.getString(1);
        memory.country = cursor.getString(2);
        memory.latitude = cursor.getDouble(3);
        memory.longitute = cursor.getDouble(4);
        memory.notes = cursor.getString(5);
        return  memory;
    }


}
