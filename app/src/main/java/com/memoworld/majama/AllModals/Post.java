package com.memoworld.majama.AllModals;

import android.provider.MediaStore;

import com.google.firebase.Timestamp;

public class Post {
    private String imageUrl;
    private Integer likeCount,dislikeCount;
    private Timestamp uploadTime;

    public Post(String imageUrl, Integer likeCount, Integer dislikeCount, Timestamp uploadTime) {
        this.imageUrl = imageUrl;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.uploadTime = uploadTime;
    }

    public Post() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getDislikeCount() {
        return dislikeCount;
    }

    public void setDislikeCount(Integer dislikeCount) {
        this.dislikeCount = dislikeCount;
    }

    public Timestamp getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Timestamp uploadTime) {
        this.uploadTime = uploadTime;
    }
}
