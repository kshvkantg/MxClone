package com.nextgenlabs.mxclone.adapters;
import android.content.Context;
import android.content.Intent;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nextgenlabs.mxclone.Activity.PostViewActivity;
import com.nextgenlabs.mxclone.Model.ExploreModel;
import com.nextgenlabs.mxclone.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ExploreFragAdapter extends RecyclerView.Adapter<ExploreFragAdapter.PostViewHolder> {
    private static final String TAG = "ExploreFragAdapter";

    public ExploreFragAdapter(Context context, ArrayList<ExploreModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    ArrayList<ExploreModel> arrayList;
    Context context;
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_f_explore_recycler_view, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostViewHolder holder, final int position) {
       final ExploreModel exploreModel = arrayList.get(position);
       holder.profileName.setText(exploreModel.getName());
       holder.postDescription.setText(exploreModel.getDescription());
       holder.likeCount.setText(String.valueOf(exploreModel.getLikes()));
       holder.downloadCount.setText(String.valueOf(exploreModel.getLikes()));
       holder.shareCount.setText(String.valueOf(exploreModel.getLikes()));
       holder.layout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(context,PostViewActivity.class);
               intent.putExtra("path",exploreModel.getPath());
               context.startActivity(intent);
           }
       });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
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
