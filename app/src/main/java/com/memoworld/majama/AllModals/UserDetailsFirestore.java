package com.memoworld.majama.AllModals;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class UserDetailsFirestore {
    private String About,firstName,lastName,profileImageUrl,userCity,username,birthDate,balance;
    private Timestamp accountCreationTime;
    private ArrayList<String> interest;
    private Integer followers,following;

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
        balance="0.0";
        followers=0;
        following=0;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public Integer getFollowers() {
        return followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public Integer getFollowing() {
        return following;
    }

    public void setFollowing(Integer following) {
        this.following = following;
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
