package com.nextgenlabs.mxclone.adapters;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nextgenlabs.mxclone.Activity.PostViewActivity;
import com.nextgenlabs.mxclone.R;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ExploreFragAdapter extends RecyclerView.Adapter<ExploreFragAdapter.PostViewHolder> {
    public ExploreFragAdapter(Context context) {
        this.context = context;
    }

    Context context;
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_f_explore_recycler_view, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.postImage.setImageResource(R.drawable.ic_launcher_background);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostViewActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 8;
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout layout;
        ImageView profileImage;
        TextView profileName,postDescription;
        ImageView postImage;
        TextView shareCount,likeCount,downloadCount;


        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.fExplore_content_layout);
            profileImage = itemView.findViewById(R.id.fExplore_profile_image);
            profileName = itemView.findViewById(R.id.fExplore_account_name);
            postDescription = itemView.findViewById(R.id.fExplore_post_description);
            postImage = itemView.findViewById(R.id.fExplore_post_image);
            shareCount = itemView.findViewById(R.id.fExplore_post_shares);
            likeCount = itemView.findViewById(R.id.fExplore_post_Likes);
            downloadCount = itemView.findViewById(R.id.fExplore_post_downloads);

        }
    }

}
