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
import com.example.notificationmanager.model.User;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;

import java.util.ArrayList;

public class RejectedAdapter extends FirestoreRecyclerAdapter<NotificationModel, RejectedAdapter.RejectedViewHolder> {

    public RejectedAdapter(@NonNull FirestoreRecyclerOptions<NotificationModel> options, Context context) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RejectedViewHolder holder, int position, @NonNull NotificationModel model) {


        DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAbsoluteAdapterPosition());
        model.setId(snapshot.getId());

        holder.notificationTitle.setText(model.getTitle());
        holder.notificationDesc.setText(model.getDescription());

        holder.approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray array = new JSONArray();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference userCollection = db.collection("Users");

                Query query = userCollection;

                ArrayList<String> departmentTypes = model.getDepartments();
                ArrayList<String> genders = model.getGenders();
                ArrayList<String> workLocations = model.getLocations();

                Log.d("RejectedAdapter", "Before Query: " + query.toString());

                if (Boolean.TRUE.equals(model.getQuery().get("Location"))){
                    Log.d("ModelQuery","Location "+model.getQuery().get("Location").toString());
                    query = query.whereIn("WorkLocation",workLocations);
                }
                if (Boolean.TRUE.equals(model.getQuery().get("Department"))){
                    Log.d("ModelQuery","Department "+model.getQuery().get("Department").toString());
                    query = query.whereArrayContainsAny("DepartmentTypes",departmentTypes);
                }
                if (Boolean.TRUE.equals(model.getQuery().get("Gender"))){
                    Log.d("ModelQuery","Gender "+model.getQuery().get("Gender").toString());
                    query = query.whereIn("Gender",genders);
                }


//                if (Boolean.TRUE.equals(model.getQuery().get("Location"))){
//                    Log.d("ModelQuery","Location "+model.getQuery().get("Location").toString());
//                    query = query.whereIn("WorkLocation",workLocations);
//                }
//                if (Boolean.TRUE.equals(model.getQuery().get("Department"))){
//                    Log.d("ModelQuery","Department "+model.getQuery().get("Department").toString());
//                    query = query.whereIn("DepartmentTypes",departmentTypes);
//                }
//                if (Boolean.TRUE.equals(model.getQuery().get("Gender"))){
//                    Log.d("ModelQuery","Gender "+model.getQuery().get("Gender").toString());
//                    query=query.whereIn("Gender",genders);
//                }

                Log.d("RejectedAdapter", "After Query: " + query.toString());

//                Log.d("ModelQuery",String.valueOf(userCollection));

//                if (!model.getDepartments().isEmpty()){
//                    query.whereIn("DepartmentTypes",departmentTypes);
//                }
//                if (!model.getLocations().isEmpty()){
//                    query.whereIn("WorkLocation",workLocations);
//                }
//                if (!model.getLocations().isEmpty()){
//                    query.whereIn("WorkLocation",workLocations);
//                }


                query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.d("Insidethis","inside the success listener");
                        // Iterate through the query results
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            // Get the document data as a User object
                            User user = document.toObject(User.class);
                            array.put(user.getFCMToken());
                            Log.d("userFcmToken",user.getFCMToken());
                        }
                        FCMSend.pushNotificationMultiple(v.getContext(),array,model.getTitle(),model.getDescription(),"please check"  );
                        Log.d("fcmArray",String.valueOf(array));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Insidethis","inside the faliure listener");

                        Log.e("TAG", "Error getting users: ", e);
                    }
                });

//                array.put("cEzbCvUXQgeAqhJTg03Iwj:APA91bE4HZlGsmf95P7Kthvm6t3zlPwzqbIPk0jKGAN2Gm65dI5meM2tNRoQ9IunrjwsxoSaJGBIewGiETLDEu7bA8qMUJ4QCDn2VXZBeFdbP02u-A6DOhZxOipTIvL0XeL8-OFyHvxv");

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
