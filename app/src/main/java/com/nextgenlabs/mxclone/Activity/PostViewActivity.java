package com.nextgenlabs.mxclone.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nextgenlabs.mxclone.R;

import java.net.URL;

public class PostViewActivity extends AppCompatActivity {
    private static final String TAG = "PostViewActivity";
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.backgroundBar));
        window.setNavigationBarColor(ContextCompat.getColor(this,R.color.backgroundBar));
        TextView song = findViewById(R.id.aPostView_musicName);
        song.setSelected(true);

        final VideoView videoView = findViewById(R.id.aPostView_videoView);

        Intent intent = getIntent();
        final String path = intent.getStringExtra("path");
        if(path != null) {
            FirebaseStorage storageRef = FirebaseStorage.getInstance();
            StorageReference videoRef = storageRef.getReference().child(path);
            final Uri[] data = new Uri[1];
            videoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                   MediaController mediaController = new MediaController(PostViewActivity.this);
                   mediaController.setAnchorView(videoView);
                   videoView.setMediaController(mediaController);
                   videoView.setVideoURI(uri);

                   videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                       @Override
                       public void onPrepared(MediaPlayer mp) {
                           mp.setLooping(true);
                           videoView.start();
                       }
                   });
                    Log.i(TAG, "previous onSuccess: "+uri);
                }
            });
//            videoRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
//                @Override
//                public void onComplete(@NonNull Task<Uri> task) {
//                    task.addOnCompleteListener(new OnCompleteListener<Uri>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Uri> task) {
//                            videoView.setVideoURI(data[0]);
//                            Log.i(TAG, "onSuccess: Video Uri creation" + data[0]);
//                            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                                @Override
//                                public void onPrepared(MediaPlayer mp) {
//                                    mp.setLooping(true);
//                                    videoView.start();
//                                }
//                            });
//                        }
//                    });
//                }
//            });


            final FirebaseFirestore db = FirebaseFirestore.getInstance();

            final TextView likes = findViewById(R.id.aPostView_LlikeCount);
            final TextView description = findViewById(R.id.aPostView_postDescription);
            final TextView profile = findViewById(R.id.aPostView_accountName);

            db.collection("videos").whereEqualTo("storageReference", path).limit(1)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                Log.e(TAG, "onEvent: get Credentials ", error);
                            }
                            assert value != null;
                            for (QueryDocumentSnapshot doc : value) {
                                likes.setText(String.valueOf(doc.get("likes")));
                                description.setText(doc.getString("description"));
                                profile.setText(doc.getString("owner"));
                            }
                        }
                    });
        }
    }
}