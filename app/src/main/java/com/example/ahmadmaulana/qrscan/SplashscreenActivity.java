package com.example.ahmadmaulana.qrscan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SplashscreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splashscreen);
        showLoading();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void showLoading(){

        new Thread(new Runnable() {

            public void run() {
                for (int x = 0;x < 1;x++){
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Intent x = new Intent(SplashscreenActivity.this, LoginActivity.class);
                startActivity(x);
                finish();
            }
        }).start();
    }
}
