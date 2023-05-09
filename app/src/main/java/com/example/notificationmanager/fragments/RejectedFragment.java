package com.example.notificationmanager.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.notificationmanager.adapter.RejectedAdapter;
import com.example.notificationmanager.databinding.FragmentRejectedBinding;
import com.example.notificationmanager.model.NotificationModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class RejectedFragment extends Fragment {
    private FragmentRejectedBinding binding;
    private ArrayList<NotificationModel> notificationModelArrayList;
    private RejectedAdapter adapter;


    public RejectedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRejectedBinding.inflate(inflater,container,false);


//        Query query = FirebaseFirestore.getInstance().collection("notifications")
//                .whereEqualTo("isApproved",false).whereEqualTo("stage", "pre-approval");
//
//        FirestoreRecyclerOptions<NotificationModel> response = new FirestoreRecyclerOptions.Builder<NotificationModel>()
//                .setQuery(query, NotificationModel.class).build();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collection = db.collection("notifications");
        Query query = collection.whereEqualTo("status","open");
        FirestoreRecyclerOptions<NotificationModel> options =
                new FirestoreRecyclerOptions.Builder<NotificationModel>()
                        .setQuery(query, NotificationModel.class)
                        .build();

        adapter = new RejectedAdapter(options,requireContext());
        binding.checkRv.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.checkRv.setAdapter(adapter);




        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    //    void getData(){
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        CollectionReference formsRef = db.collection("notifications");
//
//        formsRef.whereEqualTo("isApproved", false)
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
//                        NotificationModel form = documentSnapshot.toObject(NotificationModel.class);
//                        notificationModelArrayList.add(form);
//                    }
//                    // Do something with the formsList here
////                    adapter = new RejectedAdapter();
////                    binding.checkRv.setLayoutManager(new LinearLayoutManager(requireContext()));
////                    binding.checkRv.setAdapter(adapter);
//
//
//
//
//
//                })
//                .addOnFailureListener(e -> {
//                    // Handle any errors here
//                });
//
//    }

}