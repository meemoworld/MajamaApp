package com.memoworld.majama.AllModals;

import com.google.firebase.Timestamp;

public class PostImage {
    private String imageUrl,caption;
    private Timestamp timestamp;

    public PostImage(String imageUrl, String caption, Timestamp timestamp) {
        this.imageUrl = imageUrl;
        this.caption = caption;
        this.timestamp = timestamp;
    }

    public PostImage() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
