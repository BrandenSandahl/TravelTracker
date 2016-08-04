package com.sixtel.traveltracker;

import android.content.Context;
import android.database.Cursor;

/**
 * Created by branden on 8/4/16.
 */
public class MemoriesLoader extends DBCursorLoader {

    private MemoriesDataSource mMemoriesDataSource;

    public MemoriesLoader(Context context, MemoriesDataSource memoriesDataSource) {
        super(context);
        this.mMemoriesDataSource = memoriesDataSource;
    }

    @Override
    protected Cursor loadCursor() {
        return mMemoriesDataSource.allMemoriesCursor();
    }
}
