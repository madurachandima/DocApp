package com.example.docapp.category_main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.docapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class Catogary extends AppCompatActivity {

    private List<CategoryModel> itemCategory;
    private TextView verifyUser;
    private FirebaseAuth mAuth;
    private String uId;

    private static final String TAG = "Catogary";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catogary);
        verifyUser = findViewById(R.id.id_verifyUser);

        itemCategory = new ArrayList<>();


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        uId = mAuth.getCurrentUser().getUid();

        itemCategory.add(new CategoryModel
                ("Music", R.drawable.music, "com.example.docapp.category_music.CategoryMusicMain"));
        itemCategory.add(new CategoryModel

        ("Video", R.drawable.play, "com.example.docapp.Category_video.VideoPlayer.ViewVideoFiles"));
        itemCategory.add(new CategoryModel
                ("Consultant", R.drawable.consultant, "com.example.docapp.Category_consulter.Consulter"));
        itemCategory.add(new CategoryModel
                ("Sign out", R.drawable.sendfile, "com.example.docapp.user_handeling.user_profile_settings.UserProfileManage"));

        RecyclerView recyclerView = findViewById(R.id.categoryRecyclerView);
        ViewHolderAdapter holderAdapter = new ViewHolderAdapter(this, itemCategory);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setAdapter(holderAdapter);

        if (!user.isEmailVerified()) {

            verifyUser.setVisibility(View.VISIBLE);

            verifyUser.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //Send Verify Link
                    FirebaseUser user = mAuth.getCurrentUser();

                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(Catogary.this, "Verification Link Has been sent..", Toast.LENGTH_SHORT).show();
                            verifyUser.setVisibility(View.INVISIBLE);
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Log.d(TAG, "onFailure: " + e.getMessage());
                        }
                    });
                }
            });
        }
    }
}