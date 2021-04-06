package com.example.docapp.Category_video.VideoPlayer;

import com.google.firebase.firestore.PropertyName;

public class VideoModel {
    private String mVideoCategory;
    private String mVideoLikeCount;
    private String mVideoName;
    private String mVideoURL;

    public VideoModel() {
    }

    public VideoModel(String mVideoCategory, String mVideoLikeCount, String mVideoName, String mVideoURL) {
        this.mVideoCategory = mVideoCategory;
        this.mVideoLikeCount = mVideoLikeCount;
        this.mVideoName = mVideoName;
        this.mVideoURL = mVideoURL;
    }

    @PropertyName("mVideoCategory")
    public String getmVideoCategory() {
        return mVideoCategory;
    }

    public void setmVideoCategory(String mVideoCategory) {
        this.mVideoCategory = mVideoCategory;
    }

    @PropertyName("mVideoLikeCount")
    public String getmVideoLikeCount() {
        return mVideoLikeCount;
    }

    public void setmVideoLikeCount(String mVideoLikeCount) {
        this.mVideoLikeCount = mVideoLikeCount;
    }

    @PropertyName("mVideoName")
    public String getmVideoName() {
        return mVideoName;
    }

    public void setmVideoName(String mVideoName) {
        this.mVideoName = mVideoName;
    }

    @PropertyName("mVideoURL")
    public String getmVideoURL() {
        return mVideoURL;
    }

    public void setmVideoURL(String mVideoURL) {
        this.mVideoURL = mVideoURL;
    }
}
