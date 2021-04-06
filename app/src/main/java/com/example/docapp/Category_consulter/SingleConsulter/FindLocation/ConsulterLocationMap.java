package com.example.docapp.Category_consulter.SingleConsulter.FindLocation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.docapp.R;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class ConsulterLocationMap extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap map;
    private Double mLat;
    private Double mLng;

    private Location userLocation;

    private FusedLocationProviderClient locationProviderClient;

    private static final String TAG = "ConsulterLocationMap";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        mLat = Double.valueOf(intent.getStringExtra("lat"));
        mLng = Double.valueOf(intent.getStringExtra("lng"));


        locationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        grantLocationPermission();

        setContentView(R.layout.activity_consulter_location_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private void grantLocationPermission() {
        Dexter.withContext(getApplicationContext()).withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        @SuppressLint("MissingPermission") Task<Location> task = locationProviderClient.getLastLocation();

                        task.addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    userLocation = location;
                                }
                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        if (!mLat.equals(null) && !mLng.equals(null)) {

            map = googleMap;
            LatLng location = new LatLng(mLat, mLng);
            map.addMarker(new MarkerOptions().position(location).title("Place"));
            map.moveCamera(CameraUpdateFactory.newLatLng(location));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.latitude, location.longitude), 15.0f));

            Toast.makeText(this, "Click on The MapMaker to open Direction", Toast.LENGTH_LONG).show();

            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Log.d(TAG, "onMarkerClick: " + marker.toString());

                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//                        Uri.parse("http://maps.google.com/maps?saddr=20.344,34.34&daddr=20.5666,45.345"));
                            Uri.parse("http://maps.google.com/maps?saddr=" + mLat + "," + mLng + "" +
                                    "&daddr=" + userLocation.getLatitude() + "," + userLocation.getLongitude() + ""));

                    startActivity(intent);

                    //To start the navigation from the current location, remove the saddr parameter and value.

                    return false;
                }
            });

            //    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12.0f));

        }else {
            Toast.makeText(this, "Unable Load Map..", Toast.LENGTH_SHORT).show();
        }


    }
}