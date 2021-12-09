package com.krak.trainingproject.tools;

import android.util.Log;

import com.krak.trainingproject.main_activity.Note;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class User {

    public static final String NOTES = "NOTES";

    private volatile String email;
    private volatile String password;
    private volatile String name;
    private volatile String imageSrc;
    private volatile ArrayList<Note> notes;

    public User(String email, String password, String name, ArrayList<Note> notes) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.notes = notes;
        this.imageSrc = "";
    }

    public void addNote(Note note){
        notes.add(note);
    }

    public String getName() {
        return name;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public String getEmail() {
        return email;
    }

    public String getNotes() {
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        try {
            for (Note note : notes) {
                array.put(new JSONObject(note.toString()));
            }
            object.put(NOTES, array);
        } catch (JSONException e){
            Log.e("USER", "ERROR WHILE RETURNING NOTES");
            return "{}";
        }
        return object.toString();
    }

    public void updateNote(Note prev, Note update) {
        int index = getIndexOf(prev);
        if (index >= 0){
            notes.set(index, update);
        }
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void removeNote(Note deleted) {
        int index = getIndexOf(deleted);
        if (index >= 0){
            notes.remove(index);
        }
    }

    private int getIndexOf(Note note){
        for (int i = 0; i < notes.size(); i++){
            if (notes.get(i).toString().equals(note.toString())){
                return i;
            }
        }
        return -1;
    }
}
