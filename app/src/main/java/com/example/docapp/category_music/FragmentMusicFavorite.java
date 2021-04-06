package com.example.docapp.category_music;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.docapp.R;
import com.example.docapp.category_music.player.MusicPlayer;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;


public class FragmentMusicFavorite extends Fragment {

    private ListView mAllMusicList;
    private ArrayAdapter<String> mMusicArrayAdapter;
    private ArrayList<String> songs;
    private ArrayList<File> musics;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static boolean IS_LONG_CLICKED = false;
    private static boolean IS_SONG_DELETED = false;


    private String mParam1;
    private String mParam2;
    private static final String TAG = "FragmentMusicFavorite";

    public FragmentMusicFavorite() {
        // Required empty public constructor
    }


    public static FragmentMusicFavorite newInstance(String param1, String param2) {
        FragmentMusicFavorite fragment = new FragmentMusicFavorite();
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
    }

    @Override
    public void onResume() {
        super.onResume();
//        musics = findMusicFiles(Environment.getExternalStorageDirectory());
//        songs = new ArrayList<>();
//
//        for (int i = 0; i < musics.size(); i++) {
//            songs.add(musics.get(i).getName());
//
//        }

        String path = Environment.getExternalStorageDirectory().toString() + "/Android/data/com.example.docapp/files/Download";
        File directory = new File(path);

        musics = findMusicFiles(directory);
        songs = new ArrayList<>();

        for (int i = 0; i < musics.size(); i++) {
            songs.add(musics.get(i).getName());
        }

        mMusicArrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, songs);
        mAllMusicList.setAdapter(mMusicArrayAdapter);
    }

//    private class MusicThread implements Runnable {
//        @Override
//        public void run() {
//
//
//            String path = Environment.getExternalStorageDirectory().toString() + "/Android/data/com.example.docapp/files/Download";
//            File directory = new File(path);
//
//            musics = findMusicFiles(directory);
//            songs = new ArrayList<>();
//
//            for (int i = 0; i < musics.size(); i++) {
//                songs.add(musics.get(i).getName());
//            }
//
//            mMusicArrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, songs);
//            mAllMusicList.setAdapter(mMusicArrayAdapter);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view;


        view = inflater.inflate(R.layout.fragment_music_favorite, container, false);

        mAllMusicList = view.findViewById(R.id.mySongList);


        Dexter.withContext(getActivity()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

//                MusicThread musicThread = new MusicThread();
//                musicThread.run();

                String path = Environment.getExternalStorageDirectory().toString() + "/Android/data/com.example.docapp/files/Download";
                File directory = new File(path);

                musics = findMusicFiles(directory);
                songs = new ArrayList<>();

                for (int i = 0; i < musics.size(); i++) {
                    songs.add(musics.get(i).getName());
                }

                mMusicArrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, songs);
                mAllMusicList.setAdapter(mMusicArrayAdapter);
//                mMusicArrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, songs);
                mMusicArrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, songs);
                mAllMusicList.setAdapter(mMusicArrayAdapter);

//                mAllMusicList.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//             //TODO.............................................
//                    }
//                });
//                mAllMusicList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//                    @Override
//                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                        IS_LONG_CLICKED = true;//check OnItemClick and On LOngClick .....
//
//                        final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
//                        dialog.setTitle("Delete Song");
//                        dialog.setMessage("Conform to Delete the Song");
//
//                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                                Log.d(TAG, "onItemLongClick: position " + musics.get(i));
//
//                                File songFile = musics.get(i);
//                                File file = new File(String.valueOf(songFile));
//                                file.delete();
//
//                                songs.remove(i);
//                                mMusicArrayAdapter.notifyDataSetChanged();
//                                mMusicArrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, songs);
//                                mAllMusicList.setAdapter(mMusicArrayAdapter);
//
//                            }
//                        });
//
//                        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int i) {
//                                dialog.dismiss();
//
//                            }
//                        });
//
//                        dialog.show();
//
//                        return IS_LONG_CLICKED = true;
//
//                    }
//
//                });


                //TODO..ListView CLick listener

                mAllMusicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent player = new Intent(getActivity(), MusicPlayer.class);

                        player.putExtra("songFileList", musics);
                        player.putExtra("position", i);
                        Log.d(TAG, "onItemClick: " + i + " " + musics);
                        startActivity(player);

                    }
                });


            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();

        return view;


    }


    private ArrayList<File> findMusicFiles(File file) {

        ArrayList<File> allMusicFilesObject = new ArrayList<>();
        File[] files = file.listFiles();

        try {
            for (File currentFile : files) {
                // Log.d(TAG, "findMusicFiles: "+currentFile);
                if (currentFile.isDirectory() && !currentFile.isHidden()) {

                    allMusicFilesObject.addAll(findMusicFiles(currentFile));
                    //   Log.d(TAG, "findMusicFiles current file : " + currentFile);

                } else {

                    if (currentFile.getName().endsWith(".mp3") || currentFile.getName().endsWith(".mp4a") || currentFile.getName().endsWith(".wav")) {
                        allMusicFilesObject.add(currentFile);
                    }

                }
            }
        } catch (Exception e) {
            e.getMessage();
        }

        return allMusicFilesObject;
    }


}