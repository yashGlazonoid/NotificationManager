package com.example.notificationmanager.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.notificationmanager.adapter.ChoiceAdapter;
import com.example.notificationmanager.databinding.FragmentHomeBinding;
import com.example.notificationmanager.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private static ArrayList<String> mArrayList = new ArrayList<>();
    private static ArrayList<String> departmentList = new ArrayList<>();
    private static ArrayList<String> locationList = new ArrayList<>();
    private static ArrayList<String> genderList = new ArrayList<>();
    private static ArrayList<String> finalLocationList = new ArrayList<>();
    private static ArrayList<String> finalDepartmentList = new ArrayList<>();
    private static ArrayList<String> finalGenderList = new ArrayList<>();
    private ArrayList<String> items;
    private ChoiceAdapter adapter, locationAdapter;
    private ChoiceAdapter genderAdapter;

    private String documentId;
    private Map<String, Boolean> query = new HashMap<>();
    private int checkDatePicker = 0;

    private String ageEt;
    private int checkForSpinner = 2;
    private String ageShouldBe;

    private String dateFrom, dateTo;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            documentId = bundle.getString("documentId");
            Log.d("documentId", documentId);
            Log.d("DocumentId", documentId);
            getDataFromDocument(documentId);
        } else {
            getDataFromFirebase("no");
            getData("no");
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
                        setDataForAcceptance(query, checkForSpinner, "no");
                    } else {
                        Toast.makeText(requireContext(), "Title and desc empty", Toast.LENGTH_SHORT).show();
                    }
//                    if (!finalLocationList.isEmpty()&&!finalGenderList.isEmpty()&&!finalDepartmentList.isEmpty()
//                            && !binding.tittleEt.getText().toString().trim().isEmpty()
//                            && !binding.desceEt.getText().toString().trim().isEmpty()
//                    ){
//                        Log.d("DepartmentList",finalDepartmentList.toString());
//                        Log.d("DepartmentList",finalGenderList.toString());
//                        Log.d("DepartmentList",finalLocationList.toString());
//                        FCMSend.pushNotification(requireContext(),
//                                "cEzbCvUXQgeAqhJTg03Iwj:APA91bE4HZlGsmf95P7Kthvm6t3zlPwzqbIPk0jKGAN2Gm65dI5meM2tNRoQ9IunrjwsxoSaJGBIewGiETLDEu7bA8qMUJ4QCDn2VXZBeFdbP02u-A6DOhZxOipTIvL0XeL8-OFyHvxv",
//                                "Notification created successfully with title "+binding.tittleEt.getText().toString().trim(),"Please wait for the approval","initial");
//                        setDataForAcceptance();
//                        getUserFilter();
//                    }
//                    else{
//                        Toast.makeText(requireContext(), "Every field needs to be filled", Toast.LENGTH_SHORT).show();
//                    }
                }
            });
        }

        binding.ageRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = requireView().findViewById(checkedId);
                ageShouldBe = radioButton.getText().toString().trim();
                Log.d("ageShouldBe", ageShouldBe);
            }
        });

//        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
//            @Override
//            public void onComplete(@NonNull Task<String> task) {
//                Toast.makeText(requireContext(), "this is your token" + task.getResult(), Toast.LENGTH_SHORT).show();
//                Log.d("TokenAuth",task.getResult());
//            }
//        });

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


        genderList.clear();


        String[] age = {"Age"};

        genderList.add("Male");
        genderList.add("Female");

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


