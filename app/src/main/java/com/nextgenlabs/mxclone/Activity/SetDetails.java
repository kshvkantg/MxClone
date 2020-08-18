package com.nextgenlabs.mxclone.Activity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.nextgenlabs.mxclone.MainActivity;
import com.nextgenlabs.mxclone.Model.ProfileModel;
import com.nextgenlabs.mxclone.R;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;

public class SetDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.backgroundBar));
        window.setNavigationBarColor(ContextCompat.getColor(this,R.color.backgroundBar));
        setContentView(R.layout.activity_set_details);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference doc = db.collection("userProfiles").document(user.getPhoneNumber() + "_");

        final EditText setNameEt = findViewById(R.id.setDisplayName);
        final EditText setEmailEt = findViewById(R.id.setEmail);
        Button button = findViewById(R.id.setDetails);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String setName = setNameEt.getText().toString();
                String email = setEmailEt.getText().toString();
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(setName).build();
                HashMap<String, Object> map = new HashMap<>();
                ProfileModel profileModel = new ProfileModel(setName,user.getPhoneNumber(),email,true,0,0);
                map.put("name", profileModel.getName());
                map.put("phone",profileModel.getPhone());
                map.put("uniqueId",profileModel.getUniqueId());
                map.put("email",profileModel.getEmail());
                map.put("profileSet",profileModel.getProfileUpdated());
                map.put("likedVideos",new ArrayList<DocumentReference>());
                map.put("myVideos",new ArrayList<DocumentReference>());
                map.put("hashTags",new HashMap<String,Integer>());
                map.put("followers",0);
                map.put("following",0);

                doc.update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(SetDetails.this,MainActivity.class));
                    }
                });
            }
        });
    }
}