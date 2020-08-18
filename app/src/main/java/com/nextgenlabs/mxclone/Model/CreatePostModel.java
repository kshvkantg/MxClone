package com.nextgenlabs.mxclone.Model;

import android.net.Uri;
import com.google.firebase.storage.StorageReference;

public class CreatePostModel {
    static StorageReference storageReference;
    static Uri data;
    static String description;

    public CreatePostModel(StorageReference storageReference, Uri data, String description) {
        CreatePostModel.storageReference = storageReference;
        CreatePostModel.data = data;
        CreatePostModel.description = description;
    }

    public StorageReference getStorageReference() {
        return storageReference;
    }

    public Uri getData() {
        return data;
    }

    public String getDescription() {
        return description;
    }
}
