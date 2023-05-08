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

    String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String dateStartFrom , dateTo,age , ageShouldBe;
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

    public String getDateStartFrom() {
        return dateStartFrom;
    }

    public void setDateStartFrom(String dateStartFrom) {
        this.dateStartFrom = dateStartFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAgeShouldBe() {
        return ageShouldBe;
    }

    public void setAgeShouldBe(String ageShouldBe) {
        this.ageShouldBe = ageShouldBe;
    }

//    public NotificationModel(String documentId, String title, String description, ArrayList<String> department, ArrayList<String> gender, ArrayList<String> location, boolean isApproved, Map<String, Boolean> query) {
//        this.documentId = documentId;
//        this.title = title;
//        this.description = description;
//        this.departments = department;
//        this.genders = gender;
//        this.locations = location;
//        this.isApproved = isApproved;
//        this.query = query;
//    }


    public NotificationModel(String id, String documentId, String title, String description, ArrayList<String> departments, ArrayList<String> genders, ArrayList<String> locations, boolean isApproved, Map<String, Boolean> query, String status, String dateStartFrom, String dateTo, String age, String ageShouldBe) {
        this.id = id;
        this.documentId = documentId;
        this.title = title;
        this.description = description;
        this.departments = departments;
        this.genders = genders;
        this.locations = locations;
        this.isApproved = isApproved;
        this.query = query;
        this.status = status;
        this.dateStartFrom = dateStartFrom;
        this.dateTo = dateTo;
        this.age = age;
        this.ageShouldBe = ageShouldBe;
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
