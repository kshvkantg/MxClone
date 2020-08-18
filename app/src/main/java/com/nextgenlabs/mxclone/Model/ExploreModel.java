package com.nextgenlabs.mxclone.Model;

import android.net.Uri;

public class ExploreModel {
    String description;
    String Name;
    long likes;
    long shares;
    long downloads;
    String path;

    public ExploreModel(String path,String description, String name, long likes, long shares, long downloads) {
        this.path = path;
        this.description = description;
        Name = name;
        this.likes = likes;
        this.shares = shares;
        this.downloads = downloads;
    }

    public String getPath(){
        return path;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return Name;
    }

    public long getLikes() {
        return likes;
    }

    public long getShares() {
        return shares;
    }

    public long getDownloads() {
        return downloads;
    }
}
