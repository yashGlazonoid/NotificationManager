package com.example.notificationmanager.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.notificationmanager.adapter.AcceptedAdapter;
import com.example.notificationmanager.databinding.FragmentAcceptedBinding;
import com.example.notificationmanager.model.NotificationModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;


public class AcceptedFragment extends Fragment {
    private FragmentAcceptedBinding binding;
    private ArrayList<NotificationModel> notificationList;
    private AcceptedAdapter adapter;


    public AcceptedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAcceptedBinding.inflate(inflater, container, false);


        notificationList = new ArrayList<>();

        getData();


        return binding.getRoot();
    }

    void getData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference formsRef = db.collection("notifications");

        formsRef.whereEqualTo("isApproved", false)
//        formsRef
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        NotificationModel form = documentSnapshot.toObject(NotificationModel.class);
                        Log.d("Thisis",String.valueOf(form.isApproved()));
                        Toast.makeText(requireContext(), String.valueOf(form.isApproved()), Toast.LENGTH_SHORT).show();
                        notificationList.add(form);
                    }
                    adapter = new AcceptedAdapter(notificationList);
                    binding.approvedRv.setLayoutManager(new LinearLayoutManager(requireContext()));
                    binding.approvedRv.setAdapter(adapter);

                })
                .addOnFailureListener(e -> {
                    // Handle any errors here
                });

    }
}