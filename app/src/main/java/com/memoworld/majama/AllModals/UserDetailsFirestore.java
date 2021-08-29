package com.memoworld.majama.AllModals;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class UserDetailsFirestore {
    private String About,firstName,lastName,profileImageUrl,userCity,username,birthDate;
    private Timestamp accountCreationTime;
    private ArrayList<String> interest;

    public UserDetailsFirestore(String about, String firstName, String lastName, String profileImageUrl, String userCity, String username, String birthDate, Timestamp accountCreationTime, ArrayList<String> interest) {
        About = about;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImageUrl = profileImageUrl;
        this.userCity = userCity;
        this.username = username;
        this.birthDate = birthDate;
        this.accountCreationTime = accountCreationTime;
        this.interest = interest;
    }

    public ArrayList<String> getInterest() {
        return interest;
    }

    public void setInterest(ArrayList<String> interest) {
        this.interest = interest;
    }

    public UserDetailsFirestore() {
    }

    public String getAbout() {
        return About;
    }

    public void setAbout(String about) {
        About = about;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public Timestamp getAccountCreationTime() {
        return accountCreationTime;
    }

    public void setAccountCreationTime(Timestamp accountCreationTime) {
        this.accountCreationTime = accountCreationTime;
    }
}
