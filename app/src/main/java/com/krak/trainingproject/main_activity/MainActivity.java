package com.krak.trainingproject.main_activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.krak.trainingproject.R;
import com.krak.trainingproject.databinding.MainActivityBinding;
import com.krak.trainingproject.new_note_activity.NewNoteActivity;
import com.krak.trainingproject.profile.ProfileActivity;
import com.krak.trainingproject.tools.DatabaseRequestManager;
import com.krak.trainingproject.tools.Session;
import com.krak.trainingproject.tools.User;

import java.net.URI;

public class MainActivity extends AppCompatActivity {

    private MainActivityBinding binding;
    private DatabaseRequestManager databaseRequestManager;
    private User user;
    private static final String LOG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MainActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        user = Session.getUser();
        init();
        addOnClickListeners();
        binding.notesList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        addItems();
    }

    private void init(){
        initManagers();
        addViewsContent();
        addItems();
    }

    private void addViewsContent(){
        binding.nameTW.setText(user.getName());
        if (!user.getImageSrc().equals("")){
            binding.profileImage.setImageURI(Uri.parse(user.getImageSrc()));
        }
    }

    private void initManagers() {
        databaseRequestManager = new DatabaseRequestManager(this);
    }

    private void addOnClickListeners() {
        binding.addNoteBtn.setOnClickListener(view -> startNewNote());
        binding.profileImage.setOnClickListener(view -> startProfile());
    }

    private void startNewNote(){
        Intent intent = new Intent(this, NewNoteActivity.class);
        startActivity(intent);
    }

    private void startProfile(){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    private void addItems() {
        NotesAdapter adapter = new NotesAdapter(this, databaseRequestManager.getAvailableNotes());
        binding.notesList.setAdapter(adapter);
        binding.notesList.invalidateItemDecorations();
    }

    @Override
    public void onBackPressed(){}
}