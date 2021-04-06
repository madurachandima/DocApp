package com.example.docapp.admin_handeling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.docapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ApproveCon extends AppCompatActivity {

    private TextView mEditTextName;
    private TextView mEditTextConNumber;
    private TextView mEditTextLocation;
    private TextView mEditTextTime;
    private TextView mEditTextDescription;
    private TextView mEditTextExperience;
    private TextView mEditTextHospitalName;
    private TextView mEditTextEmail;

    private Button mButtonApprove;
    private Button mButtonReject;

    private String mName;
    private String mTime;
    private String mNumber;
    private String mHname;
    private String mDesc;
    private String mEmail;
    private String xP;
    private String mLocation;
    private String mType;
    private String requestUserId;

    private FirebaseAuth mAuthNew;

    private FirebaseFirestore mFirestore;

    private static final String TAG = "ApproveCon";

    public static final String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz|!£$%&/=@#";
    public static Random RANDOM = new Random();
    private static int REQUEST_TYPE_REJECT = 0;
    private static int REQUEST_TYPE_SELECT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_con);

        mAuthNew = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        mEditTextName = findViewById(R.id.Req_edit_name);
        mEditTextConNumber = findViewById(R.id.Req_edit_contactNo);
        mEditTextLocation = findViewById(R.id.Req_edit_location);
        mEditTextTime = findViewById(R.id.Req_edit_avlTime);
        mEditTextDescription = findViewById(R.id.Req_edit_description);
        mEditTextExperience = findViewById(R.id.Req_edit_Xp_years);
        mEditTextHospitalName = findViewById(R.id.Req_edit_Hospital_name);

        mButtonApprove = findViewById(R.id.btn_Req_edit_approve);
        mButtonReject = findViewById(R.id.btn_Req_edit_reject);

        Intent intent = getIntent();
        requestUserId = intent.getStringExtra("ID");


        getData();
        mButtonApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                randomString(8);
                String rand = randomString(8);
                Log.d(TAG, "onClick: " + rand);
                singUp(rand);
            }
        });

        mButtonReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ID
                deleteUser(requestUserId, "Request Removed", REQUEST_TYPE_REJECT);

            }
        });
    }


    private void getData() {

        Intent intent = getIntent();

        mName = intent.getStringExtra("NAME");
        mTime = intent.getStringExtra("TIME");
        mNumber = intent.getStringExtra("NUMBER");
        mDesc = intent.getStringExtra("DESC");
        mEmail = intent.getStringExtra("EMAIL");
        mLocation = intent.getStringExtra("LOCATION");
        xP = intent.getStringExtra("XP");
        mHname = intent.getStringExtra("HNAME");
        mType = intent.getStringExtra("TYPE");

        Log.d(TAG, "getData: " + mType);

        mEditTextName.setText("Name : " + mName);
        mEditTextConNumber.setText("Contact Number : " + mNumber);
        mEditTextLocation.setText("Location : " + mLocation);
        mEditTextTime.setText("Avl Time : " + mTime);
        mEditTextDescription.setText("Desc : " + mDesc);
        mEditTextExperience.setText("Experience : " + xP);
        mEditTextHospitalName.setText("Hospital Name : " + mHname);

    }

    public static String randomString(int len) {

        StringBuilder sb = new StringBuilder(len);

        for (int i = 0; i < len; i++) {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        }

        return sb.toString();
    }

    private void singUp(final String rand) {

        mAuthNew.createUserWithEmailAndPassword(mEmail, rand)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        final String id = mAuthNew.getCurrentUser().getUid();

                        Log.d(TAG, "onSuccess: " + id);
                        DocumentReference documentReference = mFirestore.collection("consulterDetails").document(id);

                        Map<String, Object> con = new HashMap<>();

                        con.put("conAvailableTime", mTime);
                        con.put("conContactNumber", mNumber);
                        con.put("conDescription", mDesc);
                        con.put("conLocation", mLocation);
                        con.put("conName", mName);
                        con.put("type", mType);
                        con.put("experienceYears", xP);
                        con.put("hospitalName", mHname);
                        con.put("conEmail", mEmail);

                        documentReference.set(con).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {


                                Log.d(TAG, "onSuccess: " + mEmail);
                                String subject = "Close To Heart";
                                String body = "Welcome to our Online Mental Health Hub..\n" +
                                        "Your Password is : " + rand + "\n" +
                                        " Your User Name is Your Entered Email Address.\n" +
                                        "\n ThankYou..";


                                Toast.makeText(ApproveCon.this, "Registration Success..", Toast.LENGTH_SHORT).show();

                                String[] to = {mEmail};
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setData(Uri.parse("mailto:"));
                                intent.setType("text/plain");
                                intent.putExtra(Intent.EXTRA_EMAIL, to);
                                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                                intent.putExtra(Intent.EXTRA_TEXT, body);
                                intent.setType("message/rfc822");
                                Log.d(TAG, "onSuccess: " + to);
                                startActivity(intent);

                                deleteUser(requestUserId, "Request Approved", REQUEST_TYPE_SELECT);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: " + e.getMessage());
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ApproveCon.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void deleteUser(String deleteID, final String message, final int requestType) {

        mFirestore.collection("ConsultantRequest").document(deleteID)

                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ApproveCon.this, message, Toast.LENGTH_SHORT).show();

                if (requestType == REQUEST_TYPE_SELECT) {
                    mAuthNew.signOut();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }
        });
    }
}