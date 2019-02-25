package com.delfinerija.baristaApp.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.GnssStatus;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.delfinerija.baristaApp.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import es.dmoral.toasty.Toasty;

import static android.content.Context.LOCATION_SERVICE;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap gmap;
    private LocationManager mLocationManager;
    private boolean isMyLocationOn = false;


    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static final int LOCATION_REQUEST_CODE = 1997;
    private static final int ENABLE_LOCATION_RESULT_CODE = 42;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_coffeshop_locations, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //maps setup
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }


        //setting map with coffeshops
        mapView = view.findViewById(R.id.map_view);
        mapView.onCreate(mapViewBundle);
        initMaps();

        if (checkPermission()) {
            checkIfLocationIsEnabled();
        }

    }


    private void checkIfLocationIsEnabled() {
        mLocationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(LOCATION_SERVICE);
        setLocationListeners();

        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }else{
            isMyLocationOn = true;
            initMaps();
        }
    }

    private void initMaps() {
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        putPinsOnMap(gmap);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        gmap.setMyLocationEnabled(true);
        if(isMyLocationOn){
            zoomToMyLocation(gmap);
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),ENABLE_LOCATION_RESULT_CODE);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        isMyLocationOn = false;
                        initMaps();
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    private void zoomToMyLocation(GoogleMap googleMap) {
        Location location = getLastKnownLocation();

        if(location != null){
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            LatLng latLng = new LatLng(latitude, longitude);

            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(latLng, 8);
            googleMap.animateCamera(yourLocation);
        }else{
            Toasty.error(getActivity(),"Your GPS location is not recognizable!", Toast.LENGTH_SHORT,false).show();
        }
    }

    private void putPinsOnMap(GoogleMap googleMap){
        //TODO proci kroz listu svih lokala i sve lokacije staviti na kartu
        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(resizeMapIcons(R.drawable.coffee_marker,65,65));

        googleMap.addMarker(new MarkerOptions().position(new LatLng(45.813823,15.983918)).title("Caffe Bar Finjak").icon(icon));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(45.817365,15.976507)).title("Caffe Bar History").icon(icon));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(45.732295,15.993450)).title("Caffe Bar Oxygen").icon(icon));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(45.791110,15.915790)).title("Caffe Bar Vivas Prečko").icon(icon));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(45.716210,15.997670)).title("Caffe Bar Dobardan").icon(icon));
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
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
                    checkIfLocationIsEnabled();
                } else {
                    getActivity().finish();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    private Bitmap resizeMapIcons(int iconName, int width, int height){
        Bitmap bm = BitmapFactory.decodeResource(getResources(), iconName);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bm, width, height, false);
        return resizedBitmap;
    }


    private Location getLastKnownLocation() {
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    private void setLocationListeners(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mLocationManager.registerGnssStatusCallback(new GnssStatus.Callback() {
                @Override
                public void onFirstFix(int ttffMillis) {
                    isMyLocationOn = true;
                    initMaps();
                }

            });
        } else{
            mLocationManager.addGpsStatusListener(new GpsStatus.Listener() {
                @Override
                public void onGpsStatusChanged(int event) {
                    if(event == GpsStatus.GPS_EVENT_FIRST_FIX){
                        isMyLocationOn = true;
                        initMaps();
                    }
                }
            });
        }
    }





}
