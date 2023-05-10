package com.example.notificationmanager.adapter;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.notificationmanager.R;
import com.example.notificationmanager.databinding.FragmentMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MainFragment extends Fragment {

    private FragmentMainBinding binding;
    private FirebaseFirestore firestoreDB;
    private ListView departmentAdapter;
    private ListView genderAdapter;
    private ListView locationAdapter;
    private ArrayList<String> finalDepartmentList,finalGenderList,finalLocationList;
    private ArrayList<String> intentDepartmentList,intentGenderList,intentLocationList;
    private String ageShouldBe;

    private String documentId;
    private Map<String, Boolean> query = new HashMap<>();
    private int checkDatePicker = 0;

    private String ageEt;
    private int checkForSpinner = 2;
    private String dateFrom, dateTo;
    
    ArrayList<String> finalUserList = new ArrayList<>();


    private FirebaseFirestore mFirebaseFirestore;
    private ArrayAdapter<String> userListAdapter;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater,container,false);
        firestoreDB = FirebaseFirestore.getInstance();
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        finalDepartmentList = new ArrayList<>();
        finalGenderList = new ArrayList<>();
        finalLocationList = new ArrayList<>();
        intentDepartmentList = new ArrayList<>();
        intentGenderList = new ArrayList<>();
        intentLocationList = new ArrayList<>();

        getTypesOfDepartments();
        getTypesOfLocation();
        getTypesOfGender();

        Bundle bundle = getArguments();
        if (bundle != null) {
            documentId = bundle.getString("documentId");
            getDataFromDocument(documentId);
        } else {
            binding.addBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!finalLocationList.isEmpty()) {
                        query.put("Location", true);
                    } else {
                        query.put("Location", false);
                    }
                    if (!finalGenderList.isEmpty()) {
                        query.put("Gender", true);
                    } else {
                        query.put("Gender", false);
                    }
                    if (!finalDepartmentList.isEmpty()) {
                        query.put("Department", true);
                    } else {
                        query.put("Department", false);
                    }
                    if (!finalUserList.isEmpty()){
                        query.put("extraUser",true);
                    }
                    else{
                        query.put("extraUser",false);
                    }

                    if (!binding.tittleEt.getText().toString().trim().isEmpty() && !binding.desceEt.getText().toString().trim().isEmpty()) {
                        setDataForAcceptance(query, checkForSpinner, "no");
                    } else {
                        Toast.makeText(requireContext(), "Title and desc empty", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        binding.ageRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = requireView().findViewById(checkedId);
                ageShouldBe = radioButton.getText().toString().trim();
            }
        });


        binding.joinDateFromBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate("join");
            }
        });

        binding.joinDateToBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkDatePicker == 1) {
                    setDate("to");
                } else {
                    Toast.makeText(requireContext(), "Choose start date first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        String[] age = {"Age"};

        ArrayAdapter ad = new ArrayAdapter(requireContext(),
                android.R.layout.simple_spinner_item,
                age);

        binding.spinner.setAdapter(ad);

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 1) {
//                    Toast.makeText(requireContext(), "First", Toast.LENGTH_SHORT).show();
                    binding.ageCardView.setVisibility(View.GONE);
                    binding.dateCardView.setVisibility(View.VISIBLE);
                    checkForSpinner = 1;
                    binding.ageEt.setText("");
                    binding.ageGreaterThan.setChecked(false);
                    binding.ageLessThan.setChecked(false);
                } else if (position == 0) {
//                    Toast.makeText(requireContext(), "Zero", Toast.LENGTH_SHORT).show();
                    binding.ageCardView.setVisibility(View.VISIBLE);
                    binding.dateCardView.setVisibility(View.GONE);
                    ageEt = binding.ageEt.getText().toString().trim();
                    checkForSpinner = 0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getUserForSearch();


        return binding.getRoot();
    }

    private void getDataFromDocument(String documentId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference notificationsRef = db.collection("notifications");
        DocumentReference notificationDocRef = notificationsRef.document(documentId);

        notificationDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
//                    binding.progressBar.setVisibility(View.GONE);
                    binding.maivCardView.setVisibility(View.VISIBLE);
//                    binding.selectGendertBt.setVisibility(View.GONE);
//                    binding.selectDepartmentBt.setVisibility(View.GONE);
//                    binding.selectLocationBt.setVisibility(View.GONE);
//                    binding.spinner.setVisibility(View.GONE);
//                    binding.ageCardView.setVisibility(View.GONE);
                    String title = documentSnapshot.getString("title");
                    String description = documentSnapshot.getString("description");
//                    boolean isApproved = documentSnapshot.getBoolean("isApproved");

                    Map data = (Map<String,Object>) documentSnapshot.get("query");
                    Boolean isDepartment = (Boolean) data.get("Department");
                    Boolean isLocation = (Boolean) data.get("Location");
                    Boolean isGender = (Boolean) data.get("Gender");

                    Log.d("isDeparment",isDepartment.toString());
                    Log.d("isDeparment",isGender.toString());
                    Log.d("isDeparment",isLocation.toString());


                    intentDepartmentList = (ArrayList<String>) documentSnapshot.get("departments");
                    intentGenderList = (ArrayList<String>) documentSnapshot.get("genders");
                    intentLocationList = (ArrayList<String>) documentSnapshot.get("locations");
                    String age = documentSnapshot.getString("age");
                    String ageShouldBe = documentSnapshot.getString("ageShouldBe");
                    Log.d("WorkLocations", String.valueOf(intentDepartmentList));
                    Log.d("WorkLocations", String.valueOf(intentLocationList));
                    Log.d("WorkLocations", String.valueOf(intentGenderList));
                    Log.d("GetNotification", "Notification Title: " + title);
                    binding.tittleEt.setText(title);
                    binding.desceEt.setText(description);
                    binding.ageEt.setText(age);

                    if (ageShouldBe!=null && age!=null){
                        if (ageShouldBe.equals("Greater than")){
                            binding.ageGreaterThan.setChecked(true);
                        }
                        else{
                            binding.ageLessThan.setChecked(true);
                        }
                    }


                    if (isGender && intentGenderList!=null){
                        for (int i = 0; i < binding.selectGenderLayout.getChildCount(); i++) {
                            View child = binding.selectGenderLayout.getChildAt(i);
                            if (child instanceof CheckBox) {
                                CheckBox checkBox = (CheckBox) child;
                                String checkBoxText = checkBox.getText().toString();
                                if (intentGenderList != null && intentGenderList.contains(checkBoxText)) {
                                    checkBox.setChecked(true);
                                    if (!finalGenderList.contains(checkBoxText)){
                                        finalGenderList.add(checkBoxText);
                                    }
                                }
                            }
                        }

                    }
                    if (isLocation && intentLocationList!=null){
                        for (int i = 0; i < binding.selectLocationLayout.getChildCount(); i++) {
                            View child = binding.selectLocationLayout.getChildAt(i);
                            if (child instanceof CheckBox) {
                                CheckBox checkBox = (CheckBox) child;
                                String checkBoxText = checkBox.getText().toString();
                                if (intentLocationList != null && intentLocationList.contains(checkBoxText)) {
                                    checkBox.setChecked(true);
                                    if(!finalLocationList.contains(checkBoxText)){
                                        finalLocationList.add(checkBoxText);

                                    }
                                }
                            }
                        }
                    }

                    if (isDepartment){
                        if ( intentDepartmentList!=null){
                            for (int i = 0; i < binding.selectDepartmentLayout.getChildCount(); i++) {
                                View child = binding.selectDepartmentLayout.getChildAt(i);
                                if (child instanceof CheckBox) {
                                    CheckBox checkBox = (CheckBox) child;
                                    String checkBoxText = checkBox.getText().toString();
                                    if (intentDepartmentList != null && intentDepartmentList.contains(checkBoxText)) {
                                        checkBox.setChecked(true);
                                        if (!finalDepartmentList.contains(checkBoxText)){
                                            finalDepartmentList.add(checkBoxText);
                                        }
                                        else{
                                            finalDepartmentList.remove(checkBoxText);
                                        }
                                    }
                                }
                            }
                        }
                    }






//                    adapter = new ChoiceAdapter(departments,finalDepartmentList,"Department");
//                    genderAdapter = new ChoiceAdapter(genders,finalGenderList,"Gender");
//                    locationAdapter = new ChoiceAdapter(locations,finalLocationList,"Location");
//
//                    binding.locationRv.setAdapter(locationAdapter);
//                    binding.locationRv.setLayoutManager(new LinearLayoutManager(getContext()));
//
//                    binding.genderRv.setAdapter(genderAdapter);
//                    binding.genderRv.setLayoutManager(new LinearLayoutManager(getContext()));
//
//                    binding.departmentRv.setAdapter(adapter);
//                    binding.departmentRv.setLayoutManager(new LinearLayoutManager(getContext()));

                    binding.addBt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!finalLocationList.isEmpty()) {
                                query.put("Location", true);
                            } else {
                                query.put("Location", false);
                            }
                            if (!finalGenderList.isEmpty()) {
                                query.put("Gender", true);
                            } else {
                                query.put("Gender", false);
                            }
                            if (!finalDepartmentList.isEmpty()) {
                                query.put("Department", true);
                            } else {
                                query.put("Department", false);
                            }

                            if (!binding.tittleEt.getText().toString().trim().isEmpty() && !binding.desceEt.getText().toString().trim().isEmpty()) {
                                setDataForAcceptance(query, checkForSpinner, "yes");
                            } else {
                                Toast.makeText(requireContext(), "Title and desc empty", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                } else {
                    // Document not found
                    Log.d("GetNotification", "Document not found");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Error occurred
                Log.e("GetNotification", "Error getting document: " + e.getMessage());
            }
        });

    }

    private void setDate(String dateType) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, monthOfYear, dayOfMonth);


                if (selectedDate.after(calendar)) { // Check if selected date is after current date
                    Toast.makeText(requireContext(), "Selected date cannot be greater than current date", Toast.LENGTH_SHORT).show();
                    return;
                }

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String selectedDateString = sdf.format(selectedDate.getTime());

                if (dateType.equals("join")) {
                    binding.joinDateFromBt.setText(selectedDateString);
                    long dateInMillis = selectedDate.getTimeInMillis();
                    dateFrom = String.valueOf(dateInMillis);
                    checkDatePicker = 1;
                } else {
                    String fromDate = binding.joinDateFromBt.getText().toString();
                    try {
                        Date from = sdf.parse(fromDate);
                        Date to = sdf.parse(selectedDateString);
                        if (to.before(from)) {
                            Toast.makeText(requireContext(), "Selected date cannot be less than from date", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long dateInMillis = selectedDate.getTimeInMillis();
                    dateTo = String.valueOf(dateInMillis);
                    binding.joinDateToBt.setText(selectedDateString);
                }
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    void setDataForAcceptance(Map<String, Boolean> query, int checkForSpinner, String intent) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference notificationsRef = db.collection("notifications");
        DocumentReference newNotificationRef = notificationsRef.document();


        String title = binding.tittleEt.getText().toString().trim();
        String description = binding.desceEt.getText().toString().trim();

        Map<String, Object> data = new HashMap<>();
        if (!finalUserList.isEmpty()){
            data.put("extraUsers",finalUserList);
            Log.d("extraUsers",finalUserList.toString());
        }
        if (!finalDepartmentList.isEmpty()) {
            data.put("departments", finalDepartmentList);
            Log.d("finalList",finalDepartmentList.toString());
        }
        if (!finalGenderList.isEmpty()) {
            data.put("genders", finalGenderList);
            Log.d("finalList",finalGenderList.toString());

        }
        if (!finalLocationList.isEmpty()) {
            data.put("locations", finalLocationList);
            Log.d("finalList",finalLocationList.toString());

        }
        if (checkForSpinner == 0) {
            if (!binding.ageEt.getText().toString().isEmpty()) {
                data.put("age", binding.ageEt.getText().toString().trim());
                data.put("ageShouldBe", ageShouldBe);
                query.put("age", true);
                query.put("date", false);
            } else {
                query.put("age", false);
                query.put("date", false);
            }
        } else {
            if (checkDatePicker == 1) {
                data.put("dateStartFrom", dateFrom);
                data.put("dateTo", dateTo);
                query.put("date", true);
                query.put("age", false);
            } else {
                query.put("date", false);
                query.put("age", false);
            }
        }
        data.put("title", title);
        data.put("description", description);
//        data.put("isApproved", "No");
        data.put("query", query);
        Map<String, Object> userDetails = new HashMap<>();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        userDetails.put("uid", auth.getCurrentUser().getUid().toString());
        userDetails.put("userPhoto", auth.getCurrentUser().getPhotoUrl().toString());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String formattedDate = sdf.format(date);
        userDetails.put("date", formattedDate);
        userDetails.put("time", System.currentTimeMillis());
        userDetails.put("userFcmToken", "fcIcc07fTp6tplyM7LlCkF:APA91bF8MHi3rHRQL1iHj8mKyTi3VNpxonzCpya10Z255_lbh6Ry42BKqWCEkF6uKKwPxMeAjTX-gVwEUxU0EMpRRTI9UWq0aX8QuhiFBISHL8Yb3GnCNgEY0h0s4ee_xqq5pQhvVg-9");
        userDetails.put("notificationCreatedBy", auth.getCurrentUser().getDisplayName());
        data.put("userDetails", userDetails);
        data.put("status", "open");

        if (intent.equals("no")) {
            String documentId = newNotificationRef.getId();
            data.put("documentId", documentId);

            newNotificationRef.set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(requireContext(), "Notification Created Successfully", Toast.LENGTH_SHORT).show();
                            // ...

                            binding.desceEt.setText("");
                            binding.tittleEt.setText("");
                            binding.ageEt.setText("");
                            binding.ageLessThan.setChecked(false);
                            binding.ageGreaterThan.setChecked(false);
                            finalUserList.clear();
                            userListAdapter.notifyDataSetChanged();
                            for (int i = 0; i < binding.selectGenderLayout.getChildCount(); i++) {
                                View child = binding.selectGenderLayout.getChildAt(i);
                                if (child instanceof CheckBox) {
                                    CheckBox checkBox = (CheckBox) child;
                                    checkBox.setChecked(false);
                                }
                            }
                            for (int i = 0; i < binding.selectLocationLayout.getChildCount(); i++) {
                                View child = binding.selectLocationLayout.getChildAt(i);
                                if (child instanceof CheckBox) {
                                    CheckBox checkBox = (CheckBox) child;
                                    checkBox.setChecked(false);
                                }
                            }
                            for (int i = 0; i < binding.selectDepartmentLayout.getChildCount(); i++) {
                                View child = binding.selectDepartmentLayout.getChildAt(i);
                                if (child instanceof CheckBox) {
                                    CheckBox checkBox = (CheckBox) child;
                                    checkBox.setChecked(false);
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(requireContext(), "Notification Failed to create", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            FirebaseFirestore.getInstance().collection("notifications").document(documentId)
                    .update(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(requireContext(), "Notification updated Successfully", Toast.LENGTH_SHORT).show();
                            // ...

                            binding.desceEt.setText("");
                            binding.tittleEt.setText("");
                            binding.ageEt.setText("");
                            binding.ageLessThan.setChecked(false);
                            binding.ageGreaterThan.setChecked(false);
                            NavHostFragment.findNavController(MainFragment.this)
                                    .navigate(R.id.action_mainFragment_to_acceptedFragment);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(requireContext(), "Failed to update data", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }


    private void getTypesOfGender() {
        ArrayList<String> group = new ArrayList<>();
        group.add("Male");
        group.add("Female");
        assert group != null;
        for (int i = 0; i < group.size(); i++) {
            setCheckboxesGender(group.get(i));
        }
    }

    private void setCheckboxesGender(String name) {
        float scale = getContext().getApplicationContext().getResources().getDisplayMetrics().density;
        final int margins = (int) (20 * scale + 0.5f);
        final LinearLayout.LayoutParams checkBoxParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                margins * 2);
        checkBoxParams.setMargins(margins, margins, margins, 0);

        CheckBox test = new CheckBox(requireContext());
        test.setText(name);
//        test.setBackgroundColor(getColor(R.color.darkBackgroundTextvie));
        test.setLayoutParams(checkBoxParams);
        binding.selectGenderLayout.addView(test);
//        if (intentGenderList!=null){
//            Log.d("intentGender",intentGenderList.toString());
//            if (intentGenderList.contains(test.getText().toString())){
//                test.setChecked(true);
//                if (finalGenderList.contains(test.getText().toString().trim())){
//                    finalGenderList.remove(test.getText().toString().trim());
//                }
//                else{
//                    finalGenderList.add(test.getText().toString().trim());
//                }
//            }
//        }
//        else{
//            Toast.makeText(requireContext(), "Gender list is empty", Toast.LENGTH_SHORT).show();
//        }

        // Check if intentGenderList is not null and contains the name
//        if (intentGenderList != null && intentGenderList.contains(name)) {
//            test.setChecked(true);
//        }

        test.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    if (!finalGenderList.contains(name)){
                        finalGenderList.add(name);
                    }
                }
                else{
                    finalGenderList.remove(name);
                }
            }
        });

    }

    private void updateListData(CheckBox checkBox, List<String> dataList) {
        if (checkBox.isChecked()) {
            String checkBoxText = checkBox.getText().toString();
            if (!dataList.contains(checkBoxText)) {
                dataList.add(checkBoxText);
            }
        } else {
            String checkBoxText = checkBox.getText().toString();
            if (dataList.contains(checkBoxText)) {
                dataList.remove(checkBoxText);
            }
        }
    }


    private void getTypesOfLocation() {
        FirebaseFirestore.getInstance().collection("ListOfList")
                .document("CompanyRealEstate").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                List<String> group = (List<String>) document.get("Locations");
                                Collections.sort(group, String.CASE_INSENSITIVE_ORDER);
                                assert group != null;
                                for (int i = 0; i < group.size(); i++) {
                                    setCheckboxesLocation(group.get(i));
                                }
                            }
                        }
                    }
                });

    }

    private void setCheckboxesLocation(String name) {
        float scale = getContext().getApplicationContext().getResources().getDisplayMetrics().density;
        final int margins = (int) (20 * scale + 0.5f);
        final LinearLayout.LayoutParams checkBoxParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                margins * 2);
        checkBoxParams.setMargins(margins, margins, margins, 0);

        CheckBox test = new CheckBox(requireContext());
        test.setText(name);
