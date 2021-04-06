package com.example.docapp.category_music;

public class CategoryMusicModel {
    private String musicId;
    private String url;
    private String title;
    private String musicCategory;

    public CategoryMusicModel() {
    }

    public CategoryMusicModel(String musicId, String url, String title, String musicCategory) {
        this.musicId = musicId;
        this.url = url;
        this.title = title;
        this.musicCategory = musicCategory;
    }

    public String getMusicId() {
        return musicId;
    }

    public void setMusicId(String musicId) {
        this.musicId = musicId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMusicCategory() {
        return musicCategory;
    }

    public void setMusicCategory(String musicCategory) {
        this.musicCategory = musicCategory;
    }
}
