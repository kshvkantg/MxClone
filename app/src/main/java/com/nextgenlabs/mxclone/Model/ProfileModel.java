package com.nextgenlabs.mxclone.Model;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class ProfileModel {
    String name;
    String phone;
    String email;
    String UniqueId;
    Boolean profileUpdated;
    int followers;
    int following;

    ArrayList<DocumentReference> likedVideos;
    ArrayList<DocumentReference> myVideos;
    ArrayList<CollectionReference> hashTags;

    public ProfileModel(String name, String phone, String email,Boolean profileUpdated,int followers,int following) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.profileUpdated = profileUpdated;
        this.UniqueId = name.substring(0,3) + phone.substring(1) + System.currentTimeMillis();
        this.followers = followers;
        this.following = following;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getUniqueId() {
        return UniqueId;
    }

    public Boolean getProfileUpdated() {
        return profileUpdated;
    }
    public int getFollowers() {
        return followers;
    }

    public int getFollowing() {
        return following;
    }

}
