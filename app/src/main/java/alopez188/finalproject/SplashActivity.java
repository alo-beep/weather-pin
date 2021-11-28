package alopez188.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * SplashActivity shows the user the splash screen upon start up
 * @author Angel Lopez
 * @version 1.0
 */
public class SplashActivity extends AppCompatActivity {

    // once application starts, show splash screen for 1.5s
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        }, 1500);
    }
}