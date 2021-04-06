package com.example.docapp.spalsh_screen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.docapp.Category_consulter.consulter_dashboard.ConsulterDashbordMain;
import com.example.docapp.admin_handeling.AdminView;
import com.example.docapp.category_main.Catogary;
import com.example.docapp.user_handeling.UserLogin;
import com.example.docapp.user_handeling.user_profile_settings.UserProfileManage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashScreen extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;
    private String mUserType;

    private static final String TAG = "SplashScreen";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        Log.d(TAG, "onCreate: " + mAuth.getUid());

        try {
            if (!mAuth.getCurrentUser().getUid().equals(null)) {


                final String uID = mAuth.getCurrentUser().getUid();
                Log.d(TAG, "onComplete: User ID " + uID);


                CollectionReference reference = mFirestore.collection("userDetails");
                reference.document(uID).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Log.d(TAG, "onSuccess: " + documentSnapshot);

                                try {

                                    mUserType = documentSnapshot.get("type").toString();

                                    if (mUserType.equals("user")) {

                                        Log.d(TAG, "onComplete: User..");
                                        Intent splashIntent = new Intent(SplashScreen.this, Catogary.class);
                                        splashIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(splashIntent);
                                        finish();

                                        //    startActivity(new Intent(getApplicationContext(), Catogary.class));

                                    }

                                } catch (Exception e1) {

                                    CollectionReference reference = mFirestore.collection("consulterDetails");
                                    reference.document(uID).get()
                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    try {
                                                        Log.d(TAG, "onSuccess: " + documentSnapshot);
                                                        mUserType = documentSnapshot.get("type").toString();

                                                        if (mUserType.equals("consulter")) {
                                                            Log.d(TAG, "onComplete: Consultant..");

                                                            Intent splashIntent = new Intent(SplashScreen.this, ConsulterDashbordMain.class);
                                                            splashIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(splashIntent);
                                                            finish();

                                                            //       startActivity(new Intent(getApplicationContext(), ConsulterDashbordMain.class));//consulter
                                                            //     Toast.makeText(SplashScreen.this, "Suceccfully Login...", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } catch (Exception e2) {


                                                        CollectionReference reference = mFirestore.collection("adminDeails");
                                                        reference.document(uID).get()
                                                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

                                                                    @Override
                                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                        Log.d(TAG, "onSuccess: " + documentSnapshot);
                                                                        mUserType = documentSnapshot.get("type").toString();

                                                                        if (mUserType.equals("admin")) {

                                                                            Log.d(TAG, "onComplete: Admin..");

                                                                            Intent splashIntent = new Intent(SplashScreen.this, AdminView.class);
                                                                            splashIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                            startActivity(splashIntent);
                                                                            finish();

                                                                            //    startActivity(new Intent(getApplicationContext(), AdminView.class));
                                                                            //     Toast.makeText(SplashScreen.this, "Suceccfully Login...", Toast.LENGTH_SHORT).show();
                                                                        }

                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(SplashScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }


                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(SplashScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SplashScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        } catch (Exception e) {

            Intent splashIntent = new Intent(SplashScreen.this, UserLogin.class);
            splashIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(splashIntent);
            finish();

            //   startActivity(new Intent(getApplicationContext(), UserLogin.class));
        }

    }

}
