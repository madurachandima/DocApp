package com.example.docapp.Category_consulter.consulter_dashboard.consultant_map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.docapp.Category_consulter.consulter_dashboard.FragmentProfileSetting;
import com.example.docapp.R;
import com.example.docapp.user_handeling.ConsultantRegistration;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class ConsultantMap extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "ConsultantMap";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final float DEFAULT_ZOOM = 15.0f;
    private GoogleMap mGoogleMap;
    private FusedLocationProviderClient mLocationProviderClient;
    private Location mLocation;
    private ImageView mImgSetLocation;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    private String className;
    private String mConID;

    //TODO GetCurrent Location
    //TODO BAck To Fragment..
    //TODO Drower Header Done

    //widget
    private EditText mSearchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultant_map);

        Intent intent = getIntent();
        className = intent.getStringExtra("CLASS");

        mSearchEditText = findViewById(R.id.id_con_map_inputSearch);
        mImgSetLocation = findViewById(R.id.id_add_con_location);
        //   Places.initialize(this, "AIzaSyA6HjyJO2q3uWDKWYTsv71FseYbmjvgKSU");

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        if (isServiceOk()) {
            init();
        }

        mLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        grantLocationPermission();

//        setContentView(R.layout.activity_consultant_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.consultant_map);
        mapFragment.getMapAsync(this);


    }

    private void getSearchText() {
        mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {

                    //exe Search method
                    geoLocate();
                }
                return false;
            }
        });

    }

    private void geoLocate() {
        String searchText = mSearchEditText.getText().toString();

//        List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,
//                Place.Field.LAT_LNG, Place.Field.NAME);
//
//        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList)
//                .build(ConsultantMap.this);
//
//        startActivityForResult(intent, 100);


        Geocoder geocoder = new Geocoder(ConsultantMap.this);
        List<Address> addressList = new ArrayList<>();
        try {
            addressList = geocoder.getFromLocationName(searchText, 1);

        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "geoLocat: " + e.getMessage());
        }

        if (addressList.size() > 0) {
            final Address address = addressList.get(0);

            Log.d(TAG, "geoLocat: " + address.toString());

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getAddressLine(0));

            if (!searchText.isEmpty()) {

                mImgSetLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final String lat = String.valueOf(address.getLatitude());
                        final String log = String.valueOf(address.getLongitude());


                        if (!lat.isEmpty() && !log.isEmpty()) {

                            mConID = mAuth.getCurrentUser().getUid();

                            SharedPreferences.Editor preferences = getSharedPreferences(FragmentProfileSetting.CONSULTANT_PREFS, 0).edit();
                            preferences.putString(FragmentProfileSetting.SAVE_LAT_KEY, lat);
                            preferences.putString(FragmentProfileSetting.SAVE_LONG_KEY, log);
                            preferences.apply();

//                            DocumentReference reference = mFirestore.collection("consulterDetails").document(mConID);
//
//                            reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                @Override
//                                public void onSuccess(DocumentSnapshot snapshot) {
//
//                                    String conAvailableTime = snapshot.getString("conAvailableTime");
//                                    String conContactNumber = snapshot.getString("conContactNumber");
//                                    String conDescription = snapshot.getString("conDescription");
//                                    String conLocation = snapshot.getString("conLocation");
//                                    String conName = snapshot.getString("conName");
//                                    String imageURL = snapshot.getString("imageURL");
//
//                                    String experienceYears = snapshot.getString("experienceYears");
////                                    String doctorId = snapshot.getString("doctorId");
//                                    String hospitalName = snapshot.getString("hospitalName");
//
//                                    Toast.makeText(ConsultantMap.this, "Successfully Set Location.. ", Toast.LENGTH_SHORT).show();
//
//                                    updateData(conAvailableTime, conContactNumber, conDescription, conLocation,
//                                            conName, imageURL, experienceYears, hospitalName, lat, log);
//
//
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(getApplicationContext(), "Error..", Toast.LENGTH_SHORT).show();
//                                }
//                            });

                        }

                    }
                });
            } else {
                Toast.makeText(this, "Search Your Location..", Toast.LENGTH_SHORT).show();
            }

        }
    }

//    private void updateData(String conAvailableTime, String conContactNumber, String conDescription,
//                            String conLocation, String conName, String imageURL, String experienceYears,
//                            String hospitalName, String lat, String log) {
//
//        DocumentReference reference1 = mFirestore.collection("consulterDetails").document(mConID);
//
//        Map<String, Object> updateConData = new HashMap<>();
//
//        updateConData.put("conAvailableTime", conAvailableTime);
//        updateConData.put("conContactNumber", conContactNumber);
//        updateConData.put("conDescription", conDescription);
//        updateConData.put("conLocation", conLocation);
//        updateConData.put("conName", conName);
//        updateConData.put("imageURL", imageURL);
//        updateConData.put("type", "consulter");
////        updateConData.put("userId", userId);
//
//
//        updateConData.put("experienceYears", experienceYears);
////        updateConData.put("doctorId", doctorId);
//        updateConData.put("hospitalName", hospitalName);
//        updateConData.put("conLatitude", lat);
//        updateConData.put("conLongitude", log);
//
//        reference1.set(updateConData).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
////                startActivity(new Intent(ConsultantMap.this,FragmentProfileSetting.class));
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);

            Log.d(TAG, "onActivityResult: " + place.getAddress());
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);
            Log.d(TAG, "onActivityResult: " + status.getStatusMessage());
            Toast.makeText(ConsultantMap.this, status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void init() {

    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(title);
        mGoogleMap.addMarker(options);
    }

    public boolean isServiceOk() {
        Log.d(TAG, "isServiceOk: Checking Google service..");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(ConsultantMap.this);

        if (available == ConnectionResult.SUCCESS) {
            Log.d(TAG, "isServiceOk: Google Play service Working..");

            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Log.d(TAG, "isServiceOk: Error...");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(ConsultantMap.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "You Cant make map request..", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void grantLocationPermission() {

        Dexter.withContext(getApplicationContext()).withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        @SuppressLint("MissingPermission") Task<Location> task = mLocationProviderClient.getLastLocation();

                        task.addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    mLocation = location;
                                    Log.d(TAG, "onSuccess: " + location);
                                }
                            }
                        });

                        getSearchText();
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
        mGoogleMap = googleMap;


        LatLng sydney = new LatLng(-34, 151);
        mGoogleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }


}