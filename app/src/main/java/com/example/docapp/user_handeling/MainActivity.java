package com.example.docapp.user_handeling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    EditText mUserFullName;
    EditText mUserPhoneNumber;
    EditText mUserPassword;
    EditText mUserPassword2;
    EditText mUserEmail;
    TextView mLoginBtn;
    Button mRegisterBtn;
    TextView mConsReg;
    ProgressBar mProgressBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    private static final String TAG = "MainActivity";

    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();


        mUserFullName = findViewById(R.id.id_fullName);
        mUserPhoneNumber = findViewById(R.id.id_phoneNumber);
        mUserPassword = findViewById(R.id.id_password);
        mUserPassword2 = findViewById(R.id.id_password_conform);
        mUserEmail = findViewById(R.id.id_emailAddress);
        mRegisterBtn = findViewById(R.id.id_btnRegister);
        mLoginBtn = findViewById(R.id.id_alreadyRegisted);
        mConsReg = findViewById(R.id.id_consultantRegistration);

        mProgressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        if (mAuth.getCurrentUser() != null) {
            //already login
        }

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = mUserEmail.getText().toString().trim();
                final String password = mUserPassword.getText().toString().trim();
                final String password2 = mUserPassword2.getText().toString().trim();

                final String fullName = mUserFullName.getText().toString();
                final String phone = mUserPhoneNumber.getText().toString();


                if (TextUtils.isEmpty(email)) {
                    mUserEmail.setError("Email is Required..");
                    return;
                }

                if (TextUtils.isEmpty(password)) {  //user Regex
                    mUserPassword.setError("Password is Required..");
                    return;
                }
                if (TextUtils.isEmpty(password2)) {
                    mUserPassword2.setError("Password is Required..");
                    return;
                }

                if (!password.equals(password2)) {
                    Toast.makeText(MainActivity.this, "Password doesn't matching", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    mUserPassword.setError("Password length must be more than six characters");
                    return;
                }

                if (TextUtils.isEmpty(fullName)) {
                    mUserFullName.setError("Full name is Required..");
                    return;
                }

                if (TextUtils.isEmpty(phone)) {
                    mUserPhoneNumber.setError("Phone Number is Required");
                }

                mProgressBar.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            userId = mAuth.getCurrentUser().getUid();

                            DocumentReference documentReference = mFirestore.collection("userDetails").document(userId);

                            Map<String, Object> user = new HashMap<>();
                            user.put("fullName", fullName);
                            user.put("email", email);
                            user.put("phoneNumber", phone);
                            user.put("type", "user");

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: " + userId);
                                }
                            });


                            Toast.makeText(MainActivity.this, "User Created", Toast.LENGTH_SHORT).show();

                            mProgressBar.setVisibility(View.GONE);

                            Intent loginIntent = new Intent(MainActivity.this, UserLogin.class);
                            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(loginIntent);
                            finish();
                            //go new activity
                        } else {
                            Toast.makeText(MainActivity.this, "Error.." + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(MainActivity.this, UserLogin.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(loginIntent);
                finish();

//                startActivity(new Intent(getApplicationContext(), UserLogin.class));
            }
        });

        mConsReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent consultantRegIntent = new Intent(MainActivity.this, ConsultantRegistration.class);
                consultantRegIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(consultantRegIntent);
                finish();
//                startActivity(new Intent(getApplicationContext(), ConsultantRegistration.class));
            }
        });
    }
}