package com.example.notificationmanager.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notificationmanager.FCMSend;
import com.example.notificationmanager.R;
import com.example.notificationmanager.model.NotificationModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;

public class RejectedAdapter extends FirestoreRecyclerAdapter<NotificationModel, RejectedAdapter.RejectedViewHolder> {

    public RejectedAdapter(@NonNull FirestoreRecyclerOptions<NotificationModel> options, Context context) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RejectedViewHolder holder, int position, @NonNull NotificationModel model) {

        holder.notificationTitle.setText(model.getTitle());
        holder.notificationDesc.setText(model.getDescription());

        holder.approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray array = new JSONArray();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference notificationCollection = db.collection("notifications");

                Log.d("NotificationModel", "Document ID: " + model.getDocumentId());
                Log.d("NotificationModel", "Title: " + model.getTitle());
                Log.d("NotificationModel", "Description: " + model.getDescription());
                Log.d("NotificationModel", "Department: " + model.getDepartment());
                Log.d("NotificationModel", "Gender: " + model.getGender());
                Log.d("NotificationModel", "Location: " + model.getLocation());
                Log.d("NotificationModel", "Approved: " + model.isApproved());

                Log.d("DataFromFirebaseDepartment",String.valueOf(model.getDepartment()));
                Log.d("DataFromFirebaseGender",String.valueOf(model.getGender()));
                Log.d("DataFromFirebaseLocation",String.valueOf(model.getLocation()));

//                List<String> departmentTypes = model.getDepartment();
//                List<String> genders = model.getGender();
//                List<String> workLocations = model.getLocation();

//                Query query = usersCollection
//                        .whereIn("DepartmentTypes", departmentTypes)
//                        .whereIn("Gender", genders)
//                        .whereIn("WorkLocation", workLocations);
//
//                query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        // Iterate through the query results
//                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
//                            // Get the document data as a User object
//                            User user = document.toObject(User.class);
//                            array.put(user.getFCMToken());
//                            Log.d("userFcmToken",user.getFCMToken());
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.e("TAG", "Error getting users: ", e);
//                    }
//                });

                array.put("cEzbCvUXQgeAqhJTg03Iwj:APA91bE4HZlGsmf95P7Kthvm6t3zlPwzqbIPk0jKGAN2Gm65dI5meM2tNRoQ9IunrjwsxoSaJGBIewGiETLDEu7bA8qMUJ4QCDn2VXZBeFdbP02u-A6DOhZxOipTIvL0XeL8-OFyHvxv");
                FCMSend.pushNotificationMultiple(v.getContext(),array,model.getTitle(),model.getDescription(),"please check"  );
            }
        });
    }

    @NonNull
    @Override
    public RejectedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checking_layout, parent, false);
        return new RejectedViewHolder(view);
    }

    public static class RejectedViewHolder extends RecyclerView.ViewHolder {
        TextView notificationTitle;
        TextView notificationDesc;
        ImageView approve;
        ImageView reject;
        ImageView notificationImage;

        public RejectedViewHolder(View itemView) {
            super(itemView);
            notificationTitle = itemView.findViewById(R.id.notificationTitleTT);
            notificationDesc = itemView.findViewById(R.id.notificationDesc);
            approve = itemView.findViewById(R.id.approveBt);
            reject = itemView.findViewById(R.id.rejectBt);
            notificationImage = itemView.findViewById(R.id.notificationIV);
        }
    }
}
