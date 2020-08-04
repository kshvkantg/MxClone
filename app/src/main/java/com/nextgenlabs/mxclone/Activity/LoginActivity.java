package com.nextgenlabs.mxclone.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.nextgenlabs.mxclone.R;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LOGIN_ACTIVITY";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = this;
        setContentView(R.layout.activity_login);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.backgroundBar));
        window.setNavigationBarColor(ContextCompat.getColor(this,R.color.backgroundBar));
        Button button;
        final EditText countryCodeEt = findViewById(R.id.countryCode);
        final EditText phoneNumberEt = findViewById(R.id.phone_Number);
        button = findViewById(R.id.generateOtp);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber;
                if(!countryCodeEt.getText().toString().equals("")) {
                    phoneNumber = "+" + countryCodeEt.getText().toString() + phoneNumberEt.getText().toString();
                }
                else {
                    phoneNumber = "+91" + phoneNumberEt.getText().toString();
                }
                Log.i(TAG,"not empty " + phoneNumber);
                Intent intent = new Intent(context,VerifyLoginActivity.class);
                intent.putExtra("phoneNumber",phoneNumber);
                startActivity(intent);
            }
        });
    }
}