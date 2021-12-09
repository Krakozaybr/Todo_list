package com.krak.trainingproject.tools;

public class Session {
    public static User user = null;

    public static User getUser(){
        return user;
    }

    public static void setUser(User user) {
        Session.user = user;
    }
}
