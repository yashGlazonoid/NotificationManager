package com.example.notificationmanager.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notificationmanager.databinding.ActivityHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private ListView departmentAdapter;
    private ListView genderAdapter;
    private ListView locationAdapter;
    private ArrayList<String> departmentList;
    private FirebaseFirestore mFirebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mFirebaseFirestore = FirebaseFirestore.getInstance();

        getData();
    }

    public void getTypesOfDepartments() {

        final DocumentReference rates = mFirebaseFirestore.collection("ListOfList")
                .document("DepartmentType");

        rates.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Log.d("listItem","after task result");

                    assert document != null;
                    if (document.exists()) {
                        List<String> group = (List<String>) document.get("Types");
                        Collections.sort(group, String.CASE_INSENSITIVE_ORDER);
                        assert group != null;
                        for (int i = 0; i < group.size(); i++) {
                            Log.d("listItem","inside for loop");
                            Log.d("listItem",group.get(i));
                            setCheckboxes(group.get(i));
                        }

                    }
                }
            }

        });
    }

    public void setCheckboxes(final String name) {

        float scale =getApplicationContext().getResources().getDisplayMetrics().density;
        final int margins = (int) (20 * scale + 0.5f);

        final LinearLayout.LayoutParams checkBoxParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                margins * 2);
        checkBoxParams.setMargins(margins, margins, margins, 0);

        CheckBox test = new CheckBox(this);
        test.setText(name);
//        test.setBackgroundColor(getColor(R.color.darkBackgroundTextvie));
        test.setLayoutParams(checkBoxParams);
        binding.selectLocationLayout.addView(test);
        test.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                if (isChecked) {
//                    departmentList.add(name);
//                } else {
//
//                    if (typeOfDepartmentsArray.contains(name)) {
//
//                        typeOfDepartmentsArray.remove(name);
//                    }
//                }
            }
        });

    }

    void getData() {
        FirebaseFirestore.getInstance().collection("ListOfList").document("DepartmentType").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<String> group = (List<String>) document.get("Types");
                        Collections.sort(group, String.CASE_INSENSITIVE_ORDER);
                        assert group != null;
                        for (int i = 0; i < group.size(); i++) {
                            Log.d("listItem","inside for loop");
                            Log.d("listItem",group.get(i));
                            setCheckboxes(group.get(i));
                        }
                    } else {
                        Toast.makeText(HomeActivity.this, "No such element", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("Working", task.getException().toString());
                }
            }
        });
    }

}