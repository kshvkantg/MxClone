package com.nextgenlabs.mxclone.Model;

import android.content.Context;
import android.net.Uri;
import com.google.firebase.storage.StorageReference;

public class CreateVideoStorageModel {
    static StorageReference storageReference;
    static Uri data;
    static String description;
    Context context;

    public CreateVideoStorageModel(StorageReference storageReference, Uri data, String description, Context context) {
        CreateVideoStorageModel.storageReference = storageReference;
        CreateVideoStorageModel.data = data;
        CreateVideoStorageModel.description = description;
        this.context = context;
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

    public Context getContext() {
        return context;
    }
}
