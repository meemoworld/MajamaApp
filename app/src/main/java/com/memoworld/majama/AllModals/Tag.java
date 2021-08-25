package com.memoworld.majama.AllModals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tag {
    private ArrayList<HashMap<String,String>> tagmap=new ArrayList<>() ;

    public Tag(ArrayList<HashMap<String, String>> tagmap) {
        this.tagmap = tagmap;
    }

    public ArrayList<HashMap<String, String>> getTagmap() {
        return tagmap;
    }

    public void setTagmap(ArrayList<HashMap<String, String>> tagmap) {
        this.tagmap = tagmap;
    }

    public Tag() {
    }
}
