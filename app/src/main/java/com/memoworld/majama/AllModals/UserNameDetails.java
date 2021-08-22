package com.memoworld.majama.AllModals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserNameDetails {
   private String email;
   private String password;

    public UserNameDetails() {
    }

    public UserNameDetails(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

