package com.example.notificationmanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notificationmanager.FCMSend;
import com.example.notificationmanager.R;
import com.example.notificationmanager.activity.LoginActivity;
import com.example.notificationmanager.model.NotificationModel;
import com.example.notificationmanager.model.User;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        holder.mainNotificationTitle.setText(model.getTitle() + model.getDescription());

        if (Boolean.TRUE.equals(model.getQuery().get("Department"))){
            holder.departmentHelper.setVisibility(View.VISIBLE);
            holder.department.setVisibility(View.VISIBLE);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < model.getDepartments().size(); i++) {
                sb.append(model.getDepartments().get(i));
                if (i != model.getDepartments().size() - 1) {
                    sb.append(", ");
                }
            }
            holder.department.setText(sb.toString());
        }
        if (Boolean.TRUE.equals(model.getQuery().get("Gender"))){
            holder.genderHelper.setVisibility(View.VISIBLE);
            holder.gender.setVisibility(View.VISIBLE);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < model.getGenders().size(); i++) {
                sb.append(model.getGenders().get(i));
                if (i != model.getGenders().size() - 1) {
                    sb.append(", ");
                }
            }
            holder.gender.setText(sb.toString());
        }

        if (Boolean.TRUE.equals(model.getQuery().get("Location"))){
            holder.locationHelper.setVisibility(View.VISIBLE);
            holder.location.setVisibility(View.VISIBLE);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < model.getLocations().size(); i++) {
                sb.append(model.getLocations().get(i));
                if (i != model.getLocations().size() - 1) {
                    sb.append(", ");
                }
            }
            holder.location.setText(sb.toString());
        }
        if (Boolean.TRUE.equals(model.getQuery().get("age"))){
            holder.ageHelper.setVisibility(View.VISIBLE);
            holder.age.setVisibility(View.VISIBLE);
            holder.age.setText(model.getAgeShouldBe() + " " + model.getAge());

        }
        if (Boolean.TRUE.equals(model.getQuery().get("date"))){
            holder.date.setVisibility(View.VISIBLE);
            holder.dateHelper.setVisibility(View.VISIBLE);
            holder.date.setText("From "+model.getDateStartFrom() + " To " + model.getDateTo() );

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionSet transitionSet = new TransitionSet();
                transitionSet.addTransition(new AutoTransition());
                transitionSet.addTransition(new ChangeBounds());

                if (holder.secondaryCardView.getVisibility() == View.VISIBLE) {
                    TransitionManager.beginDelayedTransition(holder.mainCardView, transitionSet);
                    holder.secondaryCardView.setVisibility(View.GONE);
                } else {
                    TransitionManager.beginDelayedTransition(holder.mainCardView, transitionSet);
                    holder.secondaryCardView.setVisibility(View.VISIBLE);
                }

            }
        });

        holder.rejectBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference docRef = db.collection("notifications").document(model.getDocumentId());
                Map<String, Object> updates = new HashMap<>();
                updates.put("status", "pending");
                docRef.update(updates)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("rejected", "Document updated successfully!");
                                JSONArray array = new JSONArray();
                                array.put(model.getUserDetails().get("userFcmToken"));
                                System.out.println(array);
                                FCMSend.pushNotificationMultiple(v.getContext(), array, "Your notification has been rejected", "Please check the faults and update it", "notificationUpdater");
