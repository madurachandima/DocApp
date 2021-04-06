package com.example.docapp.Category_consulter.consulter_dashboard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.docapp.Category_consulter.consulter_dashboard.consultant_map.ConsultantMap;
import com.example.docapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class FragmentProfileSetting extends Fragment {
    private static final String TAG = "FragmentProfileSetting";

    private EditText mEditTextName;
    private EditText mEditTextConNumber;
    private EditText mEditTextLocation;
    private EditText mEditTextTime;
    private EditText mEditTextDescription;
    private EditText mEditTextExperience;
    //    private EditText mEditTextDoctorId;
    private EditText mEditTextHospitalName;

    private ImageView mImageViewProPic;
    private ImageView mImageViewBtnChangePic;

    private Button mButtonOpenMap;
    private Button mButtonSave;
    private Button mButtonChangePw;
    private ProgressBar mBar;
    private FirebaseFirestore mFirestore;
    private StorageReference mStorageReference;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;


    private String mConID;

    private String conAvailableTime;
    private String conContactNumber;
    private String conDescription;
    private String conLocation;
    private String conName;
    private String imageURL;
    private String userId;
    private String lat;
    private String longt;

    private String experienceYears;
    //    private String doctorId;
    private String hospitalName;

//    private String mNewImageUrl;


    //Shared Pref Keys
    public static final String CONSULTANT_PREFS = "consultantPref";

//    public static final String SAVE_TIME_KEY = "conAvailableTime";
//    public static final String SAVE_NUMBER_KEY = "conContactNumber";
//    public static final String SAVE_DESCRIPTION_KEY = "conDescription";
//    public static final String SAVE_LOCATION_KEY = "conLocation";
//    public static final String SAVE_NAME_KEY = "conName";
//    public static final String SAVE_IMAGE_URL_KEY = "imageURL";
//    public static final String SAVE_TYPE_KEY = "consulter";
//    public static final String SAVE_USER_ID_KEY = "userId";
//    public static final String SAVE_XP_KEY = "xp";
//    public static final String SAVE_HOSPITAL_KEY = "hospitalName";

    public static final String SAVE_LAT_KEY = "conLatitude";
    public static final String SAVE_LONG_KEY = "conLongitude";


    public FragmentProfileSetting() {
        // Required empty public constructor
    }

    public static FragmentProfileSetting newInstance(String param1, String param2) {
        FragmentProfileSetting fragment = new FragmentProfileSetting();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mFirestore = FirebaseFirestore.getInstance();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mAuth = mAuth.getInstance();

        mConID = mAuth.getCurrentUser().getUid();

        mFirebaseUser = mAuth.getCurrentUser();
        View view = inflater.inflate(R.layout.fragment_profile_setting, container, false);
        //   setHasOptionsMenu(true);
        bindViews(view);


        retrieveConsultantData();

        mImageViewBtnChangePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePic();
            }
        });

        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (inputValidation() == true ) {
                    saveUpdatedData();
                }


            }
        });

        mButtonOpenMap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (inputValidation() == true) {

                //    saveDataInSP();

                    Intent intent = new Intent(getContext(), ConsultantMap.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                    //  startActivity(new Intent(getContext(), ConsultantMap.class));

                } else {
                    Toast.makeText(getContext(), "PleaseFill above data and Upload Profile Picture..", Toast.LENGTH_SHORT).show();

                }
            }
        });

//        String lat = null;
//        String log = null;

//        if (lat != null && log != null) {
//
//            lat = getArguments().getString("LAT");
//            log = getArguments().getString("LONG");
//            Log.d(TAG, "onCreateView: " + lat + "   " + log);
//        }


