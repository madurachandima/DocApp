package com.example.docapp.category_main;

public class CategoryModel {
    private String categoryType;
    private int thumbnail;
    private String activityCategoryTypes;

    private static final String TAG = "CategoryModel";

    public CategoryModel() {
    }

    public CategoryModel(String categoryType, int thumbnail, String activityCategoryTypes) {
        this.categoryType = categoryType;
        this.thumbnail = thumbnail;
        this.activityCategoryTypes = activityCategoryTypes;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getActivityCategoryTypes() {
        return activityCategoryTypes;
    }

    public void setActivityCategoryTypes(String activityCategoryTypes) {
        this.activityCategoryTypes = activityCategoryTypes;
    }
}
