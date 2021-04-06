package com.example.docapp.Category_consulter.consulter_dashboard;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.docapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class FragmentSongUpload extends Fragment {

    private static final String TAG = "FragmentSongUpload";
    private static final int ImageBack = 1;

    private StorageReference reference;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;

    private String songCat;
    private String name;
    private String mConName;
    private TextView musicNameTextView;
    private ProgressBar progressBar;
    private Button buttonUpload;
    private TextView mTextViewUploadBy;
    private TextView mTextViewUploadTime;


    private String mDate;

    public FragmentSongUpload() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_song_upload, container, false);

        Spinner spinner = view.findViewById(R.id.appCompatSpinner2);
        musicNameTextView = view.findViewById(R.id.id_uploadSongName);
        progressBar = view.findViewById(R.id.progressUpload);
//        mTextViewUploadBy = view.findViewById(R.id.id_uploadVideoBy);
        mTextViewUploadTime = view.findViewById(R.id.id_uploadVideoDate);

        buttonUpload = view.findViewById(R.id.btn_id_upload_song);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        //String ConId = mAuth.getCurrentUser().getUid();

        reference = FirebaseStorage.getInstance().getReference().child("Music");

//
//        DocumentReference documentReference = mFirestore.collection("consulterDetails").document( mAuth.getCurrentUser().getUid());
//
//        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//
//                mConName = documentSnapshot.get("conName").toString();
//
//            }
//
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d(TAG, "onFailure: "+e.getMessage());
//            }
//        });
//
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.SongCat, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                songCat = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        Log.d(TAG, "onSuccess: "+mConName);



        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = musicNameTextView.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    musicNameTextView.setError("Song Name is Required..");
                    return;
                }

                uploadMusic();

            }
        });

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mDate = dateFormat.format(calendar.getTime());

        Log.d(TAG, "onCreateView: " + mDate);

       // mTextViewUploadBy.setText("Music Upload By :"+mConName);
        return view;

    }


    private void uploadMusic() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/mpeg");
        startActivityForResult(intent, ImageBack);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImageBack) {
            if (resultCode == RESULT_OK) {
                Uri ImageDta = data.getData();
                Toast.makeText(getContext(), "Uploading...", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.VISIBLE);
                buttonUpload.setEnabled(false);
                final StorageReference storageReference = reference.child(songCat)
                        .child(name + " " + ImageDta.getLastPathSegment());


                storageReference.putFile(ImageDta).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d(TAG, "onSuccess: " + uri.toString());
                                FirebaseFirestore firestore = FirebaseFirestore.getInstance();


                                DocumentReference reference = firestore.collection("songs")
                                        .document();
                                Map<String, Object> data = new HashMap<>();
                                data.put("mSongCategory", songCat);
                                data.put("mSongLikeCount", "0");
                                data.put("mSongName", name);
                                data.put("mSongURL", uri.toString());
                                data.put("mMusciUploadBy", mConName);
                                data.put("mUploadDate", mDate);

//TODO.........................................................................
                                reference.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        musicNameTextView.setText("");
                                        Toast.makeText(getContext(), "Upload Complete", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.INVISIBLE);
                                        buttonUpload.setEnabled(true);
                                    }
                                });
                            }
                        });

                    }
                });
            }
        }
    }
}