//        Log.d(TAG, "onCreateView: " + lat + " " + log);

        mButtonChangePw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText resetPw = new EditText(getContext());

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Reset Password");
                builder.setMessage("Enter new password > 6 Character long");
                builder.setView(resetPw);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newPassword = resetPw.getText().toString();

                        mFirebaseUser.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "Password reset success..", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
                                Log.d(TAG, "onFailure: " + e.getMessage());
                            }
                        });

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.create().show();
            }
        });

        return view;
    }

    private void saveDataInSP() {

//        //Get data from Edit Text
//        final String name = mEditTextName.getText().toString();
//        final String number = mEditTextConNumber.getText().toString();
//        final String location = mEditTextLocation.getText().toString();
//        final String time = mEditTextTime.getText().toString();
//        final String desc = mEditTextDescription.getText().toString();
//        final String xp = mEditTextExperience.getText().toString();
//        final String hspName = mEditTextHospitalName.getText().toString();
//
//
//        if (!imageURL.equals(null)) {
//
//
//            //Save data in SP
//
//            SharedPreferences.Editor preferences = this.getActivity().getSharedPreferences(CONSULTANT_PREFS, 0).edit();
//
//            preferences.putString(SAVE_NAME_KEY, name);
//            preferences.putString(SAVE_NUMBER_KEY, number);
//            preferences.putString(SAVE_LOCATION_KEY, location);
//            preferences.putString(SAVE_TIME_KEY, time);
//            preferences.putString(SAVE_DESCRIPTION_KEY, desc);
//            preferences.putString(SAVE_XP_KEY, xp);
//            preferences.putString(SAVE_HOSPITAL_KEY, hspName);
//            preferences.putString(SAVE_IMAGE_URL_KEY, imageURL);
//            preferences.apply();
//
//        } else {
//            SharedPreferences.Editor preferences = this.getActivity().getSharedPreferences(CONSULTANT_PREFS, 0).edit();
//
//            preferences.putString(SAVE_NAME_KEY, name);
//            preferences.putString(SAVE_NUMBER_KEY, number);
//            preferences.putString(SAVE_LOCATION_KEY, location);
//            preferences.putString(SAVE_TIME_KEY, time);
//            preferences.putString(SAVE_DESCRIPTION_KEY, desc);
//            preferences.putString(SAVE_XP_KEY, xp);
//            preferences.putString(SAVE_HOSPITAL_KEY, hspName);
//            preferences.apply();
//
//        }

    }

    private void bindViews(View view) {


        mEditTextName = view.findViewById(R.id.con_edit_name);
        mEditTextConNumber = view.findViewById(R.id.con_edit_contactNo);
        mEditTextLocation = view.findViewById(R.id.con_edit_location);
        mEditTextTime = view.findViewById(R.id.con_edit_avlTime);
        mEditTextDescription = view.findViewById(R.id.con_edit_description);

        mEditTextExperience = view.findViewById(R.id.con_edit_Xp_years);
//        mEditTextDoctorId = view.findViewById(R.id.con_edit_doctor_id);
        mEditTextHospitalName = view.findViewById(R.id.con_edit_Hospital_name);
        mButtonChangePw = view.findViewById(R.id.btn_con_edit_changepw);


        mImageViewProPic = view.findViewById(R.id.con_change_proPic);
        mImageViewBtnChangePic = view.findViewById(R.id.btn_con_change_proPic);


        mButtonOpenMap = view.findViewById(R.id.btn_con_edit_openMap);
        mButtonSave = view.findViewById(R.id.btn_con_edit_save);

        mBar = view.findViewById(R.id.con_edit_prBar);
    }

    private void saveUpdatedData() {

        StorageReference reference = mStorageReference.child("ConsulterProfiles").child(mConID).child("ConImage" + mConID);

        SharedPreferences preferences = this.getActivity().getSharedPreferences(CONSULTANT_PREFS, Context.MODE_PRIVATE);

        lat = preferences.getString(SAVE_LAT_KEY, null);
        longt = preferences.getString(SAVE_LONG_KEY, null);


        final String name = mEditTextName.getText().toString();
        final String number = mEditTextConNumber.getText().toString();
        final String location = mEditTextLocation.getText().toString();
        final String time = mEditTextTime.getText().toString();
        final String desc = mEditTextDescription.getText().toString();
        final String xp = mEditTextExperience.getText().toString();
        final String hspName = mEditTextHospitalName.getText().toString();

        if (inputValidation() == true && !lat.isEmpty() && !longt.isEmpty()) {
            //TODO set data to the SharedPref and save it to firebase
            mBar.setVisibility(View.VISIBLE);

            //Get image URL from Storage..
            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                @Override
                public void onSuccess(Uri uri) {

                    String imageURL = uri.toString();

                    if (!imageURL.equals(null)) {


                        DocumentReference reference1 = mFirestore.collection("consulterDetails").document(mConID);

                        Map<String, Object> updateConData = new HashMap<>();

                        updateConData.put("conAvailableTime", time);
                        updateConData.put("conContactNumber", number);
                        updateConData.put("conDescription", desc);
                        updateConData.put("conLocation", location);
                        updateConData.put("conName", name);
                        updateConData.put("imageURL", imageURL);
                        updateConData.put("type", "consulter");
                        updateConData.put("userId", userId);
                        updateConData.put("experienceYears", xp);
                        updateConData.put("hospitalName", hspName);
                        updateConData.put("conLatitude", lat);
                        updateConData.put("conLongitude", longt);

                        reference1.set(updateConData).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(getContext(), "Successfully Updated..", Toast.LENGTH_SHORT).show();
                                mBar.setVisibility(View.INVISIBLE);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Update Fail..", Toast.LENGTH_SHORT).show();

                            }
                        });
                    } else {
                        Toast.makeText(getContext(), "Upload Profile Image", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {

                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Failed load image..", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private boolean inputValidation() {
        //get Data From EditText Field

        final String name = mEditTextName.getText().toString();
        final String number = mEditTextConNumber.getText().toString();
        final String location = mEditTextLocation.getText().toString();
        final String time = mEditTextTime.getText().toString();
        final String desc = mEditTextDescription.getText().toString();

        final String xp = mEditTextExperience.getText().toString();
//        final String docId = mEditTextDoctorId.getText().toString();
        final String hspName = mEditTextHospitalName.getText().toString();


        if (TextUtils.isEmpty(xp)) {
            mEditTextExperience.setError("Year of Experience is Empty..");
            mEditTextExperience.requestFocus();
            return false;
        }
//        if (TextUtils.isEmpty(docId)) {
//            mEditTextDoctorId.setError("Doctor Id is Empty..");
//        }
        if (TextUtils.isEmpty(hspName)) {
            mEditTextHospitalName.setError("Hospital Name is Empty..");
            mEditTextHospitalName.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(name)) {
            mEditTextName.setError("Name is Empty..");
            mEditTextName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(number)) {
            mEditTextConNumber.setError("Contact Number is Empty..");
            mEditTextConNumber.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(location)) {
            mEditTextLocation.setError("Location is Empty..");
            mEditTextLocation.requestFocus();
            return false;

        }
        if (TextUtils.isEmpty(time)) {
            mEditTextTime.setError("Available time is Empty..");
            mEditTextTime.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(desc)) {
            mEditTextDescription.setError("Description is Empty..");
            mEditTextDescription.requestFocus();
            return false;
        }
        return true;
    }

    private void retrieveConsultantData() {

        try {
            Log.d(TAG, "retrieveConsultantData: " + mConID);
            DocumentReference reference = mFirestore.collection("consulterDetails").document(mConID);

            reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot snapshot) {

                    Log.d(TAG, "onSuccess: " + snapshot);
//                    updateConData.put("conAvailableTime", time);
//                    updateConData.put("conContactNumber", number);
//                    updateConData.put("conDescription", desc);
//                    updateConData.put("conLocation", location);
//                    updateConData.put("conName", name);
//                    updateConData.put("imageURL", imageURL);
//                    updateConData.put("type", "consulter");
//                    updateConData.put("userId", userId);
//                    updateConData.put("experienceYears", xp);
//                    updateConData.put("hospitalName", hspName);
//                    updateConData.put("conLatitude", lat);
//                    updateConData.put("conLongitude", longt);

                    conAvailableTime = snapshot.getString("conAvailableTime");
                    conContactNumber = snapshot.getString("conContactNumber");
                    conDescription = snapshot.getString("conDescription");
                    conLocation = snapshot.getString("conLocation");
                    conName = snapshot.getString("conName");
                    imageURL = snapshot.getString("imageURL");
                    experienceYears = snapshot.getString("experienceYears");
                    hospitalName = snapshot.getString("hospitalName");

                    lat = snapshot.getString("conLatitude");
                    longt = snapshot.getString("conLongitude");


                    Picasso.get()
                            .load(imageURL)
                            .into(mImageViewProPic);

                    mEditTextName.setText(conName);
                    mEditTextConNumber.setText(conContactNumber);
                    mEditTextLocation.setText(conLocation);
                    mEditTextTime.setText(conAvailableTime);
                    mEditTextDescription.setText(conDescription);
                    mEditTextExperience.setText(experienceYears);
                    mEditTextHospitalName.setText(hospitalName);


                }
            }).

                    addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Error..", Toast.LENGTH_SHORT).show();
                        }
                    });