//        FirebaseMessaging.getInstance().getToken()
//                .addOnCompleteListener(new OnCompleteListener<String>() {
//                    @Override
//                    public void onComplete(@NonNull Task<String> task) {
//                        if (!task.isSuccessful()) {
//                            Log.w("Nootification", "Fetching FCM registration token failed", task.getException());
//                            return;
//                        }
//
//                        // Get new FCM registration token
//                        String token = task.getResult();
//
//                        // Log and toast
////                        String msg = getString(R.string.msg_token_fmt, token);
//                        Log.d("Nootification", token);
//                        Toast.makeText(requireContext(), token, Toast.LENGTH_SHORT).show();
//                        sendNotificationToUserNew(token);
//
//                    }
//                });

        getUserData();

        return binding.getRoot();
    }

    private void getUserData() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String currentUser = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        Log.d("currentUser", currentUser);
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


    private void getDataFromDocument(String documentId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference notificationsRef = db.collection("notifications");
        DocumentReference notificationDocRef = notificationsRef.document(documentId);

        notificationDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.maivCardView.setVisibility(View.VISIBLE);
//                    binding.selectGendertBt.setVisibility(View.GONE);
//                    binding.selectDepartmentBt.setVisibility(View.GONE);
//                    binding.selectLocationBt.setVisibility(View.GONE);
//                    binding.spinner.setVisibility(View.GONE);
//                    binding.ageCardView.setVisibility(View.GONE);
                    String title = documentSnapshot.getString("title");
                    String description = documentSnapshot.getString("description");
//                    boolean isApproved = documentSnapshot.getBoolean("isApproved");
                   ArrayList<String> intentDepartment = (ArrayList<String>) documentSnapshot.get("departments");
                    ArrayList<String> intentGender = (ArrayList<String>) documentSnapshot.get("genders");
                    ArrayList<String> intentLocation = (ArrayList<String>) documentSnapshot.get("locations");
//                    Log.d("WorkLocations", String.valueOf(intentDepartment));
//                    Log.d("WorkLocations", String.valueOf(intentLocation));
//                    Log.d("WorkLocations", String.valueOf(intentGender));
                    Log.d("GetNotification", "Notification Title: " + title);
                    binding.tittleEt.setText(title);
                    binding.desceEt.setText(description);


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

                    getData("yes");
                    getDataFromFirebase("yes");

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

    void getDataFromFirebase(String intent) {
        FirebaseFirestore.getInstance().collection("ListOfList")
                .document("CompanyRealEstate").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                locationList = (ArrayList<String>) document.get("Locations");
                                Log.d("Location", locationList.toString());

                                locationAdapter = new ChoiceAdapter(locationList, finalLocationList, intent);

                                binding.locationRv.setAdapter(locationAdapter);
                                if (isAdded()) {
                                    binding.locationRv.setLayoutManager(new LinearLayoutManager(requireContext()));
                                }
                            }
                        }
                    }
                });
    }

    void getData(String intent) {
        FirebaseFirestore.getInstance().collection("ListOfList").document("DepartmentType").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        binding.maivCardView.setVisibility(View.VISIBLE);
                        binding.progressBar.setVisibility(View.GONE);

                        departmentList = (ArrayList<String>) document.get("Types");
                        Log.d("departmentList", departmentList.toString());


                        Log.d("Gender", genderList.toString());

                        if (intent.equals("yes")){
                            genderAdapter = new ChoiceAdapter(genderList, finalGenderList, intent);
                             adapter = new ChoiceAdapter(departmentList, finalDepartmentList, intent);
                            binding.genderRv.setAdapter(genderAdapter);
                            binding.genderRv.setLayoutManager(new LinearLayoutManager(requireContext()));
                            binding.departmentRv.setAdapter(adapter);
                            binding.departmentRv.setLayoutManager(new LinearLayoutManager(getContext()));
                        }
                        else{
                            genderAdapter = new ChoiceAdapter(genderList, finalGenderList, intent);
                            adapter = new ChoiceAdapter(departmentList, finalDepartmentList, intent);
                        }



                        binding.genderRv.setAdapter(genderAdapter);
                        binding.genderRv.setLayoutManager(new LinearLayoutManager(requireContext()));
                        binding.departmentRv.setAdapter(adapter);
                        binding.departmentRv.setLayoutManager(new LinearLayoutManager(getContext()));

                    } else {
                        Toast.makeText(requireContext(), "No such element", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("Working", task.getException().toString());
                }
            }
        });
    }

    private int returnTime() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("HHmm");
        String millisInString = dateFormat.format(new Date());
        return Integer.parseInt(millisInString);
    }

    private long returnDateNumber() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        String formattedDate = df.format(c);
        return Long.parseLong(formattedDate);
    }

    void setDataForAcceptance(Map<String, Boolean> query, int checkForSpinner, String intent) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference notificationsRef = db.collection("notifications");
        DocumentReference newNotificationRef = notificationsRef.document();


        String title = binding.tittleEt.getText().toString().trim();
        String description = binding.desceEt.getText().toString().trim();

        Map<String, Object> data = new HashMap<>();
        if (!finalDepartmentList.isEmpty()) {
            data.put("departments", finalDepartmentList);
        }
        if (!finalGenderList.isEmpty()) {
            data.put("genders", finalGenderList);
        }
        if (!finalLocationList.isEmpty()) {
            data.put("locations", finalLocationList);
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
        Log.d("CheckForSpinner", String.valueOf(checkForSpinner));
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

            Log.d("DocumentId", documentId);
            newNotificationRef.set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("SetData", "Notification added successfully");
                            Toast.makeText(requireContext(), "Notification Created Successfully", Toast.LENGTH_SHORT).show();
                            // ...

                            binding.desceEt.setText("");
                            binding.tittleEt.setText("");
                            adapter.uncheckAll();
                            genderAdapter.uncheckAll();
                            locationAdapter.uncheckAll();
                            binding.ageEt.setText("");
                            binding.ageLessThan.setChecked(false);
                            binding.ageGreaterThan.setChecked(false);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("SetData", "Error adding notification", e);
                            Toast.makeText(requireContext(), "Notification Failed to create", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Log.d("DocumentId", documentId);
            FirebaseFirestore.getInstance().collection("notifications").document(documentId)
                    .update(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("SetData", "Notification updated successfully");
                            Toast.makeText(requireContext(), "Notification updated Successfully", Toast.LENGTH_SHORT).show();
                            // ...

                            binding.desceEt.setText("");
                            binding.tittleEt.setText("");
                            adapter.uncheckAll();
                            genderAdapter.uncheckAll();
                            locationAdapter.uncheckAll();
                            binding.ageEt.setText("");
                            binding.ageLessThan.setChecked(false);
                            binding.ageGreaterThan.setChecked(false);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(requireContext(), "Failed to update data", Toast.LENGTH_SHORT).show();
                            Log.d("WorkLocation", e.getLocalizedMessage());
                        }
                    });
        }


