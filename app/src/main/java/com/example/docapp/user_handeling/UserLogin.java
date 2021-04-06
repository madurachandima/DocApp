package com.example.docapp.user_handeling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.docapp.Category_consulter.consulter_dashboard.ConsulterDashbordMain;
import com.example.docapp.R;
import com.example.docapp.admin_handeling.AdminView;
import com.example.docapp.category_main.Catogary;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserLogin extends AppCompatActivity {

    private EditText mUserPassword;
    private EditText mUserEmail;
    private Button mLogin;
    private TextView mRegisterBtn;
    private TextView mForgetPasswordBtn;
    private ProgressBar mProgressBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    private String mUSerID;
    private String mUserType;

    private static final String TAG = "UserLogin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);


        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();


//            Log.d(TAG, "onCreate: Exception " + e);
        mUserPassword = findViewById(R.id.id_loginPawwsord);
        mUserEmail = findViewById(R.id.id_loginEmail);
        mLogin = findViewById(R.id.id_btnLogin);
        mRegisterBtn = findViewById(R.id.id_loginCreateAccount);
        mForgetPasswordBtn = findViewById(R.id.id_loginForgetPW);
        mProgressBar = findViewById(R.id.id_loginProgressBar);


        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String email = mUserEmail.getText().toString().trim();
                String password = mUserPassword.getText().toString().trim();


                if (TextUtils.isEmpty(email)) {
                    mUserEmail.setError("Email is Required..");
                    return;
                }

                if (TextUtils.isEmpty(password)) {  //user Regex
                    mUserPassword.setError("Password is Required..");
                    return;
                }


                mProgressBar.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

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

                                                    Intent userLoginIntent = new Intent(UserLogin.this, Catogary.class);
                                                    userLoginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(userLoginIntent);
                                                    finish();


//                                                    startActivity(new Intent(getApplicationContext(), Catogary.class));

                                                    Toast.makeText(UserLogin.this, "Suceccfully Login...", Toast.LENGTH_SHORT).show();
//                                                    if (!user.isEmailVerified()) {
//
//                                                        //TODO Must change the verification ..... use click event for that.......
//
//                                                        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                            @Override
//                                                            public void onSuccess(Void aVoid) {
//
//                                                                Toast.makeText(UserLogin.this, "Verification Link Has been sent..", Toast.LENGTH_SHORT).show();
//                                                            }
//                                                        }).addOnFailureListener(new OnFailureListener() {
//                                                            @Override
//                                                            public void onFailure(@NonNull Exception e) {
//                                                                Log.d(TAG, "onFailure: " + e.getMessage());
//                                                            }
//                                                        });
//                                                    }
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

                                                                        Intent consultantLoginIntent = new Intent(UserLogin.this, ConsulterDashbordMain.class);
                                                                        consultantLoginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                        startActivity(consultantLoginIntent);
                                                                        finish();

//                                                                        startActivity(new Intent(getApplicationContext(), ConsulterDashbordMain.class));//consulter

                                                                        Toast.makeText(UserLogin.this, "Suceccfully Login...", Toast.LENGTH_SHORT).show();
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


                                                                                        Intent adminLoginIntent = new Intent(UserLogin.this, AdminView.class);
                                                                                        adminLoginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                                        startActivity(adminLoginIntent);
                                                                                        finish();
//
//                                                                                        startActivity(new Intent(getApplicationContext(), AdminView.class));
                                                                                        Toast.makeText(UserLogin.this, "Suceccfully Login...", Toast.LENGTH_SHORT).show();
                                                                                    }

                                                                                }
                                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Toast.makeText(UserLogin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
                                                                }

                                                                //                                                                if (mUserType.equals("user")) {
//
//                                                                    Log.d(TAG, "onComplete: User..");
//                                                                    startActivity(new Intent(getApplicationContext(), Catogary.class));
//
//                                                                }

                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(UserLogin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }


                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UserLogin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                            //Send Verify Link
//                            if (!user.isEmailVerified()) {
//
//                                //TODO Must change the verification ..... use click event for that.......
//
//                                user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//
//                                        Toast.makeText(UserLogin.this, "Verification Link Has been sent..", Toast.LENGTH_SHORT).show();
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Log.d(TAG, "onFailure: " + e.getMessage());
//                                    }
//                                });
//                            }

//                            Toast.makeText(UserLogin.this, "Suceccfully Login...", Toast.LENGTH_SHORT).show();
                            mUSerID = mAuth.getCurrentUser().getUid();


                            Log.d(TAG, "User ID:  " + mUSerID);
                            //TODO... get USer Id
                            // isConsulter can replay the user feedback..


                        } else {
                            Toast.makeText(UserLogin.this, "Error." + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        mProgressBar.setVisibility(View.INVISIBLE);
                    }
                });

            }
        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    Log.d(TAG, "onClick: Register...");

                Intent registerLoginIntent = new Intent(UserLogin.this, MainActivity.class);
                registerLoginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(registerLoginIntent);
                finish();
//
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));

            }
        });

        mForgetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO go forget PW activity....
//                    Log.d(TAG, "onClick: ForgetPW worked..");

                final EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password..");
                passwordResetDialog.setMessage("Enter Your Email to Received Reset Link..");
                passwordResetDialog.setView(resetMail);


                passwordResetDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO get Mail and set ResetLink


                        String mail = resetMail.getText().toString();
                        mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(UserLogin.this, "Reset Link sent your Email Address..", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UserLogin.this, "Error.. Can not sent Reset Link.. " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                passwordResetDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Close Dialog Box
                    }
                });
                passwordResetDialog.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }
}


