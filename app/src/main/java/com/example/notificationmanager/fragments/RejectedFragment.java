package com.example.notificationmanager.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.notificationmanager.adapter.RejectedAdapter;
import com.example.notificationmanager.databinding.FragmentRejectedBinding;
import com.example.notificationmanager.model.NotificationModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        // Inflate the layout for this fragment

        notificationModelArrayList = new ArrayList<>();
        getData();


        return binding.getRoot();
    }
    void getData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference formsRef = db.collection("notifications");

        formsRef.whereEqualTo("isApproved", false)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        NotificationModel form = documentSnapshot.toObject(NotificationModel.class);
                        notificationModelArrayList.add(form);
                    }
                    // Do something with the formsList here
                    adapter = new RejectedAdapter(notificationModelArrayList);
                    binding.checkRv.setLayoutManager(new LinearLayoutManager(requireContext()));
                    binding.checkRv.setAdapter(adapter);



                })
                .addOnFailureListener(e -> {
                    // Handle any errors here
                });

    }

}