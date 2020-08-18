package com.nextgenlabs.mxclone.Model;

public class InitialTableModel {
    String phone;
    boolean profileSet;

    public InitialTableModel(String phone, boolean profileSet) {
        this.phone = phone;
        this.profileSet = profileSet;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isProfileSet() {
        return profileSet;
    }
}
