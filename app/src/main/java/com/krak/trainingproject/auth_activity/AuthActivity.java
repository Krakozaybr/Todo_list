package com.krak.trainingproject.auth_activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.krak.trainingproject.R;
import com.krak.trainingproject.databinding.AuthActivityBinding;
import com.krak.trainingproject.main_activity.MainActivity;
import com.krak.trainingproject.registration_activity.RegistrationActivity;
import com.krak.trainingproject.tools.DatabaseRequestManager;
import com.krak.trainingproject.tools.PreferencesManager;
import com.krak.trainingproject.tools.Session;
import com.krak.trainingproject.tools.User;

public class AuthActivity extends AppCompatActivity {

    public static final String REGISTERED = "REGISTERED";
    public static final String PASSWORD = "PASSWORD";
    public static final String DELETED = "DELETED";
    public static final String SIGNED_OUT = "SIGNED_OUT";
    public static final String EMAIL = "EMAIL";
    private static final String IS_AUTHORIZED = "IS_AUTHORIZED";
    private static final String SAVED_EMAIL = "SAVED_EMAIL";
    private static final String SAVED_PASSWORD = "SAVED_PASSWORD";
    private static final String LOG_TAG = "AuthActivity";
    private AuthActivityBinding binding;
    private PreferencesManager preferencesManager;
    private DatabaseRequestManager databaseRequestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AuthActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        addOnClickListeners();
        setValuesToInputs();
        checkAuth();
    }

    private void init(){
        initManagers();
    }

    private void setValuesToInputs() {
        binding.emailInput.setText(getSavedEmail());
    }

    private void addOnClickListeners(){
        binding.authRegisterBtn.setOnClickListener(view -> startRegistration());
        binding.authSignInBtn.setOnClickListener(view -> trySignIn());
    }

    private void startRegistration(){
        Intent intent = new Intent(AuthActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }

    private void trySignIn(){
        String email = binding.emailInput.getText().toString();
        String password = binding.passwordInput.getText().toString();
        if (checkInputs()) {
            signIn(email, password);
        }
    }

    private boolean checkInputs(){
        String email = binding.emailInput.getText().toString().trim();
        String password = binding.passwordInput.getText().toString().trim();
        return email.matches(".{3,}@.+\\..{2,}") && !password.equals("");
    }

    private void signIn(String email, String password){
        Log.i(LOG_TAG, "Email: " + email + " password: " + password);
        User user = databaseRequestManager.getUser(email, password);
        if (user != null){
            Intent intent = new Intent(this, MainActivity.class);
            saveEmail(binding.emailInput.getText().toString());
            savePassword(binding.passwordInput.getText().toString());
            setIsAuthorized(true);
            Session.setUser(user);
            startActivity(intent);
        } else {
            showMessage(getResources().getString(R.string.invalid_auth));
        }
    }

    private void showMessage(String message){
        binding.authErrorMessageTW.setText(message);
        Log.i(LOG_TAG, "Message '" + message + "' is shown");
    }

    private void initManagers(){
        preferencesManager = new PreferencesManager(this);
        databaseRequestManager = new DatabaseRequestManager(this);
    }

    private void checkAuth(){
        if (getIntent().getBooleanExtra(REGISTERED, false)){
            Log.i(LOG_TAG, "REGISTERED");
            setIsAuthorized(true);
            savePassword(getIntent().getStringExtra(PASSWORD));
            saveEmail(getIntent().getStringExtra(EMAIL));
            signIn(getSavedEmail(), getSavedPassword());
        } else if (getIntent().getBooleanExtra(DELETED, false)){
            Log.i(LOG_TAG, "DELETED");
            setIsAuthorized(false);
            savePassword(getIntent().getStringExtra(""));
            saveEmail(getIntent().getStringExtra(""));
        } else if (getIntent().getBooleanExtra(SIGNED_OUT, false)){
            Log.i(LOG_TAG, "SIGNED OUT");
            setIsAuthorized(false);
            savePassword("");
        } else if (preferencesManager.getBoolean(IS_AUTHORIZED)){
            signIn(getSavedEmail(), getSavedPassword());
        }
    }

    private void saveEmail(String email){
        preferencesManager.putString(SAVED_EMAIL, email);
    }

    private void savePassword(String password){
        preferencesManager.putString(SAVED_PASSWORD, password);
    }

    private void setIsAuthorized(boolean value){
        preferencesManager.putBoolean(IS_AUTHORIZED, value);
    }
    
    private String getSavedEmail(){
        return preferencesManager.getString(SAVED_EMAIL);
    }

    private String getSavedPassword(){
        return preferencesManager.getString(SAVED_PASSWORD);
    }

    @Override
    public void onBackPressed() {}
}