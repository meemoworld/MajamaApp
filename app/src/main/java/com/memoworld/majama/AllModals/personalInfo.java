package com.memoworld.majama.AllModals;

public class personalInfo {
    private String birthday, city, lastName, username, profileImageUrl;

    public personalInfo() {
    }

    public personalInfo(String birthday, String city, String lastName, String username, String profileImageUrl) {
        this.birthday = birthday;
        this.city = city;
        this.lastName = lastName;
        this.username = username;
        this.profileImageUrl = profileImageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