//        test.setBackgroundColor(getColor(R.color.darkBackgroundTextvie));
        test.setBackgroundResource(R.drawable.checkbox_background);
        test.setLayoutParams(checkBoxParams);
        binding.selectLocationLayout.addView(test);
        if (intentLocationList!=null ){
            Log.d("intentLocation",intentLocationList.toString());
            if (intentLocationList.contains(test.getText().toString())){
                test.setChecked(true);
                if (finalLocationList.contains(name)){
                    finalLocationList.remove(name);
                }
                else{
                    finalLocationList.add(name);
                }
            }
        }
        test.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    if (!finalLocationList.contains(name)){
                        finalLocationList.add(name);
                    }
                }
                else{
                    finalLocationList.remove(name);
                }
            }
        });

    }

    public void getTypesOfDepartments() {

        final DocumentReference rates = mFirebaseFirestore.collection("ListOfList")
                .document("DepartmentType");

        rates.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    assert document != null;
                    if (document.exists()) {
                        List<String> group = (List<String>) document.get("Types");
                        Collections.sort(group, String.CASE_INSENSITIVE_ORDER);
                        assert group != null;
                        for (int i = 0; i < group.size(); i++) {
                            setCheckboxesDepartment(group.get(i));
                        }

                    }
                }
            }

        });
    }

    public void setCheckboxesDepartment(final String name) {
        float scale = getContext().getApplicationContext().getResources().getDisplayMetrics().density;
        final int margins = (int) (20 * scale + 0.5f);
        final LinearLayout.LayoutParams checkBoxParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                margins * 2);
        checkBoxParams.setMargins(margins, margins, margins, 0);

        CheckBox test = new CheckBox(requireContext());
        test.setText(name);
