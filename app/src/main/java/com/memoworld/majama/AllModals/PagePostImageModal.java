package com.memoworld.majama.AllModals;

import com.google.firebase.Timestamp;

public class PagePostImageModal {

    String pageId, profileImageUrl, tags, currentFollowers;
    Timestamp timestamp;
    Long likes, dislikes;

    public PagePostImageModal(String pageId, String profileImageUrl, String tags, String currentFollowers, Timestamp timestamp) {
        this.pageId = pageId;
        this.profileImageUrl = profileImageUrl;
        this.tags = tags;
        this.currentFollowers = currentFollowers;
        this.timestamp = timestamp;
        dislikes = 0L;
        likes = 0L;
    }

    public PagePostImageModal() {
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCurrentFollowers() {
        return currentFollowers;
    }

    public void setCurrentFollowers(String currentFollowers) {
        this.currentFollowers = currentFollowers;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    public Long getDislikes() {
        return dislikes;
    }

    public void setDislikes(Long dislikes) {
        this.dislikes = dislikes;
    }
}
