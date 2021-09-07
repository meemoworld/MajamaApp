package com.memoworld.majama.AllModals;

import java.util.ArrayList;

public class UserPageFollowing {
    ArrayList<String> followings=new ArrayList<>();

    public UserPageFollowing(ArrayList<String> followings) {
        this.followings = followings;
    }

    public UserPageFollowing() {
    }

    public ArrayList<String> getFollowings() {
        return followings;
    }

    public void setFollowings(ArrayList<String> followings) {
        this.followings = followings;
    }
}
