package com.nextgenlabs.mxclone;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.nextgenlabs.mxclone.Fragments.ExploreFragment;
import com.nextgenlabs.mxclone.Fragments.HomeFragment;
import com.nextgenlabs.mxclone.Fragments.NotificationFragment;
import com.nextgenlabs.mxclone.Fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.backgroundBar));
        window.setNavigationBarColor(ContextCompat.getColor(this,R.color.backgroundBar));
        setContentView(R.layout.activity_main);
        FrameLayout frameLayout = findViewById(R.id.a_main_frameLayout);
        BottomNavigationView navigationView = findViewById(R.id.a_main_bottom_nav);
        navigationView.setSelectedItemId(R.id.nav_home);
        int id  = navigationView.getSelectedItemId();
        HomeFragment homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.a_main_frameLayout,homeFragment).commit();
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFrag = new HomeFragment();
                int id;
                switch (item.getItemId()){
                    case R.id.nav_home:
                        item.setIcon(R.drawable.checked_home);
                        selectedFrag = new HomeFragment();
                        break;
                    case R.id.nav_Explore:
                        item.setIcon(R.drawable.checked_explore_btn);
                        selectedFrag = new ExploreFragment();
                        break;
                    case R.id.nav_Add:
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent,10);
                        break;
                    case R.id.nav_Notification:
                        item.setIcon(R.drawable.checked_notification);
                        selectedFrag = new NotificationFragment();
                        break;
                    case R.id.nav_Me:
                        item.setIcon(R.drawable.checked_account_btn);
                        selectedFrag = new ProfileFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().add(R.id.a_main_frameLayout,selectedFrag).commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.a_main_frameLayout,selectedFrag).commit();
                return true;
            }
        });
    }
}