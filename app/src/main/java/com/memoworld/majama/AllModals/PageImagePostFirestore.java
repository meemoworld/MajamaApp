package com.memoworld.majama.AllModals;


import com.google.firebase.Timestamp;

public class PageImagePostFirestore {
    private String imageUrl;
    private Timestamp timestamp;

    public PageImagePostFirestore(String imageUrl, Timestamp timestamp) {
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
    }

    public PageImagePostFirestore() {
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
