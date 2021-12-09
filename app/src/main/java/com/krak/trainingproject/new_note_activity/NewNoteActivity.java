package com.krak.trainingproject.new_note_activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.krak.trainingproject.R;
import com.krak.trainingproject.databinding.NewNoteActivityBinding;
import com.krak.trainingproject.main_activity.MainActivity;
import com.krak.trainingproject.main_activity.Note;
import com.krak.trainingproject.profile.ProfileActivity;
import com.krak.trainingproject.tools.DatabaseRequestManager;
import com.krak.trainingproject.tools.PreferencesManager;
import com.krak.trainingproject.tools.Session;
import com.krak.trainingproject.tools.User;

import org.json.JSONException;

public class NewNoteActivity extends AppCompatActivity {

    private static final String TEXT = "TEXT";
    public static final String IS_UPDATE = "IS_UPDATE";
    public static final String NOTE = "NOTE";
    private static final String LOG_TAG = "NewNoteActivity";
    private NewNoteActivityBinding binding;
    DatabaseRequestManager databaseRequestManager;
    PreferencesManager preferencesManager;
    User user;
    boolean isUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = NewNoteActivityBinding.inflate(getLayoutInflater());
        isUpdate = getIntent().getBooleanExtra(IS_UPDATE, false);
        setContentView(binding.getRoot());
        user = Session.getUser();
        init();
        addOnClickListeners();
    }

    private void init(){
        initManagers();
        addViewsContent();
        update();
    }

    private void initManagers(){
        databaseRequestManager = new DatabaseRequestManager(this);
        preferencesManager = new PreferencesManager(this);
    }

    private void addOnClickListeners(){
        if (isUpdate) {
            binding.addNoteBtnNN.setText(getResources().getString(R.string.update));
            try {
                binding.isPublicCheckBox.setChecked(Note.parse(getIntent().getStringExtra(NOTE)).isPublic());
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error while parsing note");
            }
            binding.addNoteBtnNN.setOnClickListener(view -> {
                try {
                    user.updateNote(Note.parse(getIntent().getStringExtra(NOTE)),
                            new Note(user.getName(), getText(), user.getImageSrc(), user.getEmail(), binding.isPublicCheckBox.isChecked()));
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "Error while parsing note");
                }
                databaseRequestManager.updateUserInfo();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            });
            binding.deleteNoteBtn.setOnClickListener(view -> {
                try {
                    user.removeNote(Note.parse(getIntent().getStringExtra(NOTE)));
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "Error while parsing note");
                }
                databaseRequestManager.updateUserInfo();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            });
        } else {
            binding.deleteNoteBtn.setVisibility(View.GONE);
            binding.addNoteBtnNN.setOnClickListener(view -> {
                user.addNote(new Note(user.getName(), getText(), user.getImageSrc(), user.getEmail(), binding.isPublicCheckBox.isChecked()));
                databaseRequestManager.updateUserInfo();
                preferencesManager.putString(TEXT, "");
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            });
            binding.saveBtnNN.setOnClickListener(view -> {
                preferencesManager.putString(TEXT, getText());
            });
        }
        binding.cancelBtnNN.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
        binding.profileImageNN.setOnClickListener(view -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        });
    }

    private void update(){
        if (isUpdate){
            try {
                binding.textNN.setText(Note.parse(getIntent().getStringExtra(NOTE)).getText());
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error while parsing note");
            }
        } else {
            binding.textNN.setText(preferencesManager.getString(TEXT));
        }
    }

    private String getText(){
        return binding.textNN.getText().toString();
    }

    private void addViewsContent(){
        binding.nameTWNN.setText(user.getName());
        if (!user.getImageSrc().equals("")){
            binding.profileImageNN.setImageURI(Uri.parse(user.getImageSrc()));
        }
    }
}
