package com.sixtel.traveltracker;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;

/**
 *
 * This will handle a task asynchronously, but will allow it to preserve through state changes
 * such as rotations.
 *
 * Created by branden on 8/4/16.
 */
public abstract class DBCursorLoader extends AsyncTaskLoader<Cursor> {

    private Cursor mCursor;


    public DBCursorLoader(Context context) {
        super(context);
    }

    protected abstract Cursor loadCursor();

    @Override
    public Cursor loadInBackground() {
        Cursor cursor = loadCursor();
        if (cursor != null) {
            cursor.getCount();  //makes sure all the data is loaded in, as it may not be.
        }

        return cursor;
    }

    @Override
    public void deliverResult(Cursor data) {
        Cursor oldCursor = mCursor;
        mCursor = data;

        if (isStarted()) {
            super.deliverResult(data);
        }

        //clean up old resoruces
        if (oldCursor != null && oldCursor != data)  {
            onReleaseResources(oldCursor);
        }
    }

    @Override
    protected void onStartLoading() {
        if (mCursor != null) {
            deliverResult(mCursor);
        }
        if (takeContentChanged() || mCursor == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    public void onCanceled(Cursor data) {
        super.onCanceled(data);
        if (data != null) {
            onReleaseResources(data);
        }
    }

    @Override
    protected void onReset() {
        super.onReset();

        onStopLoading();

        if (mCursor != null) {
            onReleaseResources(mCursor);
        }
        mCursor = null;
    }

    private void onReleaseResources(Cursor cursor) {
        if (!cursor.isClosed()) {
            cursor.close();
        }
    }


}
