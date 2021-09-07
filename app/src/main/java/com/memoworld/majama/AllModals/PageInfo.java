package com.memoworld.majama.AllModals;

import com.google.firebase.Timestamp;

public class PageInfo {
    private String name,about,imageUrl,ownerUid;
    Timestamp timestamp;

    public PageInfo(String name, String about, String imageUrl,Timestamp timestamp,String ownerUid) {
        this.name = name;
        this.about = about;
        this.imageUrl = imageUrl;
        this.timestamp=timestamp;
        this.ownerUid=ownerUid;
    }

    public PageInfo() {
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

    public String getOwnerUid() {
        return ownerUid;
    }

    public void setOwnerUid(String ownerUid) {
        this.ownerUid = ownerUid;
    }
}
