package com.example.notificationmanager.model;

import java.util.ArrayList;
import java.util.Map;

public class NotificationModel {
    String id;
    private String documentId;
    private String title;
    private String description;
    private ArrayList<String> departments = new ArrayList<>();
    private ArrayList<String> genders = new ArrayList<>();
    private ArrayList<String> locations = new ArrayList<>();
    private boolean isApproved;
    private Map<String, Boolean> query;

    public ArrayList<String> getDepartments() {
        return departments;
    }

    public ArrayList<String> getGenders() {
        return genders;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public ArrayList<String> getLocations() {
        return locations;
    }

    public NotificationModel() {
    }

    public NotificationModel(String documentId, String title, String description, ArrayList<String> department, ArrayList<String> gender, ArrayList<String> location, boolean isApproved, Map<String, Boolean> query) {
        this.documentId = documentId;
        this.title = title;
        this.description = description;
        this.departments = department;
        this.genders = gender;
        this.locations = location;
        this.isApproved = isApproved;
        this.query = query;
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

    public void setDepartment(ArrayList<String> department) {
        this.departments = department;
    }

    public void setGender(ArrayList<String> gender) {
        this.genders = gender;
    }

    public void setLocation(ArrayList<String> location) {
        this.locations = location;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public Map<String, Boolean> getQuery() {
        return query;
    }

    public void setQuery(Map<String, Boolean> query) {
        this.query = query;
    }
}
