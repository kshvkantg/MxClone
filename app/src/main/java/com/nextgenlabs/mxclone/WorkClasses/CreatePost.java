package com.nextgenlabs.mxclone.WorkClasses;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.nextgenlabs.mxclone.Database.AddVideo;
import com.nextgenlabs.mxclone.Model.CreatePostModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreatePost extends AsyncTask<Void,Void,Void> {
    private static final String TAG = "CreatePost";
    StorageReference storageReference;
    Uri data;
    String description;

    public CreatePost(StorageReference storageReference, Uri data, String description) {
        this.storageReference = storageReference;
        this.data = data;
        this.description = description;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Log.i(TAG, "createVideoFile: started");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Map<String, Object> video = new HashMap<>();
        assert user != null;
        video.put("owner", user.getDisplayName() + "_" + user.getPhoneNumber());
        video.put("description", description);
        video.put("storageReference",storageReference);
        Log.i(TAG, "createVideoFile: hash created"  );
        final ArrayList<String> hashTags = new ArrayList<>();
        video.put("hashTags", hashTags);
        video.put("likes", 0);
        Log.i(TAG, "createVideoFile: sucess Achieved 50%");
        final DocumentReference doc = db.collection("videos").document(System.currentTimeMillis()+ "_" );
        doc.set(video).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i(TAG, "postCreated: " + "SuccessFully" + doc);
            }
        });

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

}