//                                FCMSend.pushNotification(v.getContext(),model.getUserFcmToken(),"Your notification has been rejected","check the notification section and update it","notificationUpdater");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("rejected", "Error updating document", e);
                            }
                        });

            }
        });

        holder.approveBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray array = new JSONArray();
                CollectionReference usersCollection = FirebaseFirestore.getInstance().collection("Users");
                Query query = usersCollection;

                ArrayList<String> workLocations = model.getLocations();
                ArrayList<String> departmentTypes = model.getDepartments();
                ArrayList<String> genders = model.getGenders();



                ArrayList<Task<QuerySnapshot>> tasks = new ArrayList<>();

                if (workLocations != null && !workLocations.isEmpty()) {
                    Log.d("WorkLocations",String.valueOf(workLocations));
                    tasks.add(usersCollection.whereIn("WorkLocation", workLocations).get());
                }
                if (departmentTypes != null && !departmentTypes.isEmpty()) {
                    Log.d("WorkLocations",String.valueOf(departmentTypes));
                    tasks.add(usersCollection.whereArrayContainsAny("DepartmentTypes", departmentTypes).get());
                }
                if (genders != null && !genders.isEmpty()) {
                    Log.d("WorkLocations",String.valueOf(genders));
                    tasks.add(usersCollection.whereIn("Gender", genders).get());
                }
                Tasks.whenAllSuccess(tasks).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                    @Override
                    public void onSuccess(List<Object> objects) {
                        JSONArray array = new JSONArray();
                        ArrayList<String> tokenList = new ArrayList<>();
                        for (Object object : objects) {
                            QuerySnapshot querySnapshot = (QuerySnapshot) object;
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                User user = document.toObject(User.class);

                                if (tokenList.contains(user.getFCMToken())){
                                    Log.d("fcm","Already in");
                                }
                                else{
                                    tokenList.add(user.getFCMToken());
                                }
//                                array.put(user.getFCMToken());
                            }
                        }
                        for (String token : tokenList) {
                            array.put(token);
                        }

                        System.out.println(array);
//                        Gson gson = new Gson();
//                        array = gson.toJsonTree(tokenList).getAsJsonArray();
//                        System.out.println(array);

//                        WriteBatch batch = FirebaseFirestore.getInstance().batch();
                        Log.d("fcm", String.valueOf(array));
                        System.out.println(array);
                        FCMSend.pushNotificationMultiple(v.getContext(), array, model.getTitle(), model.getDescription(), "notificationUpdater");
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        if (mAuth.getCurrentUser() != null) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            DocumentReference docRef = db.collection("notifications").document(model.getDocumentId());
                            Map<String, Object> updates = new HashMap<>();
                            updates.put("status", "close");
                            docRef.update(updates)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("rejected", "Document updated successfully!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("rejected", "Error updating document", e);
                                        }
                                    });
                        } else {
                            v.getContext().startActivity(new Intent(v.getContext(), LoginActivity.class));
                        }

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
        TextView mainNotificationTitle;
        ImageView notificationImage;
        CardView mainCardView;
        CardView secondaryCardView;

        TextView location,department,gender,age;
        TextView departmentHelper,locationHelper,genderHelper,ageHelper,dateHelper,date;
        Button approveBt , rejectBt;

        public RejectedViewHolder(View itemView) {
            super(itemView);
            notificationTitle = itemView.findViewById(R.id.notificationTitle);
            notificationDesc = itemView.findViewById(R.id.notificationDesc);
            notificationImage = itemView.findViewById(R.id.notificationImage);
            mainNotificationTitle = itemView.findViewById(R.id.mainNotificationTitle);
            mainCardView = itemView.findViewById(R.id.mainCardView);
            secondaryCardView = itemView.findViewById(R.id.secondaryCardView);

            location = itemView.findViewById(R.id.notificationLocation);
            department = itemView.findViewById(R.id.notificationDepartments);
            gender = itemView.findViewById(R.id.notificationGender);
            age = itemView.findViewById(R.id.notificationAge);

            locationHelper = itemView.findViewById(R.id.locationHelper);
            genderHelper = itemView.findViewById(R.id.genderHelper);
            ageHelper = itemView.findViewById(R.id.ageHelper);
            departmentHelper = itemView.findViewById(R.id.departmentHelper);
            dateHelper = itemView.findViewById(R.id.dateHelper);
            date = itemView.findViewById(R.id.notificationDate);

            approveBt = itemView.findViewById(R.id.acceptBt);
            rejectBt = itemView.findViewById(R.id.rejectBt);

        }
    }
}
