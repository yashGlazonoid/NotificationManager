package com.example.notificationmanager.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notificationmanager.R;
import com.example.notificationmanager.model.NotificationModel;

import java.util.ArrayList;

public class AcceptedAdapter extends RecyclerView.Adapter<AcceptedAdapter.AcceptedViewHolder> {

    private ArrayList<NotificationModel> mlist;

    public AcceptedAdapter(ArrayList<NotificationModel> list){
        this.mlist = list;
    }

    @NonNull
    @Override
    public AcceptedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.approved_item,parent
                ,false);
        return new AcceptedViewHolder(view);
    }

    public void onBindViewHolder(@NonNull AcceptedViewHolder holder, int position) {
        NotificationModel current = mlist.get(position);

        holder.notificationTitle.setText(current.getTitle());
        holder.notificationDesc.setText(current.getDescription());

        if (current.isApproved()) {
            holder.status.setImageResource(R.drawable.ic_green);
        } else {
            holder.status.setImageResource(R.drawable.ic_red);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current.isApproved()) {
                    // Do something if the notification is approved
                } else {
                    // If the notification is not approved, navigate to the home fragment and pass the document ID as an argument
                    Bundle bundle = new Bundle();
                    bundle.putString("documentId", current.getDocumentId());
                    Navigation.findNavController(v).navigate(R.id.action_acceptedFragment_to_homeFragment, bundle);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mlist.size();
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
