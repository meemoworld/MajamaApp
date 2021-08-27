package com.memoworld.majama.AllModals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tag {
    private HashMap<String,String> tagmap=new HashMap<>() ;

    public Tag(HashMap<String, String> tagmap) {
        this.tagmap = tagmap;
    }

    public HashMap<String, String> getTagmap() {
        return tagmap;
    }

    public void setTagmap(HashMap<String, String> tagmap) {
        this.tagmap = tagmap;
    }

    public Tag() {
    }
}
