package com.memoworld.majama.MainActivityFragments;

public class MainCardModel {
    private String pageName;
    private String postImageUrl;

    public MainCardModel(String pageName) {
        this.pageName = pageName;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }
}
