package com.memoworld.majama.AllModals;

public class UserNameDetails {
    private String UID;
    private String password;

    public UserNameDetails() {
    }

    public UserNameDetails(String UID, String password) {
        this.UID = UID;
        this.password = password;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

