package com.krak.trainingproject.tools;

import android.util.Log;


// Чисто Debug класс
public class ConsoleHelper {

    private static final String LOG_TAG = "ConsoleHelper";

    public static void printUserInfo(){
        User user = Session.getUser();
        Log.i(LOG_TAG, String.format("name: %s, password: %s, email: %s, list: %s", user.getName(), user.getPassword(), user.getEmail(), user.getNotes()));
    }
}
