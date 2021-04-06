package com.example.docapp.category_music.playList;

import com.google.firebase.firestore.PropertyName;

public class PlayListModel {


    private String mSongName;
    //  private String mSongID;
    // private String mSongCategory;
    private String mSongLikeCount;
    private String mSongURL;

    // private static final String TAG = "PlayListModel";


    public PlayListModel() {
    }

    @PropertyName("mSongName")
    public String getSongName() {
        return mSongName;
    }

    public void setSongName(String songName) {
        mSongName = songName;
    }

    @PropertyName("mSongLikeCount")
    public String getSongLikeCount() {
        return mSongLikeCount;
    }

    public void setSongLikeCount(String songLikeCount) {
        mSongLikeCount = songLikeCount;
    }

    @PropertyName("mSongURL")
    public String getSongURL() {
        return mSongURL;
    }

    public void setSongURL(String songURL) {
        mSongURL = songURL;
    }
}
