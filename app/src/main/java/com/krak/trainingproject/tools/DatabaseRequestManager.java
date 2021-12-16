package com.krak.trainingproject.tools;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.krak.trainingproject.main_activity.Note;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DatabaseRequestManager {

    private static final String LOG_TAG = "DATABASE";
    private static final String CREATE_DB = "CREATE TABLE IF NOT EXISTS users (email TEXT, password TEXT, name TEXT, list TEXT, image TEXT)";
    private static final String DB_NAME = "TODOLIST_DATABASE_01.db";
    private static final String GET_USERS = "SELECT * FROM users;";
    private static final String INSERT = "INSERT OR IGNORE INTO users VALUES ";
    private static final String UPDATE_LIST = "UPDATE users SET list = '%s' WHERE email = '%s'";
    private static final String UPDATE_PASSWORD = "UPDATE users SET password = '%s' WHERE email = '%s'";
    private static final String UPDATE_EMAIL = "UPDATE users SET email = '%s' WHERE email = '%s'";
    private static final String UPDATE_IMAGE = "UPDATE users SET image = '%s' WHERE email = '%s'";
    private static final String UPDATE_NAME = "UPDATE users SET name = '%s' WHERE email = '%s'";
    private static final String DELETE_USER = "DELETE FROM users WHERE email = '%s'";

    private AppCompatActivity activity;
    private SQLiteDatabase database;

    public DatabaseRequestManager(AppCompatActivity activity) {
        this.activity = activity;
        database = activity.getBaseContext().openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        database.execSQL(CREATE_DB);
    }

    // Это легаси метод. Он был написан еще до создания мира. Плохой он в общем. Не используйте его
    @Deprecated
    public boolean checkAuth(String email, String password){
        Cursor cursor = database.rawQuery(GET_USERS, null);
        while(cursor.moveToNext()){
            if (cursor.getString(0).equals(email) && cursor.getString(1).equals(password)){
                Log.i(LOG_TAG, "SUCCESS");
                return true;
            } else {
                Log.i(LOG_TAG, email + "does not equal to " + cursor.getString(0));
                Log.i(LOG_TAG, password + "does not equal to " + cursor.getString(1));
            }
        }
        return false;
    }

    public User getUser(String email, String password){
        Cursor cursor = database.rawQuery(GET_USERS, null);
        while(cursor.moveToNext()){
            if (cursor.getString(0).equals(email) && cursor.getString(1).equals(password)){
                Log.i(LOG_TAG, "SUCCESS");
                ArrayList<Note> notes = new ArrayList<>();
                try{
                    JSONObject object = new JSONObject(cursor.getString(3));
                    JSONArray array = object.getJSONArray(User.NOTES);
                    for (int i = 0; i < array.length(); i++) {
                        notes.add(Note.parse(array.getJSONObject(i)));
                        Log.i(LOG_TAG, "SUCCESS " + i);
                    }
                } catch (JSONException e){
                    Log.e(LOG_TAG, "EXCEPTION WHILE PARSING JSON: " + cursor.getString(3));
                }
                return new User(email, password, cursor.getString(2), notes);
            }
        }
        Log.i(LOG_TAG, "USER IS NOT FOUND");
        return null;
    }

    public boolean register(String email, String password, String name){
        boolean unique = true;
        Cursor cursor = database.rawQuery(GET_USERS, null);
        while(cursor.moveToNext()){
            if (cursor.getString(0).equals(email)){
                unique = false;
                break;
            }
        }
        if (unique) {
            database.execSQL(INSERT + String.format("('%s', '%s', '%s', '{}', '');", email, password, name));
        }
        return unique;
    }

    public ArrayList<Note> getAvailableNotes(){
        String email = Session.getUser().getEmail();
        ArrayList<Note> result = new ArrayList<>();
        Cursor cursor = database.rawQuery(GET_USERS, null);
        while(cursor.moveToNext()){
            if (cursor.getString(0).equals(email)){
                try {
                    JSONObject object = new JSONObject(cursor.getString(3));
                    JSONArray notes = object.getJSONArray(User.NOTES);
                    for (int i = 0; i < notes.length(); i++) {
                        JSONObject note = notes.getJSONObject(i);
                        result.add(Note.parse(note));
                    }
                } catch (JSONException e){
                    Log.e(LOG_TAG, "JSONException: " + cursor.getString(3));
                }
            } else {
                try {
                    JSONObject object = new JSONObject(cursor.getString(3));
                    JSONArray notes = object.getJSONArray(User.NOTES);
                    for (int i = 0; i < notes.length(); i++) {
                        JSONObject note = notes.getJSONObject(i);
                        if (note.getBoolean(Note.IS_PUBLIC)) {
                            result.add(Note.parse(note));
                        }
                    }
                } catch (JSONException e){
                    Log.e(LOG_TAG, "JSONException: " + cursor.getString(3));
                }
            }
        }
        return result;
    }

    public void printAllUsers(){
        Cursor cursor = database.rawQuery(GET_USERS, null);
        while(cursor.moveToNext()){
            Log.i(LOG_TAG, String.format("%s, %s, %s, %s", cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
        }
    }

    public void updateUserInfo() {
        User user = Session.getUser();
        Cursor cursor = database.rawQuery(GET_USERS, null);
        // Cycle is needed to check user's existing
        while(cursor.moveToNext()){
            if (cursor.getString(0).equals(user.getEmail())){
                database.execSQL(String.format(UPDATE_LIST, user.getNotes(), user.getEmail()));
                database.execSQL(String.format(UPDATE_PASSWORD, user.getPassword(), user.getEmail()));
                database.execSQL(String.format(UPDATE_EMAIL, user.getEmail(), user.getEmail()));
                database.execSQL(String.format(UPDATE_IMAGE, user.getImageSrc(), user.getEmail()));
                database.execSQL(String.format(UPDATE_NAME, user.getName(), user.getEmail()));
                break;
            }
        }
    }

    public void deleteCurrentUser() {
        database.execSQL(String.format(DELETE_USER, Session.getUser().getEmail()));
    }

    public boolean existUser(String email) {
        Cursor cursor = database.rawQuery(GET_USERS, null);
        while(cursor.moveToNext()){
            if (cursor.getString(0).equals(email) && !cursor.getString(0).equals(Session.getUser().getEmail())){
                return true;
            }
        }
        return false;
    }
}
