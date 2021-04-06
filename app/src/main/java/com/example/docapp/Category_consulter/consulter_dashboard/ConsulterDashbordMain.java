package com.example.docapp.Category_consulter.consulter_dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.docapp.R;
import com.example.docapp.admin_handeling.AdminView;
import com.example.docapp.spalsh_screen.SplashScreen;
import com.example.docapp.user_handeling.UserLogin;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ConsulterDashbordMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;


    private ImageView imageView;
    private TextView textViewLocation;
    private TextView textViewName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulter_dashbord_main);


        FirebaseAuth auth;
        FirebaseFirestore firestore;
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();


        toolbar = findViewById(R.id.toolBar);
        navigationView = findViewById(R.id.navigation_view);
        mDrawerLayout = findViewById(R.id.drawer);

        actionBarDrawerToggle = new ActionBarDrawerToggle
                (this, mDrawerLayout, toolbar, R.string.open, R.string.Close);

        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
//        setSupportActionBar(toolbar);


        navigationView.setNavigationItemSelectedListener(this);


        View hView = navigationView.getHeaderView(0);
        imageView = hView.findViewById(R.id.id_header_conImage);
        textViewLocation = hView.findViewById(R.id.id_header_conLocation);
        textViewName = hView.findViewById(R.id.id_header_conName);


        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new FragmentHome()).addToBackStack(null);
        fragmentTransaction.commit();


        DocumentReference reference = firestore.collection("consulterDetails").document(auth.getCurrentUser().getUid());


        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {


                String conLocation = snapshot.getString("conLocation");
                String conName = snapshot.getString("conName");
                String imageURL = snapshot.getString("imageURL");


                Picasso.get()
                        .load(imageURL)
                        .into(imageView);

                textViewLocation.setText("Location :" + conLocation);
                textViewName.setText("Name :" + conName);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error..", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        mDrawerLayout.closeDrawer(GravityCompat.START);

        if (menuItem.getItemId() == R.id.id_home) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new FragmentHome()).addToBackStack(null);
            fragmentTransaction.commit();
        }
        if (menuItem.getItemId() == R.id.id_songUpload) {

            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new FragmentSongUpload()).addToBackStack(null);
            fragmentTransaction.commit();
        }

        if (menuItem.getItemId() == R.id.id_uploadVideo) {

            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new FragmentVideoUpload()).addToBackStack(null);
            fragmentTransaction.commit();
        }

        if (menuItem.getItemId() == R.id.id_ReplyFeedback) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new FragmentReplyFeedback()).addToBackStack(null);
            fragmentTransaction.commit();
        }

        if (menuItem.getItemId() == R.id.id_ProfileSetting) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new FragmentProfileSetting()).addToBackStack(null);
            fragmentTransaction.commit();
        }

        if (menuItem.getItemId() == R.id.id_signOut) {

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();

            Intent splashIntent = new Intent(ConsulterDashbordMain.this, UserLogin.class);
            splashIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(splashIntent);
            finish();

//            startActivity(new Intent(getApplicationContext(), UserLogin.class));
        }
        return true;
    }
}