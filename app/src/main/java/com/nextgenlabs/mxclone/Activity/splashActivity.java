package com.nextgenlabs.mxclone.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nextgenlabs.mxclone.MainActivity;
import com.nextgenlabs.mxclone.R;

public class splashActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.backgroundBar));
        window.setNavigationBarColor(ContextCompat.getColor(this,R.color.backgroundBar));
    }
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(splashActivity.this, MainActivity.class);
                startActivity(intent);
            }
        },1300);

        }
        else {
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        }
    }
    public void onPause() {
        super.onPause();
        finish();
    }

}