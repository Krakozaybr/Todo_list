package com.krak.trainingproject.tools;

import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

// Just wrapper of SharedPreferences which returns default values
public class PreferencesManager {

    private final SharedPreferences preferences;

    public PreferencesManager(AppCompatActivity appCompatActivity) {
        this.preferences = appCompatActivity.getPreferences(AppCompatActivity.MODE_PRIVATE);
    }

    public Map<String, ?> getAll() {
        return preferences.getAll();
    }

    public String getString(String s) {
        return preferences.getString(s, "");
    }

    public int getInt(String s) {
        return preferences.getInt(s, -1);
    }

    public long getLong(String s) {
        return preferences.getLong(s, 0l);
    }

    public float getFloat(String s) {
        return preferences.getFloat(s, 0f);
    }

    public boolean getBoolean(String s) {
        return preferences.getBoolean(s, false);
    }

    public boolean contains(String s) {
        return preferences.contains(s);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void putLong(String key, long value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public void putFloat(String key, float value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
}
