package com.example.notificationmanager.adapter;

import android.content.Context;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RejectedAdapter extends FirestoreRecyclerAdapter<NotificationModel, RejectedAdapter.RejectedViewHolder> {

    private StringBuilder mainString= new StringBuilder();
    public RejectedAdapter(@NonNull FirestoreRecyclerOptions<NotificationModel> options, Context context) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RejectedViewHolder holder, int position, @NonNull NotificationModel model) {


        DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAbsoluteAdapterPosition());
        model.setId(snapshot.getId());

        holder.notificationTitle.setText(model.getTitle());
        holder.notificationDesc.setText(model.getDescription());
//        holder.mainNotificationTitle.setText(model.getTitle() + model.getDescription());
        mainString.setLength(0);
        mainString.append("This notice is for ");

        if (Boolean.TRUE.equals(model.getQuery().get("Gender"))) {
            holder.genderHelper.setVisibility(View.VISIBLE);
            holder.gender.setVisibility(View.VISIBLE);
            List<String> genders = model.getGenders();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < genders.size(); i++) {
                sb.append(genders.get(i));
                if (genders.size() == 2 && i == 0) {
                    sb.append(" and ");
                } else if (i != genders.size() - 1) {
                    sb.append(", ");
                }
            }
            holder.gender.setText(sb);
            mainString.append(sb.toString());
            mainString.append(" employees ");
        } else {
            mainString.append("all employees ");
        }

        if (Boolean.TRUE.equals(model.getQuery().get("Department"))) {
            if (!Boolean.TRUE.equals(model.getQuery().get("Gender"))) {
                mainString.append("who ");
            }
            mainString.append("work in ");

            holder.departmentHelper.setVisibility(View.VISIBLE);
            holder.department.setVisibility(View.VISIBLE);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < model.getDepartments().size(); i++) {
                sb.append(model.getDepartments().get(i));
                if (i != model.getDepartments().size() - 1) {
                    sb.append(", ");
                }
            }
            holder.department.setText(sb);
            mainString.append(sb.toString());
            mainString.append(" department(s) ");
        }

        if (Boolean.TRUE.equals(model.getQuery().get("Location"))) {
            mainString.append("at ");

            holder.locationHelper.setVisibility(View.VISIBLE);
            holder.location.setVisibility(View.VISIBLE);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < model.getLocations().size(); i++) {
                sb.append(model.getLocations().get(i));
                if (i != model.getLocations().size() - 1) {
                    sb.append(", ");
                }
            }
            holder.location.setText(sb);
            mainString.append(sb.toString());
            mainString.append(" location(s) ");
        }

        if (Boolean.TRUE.equals(model.getQuery().get("age"))) {
            holder.ageHelper.setVisibility(View.VISIBLE);
            holder.age.setVisibility(View.VISIBLE);
            holder.age.setText("above " + model.getAge() + " years old");
            if (!Boolean.TRUE.equals(model.getQuery().get("Gender")) && !Boolean.TRUE.equals(model.getQuery().get("Department"))) {
                mainString.append("all employees ");
            }
            mainString.append("who are above ");
            mainString.append(model.getAge());
            mainString.append(" years old ");
        }

        if (Boolean.TRUE.equals(model.getQuery().get("date"))) {
            holder.date.setVisibility(View.VISIBLE);
            holder.dateHelper.setVisibility(View.VISIBLE);
            long startDate = Long.parseLong(model.getDateStartFrom());
            long toDate = Long.parseLong(model.getDateTo());
            Date date = new Date(startDate);
            Date dateTo = new Date(toDate);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String formattedDateStart = sdf.format(date);
            String formattedDateTo = sdf.format(dateTo);

            holder.date.setText("from " + formattedDateStart + " to " + formattedDateTo);
            mainString.append("joined between ");
            mainString.append(formattedDateStart);
            mainString.append(" and ");
            mainString.append(formattedDateTo);
            mainString.append(" ");
        }

        holder.mainNotificationTitle.setText(mainString.toString());


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
                showDialog(v.getContext(),model);
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
                if (model.getAge()!=null && model.getAgeShouldBe()!=null){
                    Log.d("WorkLocations",String.valueOf(model.getAge() + " " + model.getAgeShouldBe()));
                    if (model.getAgeShouldBe().equals("Greater than")){
                        tasks.add(usersCollection.whereGreaterThan("Age",model.getAge()).get());
                        Log.d("here","we are in greater than");
                    }else {
                        tasks.add(usersCollection.whereLessThan("Age",model.getAge()).get());
                        Log.d("here","we are in less than");
                    }
                }
                if (model.getDateStartFrom() != null && model.getDateTo() != null) {
                    long startDate = Long.parseLong(model.getDateStartFrom());
                    long toDate = Long.parseLong(model.getDateTo());
                    Log.d("joinDate",model.getDateStartFrom());
                    tasks.add(usersCollection.whereGreaterThan("joinDate", startDate)
                            .whereLessThan("joinDate", toDate)
                            .get());
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

                                if (!tokenList.contains(user.getFCMToken())) {
                                    tokenList.add(user.getFCMToken());
                                } else {
                                    Log.d("fcm", "Token already in list: " + user.getFCMToken());
                                }

//                                array.put(user.getFCMToken());
                            }
                        }
                        for (String token : tokenList) {
                            array.put(token);
                        }

                        System.out.println(array);

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
            }
        });
    }

    private void showDialog(Context context, NotificationModel model) {
        // Inflate the dialog layout
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialig_layout, null);

        // Find the dialog views
        TextInputEditText editText = dialogView.findViewById(R.id.edit_text);

        // Create the dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        builder.setTitle("Provide Rejection Reason");
        builder.setPositiveButton("Reject", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle positive button click
                String text = editText.getText().toString();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference docRef = db.collection("notifications").document(model.getDocumentId());
                Map<String, Object> updates = new HashMap<>();
                updates.put("status", "pending");
                updates.put("rejectionReason",text);
                docRef.update(updates)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("rejected", "Document updated successfully!");
                                JSONArray array = new JSONArray();
                                array.put(model.getUserDetails().get("userFcmToken"));
                                System.out.println(array);
                                FCMSend.pushNotificationMultiple(context, array, "Your notification has been rejected","Reason : "+ text, "notificationUpdater");
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
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

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
