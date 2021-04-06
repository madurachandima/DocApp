package com.example.docapp.Category_video.VideoPlayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.docapp.R;
import com.example.docapp.category_music.playList.PlayListAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ViewVideoFiles extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private VideoAdapter mVideoAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<VideoModel> modelArrayList = new ArrayList<>();
    private static final String TAG = "ViewVideoFiles";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_video_files);

        mRecyclerView = findViewById(R.id.video_recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        CollectionReference reference = firestore.collection("Videos");
        reference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                            Log.d(TAG, "onSuccess: " + snapshot);
                            VideoModel model = snapshot.toObject(VideoModel.class);
                            modelArrayList.add(model);
                        }

                        mVideoAdapter = new VideoAdapter(modelArrayList);
                        mRecyclerView.setAdapter(mVideoAdapter);
                        mVideoAdapter.setOnItemClickListener(new VideoAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int pos) {

                                Intent intent = new Intent(ViewVideoFiles.this,VideoPlayer.class);
                                intent.putExtra("URL",modelArrayList.get(pos).getmVideoURL().toString());
                                startActivity(intent);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e);
            }
        });

    }
}