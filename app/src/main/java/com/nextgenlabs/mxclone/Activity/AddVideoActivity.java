package com.nextgenlabs.mxclone.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.StorageReference;
import com.nextgenlabs.mxclone.Database.AddVideo;
import com.nextgenlabs.mxclone.MainActivity;
import com.nextgenlabs.mxclone.R;
import com.nextgenlabs.mxclone.WorkClasses.AddPostToDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddVideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.backgroundBar));
        window.setNavigationBarColor(ContextCompat.getColor(this,R.color.backgroundBar));

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            final Uri data = Uri.parse(intent.getStringExtra("uid"));
            final TextView caption = findViewById(R.id.aAddVideo_videoDescription);
            Button postBtn = findViewById(R.id.aAddVideo_postBtn);
            ImageView imageView = findViewById(R.id.video_thumbnail);
            imageView.setImageURI(data);
            postBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String description = caption.getText().toString();
                    AddPostToDatabase toDatabase = new AddPostToDatabase();
                    toDatabase.addPostToDatabase(data, description);
                    startActivity(new Intent(AddVideoActivity.this, MainActivity.class));
                    finish();
                }
            });
        }

    }
}