package com.example.docapp.user_handeling.user_profile_settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.docapp.R;
import com.example.docapp.user_handeling.ConsultantRegistration;
import com.example.docapp.user_handeling.UserLogin;
import com.google.firebase.auth.FirebaseAuth;

public class UserProfileManage extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_manage);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signOut();

        Intent loginIntent = new Intent(UserProfileManage.this, UserLogin.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(loginIntent);
        finish();

        // startActivity(new Intent(UserProfileManage.this, UserLogin.class));
    }
}