package com.nextgenlabs.mxclone.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nextgenlabs.mxclone.Model.ExploreModel;
import com.nextgenlabs.mxclone.R;
import com.nextgenlabs.mxclone.adapters.ExploreFragAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExploreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExploreFragment extends Fragment {

    private static final String TAG = "ExploreFragment";
    boolean isScrolling = false;
    int currentItems,totalItems,scrolledOutItems;
    LinearLayoutManager manager;
    ArrayList<ExploreModel> modelArrayList = new ArrayList<>();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ExploreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExporeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExploreFragment newInstance(String param1, String param2) {
        ExploreFragment fragment = new ExploreFragment();
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
        Context context = getContext();
        View view =  inflater.inflate(R.layout.fragment_explore, container, false);
        TextView textView = view.findViewById(R.id.toolbar_text);
        textView.setText("Explore");
        setData(view,context);
        return view;
    }
    public void setData(final View view, final Context context){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("videos").whereGreaterThanOrEqualTo("likes",0).limit(25)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Log.e(TAG, "Fetching Data: ",error );
                        }
                        final DocumentSnapshot[] last = new DocumentSnapshot[1];
                        assert value != null;
                        for (QueryDocumentSnapshot documentSnapshot : value){
                            last[0] = documentSnapshot;
                            ExploreModel model = new ExploreModel(
                                    documentSnapshot.getString("storageReference"),
                                    documentSnapshot.getString("description"),
                                    documentSnapshot.getString("owner"),
                                    (Long) documentSnapshot.get("likes"),
                                    0,0
                            );
                             modelArrayList.add(model);
                        }
                        final DocumentSnapshot[] lastDocument = new DocumentSnapshot[1];
                        lastDocument[0] = last[0];
                        RecyclerView recyclerView = view.findViewById(R.id.fExplore_recyclerView);
                        final ExploreFragAdapter adapter = new ExploreFragAdapter(context,modelArrayList);
                        recyclerView.setAdapter(adapter);
                        manager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                        recyclerView.setLayoutManager(manager);

//                        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                            @Override
//                            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                                super.onScrollStateChanged(recyclerView, newState);
//                                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
//                                    isScrolling = true;
//                                }
//                            }
//
//                            @Override
//                            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                                super.onScrolled(recyclerView, dx, dy);
//                                currentItems = manager.getChildCount();
//                                totalItems = manager.getItemCount();
//                                scrolledOutItems = manager.findFirstCompletelyVisibleItemPosition();
//                                if(isScrolling && currentItems + scrolledOutItems == totalItems){
//                                    isScrolling = false;
//                                    fetchData(db, lastDocument[0], adapter);
//                                }
//                            }
//                        });
                    }
                });
    }

    private DocumentSnapshot fetchData(FirebaseFirestore db,DocumentSnapshot snapshot, final ExploreFragAdapter adapter) {
        final DocumentSnapshot[] returnDoc = new DocumentSnapshot[1];
        db.collection("videos").whereGreaterThanOrEqualTo("likes",0).limit(3).startAfter(snapshot)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Log.e(TAG, "Fetching Data: ",error );
                        }
                        assert value != null;
                        DocumentSnapshot[] last = new DocumentSnapshot[1];
                        for (QueryDocumentSnapshot documentSnapshot : value){
                            last[0] = documentSnapshot;
                            ExploreModel model = new ExploreModel(
                                    documentSnapshot.getString("storageReference"),
                                    documentSnapshot.getString("description"),
                                    documentSnapshot.getString("owner"),
                                    (Long) documentSnapshot.get("likes"),
                                    0,0
                            );
                            returnDoc[0] = last[0];
                            modelArrayList.add(model);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
        return returnDoc[0];
    }
}








