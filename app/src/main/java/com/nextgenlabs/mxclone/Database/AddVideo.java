package com.nextgenlabs.mxclone.Database;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AddVideo {
    private static final String TAG = "AddVideo";
    FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
    public StorageReference addVideo(final Uri videoUri,final String Description){
                final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        final StorageReference videoFolderReference = storageReference.child("videos/" + videoUri);

                videoFolderReference.putFile(videoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.i(TAG, "VideoStorage: " + "successfully added");
                        Log.i(TAG, "VideoStorage: " + "videoFileCreated at reference" + videoFolderReference);
                        createVideoFile(videoFolderReference.getPath(),Description);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "onFailure: " + e);

                    }
                });
                return videoFolderReference;
    }

    public void createVideoFile(final String videoFolderReference, final String description){
        Log.i(TAG, "createVideoFile: started");
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Map<String, Object> video = new HashMap<>();
        assert user != null;
        video.put("owner", "_" + user.getPhoneNumber());
                video.put("description", description);
                video.put("storageReference",videoFolderReference);
        Log.i(TAG, "createVideoFile: hash created"  );
                final ArrayList<String> hashTags = getHashTags(description);
                video.put("hashTags", hashTags);
                video.put("likes", 0);
        Log.i(TAG, "createVideoFile: sucess Achieved 50%");
                final DocumentReference doc = db.collection("videos").document(System.currentTimeMillis() + "" );
                doc.set(video).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.i(TAG, "postCreated: " + "SuccessFully" + doc);
                                addToHashTagBucket(doc,hashTags);
                                addVideosToProfile(doc);
                            }
                        });
        
    }

    public ArrayList<String> getHashTags(String videoDescription){
        int i = 0;
        boolean check = false;
        String tag = "";
        videoDescription = videoDescription + " ";
        ArrayList<String> tagList = new ArrayList<>();
        while (i < videoDescription.length()){
            if (videoDescription.charAt(i) == '#' && !check){
                check = true;
            }
            else if (videoDescription.charAt(i) != '#' && check){
                tag = tag + videoDescription.charAt(i);
            }
            else if (videoDescription.charAt(i) == ' '  && check){
                tagList.add(tag);
                tag = "";
                check = false;
            }
            else if (videoDescription.charAt(i) == '#'  && check){
                tagList.add(tag);
                tag = "";
            }
            i++;
        }
        return tagList;
    }
    public void addToHashTagBucket(final DocumentReference doc, final ArrayList<String> hashTags) {
        for (int i = 0; i < hashTags.size(); i++) {
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            final ArrayList<DocumentReference> documentReferenceArrayList = new ArrayList<>();
            documentReferenceArrayList.add(doc);
            
            final DocumentReference bucket = db.collection("hashTagBuckets").document(hashTags.get(i));
            db.runTransaction(new Transaction.Function<Void>() {
                @Nullable
                @Override
                public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                    DocumentSnapshot snapshot = transaction.get(bucket);
                    if (snapshot.get("videoRef") != null) {
                        ArrayList<DocumentReference> references = (ArrayList<DocumentReference>) snapshot.get("videoRef");
                        references.add(doc);
                        transaction.update(bucket, "videoRef", references);
                        ArrayList<String> profiles = (ArrayList<String>) snapshot.get("profiles");
                        if(!profiles.contains(fUser.getPhoneNumber())){
                            profiles.add(fUser.getPhoneNumber());
                            transaction.update(bucket,"profiles",profiles);
                        }
                    } else {
                        Map<String, Object> hash = new HashMap<>();
                        ArrayList<String> array = new ArrayList<>();
                        array.add(fUser.getPhoneNumber());
                        hash.put("videoRef", documentReferenceArrayList);
                        hash.put("profiles",array);

                        bucket.set(hash).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.i(TAG, "onUpdateSuccess: " + doc.toString() + "sucessfully Updated ");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i(TAG, "onUpdateFailure: " + e);
                            }
                        });
                    }
                    return null;
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.i(TAG, "onSuccess: buckets created");
                    addHashTagToProfile(bucket);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i(TAG, "onBucketFailure: " + e);
                }
            });
        }
    }

    public void addVideosToProfile(final DocumentReference doc) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                final DocumentReference user = db.collection("userProfiles")
                        .document(firebaseUser.getPhoneNumber() + "_");
                db.runTransaction(new Transaction.Function<Void>() {
                    @Nullable
                    @Override
                    public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                        DocumentSnapshot snapshot = transaction.get(user);
                        ArrayList<DocumentReference> references = (ArrayList<DocumentReference>) snapshot.get("myVideos");
                        references.add(doc);
                        transaction.update(user, "myVideos", references);
                        return null;
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "addVideoTo Profile successful");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "addVideoTo Profile Unsuccessful" + e);
                    }
                });

    }

    public void addHashTagToProfile(final DocumentReference doc){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final DocumentReference user = db.collection("userProfiles")
                .document(firebaseUser.getPhoneNumber() + "_");
        db.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(user);
                HashMap<String,Integer> map = (HashMap<String, Integer>) snapshot.get("hashTags");
                if(!map.containsKey(doc)){
                    map.put(doc.getPath(),1);
                }
                transaction.update(user, "hashTags", map);
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i(TAG, "addHashTag Successful");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "addHashTag unSuccessful" + e);
            }
        });
    }


}