//        if (intent.equals("no")){
//            newNotificationRef.set(data)
//                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            Log.d("SetData", "Notification added successfully");
//                            Toast.makeText(requireContext(), "Notification Created Successfully", Toast.LENGTH_SHORT).show();
//
//                            binding.desceEt.setText("");
//                            binding.tittleEt.setText("");
//                            adapter.uncheckAll();
//                            genderAdapter.uncheckAll();
//                            locationAdapter.uncheckAll();
//
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.e("SetData", "Error adding notification", e);
//                            Toast.makeText(requireContext(), "Notification Failed to create", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        }
//        else {
//            FirebaseFirestore.getInstance().collection("notifications").document(documentId)
//                    .set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void unused) {
//                            Log.d("SetData", "Notification updated successfully");
//                            Toast.makeText(requireContext(), "Notification updated Successfully", Toast.LENGTH_SHORT).show();
//
//                            binding.desceEt.setText("");
//                            binding.tittleEt.setText("");
//                            adapter.uncheckAll();
//                            genderAdapter.uncheckAll();
//                            locationAdapter.uncheckAll();
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(requireContext(), "Failed to update data", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        }


    }


    void getUserFilter() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("Users");

        ArrayList<String> depart = new ArrayList<>();
        depart.add("Marketing");

        db.collection("Users")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<User> salesUsers = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        User user = documentSnapshot.toObject(User.class);
                        for (int i = 0; i < finalDepartmentList.size() && i < user.getDepartmentTypes().size(); i++) {
                            if (finalDepartmentList.get(i).equals(user.getDepartmentTypes().get(i))) {
                                salesUsers.add(user);
                            }
                        }
//                        salesUsers.add(user);
                    }
                    Log.d("SalesUser", String.valueOf(salesUsers));
                })
                .addOnFailureListener(e -> {
                    // Handle any errors here
                });

    }


}