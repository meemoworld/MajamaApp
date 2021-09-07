package com.memoworld.majama.AllModals;

import com.google.firebase.Timestamp;

public class PageInfoFirestore {
    private String name,about,imageUrl;
    Timestamp timestamp;
    Float balancing;

    public PageInfoFirestore(String name, String about, String imageUrl, Timestamp timestamp) {
        this.name = name;
        this.about = about;
        this.imageUrl = imageUrl;
        this.timestamp=timestamp;
        this.balancing=0.0f;
    }

    public PageInfoFirestore() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }


    public Float getBalancing() {
        return balancing;
    }

    public void setBalancing(Float balancing) {
        this.balancing = balancing;
    }
}
