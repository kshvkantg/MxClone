package com.nextgenlabs.mxclone.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.nextgenlabs.mxclone.Model.PostModel;
import com.nextgenlabs.mxclone.R;
import com.nextgenlabs.mxclone.adapters.HomeViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrendingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrendingFragment extends Fragment {
    Context context = getContext();
    ArrayList<PostModel> models = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    HomeViewAdapter adapter = new HomeViewAdapter(models, context);
    boolean isScrolling = false;
    int currentItems,totalItems,scrolledOutItems;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "Trending Frag";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TrendingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TrendingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrendingFragment newInstance(String param1, String param2) {
        TrendingFragment fragment = new TrendingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_trending, container, false);
        getDocuments();
        RecyclerView recyclerView = view.findViewById(R.id.fTrending_recyclerView);
        layoutManager  = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = layoutManager.getChildCount();
                totalItems = layoutManager.getItemCount();
                scrolledOutItems = layoutManager.getBaseline();
                if(isScrolling && currentItems + scrolledOutItems == totalItems){
                    isScrolling = false;
                }
            }
        });
        return view;
    }
    public void getDocuments(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ArrayList<DocumentReference> referenceList = new ArrayList<>();
        db.collection("hashTagBuckets").whereArrayContains("profiles",user.getPhoneNumber()).limit(2)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            Log.i(TAG, "onEvent: getting Arrays throws Exception" + error.toString());
                        }
                        for (final QueryDocumentSnapshot doc : value){
                            final ArrayList<DocumentReference> arrayList = (ArrayList<DocumentReference>) doc.getData().get("videoRef");
                            referenceList.addAll(arrayList);
                            int start = 0;

                            if(arrayList.size() - 5 > 0)
                                start = arrayList.size() - 5;

                            for (int i = start ; i < arrayList.size();i++){
                                final DocumentReference reference = arrayList.get(i);
                                db.runTransaction(new Transaction.Function<PostModel>() {
                                    @Nullable
                                    @Override
                                    public PostModel apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                                        DocumentSnapshot snapshot = transaction.get(reference);
                                        return new PostModel(snapshot.getString("storageReference"),
                                                snapshot.getString("owner"),
                                                snapshot.getString("description"),
                                                snapshot.getLong("likes"),0,0,null);
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<PostModel>() {
                                    @Override
                                    public void onSuccess(PostModel postModel) {
                                        models.add(postModel);
                                        adapter.notifyDataSetChanged();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e(TAG, "onFailure: ",e );
                                    }
                                });
                            }

                        }
                    }
                });
        db.collection("videos").orderBy("likes").limit(5).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){

                }else {
                    for (QueryDocumentSnapshot snapshot : value){
                        PostModel postModel = new  PostModel(snapshot.getString("storageReference"),
                                snapshot.getString("owner"),
                                snapshot.getString("description"),
                                snapshot.getLong("likes"),0,0,null);
                        models.add(postModel);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}