package com.nextgenlabs.mxclone.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.nextgenlabs.mxclone.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommentSectionAdapter extends RecyclerView.Adapter<CommentSectionAdapter.CommentViewHolder> {
    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_comment_recycler_view, parent, false);
        return new CommentViewHolder(view);
    }

    public void onBindViewHolder(@NonNull final CommentViewHolder holder, int position) {
        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.likeButton.setImageResource(R.drawable.checked_heart_button);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView profileNAme,comment;
        ImageButton likeButton;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.commenterProfile);
            profileNAme = itemView.findViewById(R.id.commenterName);
            comment = itemView.findViewById(R.id.comment);
            likeButton = itemView.findViewById(R.id.commentSecLikeButton);
        }
    }

}
