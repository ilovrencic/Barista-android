package com.delfinerija.baristaApp.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.delfinerija.baristaApp.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap gmap;


    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static final int LOCATION_REQUEST_CODE = 1997;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffeshop_locations);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mapView = findViewById(R.id.map_view);
        mapView.onCreate(mapViewBundle);
        initListeners();

        if (checkPermission()) {
            initMaps();
        }

    }

    private void initMaps() {
        mapView.getMapAsync(this);
    }

    private void initListeners() {
        mapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        gmap.setMyLocationEnabled(true);
        zoomToMyLocation(gmap);
        putPinsOnMap(gmap);
    }

    private void zoomToMyLocation(GoogleMap googleMap) {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        LatLng latLng = new LatLng(latitude,longitude);

        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(latLng, 8);
        googleMap.animateCamera(yourLocation);
    }

    private void putPinsOnMap(GoogleMap googleMap){
        //TODO proci kroz listu svih lokala i sve lokacije staviti na kartu
        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(resizeMapIcons(R.drawable.coffee_marker,65,65));

        googleMap.addMarker(new MarkerOptions().position(new LatLng(45.813823,15.983918)).title("Caffe Bar Finjak").icon(icon));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(45.817365,15.976507)).title("Caffe Bar History").icon(icon));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(45.732295,15.993450)).title("Caffe Bar Oxygen").icon(icon));
    }

    public Bitmap resizeMapIcons(int iconName,int width, int height){
        Bitmap bm = BitmapFactory.decodeResource(getResources(), iconName);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bm, width, height, false);
        return resizedBitmap;
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return false;
        }
        return true;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initMaps();
                } else {
                    finish();
                }
            }
        }
    }




    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

}
