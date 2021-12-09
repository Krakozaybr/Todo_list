package com.krak.trainingproject.profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.krak.trainingproject.R;
import com.krak.trainingproject.auth_activity.AuthActivity;
import com.krak.trainingproject.databinding.ProfileActivityBinding;
import com.krak.trainingproject.main_activity.MainActivity;
import com.krak.trainingproject.tools.ConsoleHelper;
import com.krak.trainingproject.tools.DatabaseRequestManager;
import com.krak.trainingproject.tools.Session;
import com.krak.trainingproject.tools.User;

public class ProfileActivity extends AppCompatActivity {

    private static final String LOG_TAG = "ProfileActivity";

    private ProfileActivityBinding binding;
    private DatabaseRequestManager databaseRequestManager;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ProfileActivityBinding.inflate(getLayoutInflater());
        user = Session.getUser();
        setContentView(binding.getRoot());
        init();
    }

    private void init(){
        initManagers();
        addViewsContent();
        addOnClickListeners();
        addTextToInputs();
    }

    private void initManagers() {
        databaseRequestManager = new DatabaseRequestManager(this);
    }

    private void addTextToInputs(){
        binding.userNameInputProfile.setText(user.getName());
        binding.userPasswordInputProfile.setText(user.getPassword());
        binding.userEmailInputProfile.setText(user.getEmail());
    }

    private void save(){
        Log.i(LOG_TAG, "Updating user info");
        ConsoleHelper.printUserInfo();
        if (!databaseRequestManager.existUser(binding.userEmailInputProfile.getText().toString())) {
            user.setEmail(binding.userEmailInputProfile.getText().toString());
            user.setName(binding.userNameInputProfile.getText().toString());
            user.setPassword(binding.userPasswordInputProfile.getText().toString());
            Log.i(LOG_TAG, "User info updated");
            databaseRequestManager.updateUserInfo();
            ConsoleHelper.printUserInfo();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            showUserExistsDialog();
        }
    }

    private void showUserExistsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.already_exists))
                .setNeutralButton(getResources().getString(R.string.cancel), (i, k) -> {});
        builder.create().show();
    }

    private void delete(){
        databaseRequestManager.deleteCurrentUser();
        Intent intent = new Intent(this, AuthActivity.class);
        intent.putExtra(AuthActivity.DELETED, true);
        startActivity(intent);
    }

    private void addOnClickListeners(){
        binding.deleteProfileBtn.setOnClickListener(view -> delete());
        binding.saveProfileBtn.setOnClickListener(view -> save());
        binding.signOutBtn.setOnClickListener(view -> signOut());
        binding.comeBackBtn.setOnClickListener(view -> cancel());
    }

    private void signOut(){
        Intent intent = new Intent(this, AuthActivity.class);
        intent.putExtra(AuthActivity.SIGNED_OUT, true);
        startActivity(intent);
    }

    private void cancel(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void addViewsContent(){
        binding.userNameInputProfile.setText(user.getName());
        if (!user.getImageSrc().equals("")){
            binding.profileImageProfile.setImageURI(Uri.parse(user.getImageSrc()));
        }
    }
}
