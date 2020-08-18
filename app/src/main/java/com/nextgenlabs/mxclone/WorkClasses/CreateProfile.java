package com.nextgenlabs.mxclone.WorkClasses;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.nextgenlabs.mxclone.Model.ProfileModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class CreateProfile extends AsyncTask<Void, Void, ProfileModel> {
    private static final String TAG = "CreateProfile";
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ProfileModel profileModel) {
        super.onPostExecute(profileModel);
    }

    @Override
    protected ProfileModel doInBackground(Void... voids) {

        return null;
    }
}
