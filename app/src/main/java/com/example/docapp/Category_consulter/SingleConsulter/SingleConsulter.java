package com.example.docapp.Category_consulter.SingleConsulter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.docapp.Category_consulter.SingleConsulter.FindLocation.ConsulterLocationMap;
import com.example.docapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SingleConsulter extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private SingleConsulterAdapter mConsulterAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<SingleConsulterModel> mConsulterFeedbackArrayList;
    private ArrayList<String> mFeedbackIdArrayList;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private DocumentReference mDocumentReference;

    private String mConName;
    private String mImageURL;
    private String mConAvailableTime;
    private String mConContactNumber;
    private String mConDescription;
    private String mConLocation;
    private String mConID;
    private String mConLat;
    private String mConLng;

    private String mHName;
    private String mXp;

    private String mUid;
    private String mDate;
    private String mTimeStamp;
    private String mCurrentUserName;

    private TextView mButtonFeedBack;
    private MaterialEditText mFeedback;

    private static final String TAG = "SingleConsulter";

    private static final String PATH_1 = "consulterFeedback";
    private static final String PATH_2 = "feedback";
    private static final int REQUEST_PHONE_CALL = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_consulter);

        mRecyclerView = findViewById(R.id.id_singleConsulter_RView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mConsulterFeedbackArrayList = new ArrayList<>();
        mFeedbackIdArrayList = new ArrayList<>();


        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        mUid = mAuth.getCurrentUser().getUid();
        Log.d(TAG, "onCreate Current user ID..... : " + mUid);


        getData();
        getDataFromFirestore();
        getCurrentUserName();

        mButtonFeedBack = findViewById(R.id.id_feedBack_btn);
        mButtonFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog();
            }
        });

        ImageButton callButton = findViewById(R.id.id_conCallIcon);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Call Work....");
                actionCall();
            }
        });


        ImageButton whatsAppButton = findViewById(R.id.id_conWhatsapp);
        whatsAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionWhatsApp();
            }
        });

        ImageView conLocatioView = findViewById(R.id.id_ConsulterLocation);
        conLocatioView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mConLat.isEmpty() && !mConLng.isEmpty()
                        || !mConLng.equals("") && !mConLat.equals("")) {

                    Intent intent = new Intent(SingleConsulter.this, ConsulterLocationMap.class);
                    intent.putExtra("lat", mConLat);
                    intent.putExtra("lng", mConLng);
                    startActivity(intent);

                } else {
                    Toast.makeText(SingleConsulter.this, "Can not Show location.. Your Consultant did not set there location", Toast.LENGTH_LONG).show();
                }
                Log.d(TAG, "onClick:  Location Icon Work...");

            }
        });

    }

    private void actionWhatsApp() {

        String formattedNumber = mConContactNumber.trim();
        boolean installed = appInstalledOrNot("com.whatsapp");
        if (installed) {

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                    String.format("https://api.whatsapp.com/send?phone=%s&text=%s", "+94" + formattedNumber, "Sorry this is test message")));
            startActivity(intent);

        } else {
            Toast.makeText(this, "WhatsApp not installed on your Device..", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager packageManager = getPackageManager();
        boolean appInstalled;

        try {
            packageManager.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            appInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            appInstalled = false;
        }
        return appInstalled;
    }


    private void actionCall() {

        String pNumber = mConContactNumber.toString().trim();

        Log.d(TAG, "actionCall: " + pNumber);

        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + pNumber));


        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(SingleConsulter.
                    this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);

            return;

        } else {
            startActivity(intent);
        }
    }


    private void getDataFromFirestore() {

        CollectionReference collectionReference = mFirestore.collection(PATH_1)
                .document(mConID).collection(PATH_2);
        collectionReference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (DocumentSnapshot snapshot : queryDocumentSnapshots) {


                            mFeedbackIdArrayList.add(snapshot.getId());
                            SingleConsulterModel consulterModel = snapshot.toObject(SingleConsulterModel.class);
                            mConsulterFeedbackArrayList.add(consulterModel);
                        }

                        mConsulterAdapter = new SingleConsulterAdapter(mConsulterFeedbackArrayList);
                        mRecyclerView.setAdapter(mConsulterAdapter);
                        mConsulterAdapter.notifyDataSetChanged();
                        mConsulterAdapter.setonItemLongClickListener(new SingleConsulterAdapter.onItemLongClickLitner() {
                            @Override
                            public void onLongClick(int position) {

                                String userID = mConsulterFeedbackArrayList.get(position).getUserId();

                                String fBId = mFeedbackIdArrayList.get(position);

                                deleteFeedback(userID, fBId, position);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }
        });

    }


    private void deleteFeedback(String userID, final String fbId, final int position) {
        // delete feedback.....................................


        if (mUid.equals(userID)) {


            AlertDialog.Builder dialog = new AlertDialog.Builder(this);

            dialog.setTitle("Confirm to delete your Feedback");


            dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    mFirestore.collection(PATH_1).document(mConID).collection(PATH_2).document(fbId)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(SingleConsulter.this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                                }
                            });

                    ////////////////////////////////////////////////////////////////
                    //mConsulterFeedbackArrayList.remove(position);
                    mConsulterAdapter = new SingleConsulterAdapter(mConsulterFeedbackArrayList);
                    mConsulterAdapter.notifyDataSetChanged();
                    mRecyclerView.setAdapter(mConsulterAdapter);

                }
            });

            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            dialog.show();

        } else {

            Log.d(TAG, "deleteFeedback: Wrong Feedback..");
        }

    }


    private void showDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Chat..");
        dialog.setMessage("Provide your Message..");


        LayoutInflater inflater = LayoutInflater.from(this);
        View reglayout = inflater.inflate(R.layout.send_feedback, null, false);

        mFeedback = reglayout.findViewById(R.id.id_feedback_text);

        dialog.setView(reglayout);
        dialog.setPositiveButton("SEND", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(mFeedback.getText().toString())) {
                    Toast.makeText(SingleConsulter.this, "Please type your Message...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!TextUtils.isEmpty(mFeedback.getText().toString())) {
                    String feedback = mFeedback.getText().toString();
                    getCurrentDateTime();
                    sendFeedback(feedback);
                }


            }
        });
        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getCurrentDateTime() {
        //get current date time............

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mDate = dateFormat.format(calendar.getTime());

        Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());
        Instant instant = timestamp.toInstant();

        mTimeStamp = instant.toString();

        Log.d(TAG, "getCurrentDateTime: " + mDate);
        Log.d(TAG, "getCurrentDateTime TimeStamp : " + mTimeStamp);


    }


    private void sendFeedback(final String feedback) {
        //// send user feedback............
        Log.d(TAG, "sendFeedback: userID : " + mUid + "Con ID : " + mConID);


        mDocumentReference = mFirestore.collection("consulterFeedback").document(mConID)
                .collection("feedback").document();
//                .collection(mUid).document();

        Map<String, Object> userFeedback = new HashMap<>();

        userFeedback.put("userName", mCurrentUserName);
        userFeedback.put("userId", mUid);
        userFeedback.put("date", mDate);
        userFeedback.put("feedback", feedback);

        Log.d(TAG, "sendFeedback: " + userFeedback);

        mDocumentReference.set(userFeedback)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SingleConsulter.this, "Sent your Message..  ", Toast.LENGTH_SHORT).show();

                        //  mConsulterFeedbackArrayList.add(new SingleConsulterModel("you", mDate, feedback, mUid));

                        mConsulterAdapter = new SingleConsulterAdapter(mConsulterFeedbackArrayList);
                        mConsulterAdapter.notifyDataSetChanged();
                        mRecyclerView.setAdapter(mConsulterAdapter);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SingleConsulter.this, "Fail to submit " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getCurrentUserName() {
        Log.d(TAG, "getCurrentUserName: called...." + mUid);

        DocumentReference reference = mFirestore.collection("userDetails").document(mUid);
        reference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        DocumentSnapshot snapshot = task.getResult();

                        mCurrentUserName = snapshot.getString("fullName");

                        Log.d(TAG, "onComplete: " + snapshot.get("fullName"));
                        Log.d(TAG, "onComplete: " + mCurrentUserName);

                    }
                });
    }

    private void getData() {

        TextView name = findViewById(R.id.id_conName);
        TextView time = findViewById(R.id.id_conAVlTime);
        TextView location = findViewById(R.id.id_conLocation);
        TextView contactNo = findViewById(R.id.id_conContactNo);
        TextView desc = findViewById(R.id.id_conDesc);
        ImageView conProfile = findViewById(R.id.conImage);
        TextView xp = findViewById(R.id.id_conXP);
        TextView hName = findViewById(R.id.id_conHosName);


        Intent intent = getIntent();
        mConName = intent.getStringExtra("NAME");
        mConLocation = intent.getStringExtra("LOCATION");
        mConAvailableTime = intent.getStringExtra("TIME");
        mConContactNumber = intent.getStringExtra("NUMBER");
        mImageURL = intent.getStringExtra("URL");
        mConDescription = intent.getStringExtra("DESC");
        mConID = intent.getStringExtra("CONID");

        mConLat = intent.getStringExtra("LAT");
        mConLng = intent.getStringExtra("LNG");
        mXp = intent.getStringExtra("XP");
        mHName = intent.getStringExtra("Hname");

        Log.d(TAG, "getData: Lat Lng " + mConLat + " " + mConLng);

        name.setText("Name : " + mConName);
        time.setText("Time : " + mConAvailableTime);
        location.setText("Place : " + mConLocation);
        contactNo.setText("Contact No : " + mConContactNumber);
        desc.setText("About Consultant: " + mConDescription);
        xp.setText("Experience : " + mXp);
        hName.setText("Hospital Name : " + mHName);


        Picasso
                .get()
                .load(mImageURL)
                .into(conProfile);

        Log.d(TAG, "onCreate: " + mConName);
    }
}