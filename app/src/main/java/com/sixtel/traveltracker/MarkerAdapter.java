package com.sixtel.traveltracker;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by branden on 7/26/16.
 */
public class MarkerAdapter implements GoogleMap.InfoWindowAdapter {

    private LayoutInflater mLayoutInflater;
    private View mView;

    MarkerAdapter(LayoutInflater layoutInflater) {
        mLayoutInflater = layoutInflater;
    }


    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        if (mView == null) {
            mView = mLayoutInflater.inflate(R.layout.marker, null);
        }

        TextView titleView = (TextView) mView.findViewById(R.id.title);
        titleView.setText(marker.getTitle());

        TextView snippetView = (TextView) mView.findViewById(R.id.snippet);
        snippetView.setText(marker.getSnippet());

        return mView;
    }
}
