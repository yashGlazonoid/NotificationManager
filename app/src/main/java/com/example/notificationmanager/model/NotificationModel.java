package com.example.notificationmanager.model;

import java.util.ArrayList;

public class NotificationModel {
    private String documentId;
    private String title;
    private String description;
    private ArrayList<String> department;
    private ArrayList<String> gender;
    private ArrayList<String> location;
    private boolean isApproved;

    public NotificationModel() {}

    public NotificationModel(String documentId, String title, String description, ArrayList<String> department, ArrayList<String> gender, ArrayList<String> location, boolean isApproved) {
        this.documentId = documentId;
        this.title = title;
        this.description = description;
        this.department = department;
        this.gender = gender;
        this.location = location;
        this.isApproved = isApproved;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getDepartment() {
        return department;
    }

    public void setDepartment(ArrayList<String> department) {
        this.department = department;
    }

    public ArrayList<String> getGender() {
        return gender;
    }

    public void setGender(ArrayList<String> gender) {
        this.gender = gender;
    }

    public ArrayList<String> getLocation() {
        return location;
    }

    public void setLocation(ArrayList<String> location) {
        this.location = location;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }
}
