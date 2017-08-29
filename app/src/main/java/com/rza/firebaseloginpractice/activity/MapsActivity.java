package com.rza.firebaseloginpractice.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rza.firebaseloginpractice.R;

import java.security.Provider;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private Criteria criteria;
    private Location location;
    private static final int PERMISSION_REQUEST_ACCESS_LOCATION = 55;
    private String lat;
    private String lng;
    private Button btnOk;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        btnOk = (Button) findViewById(R.id.btn_maps_ok);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //btn ok zatvara activity (jer se poziva for result) i postavlja vrednosti za lat i lng
                Intent returnIntent = new Intent();
                returnIntent.putExtra("lat", lat);
                returnIntent.putExtra("lng", lng);
                Log.d("lat lng intent", lat + " " + lng);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
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
        mMap = googleMap; //odma na otvaranju pita za permission ako nema permissiona
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_ACCESS_LOCATION);
            return;
        }
        initMap();

    }


    private void initMap() { //postavlja marker na poziciju telefona
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        location = getLastKnownLocation();

        if (location != null) {
            Log.d("location", "not null");
            LatLng locationgLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            lat = String.valueOf(locationgLatLng.latitude); //odma na otvaranju postavlja lat i lng na poziciju telefona
            lng = String.valueOf(locationgLatLng.longitude);

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationgLatLng, 13)); //
            mMap.addMarker(new MarkerOptions()
                    .position(locationgLatLng)
                    .title("You are Here!"));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) { //na klik se postavlja marker i lat lng promenljive na mesto klika
                mMap.clear();
                mMap.addMarker(new MarkerOptions()
                        .position(latLng));
                lat = String.valueOf(latLng.latitude);
                lng = String.valueOf(latLng.longitude);
                Log.d("LatLng", lat + " " + lng);
            }

        });

    }

    @Override
    public void onBackPressed() { //na back pressed zatvara activity i postavlja result canceled
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        finish();
    }

    private Location getLastKnownLocation() { //vraca najprecizniju poznatu lokaciju uredjaja
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;

        for (String provider: providers) {

            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) { //otvara permission prompt
            case PERMISSION_REQUEST_ACCESS_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initMap();
                }
            }
        }
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

}
