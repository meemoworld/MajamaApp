package com.memoworld.majama.AllModals;

public class EachTag {
    private String tagName;
    private String imageUrl;

    public EachTag(String tagName, String imageUrl) {
        this.tagName = tagName;
        this.imageUrl = imageUrl;
    }

    public EachTag() {}

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
