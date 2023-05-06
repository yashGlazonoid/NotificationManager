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
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

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
                CollectionReference usersCollection = FirebaseFirestore.getInstance().collection("Users");
                Query query = usersCollection;

                ArrayList<String> workLocations = model.getLocations();
                ArrayList<String> departmentTypes = model.getDepartments();
                ArrayList<String> genders = model.getGenders();

                ArrayList<Task<QuerySnapshot>> tasks = new ArrayList<>();

// Add each query task to the tasks list
                if (workLocations != null && !workLocations.isEmpty()) {
                    tasks.add(usersCollection.whereIn("WorkLocation", workLocations).get());
                }
                if (departmentTypes != null && !departmentTypes.isEmpty()) {
                    tasks.add(usersCollection.whereArrayContainsAny("DepartmentTypes", departmentTypes).get());
                }
                if (genders != null && !genders.isEmpty()) {
                    tasks.add(usersCollection.whereIn("Gender", genders).get());
                }

// Wait for all tasks to complete before executing any further code
                Tasks.whenAllSuccess(tasks).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                    @Override
                    public void onSuccess(List<Object> objects) {
                        JSONArray array = new JSONArray();
                        for (Object object : objects) {
                            QuerySnapshot querySnapshot = (QuerySnapshot) object;
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                User user = document.toObject(User.class);
                                array.put(user.getFCMToken());
                            }
                        }
                        Log.d("fcm", String.valueOf(array));
                        FCMSend.pushNotificationMultiple(v.getContext(), array, model.getTitle(), model.getDescription(), "please check");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Firestore", "Error getting documents: ", e);
                    }
                });


//                query.get().addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            User user = document.toObject(User.class);
//                            array.put(user.getFCMToken());
//                        }
//                        Log.d("fcm",String.valueOf(array));
//                        FCMSend.pushNotificationMultiple(v.getContext(), array, model.getTitle(), model.getDescription(), "please check");
//                    } else {
//                        Log.d("Firestore", "Error getting documents: ", task.getException());
//                    }
//                });


//                if (Boolean.TRUE.equals(model.getQuery().get("Location"))){
//                    Log.d("ModelQuery","Location "+model.getQuery().get("Location").toString());
//                    query = query.whereIn("WorkLocation",workLocations);
//                    querry++;
//                }
//                if (Boolean.TRUE.equals(model.getQuery().get("Department"))){
//                    Log.d("ModelQuery","Department "+model.getQuery().get("Department").toString());
//                    query = query.whereArrayContainsAny("DepartmentTypes",departmentTypes);
//                    querry++;
//                }
//                if (Boolean.TRUE.equals(model.getQuery().get("Gender"))){
//                    Log.d("ModelQuery","Gender "+model.getQuery().get("Gender").toString());
//                    query = query.whereIn("Gender",genders);
//                    querry++;
//                }


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

//                Log.d("RejectedAdapter", "After Query: " + query.toString());


//                query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        Log.d("Insidethis","inside the success listener");
//                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
//                            // Get the document data as a User object
//                            User user = document.toObject(User.class);
//
//                            array.put(user.getFCMToken());
////                            if (Boolean.TRUE.equals(model.getQuery().get("Location"))) {
////                                if (workLocations.contains(user.getWorkLocation())) {
////                                    Log.d("ModelQuery","Location "+model.getQuery().get("Location").toString());
////
////                                    array.put(user.getFCMToken());
////                                }
////                            }
////                            if (Boolean.TRUE.equals(model.getQuery().get("Department"))) {
////                                if (departmentTypes.containsAll(user.getDepartmentTypes())) {
////                                    Log.d("ModelQuery","Department "+model.getQuery().get("Department").toString());
////                                    array.put(user.getFCMToken());
////                                }
////                            }
////                            if (Boolean.TRUE.equals(model.getQuery().get("Gender"))) {
////                                if (genders.contains(user.getGender())) {
////                                    Log.d("ModelQuery","Gender "+model.getQuery().get("Gender").toString());
////                                    array.put(user.getFCMToken());
////                                }
////                            }
//                        }
//                        FCMSend.pushNotificationMultiple(v.getContext(), array, model.getTitle(), model.getDescription(), "please check");
//                        Log.d("fcmArray", String.valueOf(array));
//
//                        // Iterate through the query results
////                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
////                            // Get the document data as a User object
////                            User user = document.toObject(User.class);
////
////                            if (Boolean.TRUE.equals(model.getQuery().get("Location"))){
////                                Log.d("ModelQuery","Location "+model.getQuery().get("Location").toString());
////                                array.put(user.getFCMToken());
////                            }
////                            if (Boolean.TRUE.equals(model.getQuery().get("Department"))){
////                                Log.d("ModelQuery","Department "+model.getQuery().get("Department").toString());
////
////                                for (int i = 0; i < array.length(); i++) {
////                                    try {
////                                        Log.d("fcm","inside the try department");
////                                        if (!array.getString(i).equals(user.getFCMToken())) {
////                                            array.put(user.getFCMToken());
////                                            break;
////                                        }
////                                    } catch (JSONException e) {
////                                        throw new RuntimeException(e);
////                                    }
////                                }
//////                                array.put(user.getFCMToken());
////
////                            }
////                            if (Boolean.TRUE.equals(model.getQuery().get("Gender"))){
////                                Log.d("ModelQuery","Gender "+model.getQuery().get("Gender").toString());
////                                for (int i = 0; i < array.length(); i++) {
////                                    try {
////                                        Log.d("fcm","inside the try gender");
////
////                                        if (!array.getString(i).equals(user.getFCMToken())) {
////                                            array.put(user.getFCMToken());
////                                            break;
////                                        }
////                                    } catch (JSONException e) {
////                                        throw new RuntimeException(e);
////                                    }
////                                }
//////                                array.put(user.getFCMToken());
////
////                            }
////
//////                            array.put(user.getFCMToken());
//////                            Log.d("userFcmToken",user.getFCMToken());
////                        }
////                        FCMSend.pushNotificationMultiple(v.getContext(),array,model.getTitle(),model.getDescription(),"please check"  );
////                        Log.d("fcmArray",String.valueOf(array));
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d("Insidethis","inside the faliure listener");
//
//                        Log.e("TAG", "Error getting users: ", e);
//                    }
//                });

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
