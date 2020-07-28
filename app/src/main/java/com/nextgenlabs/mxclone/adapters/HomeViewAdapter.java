package com.nextgenlabs.mxclone.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;

import com.nextgenlabs.mxclone.MainActivity;
import com.nextgenlabs.mxclone.R;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

public class HomeViewAdapter extends RecyclerView.Adapter<HomeViewAdapter.HomeViewHolder> {
    public HomeViewAdapter(Context context) {
        this.context = context;
    }

    Context context;

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_frags_content, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeViewHolder holder, int position) {
        holder.songName.setSelected(true);
        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.likeBtn.setImageResource(R.drawable.checked_heart_button);
            }
        });
        holder.commentBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                final Dialog commentDialog = new Dialog(context,android.R.style.Theme_Black_NoTitleBar );
                Objects.requireNonNull(commentDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.argb(80, 0, 0, 0)));
                commentDialog.setContentView(R.layout.fragment_comment);
                commentDialog.setContentView(R.layout.fragment_comment);
                commentDialog.setCancelable(true);
                commentDialog.show();

                commentDialog.findViewById(R.id.commentsBackBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        commentDialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public static class HomeViewHolder extends RecyclerView.ViewHolder {
        VideoView video;
        ImageButton profileBtn,likeBtn,commentBtn,shareBtn,songButton;
        TextView likeCount,commentCount,shareCount;
        TextView songName,postDescription,accountName;
        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            video = itemView.findViewById(R.id.fHome_videoView);
            profileBtn = itemView.findViewById(R.id.f_home_profile);
            likeBtn = itemView.findViewById(R.id.fHome_likeBtn);
            commentBtn = itemView.findViewById(R.id.fHome_commentBtn);
            shareBtn = itemView.findViewById(R.id.fHome_shareButton);
            songButton = itemView.findViewById(R.id.fHome_songBtn);
            likeCount = itemView.findViewById(R.id.fHome_LlikeCount);
            commentCount = itemView.findViewById(R.id.fHome_commentCount);
            shareCount = itemView.findViewById(R.id.fHome_shareCount);
            songName = itemView.findViewById(R.id.fHome_musicName);
            postDescription = itemView.findViewById(R.id.fHome_postDescription);
            accountName = itemView.findViewById(R.id.fHome_accountName);
        }
    }
}
