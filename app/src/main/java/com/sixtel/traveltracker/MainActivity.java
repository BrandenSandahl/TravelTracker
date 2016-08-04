package com.sixtel.traveltracker;

import android.Manifest;
import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMapClickListener, MemoryDialogFragment.Listener, GoogleMap.OnMarkerDragListener,
        GoogleMap.OnInfoWindowClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOCATION_REQUEST_CODE = 200;
    private static final String MEMORY_DIALOG_TAG = "MemoryDialog";

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private HashMap<String, Memory> mMemories = new HashMap<>();
    private MemoriesDataSource mDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST_CODE);
        }

        mDataSource = new MemoriesDataSource(this);
        getLoaderManager().initLoader(0, null, this);


        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mMap = mapFragment.getMap();
        onMapReady(mMap);

    }


    private void addGoogleAPIClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        if (requestCode == LOCATION_REQUEST_CODE) {
//            if (permissions.length == 1 &&
//                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
//                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                mMap.setMyLocationEnabled(true);
//            } else {
//                // Permission was denied. Display an error message.
//            }
//        }
//    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(this);
        mMap.setInfoWindowAdapter(new MarkerAdapter(getLayoutInflater(), mMemories));
        mMap.setOnMarkerDragListener(this);
        mMap.setOnInfoWindowClickListener(this);

        //Regular asyncTask example
//        new AsyncTask<Void, Void, List<Memory>>() {
//
//            //this happens in background
//            @Override
//            protected List<Memory> doInBackground(Void... params) {
//                return mDataSource.getAllMemories();
//            }
//
//            //this returns to main thread with results from background
//            @Override
//            protected void onPostExecute(List<Memory> memories) {
//                onFetchedMemories(memories);
//            }
//        }.execute();

    }

    private void onFetchedMemories(List<Memory> memories) {
        for (Memory m : memories) {
            addMarker(m);
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Memory memory = new Memory();

        updateMemoryPosition(memory, latLng);

        MemoryDialogFragment.newInstance(memory).show(getFragmentManager(), MEMORY_DIALOG_TAG);
    }



    @Override
    protected void onStart() {
        super.onStart();
        //commented out b/c doesnt work well with emulator,  but this is how to get a location on startup
//        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        double latitiude = location.getLatitude();
        double longitude = location.getLongitude();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    /* Methods from our interface in MemoryDialogFragment */
    @Override
    public void OnSaveClicked(Memory memory) {
        addMarker(memory);
        mDataSource.createMemory(memory);


    }


    /* loader call back stuff */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new MemoriesLoader(this, mDataSource);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        onFetchedMemories(mDataSource.cursorToMemories(data));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }



    private void addMarker(Memory memory) {
        Marker marker = mMap.addMarker(new MarkerOptions()
                .draggable(true)
                .position(new LatLng(memory.latitude, memory.longitute)));

        mMemories.put(marker.getId(), memory);
    }

    @Override
    public void OnCancelClicked(Memory memory) {

    }


    /* Drag Stuff */
    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Memory memory = mMemories.get(marker.getId());
        updateMemoryPosition(memory, marker.getPosition());
        mDataSource.updateMemory(memory);

    }


    private void updateMemoryPosition(Memory memory, LatLng latLng) {
        Geocoder geocoder = new Geocoder(this);
        List<Address> matches = null;
        try {
            matches = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Address bestMatch = (matches.isEmpty()) ? null : matches.get(0);
        int maxLines = bestMatch.getMaxAddressLineIndex();

        memory.city = bestMatch.getAddressLine(maxLines - 1);
        memory.country = bestMatch.getAddressLine(maxLines);
        memory.latitude = latLng.latitude;
        memory.longitute = latLng.longitude;
    }


    @Override
    public void onInfoWindowClick(final Marker marker) {
        final Memory memory = mMemories.get(marker.getId());
        String[] actions = {"Edit", "Delete"};
        new AlertDialog.Builder(this)
                .setTitle(memory.city.toString()+ ", "+ memory.country.toString())
                .setItems(actions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 1) { //refers to the index of delete in the actions array
                            marker.remove();
                            mDataSource.deleteMemory(memory);
                        }
                    }
                })
                .show();

    }

}
