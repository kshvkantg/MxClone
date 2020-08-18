package com.nextgenlabs.mxclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.AsyncQueryHandler;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nextgenlabs.mxclone.Activity.AddVideoActivity;
import com.nextgenlabs.mxclone.Activity.SetDetails;
import com.nextgenlabs.mxclone.Database.AddVideo;
import com.nextgenlabs.mxclone.Fragments.ExploreFragment;
import com.nextgenlabs.mxclone.Fragments.HomeFragment;
import com.nextgenlabs.mxclone.Fragments.NotificationFragment;
import com.nextgenlabs.mxclone.Fragments.ProfileFragment;
import com.nextgenlabs.mxclone.Model.CreateVideoStorageModel;
import com.nextgenlabs.mxclone.WorkClasses.CreatePost;
import com.nextgenlabs.mxclone.WorkClasses.CreateVideoStorage;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    final String description = "hey hi #hello #ok #bye #goodNight";
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
                        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        if (intent.resolveActivity(getPackageManager() )!= null) {
                            startActivityForResult(intent, 10);
                        }
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

    @Override
    protected void onActivityResult(int RequestCode, int ResultCode , final Intent data) {
        super.onActivityResult(RequestCode, ResultCode, data);
        if (RequestCode == 10 ) {
            Intent intent = new Intent(MainActivity.this, AddVideoActivity.class);
            intent.putExtra("uid",data.getData().toString());
            startActivity(intent);
        }
    }

    public static boolean checkProfileSet(){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DocumentReference doc = db.collection("userProfiles").document(user.getPhoneNumber() + "_");
        final boolean[] check = new boolean[1];
        db.runTransaction(new Transaction.Function<Boolean>() {
            @NotNull
            @Override
            public Boolean apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(doc);
                check[0] = (boolean)snapshot.get("profileSet");
             return true;
            }
        });
        return check[0];
    }
}