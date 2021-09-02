package com.memoworld.majama.AllModals;

public class UserDetailsRealtime {
    private String birthday,city,lastName;

    public UserDetailsRealtime() {
    }

    public UserDetailsRealtime(String birthday, String city, String lastName) {
        this.birthday = birthday;
        this.city = city;
        this.lastName = lastName;
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
