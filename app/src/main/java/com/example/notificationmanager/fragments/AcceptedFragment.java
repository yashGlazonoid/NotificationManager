package com.example.notificationmanager.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notificationmanager.adapter.AcceptedAdapter;
import com.example.notificationmanager.databinding.FragmentAcceptedBinding;
import com.example.notificationmanager.model.NotificationModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

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

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collection = db.collection("notifications");
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirestoreRecyclerOptions<NotificationModel> options =
                new FirestoreRecyclerOptions.Builder<NotificationModel>()
                        .setQuery(collection, NotificationModel.class)
                        .build();

        adapter = new AcceptedAdapter(options, AcceptedFragment.this);
        binding.approvedRv.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.approvedRv.setAdapter(adapter);

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (adapter.getItemCount() == 0) {
                    binding.noNotificationsText.setVisibility(View.VISIBLE);
                } else {
                    binding.noNotificationsText.setVisibility(View.GONE);
                }
            }
        });


//        getData();


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
////        formsRef
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
//                        NotificationModel form = documentSnapshot.toObject(NotificationModel.class);
//                        Log.d("Thisis",String.valueOf(form.isApproved()));
//                        Toast.makeText(requireContext(), String.valueOf(form.isApproved()), Toast.LENGTH_SHORT).show();
//                        notificationList.add(form);
//                    }
//                    adapter = new AcceptedAdapter(notificationList);
//                    binding.approvedRv.setLayoutManager(new LinearLayoutManager(requireContext()));
//                    binding.approvedRv.setAdapter(adapter);
//
//                })
//                .addOnFailureListener(e -> {
//                    // Handle any errors here
//                });
//
//    }
}