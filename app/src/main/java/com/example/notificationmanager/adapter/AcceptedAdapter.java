package com.example.notificationmanager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notificationmanager.R;
import com.example.notificationmanager.model.NotificationModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class AcceptedAdapter extends FirestoreRecyclerAdapter<NotificationModel, AcceptedAdapter.AcceptedViewHolder> {

    public AcceptedAdapter(@NonNull FirestoreRecyclerOptions<NotificationModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AcceptedViewHolder holder, int position, @NonNull NotificationModel model) {
        holder.notificationTitle.setText(model.getTitle());
        holder.notificationDesc.setText(model.getDescription());


        if (model.isApproved()){
            holder.status.setImageResource(R.drawable.ic_green);
        }
        else{
            holder.status.setImageResource(R.drawable.ic_red);
        }
    }

    @NonNull
    @Override
    public AcceptedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AcceptedViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.approved_item,parent,false));
    }

    class AcceptedViewHolder extends RecyclerView.ViewHolder{
        TextView notificationTitle;
        TextView notificationDesc;
        ImageView status;
        ImageView notificationImage;


        public AcceptedViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationDesc = itemView.findViewById(R.id.notificationDesc);
            notificationTitle = itemView.findViewById(R.id.notificationTitleTT);
            status = itemView.findViewById(R.id.status);
            notificationImage = itemView.findViewById(R.id.notificationIV);
        }
    }
}
