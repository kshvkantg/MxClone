package com.nextgenlabs.mxclone.WorkClasses;

import android.net.Uri;
import com.nextgenlabs.mxclone.Database.AddVideo;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddPostToDatabase {
    public void addPostToDatabase(final Uri data , final String description){
        final ExecutorService service = Executors.newSingleThreadScheduledExecutor();
        Thread createVideo = new Thread(new Runnable() {
            @Override
            public void run() {
                AddVideo addVideo = new AddVideo();
                addVideo.addVideo(data,description);
            }
        });
        service.submit(createVideo);
    }
}
