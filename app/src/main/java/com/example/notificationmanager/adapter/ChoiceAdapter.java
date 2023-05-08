package com.example.notificationmanager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notificationmanager.R;

import java.util.ArrayList;

public class ChoiceAdapter extends RecyclerView.Adapter<ChoiceAdapter.ChoiceViewHolder> {


    private ArrayList<String> mList;
    private ArrayList<String> mChoice;

    public ChoiceAdapter(ArrayList<String> depList, ArrayList<String> choice,String c){
        this.mList = depList;
        this.mChoice = choice;
    }



    @NonNull
    @Override
    public ChoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChoiceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.choice_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChoiceViewHolder holder, int position) {
        String current = mList.get(position);
        holder.choiceTitle.setText(current);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.addBt.setChecked(!holder.addBt.isChecked());
            }
        });
        holder.addBt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (mChoice.contains(current)){
                        mChoice.remove(current);
                    }
                    else{
                        mChoice.add(current);
                    }
                    Toast.makeText(buttonView.getContext(), current+"is checked", Toast.LENGTH_SHORT).show();
                }
                if (!isChecked){
                    mChoice.remove(current);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ChoiceViewHolder extends RecyclerView.ViewHolder {
        TextView choiceTitle;
        CheckBox addBt;
        public ChoiceViewHolder(@NonNull View itemView) {
            super(itemView);
            choiceTitle = itemView.findViewById(R.id.choiceTitle);
            addBt = itemView.findViewById(R.id.addBt);
        }
    }
}
