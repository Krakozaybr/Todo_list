package com.krak.trainingproject.start_activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.krak.trainingproject.auth_activity.AuthActivity;
import com.krak.trainingproject.databinding.StartActivityBinding;

public class StartActivity extends AppCompatActivity {

    private static final String TIME_HAS_COME = "TIME_HAS_COME";
    private StartActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = StartActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(TIME_HAS_COME));
        startWaiting();
    }
    // Комментарий далее - Deprecated не читайте его
    // @Deprecated
    // Получаем сообщение, что ВРЕМЯ ПРИШЛО И ПОРА ПОКАЗАТЬ СМЕРТНЫМ МОЩЬ ЭТОГО МИРА...

    // После 1.5 секунды показа пользователю заставки идём в AuthActivity
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LocalBroadcastManager.getInstance(StartActivity.this).unregisterReceiver(receiver);
            Intent next = new Intent(StartActivity.this, AuthActivity.class);
            startActivity(next);
        }
    };

    // Ждем 1.5 секунды
    private void startWaiting() {
        new Thread(){
            @Override
            public void run() {
                try{
                    Thread.sleep(1500);
                    LocalBroadcastManager.getInstance(StartActivity.this).sendBroadcast(new Intent(TIME_HAS_COME));
                } catch (Exception ignored){}
            }
        }.start();
    }
}
