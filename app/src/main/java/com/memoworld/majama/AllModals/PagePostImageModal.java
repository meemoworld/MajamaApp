package com.memoworld.majama.AllModals;

import com.google.firebase.Timestamp;

public class PagePostImageModal {

    private String pageId, postImageUrl, tags,pageImageUrl,pageName;
    private Long likes, dislikes, priority,currentFollowers,second;

    public PagePostImageModal(String pageId, String postImageUrl, String tags, Long currentFollowers, String pageImageUrl, String pageName, Long second, Long likes, Long dislikes, Long priority) {
        this.pageId = pageId;
        this.postImageUrl = postImageUrl;
        this.tags = tags;
        this.currentFollowers = currentFollowers;
        this.pageImageUrl = pageImageUrl;
        this.pageName = pageName;
        this.second = second;
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

    public Long getCurrentFollowers() {
        return currentFollowers;
    }

    public void setCurrentFollowers(Long currentFollowers) {
        this.currentFollowers = currentFollowers;
    }

    public Long getSecond() {
        return second;
    }

    public void setSecond(Long second) {
        this.second = second;
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

