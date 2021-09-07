package com.memoworld.majama.AllModals;

public class PageInfoRealTime {
    private String name,about,imageUrl,ownerUid;
    private Integer followers;

    public PageInfoRealTime(String name, String about, String imageUrl, String ownerUid, Integer followers) {
        this.name = name;
        this.about = about;
        this.imageUrl = imageUrl;
        this.ownerUid = ownerUid;
        this.followers = followers;
    }

    public PageInfoRealTime() {
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

    public String getOwnerUid() {
        return ownerUid;
    }

    public void setOwnerUid(String ownerUid) {
        this.ownerUid = ownerUid;
    }

    public Integer getFollowers() {
        return followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }
}
