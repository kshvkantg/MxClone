package com.nextgenlabs.mxclone.WorkClasses;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nextgenlabs.mxclone.Database.AddVideo;
import com.nextgenlabs.mxclone.Model.CreatePostModel;
import com.nextgenlabs.mxclone.Model.CreateVideoStorageModel;

import androidx.annotation.NonNull;

public class CreateVideoStorage extends AsyncTask<Void, Integer ,Void> {
    private static final String TAG = "CreateVideoStorage";
    StorageReference storageReference;
    Uri data;
    String description;

    public CreateVideoStorage(StorageReference storageReference, Uri data, String description) {
        this.storageReference = storageReference;
        this.data = data;
        this.description = description;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Log.i(TAG, "doInBackground: called");
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        final StorageReference videoFolderReference = storageReference.child("videos/" + data + '_'+ firebaseUser.getPhoneNumber());

        videoFolderReference.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.i(TAG, "VideoStorage: " + "successfully added");
                Log.i(TAG, "VideoStorage: " + "videoFileCreated");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure: " + e);

            }
        });

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void v) {
        super.onPostExecute(v);
//        CreatePostModel model = createPostModel;
//        StorageReference storageReference = model.getStorageReference();
//        Uri data = model.getData();
//        String description = model.getDescription();
//
        //new CreatePost(storageReference,data,description).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        Log.i(TAG, "onPostExecute: create post called");
    }

}