package com.example.notificationmanager.model;

import java.util.ArrayList;

public class User {
    String age = "";
    ArrayList<String> DepartmentTypes;
    String FCMToken = "";
    String Gender = "";
    String WorkLocation = "";

    public User() {
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public ArrayList<String> getDepartmentTypes() {
        return DepartmentTypes;
    }

    public void setDepartmentTypes(ArrayList<String> departmentTypes) {
        DepartmentTypes = departmentTypes;
    }

    public String getFCMToken() {
        return FCMToken;
    }

    public void setFCMToken(String FCMToken) {
        this.FCMToken = FCMToken;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getWorkLocation() {
        return WorkLocation;
    }

    public void setWorkLocation(String workLocation) {
        WorkLocation = workLocation;
    }

    public User(String age, ArrayList<String> departmentTypes, String FCMToken, String gender, String workLocation) {
        this.age = age;
        DepartmentTypes = departmentTypes;
        this.FCMToken = FCMToken;
        Gender = gender;
        WorkLocation = workLocation;
    }
}
