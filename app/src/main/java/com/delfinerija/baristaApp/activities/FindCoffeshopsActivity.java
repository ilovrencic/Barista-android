package com.delfinerija.baristaApp.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.delfinerija.baristaApp.R;
import com.delfinerija.baristaApp.adapters.SectionsPagerAdapter;
import com.delfinerija.baristaApp.entities.ApiResponse;
import com.delfinerija.baristaApp.entities.MapLocation;
import com.delfinerija.baristaApp.fragments.BeersFragment;
import com.delfinerija.baristaApp.fragments.ListOfCoffeshopsFragment;
import com.delfinerija.baristaApp.fragments.MapsFragment;
import com.delfinerija.baristaApp.network.ApiService;
import com.delfinerija.baristaApp.network.GenericResponse;
import com.delfinerija.baristaApp.network.InitApiService;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindCoffeshopsActivity extends BasicActivity implements LocationListener {

    private ApiService apiService;
    private LocationManager mLocationManager;
    private Call<GenericResponse<List<ApiResponse<MapLocation>>>> getLocations;
    private List<ApiResponse<MapLocation>> locations = new ArrayList<>();
    private LottieAnimationView animationView;
    private TextView location_text;

    private static final int ENABLE_LOCATION_RESULT_CODE = 42;
    private static final int LOCATION_REQUEST_CODE = 1997;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findcoffeshops);

        if (savedInstanceState != null) {
            InitApiService.initApiService();
        }
        apiService = InitApiService.apiService;
        location_text = findViewById(R.id.location_text);
        animationView = findViewById(R.id.location_animation);
        animationView.setSpeed((float) 0.8);
        location_text.setVisibility(View.INVISIBLE);
        animationView.setVisibility(View.INVISIBLE);


        if (checkPermission()) {
            checkIfLocationIsEnabled(0);
        }


        //setupViewPager();
    }


    private void setupViewPager() {
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        sectionsPagerAdapter.addFragment(new MapsFragment(locations));
        sectionsPagerAdapter.addFragment(new ListOfCoffeshopsFragment(locations));
        ViewPager viewPager = findViewById(R.id.holder);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return false;
        }
        return true;

    }

    private void checkIfLocationIsEnabled(int numOfCalls) {
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (numOfCalls > 0) {
                Toasty.warning(this, "This feature requires GPS signal.", Toast.LENGTH_SHORT, false).show();
                finish();
            } else {
                displayLocationSettingsRequest(this);
            }
        } else {
            loadLocationsFromServer(getUserLocation());
        }
    }

    private String getUserLocation() {
        Location location = getLastKnownLocation();
        StringBuilder stringBuilder = new StringBuilder();

        if (location != null) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();

            stringBuilder.append(latitude).append(",").append(longitude);
            return stringBuilder.toString();
        } else {
            return null;
        }
    }


    private Location getLastKnownLocation() {
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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


    private void loadLocationsFromServer(String user_location) {
        if (user_location == null) {
            //Toasty.error(this, "Something went wrong. Check your GPS signal.", Toast.LENGTH_SHORT, false).show();
            finish();
        } else {
            getLocations = apiService.getLocations(getUserToken(), user_location);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getLocations.enqueue(new Callback<GenericResponse<List<ApiResponse<MapLocation>>>>() {
                        @Override
                        public void onResponse(Call<GenericResponse<List<ApiResponse<MapLocation>>>> call, Response<GenericResponse<List<ApiResponse<MapLocation>>>> response) {
                            if (response.isSuccessful()) {
                                locations = response.body().getResponseData();

                                setupViewPager();

                            } else {
                                try {
                                    showError(response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<GenericResponse<List<ApiResponse<MapLocation>>>> call, Throwable t) {
                            showError(t.getMessage());
                            t.printStackTrace();
                        }
                    });
                }
            }, 100);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == ENABLE_LOCATION_RESULT_CODE) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            animationView.setVisibility(View.VISIBLE);
            location_text.setVisibility(View.VISIBLE);
            animationView.playAnimation();
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0,this);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkIfLocationIsEnabled(0);
                } else {
                    finish();
                }
            }
        }
    }



    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        //Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        //Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(FindCoffeshopsActivity.this,ENABLE_LOCATION_RESULT_CODE);
                        } catch (IntentSender.SendIntentException e) {
                            //Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        //Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }


    @Override
    public void onLocationChanged(Location location) {
        animationView.setVisibility(View.GONE);
        location_text.setVisibility(View.GONE);
        mLocationManager.removeUpdates(this);
        checkIfLocationIsEnabled(1);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}
