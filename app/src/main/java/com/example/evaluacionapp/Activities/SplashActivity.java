package com.example.evaluacionapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.evaluacionapp.R;

public class SplashActivity extends AppCompatActivity {

    private ImageView imageViewSplash;

    private SharedPreferences preferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Init();
    }

    private void Init() {
        imageViewSplash = findViewById(R.id.imageViewSplash);
        Animation animation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.anim_splash);
        imageViewSplash.setAnimation(animation);
        preferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                validateSession();
            }
        }, 1000);
    }

    private void validateSession() {
        if (getPreferencesLog()) {
            changeActivity(new MainActivity());
        } else {
            changeActivity(new LoginActivity());
        }
    }

    private boolean getPreferencesLog() { // obtiene la informacion si el usuario ya inicio sesion anteriormente
        return preferences.getBoolean("Log", Boolean.parseBoolean(null));
    }

    private void changeActivity(Activity activity) {
        Intent intent = new Intent(SplashActivity.this, activity.getClass());
        startActivity(intent);
        overridePendingTransition(R.anim.transition, R.anim.transitionout);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
