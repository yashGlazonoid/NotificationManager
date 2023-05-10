package com.example.notificationmanager.adapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notificationmanager.R;
import com.example.notificationmanager.model.NotificationModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class AcceptedAdapter extends FirestoreRecyclerAdapter<NotificationModel, AcceptedAdapter.AcceptedViewHolder> {

    private Fragment mFragment;

    public AcceptedAdapter(@NonNull FirestoreRecyclerOptions<NotificationModel> options,Fragment fragment) {
        super(options);
        this.mFragment = fragment;
    }

    @Override
    protected void onBindViewHolder(@NonNull AcceptedViewHolder holder, int position, @NonNull NotificationModel model) {
        holder.notificationTitle.setText(model.getTitle());
        holder.notificationDesc.setText(model.getDescription());

        if (model.getStatus().equals("pending")&&model.getRejectionReason()!=null){
            holder.reason.setVisibility(View.VISIBLE);
            holder.reasonHelper.setVisibility(View.VISIBLE);
            holder.reason.setText(model.getRejectionReason());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("documentId", model.getDocumentId());
                    Log.d("DocumentId",model.getDocumentId());
                    NavHostFragment.findNavController(mFragment)
                            .navigate(R.id.action_acceptedFragment_to_mainFragment, bundle);
                }
            });
        }

        if (model.getStatus().equals("close")){
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

        TextView reasonHelper,reason;


        public AcceptedViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationDesc = itemView.findViewById(R.id.notificationDesc);
            notificationTitle = itemView.findViewById(R.id.notificationTitleTT);
            status = itemView.findViewById(R.id.status);
            notificationImage = itemView.findViewById(R.id.notificationIV);
            reason = itemView.findViewById(R.id.reason);
            reasonHelper = itemView.findViewById(R.id.reasonHelper);
        }
    }
}
