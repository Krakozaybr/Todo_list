package com.krak.trainingproject.registration_activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.krak.trainingproject.R;
import com.krak.trainingproject.auth_activity.AuthActivity;
import com.krak.trainingproject.databinding.RegistartionActivityBinding;
import com.krak.trainingproject.tools.DatabaseRequestManager;

public class RegistrationActivity extends AppCompatActivity {

    private RegistartionActivityBinding binding;
    private DatabaseRequestManager databaseRequestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = RegistartionActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initManagers();
        addOnClickListeners();
    }

    private void initManagers() {
        databaseRequestManager = new DatabaseRequestManager(this);
    }

    private void addOnClickListeners() {
        binding.haveAccountBtnReg.setOnClickListener(view -> startAuth());
        binding.registerBtnReg.setOnClickListener(view -> register());
    }

    // Берём данные из EditText и пытаемся зарегистрироваться
    private void register(){
        if (checkInputs()) {
            String email = binding.emailInputReg.getText().toString().trim();
            String password = binding.passwordInputReg.getText().toString().trim();
            String name = binding.nameInputReg.getText().toString().trim();
            if (databaseRequestManager.register(email, password, name)) {
                Intent intent = new Intent(this, AuthActivity.class);
                intent.putExtra(AuthActivity.PASSWORD, password)
                        .putExtra(AuthActivity.EMAIL, email)
                        .putExtra(AuthActivity.REGISTERED, true);
                startActivity(intent);
            } else {
                showMessage(getResources().getString(R.string.already_exists));
            }
        } else {
            showMessage(getResources().getString(R.string.invalid_registration));
        }
    }

    // Идём в AuthActivity
    private void startAuth(){
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
    }

    // Показываем сообщение пользователю об ошибке
    private void showMessage(String message){
        binding.regErrorMessageTW.setText(message);
        Log.i("AuthActivity", "Message '" + message + "' is shown");
    }

    // Проверяет EditView на наличие текста + проверяет email
    private boolean checkInputs() {
        String email = binding.emailInputReg.getText().toString().trim();
        String password = binding.passwordInputReg.getText().toString().trim();
        String passwordRepeat = binding.repeatPasswordInputReg.getText().toString().trim();
        String name = binding.nameInputReg.getText().toString().trim();
        // Страшное ".{3,}@.+\\..{2,}" означает, что email должен соответствовать виду
        // Есть @
        // До @ как минимум 3 символа
        // После @ идёт ненулевое количество символов, точка и два или более символов
        return email.matches(".{3,}@.+\\..{2,}") &&
                !password.equals("") &&
                !passwordRepeat.equals("") &&
                !name.equals("") &&
                password.equals(passwordRepeat);
    }
}
