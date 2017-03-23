package com.prixesoft.david.weatherextract;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Marker marker;
    private double latitude,longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;



        // Add a marker in Sofia and move the camera
        LatLng sofia = new LatLng(42.697705, 23.321638);
        marker = mMap.addMarker(new MarkerOptions().position(sofia).title("Marker in Sofia"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sofia));

        mMap.setOnCameraMoveListener (new GoogleMap.OnCameraMoveListener () {
            @Override
            public void onCameraMove() {
                mMap.clear();
                marker = mMap.addMarker(new MarkerOptions().position(mMap.getCameraPosition().target));
            }


        });

        googleMap.setOnMarkerClickListener(this);


    }


    void addLocation(){
        longitude = mMap.getCameraPosition().target.longitude;
        latitude =  mMap.getCameraPosition().target.latitude;
    }



    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.equals(marker))
        {
            addLocation();
            marker.setTitle("Current Selection");
            marker.setSnippet("Lat:" + latitude + " Long:" + longitude);
        }

        return false;
    }


    public void onButtonClick(View view){
        addLocation();
        Intent intentWeatherInfo = new Intent(MapsActivity.this,WeatherInfo.class);
        intentWeatherInfo.putExtra("longitude",longitude);
        intentWeatherInfo.putExtra("latitude",latitude);
        startActivity(intentWeatherInfo);

    }

}
