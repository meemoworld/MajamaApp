package com.memoworld.majama.AllModals;

import java.util.HashMap;
import java.util.Map;

public class RealTimeUser  {
    private String username;
    personalInfo personalInfo;

    public RealTimeUser(com.memoworld.majama.AllModals.personalInfo personalInfo,String username) {
        this.personalInfo = personalInfo;
        this.username=username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public RealTimeUser() {
    }

    public com.memoworld.majama.AllModals.personalInfo getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(com.memoworld.majama.AllModals.personalInfo personalInfo) {
        this.personalInfo = personalInfo;
    }
}