//        test.setBackgroundColor(getColor(R.color.darkBackgroundTextvie));
        test.setLayoutParams(checkBoxParams);
        binding.selectDepartmentLayout.addView(test);
        if (intentDepartmentList!=null ){
            Log.d("intentDepartment",intentDepartmentList.toString());
            if (intentDepartmentList.contains(test.getText().toString())){
                test.setChecked(true);
                if (finalDepartmentList.contains(name)){
                    finalDepartmentList.remove(name);
                }
                else{
                    finalDepartmentList.add(name);
                }
            }
        }
        test.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    if (!finalDepartmentList.contains(name)){
                        finalDepartmentList.add(name);
                    }
                }
                else{
                    finalDepartmentList.remove(name);
                }
            }
        });

    }

    private void getUserForSearch(){
        CollectionReference usersRef = FirebaseFirestore.getInstance().collection("Users");
        usersRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<String> userNames = new ArrayList<>();
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                    String userName = documentSnapshot.getId();
                    Log.d("Username",userName);
                    userNames.add(userName);
                }
                setupAutoCompleteTextView(userNames);
            }
        });

    }
    private void setupAutoCompleteTextView(List<String> userNames) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, userNames);
        AutoCompleteTextView autoCompleteTextView = getView().findViewById(R.id.search_box);
        autoCompleteTextView.setAdapter(adapter);
        binding.searchBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!autoCompleteTextView.getText().toString().isEmpty()) {
                    if (userNames.contains(autoCompleteTextView.getText().toString())) {
                        if (!finalUserList.contains(autoCompleteTextView.getText().toString())) {
                            finalUserList.add(autoCompleteTextView.getText().toString().trim());
                            autoCompleteTextView.setText("");
                            userListAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(requireContext(), "User already selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else{
                    Toast.makeText(requireContext(), "First add a user", Toast.LENGTH_SHORT).show();
                }
                Log.d("finalUserList",finalUserList.toString());
            }
        });

        userListAdapter = new ArrayAdapter<String>(requireContext(),
                android.R.layout.simple_list_item_1, finalUserList);

        binding.searchResultList.setAdapter(userListAdapter);
    }



}