package com.krak.trainingproject.main_activity;

import android.util.Log;

import com.krak.trainingproject.tools.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class Note {

    private String masterName;
    private String text;
    private String masterImageSrc;
    private String email;
    private boolean isPublic;

    private Note() {
    }

    public Note(String masterName, String text, String masterImageSrc, String email, boolean isPublic) {
        this.masterName = masterName;
        this.text = text;
        this.masterImageSrc = masterImageSrc;
        this.email = email;
        this.isPublic = isPublic;
    }

    public String getMasterName() {
        return masterName;
    }

    public String getText() {
        return text;
    }

    public String getMasterImageSrc() {
        return masterImageSrc;
    }

    private static final String MASTER_NAME = "MASTER_NAME";
    private static final String TEXT = "TEXT";
    public static final String IS_PUBLIC = "IS_PUBLIC";
    public static final String EMAIL = "EMAIL";

    public static Note parse(JSONObject object){
        Note note = new Note();
        try{
            note.masterName = object.getString(MASTER_NAME);
            note.text = object.getString(TEXT);
            note.isPublic = object.getBoolean(IS_PUBLIC);
            note.email = object.getString(EMAIL);
        } catch (JSONException e){
            Log.e("NOTE", "ERROR WHILE PARSING");
        }
        return note;
    }

    public static Note parse(String obj) throws JSONException{
        return parse(new JSONObject(obj));
    }

    public String toString(){
        try{
            JSONObject object = new JSONObject();
            object.put(MASTER_NAME, masterName);
            object.put(TEXT, text);
            object.put(IS_PUBLIC, isPublic);
            object.put(EMAIL, email);
            return object.toString();
        } catch (JSONException e){
            Log.e("NOTE", "ERROR WHILE CONVERTING TO JSON");
        }
        return "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return isPublic == note.isPublic &&
                Objects.equals(getMasterName(), note.getMasterName()) &&
                Objects.equals(getText(), note.getText()) &&
                Objects.equals(getMasterImageSrc(), note.getMasterImageSrc());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMasterName(), getText(), getMasterImageSrc(), isPublic);
    }

    public String getEmail() {
        return email;
    }

    public boolean isPublic() {
        return isPublic;
    }
}
