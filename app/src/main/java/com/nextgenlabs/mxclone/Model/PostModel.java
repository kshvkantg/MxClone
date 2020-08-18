package com.nextgenlabs.mxclone.Model;

public class PostModel {
    String path;
    String name;
    String description;
    long likeCount;
    long commentCount;
    long shareCount;
    String ownerUniqueId;

    public PostModel(String path, String name, String description, long likeCount, long commentCount, long shareCount, String ownerUniqueId) {
        this.path = path;
        this.name = name;
        this.description = description;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.shareCount = shareCount;
        this.ownerUniqueId = ownerUniqueId;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public long getCommentCount() {
        return commentCount;
    }

    public long getShareCount() {
        return shareCount;
    }

    public String getOwnerUniqueId() {
        return ownerUniqueId;
    }
}
