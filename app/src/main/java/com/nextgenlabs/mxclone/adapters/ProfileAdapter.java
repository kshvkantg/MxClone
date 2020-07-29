package com.nextgenlabs.mxclone.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.nextgenlabs.mxclone.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {
    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_f_like, parent, false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 30;
    }

    public static class ProfileViewHolder extends RecyclerView.ViewHolder {
        VideoView videoView;
        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.fProfile_video);
        }
    }
}