//        SharedPreferences preferences = this.getActivity().getSharedPreferences(CONSULTANT_PREFS, Context.MODE_PRIVATE);

//get Data from SP
//        conAvailableTime = preferences.getString(SAVE_TIME_KEY, null);
//        conContactNumber = preferences.getString(SAVE_NUMBER_KEY, null);
//        conDescription = preferences.getString(SAVE_DESCRIPTION_KEY, null);
//        conLocation = preferences.getString(SAVE_LOCATION_KEY, null);
//        conName = preferences.getString(SAVE_NAME_KEY, null);
//        imageURL = preferences.getString(SAVE_IMAGE_URL_KEY, null);
//        experienceYears = preferences.getString(SAVE_XP_KEY, null);
//        hospitalName = preferences.getString(SAVE_HOSPITAL_KEY, null);
//        lat = preferences.getString(SAVE_LAT_KEY, null);
//        longt = preferences.getString(SAVE_LONG_KEY, null);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void changePic() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1000);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {

                Uri uri = data.getData();

                Picasso.get()
                        .load(uri)
                        .into(mImageViewProPic);

                uploadImageToFirebase(uri);
            }
        }
    }


    private void uploadImageToFirebase(Uri uri) {

        mBar.setVisibility(View.VISIBLE);

        StorageReference reference = mStorageReference.child("ConsulterProfiles").child(mConID).child("ConImage" + mConID);

        reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getContext(), "Upload completed..", Toast.LENGTH_SHORT).show();
                mBar.setVisibility(View.INVISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Upload failed ", Toast.LENGTH_SHORT).show();
            }
        });

    }


}