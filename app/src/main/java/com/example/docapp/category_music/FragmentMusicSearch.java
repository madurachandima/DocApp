package com.example.docapp.category_music;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.docapp.Category_consulter.SingleConsulter.SingleConsulter;
import com.example.docapp.R;
import com.example.docapp.category_music.playList.PlayListAdapter;
import com.example.docapp.category_music.playList.PlayListModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.CompositePermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.os.Environment.DIRECTORY_DOWNLOADS;


public class FragmentMusicSearch extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "FragmentMusicSearch";

    private String mParam1;
    private String mParam2;

    private RecyclerView mRecyclerView;
    private PlayListAdapter mPlayListAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<PlayListModel> mPlayListArrayList;
    private ArrayList<String> mSongId;

    private String mCount;
    private String category;
    private String songName;
    private String url;

    private int count;

    public FragmentMusicSearch() {
        // Required empty public constructor
    }

    public static FragmentMusicSearch newInstance(String param1, String param2) {
        FragmentMusicSearch fragment = new FragmentMusicSearch();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView;

        mView = inflater.inflate(R.layout.fragment_music_search, container, false);

        mRecyclerView = mView.findViewById(R.id.id_songListRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mPlayListArrayList = new ArrayList<>();
        mSongId = new ArrayList<>();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        CollectionReference collectionReference = firestore.collection("songs");
        collectionReference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                        for (DocumentSnapshot snapshot : queryDocumentSnapshots) {

                            mSongId.add(snapshot.getId());

                            PlayListModel listModel = snapshot.toObject(PlayListModel.class);
                            mPlayListArrayList.add(listModel);

                        }

                        mPlayListAdapter = new PlayListAdapter(mPlayListArrayList);
                        mRecyclerView.setAdapter(mPlayListAdapter);

                        mPlayListAdapter.setOnItemClickListener(new PlayListAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int pos) {
                                String url = mPlayListArrayList.get(pos).getSongURL();
                                String name = mPlayListArrayList.get(pos).getSongName();
                                getUserPermission(url, name, pos);
                            }

                            private void getUserPermission(final String URL, final String name, final int pos) {

                                Dexter.withContext(getContext())
                                        .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        .withListener(new PermissionListener() {
                                            @Override
                                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                                startDownload(name, ".mp3", DIRECTORY_DOWNLOADS, URL, pos);
                                            }

                                            @Override
                                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                                            }

                                            @Override
                                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                                permissionToken.cancelPermissionRequest();
                                            }
                                        }).check();
                            }
                        });
                    }
                });

        return mView;
    }

    private void startDownload(String name, String fileExtension, String destinationDirectory, String URL, int pos) {
        String url = URL.toString().trim();
        Context context = getContext();

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri.parse(url));

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, name + fileExtension);

        downloadManager.enqueue(request);
        Toast.makeText(context, "Downloading....", Toast.LENGTH_SHORT).show();

        likeCount(pos);

    }

    private void likeCount(int pos) {


        FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        final String songId = mSongId.get(pos);
        Log.d(TAG, "likeCount: " + songId);


        CollectionReference reference = firestore.collection("songs");
        reference.document(songId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        mCount = documentSnapshot.getString("mSongLikeCount").trim();
                        songName = documentSnapshot.getString("mSongName");
                        category = documentSnapshot.getString("mSongCategory");
                        url = documentSnapshot.getString("mSongURL");


                        Log.d(TAG, "onSuccess: " + mCount);

                        if (!TextUtils.isEmpty(mCount) && TextUtils.isDigitsOnly(mCount)) {
                            count = Integer.parseInt(mCount);
                            Log.d(TAG, "likeCount: " + count);

                            int newCount;
                            newCount = count + 1;
                            Log.d(TAG, "New likeCount: " + newCount);

                            DocumentReference documentReference = firestore.collection("songs")
                                    .document(songId);

                            Map<String, Object> newLikeCount = new HashMap<>();

                            newLikeCount.put("mSongCategory", category);
                            newLikeCount.put("mSongLikeCount", String.valueOf(newCount));
                            newLikeCount.put("mSongName", songName);
                            newLikeCount.put("mSongURL", url);


                            documentReference.set(newLikeCount)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "onSuccess: Like Count Update ");
                                        }
                                    });
                        }
                    }
                });


    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem searchItem = menu.findItem(R.id.id_actionSearch);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mPlayListAdapter.getFilter().filter(s);
                Log.d(TAG, "onQueryTextChange: " + s);
                return false;
            }
        });
    }
}