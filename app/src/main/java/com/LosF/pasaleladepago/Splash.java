package com.LosF.pasaleladepago;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        int DURACION_SPLASH = 3000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*Intent intent = new Intent(Splash.this,MainActivity.class);
                startActivity(intent);*/
                startActivity(new Intent(Splash.this,Menu.class));
                // startActivity(new Intent(Splash.this,MainActivity.class));
            }
        },DURACION_SPLASH);
    }
}