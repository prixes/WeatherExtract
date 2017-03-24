package com.prixesoft.david.weatherextract;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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

        // Setting custom action bar ( centered and have back button for the next activity )
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // Add a marker in Sofia and move the camera
        LatLng sofia = new LatLng(42.697705, 23.321638);
        marker = mMap.addMarker(new MarkerOptions().position(sofia));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sofia));

        // Marker will be our "X" hindsight
        // This is kind of heavy way to do it. But it's smooth and interactive.
        // Listener to start making that on move
        mMap.setOnCameraMoveListener (new GoogleMap.OnCameraMoveListener () {
            @Override
            public void onCameraMove() {
                mMap.clear();
                marker = mMap.addMarker(new MarkerOptions().position(mMap.getCameraPosition().target));
            }


        });


        // Extra listener for on marker click  ... showing long and lat
        googleMap.setOnMarkerClickListener(this);

    }

    // Simple get-set method saves some extra callings
    void addLocation(){
        longitude = mMap.getCameraPosition().target.longitude;
        latitude =  mMap.getCameraPosition().target.latitude;
    }


    // Extra listener for on marker click  ... showing long and lat
    @Override
    public boolean onMarkerClick(Marker marker) {

            addLocation();
            marker.setTitle("Current Selection");
            marker.setSnippet("Lat:" + latitude + " Long:" + longitude);


        return false;
    }



    // Move on to the next activity giving required information for getting weather
    public void onButtonClick(View view){
        addLocation();
        Intent intentWeatherInfo = new Intent(this,WeatherInfoActivity.class);
        intentWeatherInfo.putExtra("longitude",longitude);
        intentWeatherInfo.putExtra("latitude",latitude);
        startActivity(intentWeatherInfo);

    }

}
