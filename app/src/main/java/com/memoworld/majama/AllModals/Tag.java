package com.memoworld.majama.AllModals;

import java.util.ArrayList;
import java.util.List;

public class Tag {
    private List<String> tagArray = new ArrayList<>();

    public Tag() {

    }

    public Tag(List<String> tagArray) {
        this.tagArray = tagArray;
    }

    public List<String> getTagArray() {
        return tagArray;
    }

    public void setTagArray(List<String> tagArray) {
        this.tagArray = tagArray;
    }
}
