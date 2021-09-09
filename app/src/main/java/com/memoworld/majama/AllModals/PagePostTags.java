package com.memoworld.majama.AllModals;

import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class PagePostTags {
    List<String> tags=new ArrayList<>();

    public PagePostTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public PagePostTags() {
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }
}
