package com.memoworld.majama.AllModals;

import com.google.firebase.Timestamp;

public class PagePostImageModal {

    private String pageId, postImageUrl, tags, currentFollowers,pageImageUrl,pageName;
    private Timestamp timestamp;
    private Long likes, dislikes, priority;

    public PagePostImageModal(String pageId, String postImageUrl, String tags, String currentFollowers, String pageImageUrl, String pageName, Timestamp timestamp, Long likes, Long dislikes, Long priority) {
        this.pageId = pageId;
        this.postImageUrl = postImageUrl;
        this.tags = tags;
        this.currentFollowers = currentFollowers;
        this.pageImageUrl = pageImageUrl;
        this.pageName = pageName;
        this.timestamp = timestamp;
        this.likes = likes;
        this.dislikes = dislikes;
        this.priority = priority;
    }

    public PagePostImageModal() {
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public void setPostImageUrl(String postImageUrl) {
        this.postImageUrl = postImageUrl;
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

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public String getPageImageUrl() {
        return pageImageUrl;
    }

    public void setPageImageUrl(String pageImageUrl) {
        this.pageImageUrl = pageImageUrl;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }
}

