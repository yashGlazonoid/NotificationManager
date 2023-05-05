package com.example.notificationmanager.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notificationmanager.R;
import com.example.notificationmanager.model.NotificationModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RejectedAdapter extends RecyclerView.Adapter<RejectedAdapter.AcceptedViewHolder> {

    private ArrayList<NotificationModel> mlist;

    public RejectedAdapter(ArrayList<NotificationModel> list){
        this.mlist = list;
    }

    @NonNull
    @Override
    public AcceptedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checking_layout,parent
                ,false);
        return new AcceptedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AcceptedViewHolder holder, int position) {
        NotificationModel current = mlist.get(position);
        holder.notificationTitle.setText(current.getTitle());
        holder.notificationDesc.setText(current.getDescription());

        holder.approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference docRef = db.collection("notifications").document(current.getDocumentId());


                Map<String, Object> updates = new HashMap<>();
                updates.put("title", current.getTitle());
                updates.put("description", current.getDescription());
                updates.put("departments", current.getDepartment());
                updates.put("genders", current.getGender());
                updates.put("locations", current.getLocation());
                updates.put("isApproved", true);

                docRef.set(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("UpdateData", "Notification updated successfully");
//                        Toast.makeText(requireContext(), "Notification Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.e("UpdateData", "Error updating notification", e);
//                        Toast.makeText(requireContext(), "Notification Failed to update", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

//        if (current.isApproved()){
//            holder.status.setImageResource(R.drawable.ic_green);
//        }
//        else{
//            holder.status.setImageResource(R.drawable.ic_red);
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Bundle bundle = new Bundle();
//                    bundle.putString("documentId", current.getDocumentId());
//                    Navigation.findNavController(v).navigate(R.id.action_acceptedFragment_to_homeFragment,bundle);
//                }
//            });
//        }


    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    class AcceptedViewHolder extends RecyclerView.ViewHolder{
        TextView notificationTitle;
        TextView notificationDesc;
        ImageView approve;
        ImageView reject;
        ImageView notificationImage;


        public AcceptedViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationDesc = itemView.findViewById(R.id.notificationDesc);
            notificationTitle = itemView.findViewById(R.id.notificationTitleTT);
            approve = itemView.findViewById(R.id.approveBt);
            reject = itemView.findViewById(R.id.rejectBt);
            notificationImage = itemView.findViewById(R.id.notificationIV);
        }
    }
    private void sendNotificationToUser(String fcmToken) {
        FirebaseMessaging messaging = FirebaseMessaging.getInstance();

        Map<String, String> data = new HashMap<>();
        data.put("title", "Hello from Firebase!");
        data.put("body", "This is a test notification for users above 20");
        data.put("image", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSeLJnzgcw-0NEcuH0joPa1I_-MCWg-yJ_v1FrDzT8&s");

        RemoteMessage.Builder messageBuilder = new RemoteMessage.Builder(fcmToken);
        messageBuilder.setData(data);

        messaging.send(messageBuilder.build());
        Log.d("NotificationSend","NotificationSend");

    }
}
