package com.example.docapp.user_handeling;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.docapp.Category_consulter.consulter_dashboard.ConsulterDashbordMain;
import com.example.docapp.Category_consulter.consulter_dashboard.consultant_map.ConsultantMap;
import com.example.docapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class ConsultantRegistration extends AppCompatActivity {

    private EditText mEditTextName;
    private EditText mEditTextConNumber;
    private EditText mEditTextLocation;
    private EditText mEditTextTime;
    private EditText mEditTextDescription;
    private EditText mEditTextExperience;
    private EditText mEditTextHospitalName;

    private EditText mEditTextEmail;

    private Button mButtonSave;

    private ProgressBar mConRegProBar;

    private FirebaseFirestore mFirestore;

    private StorageReference mStorageReference;

    private static final String TAG = "ConsultantRegistration";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultant_registration);

        mFirestore = FirebaseFirestore.getInstance();


        mEditTextName = findViewById(R.id.conReg_edit_name);
        mEditTextConNumber = findViewById(R.id.conReg_edit_contactNo);
        mEditTextLocation = findViewById(R.id.conReg_edit_location);
        mEditTextTime = findViewById(R.id.conReg_edit_avlTime);
        mEditTextDescription = findViewById(R.id.conReg_edit_description);
        mEditTextExperience = findViewById(R.id.conReg_edit_Xp_years);
        mEditTextHospitalName = findViewById(R.id.conReg_edit_Hospital_name);

        mEditTextEmail = findViewById(R.id.conReg_email);


        mButtonSave = findViewById(R.id.btn_conReg_edit_save);


        mConRegProBar = findViewById(R.id.conReg_edit_prBar);

        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registration();
            }
        });


    }


    private void registration() {

        final String Name = mEditTextName.getText().toString().trim();
        final String ConNumber = mEditTextConNumber.getText().toString().trim();
        final String Location = mEditTextLocation.getText().toString().trim();
        final String Time = mEditTextTime.getText().toString().trim();
        final String Description = mEditTextDescription.getText().toString().trim();
        final String Experience = mEditTextExperience.getText().toString().trim();
        final String HospitalName = mEditTextHospitalName.getText().toString().trim();
        final String Email = mEditTextEmail.getText().toString().trim();

        if (TextUtils.isEmpty(Name)) {
            mEditTextName.setError("Email is Required..");
            return;
        }
        if (TextUtils.isEmpty(ConNumber)) {
            mEditTextConNumber.setError("Email is Required..");
            return;
        }
        if (TextUtils.isEmpty(Location)) {
            mEditTextLocation.setError("Email is Required..");
            return;
        }
        if (TextUtils.isEmpty(Time)) {
            mEditTextTime.setError("Email is Required..");
            return;
        }
        if (TextUtils.isEmpty(Description)) {
            mEditTextDescription.setError("Email is Required..");
            return;
        }
        if (TextUtils.isEmpty(Experience)) {
            mEditTextExperience.setError("Email is Required..");
            return;
        }
        if (TextUtils.isEmpty(HospitalName)) {
            mEditTextHospitalName.setError("Email is Required..");
            return;
        }

        if (TextUtils.isEmpty(Email)) {
            mEditTextEmail.setError("Email is Required..");
            return;
        }

        mConRegProBar.setVisibility(View.VISIBLE);


        DocumentReference documentReference = mFirestore.collection("ConsultantRequest").document();

        Map<String, Object> updateConData = new HashMap<>();

        updateConData.put("conAvailableTime", Time);
        updateConData.put("conContactNumber", ConNumber);
        updateConData.put("conDescription", Description);
        updateConData.put("conLocation", Location);
        updateConData.put("conName", Name);
        updateConData.put("experienceYears", Experience);
        updateConData.put("type", "consulter");
        updateConData.put("hospitalName", HospitalName);
        updateConData.put("conEmail", Email);


        documentReference.set(updateConData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(ConsultantRegistration.this, "Your Request was send to the admin if your Hired you will receive a Email", Toast.LENGTH_LONG).show();

                mConRegProBar.setVisibility(View.INVISIBLE);

                Intent loginIntent = new Intent(ConsultantRegistration.this, UserLogin.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(loginIntent);
                finish();

//                startActivity(new Intent(ConsultantRegistration.this, UserLogin.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ConsultantRegistration.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}




