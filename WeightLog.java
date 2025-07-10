package com.zybooks.projecttwo;

import android.content.Intent;

// The Log class that holds variables related to the weight logs.
public class WeightLog {
    private String date;
    private double currentWeight;
    private String comment;
    private Long id;

    public WeightLog(String date, double currentWeight, String comment, Long id) {
        this.date = date;
        this.currentWeight = currentWeight;
        this.comment = comment;
        this.id = id;
    }
    public String getDate() {
        return date;
    }
    public double getCurrentWeight() {
        return currentWeight;
    }
    public String getComment() {
        return comment;
    }
    public Long getId() {return id; }

    public void setDate(String date) {
        this.date = date;
    }
    public void setCurrentWeight(double currentWeight) {
        this.currentWeight = currentWeight;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public void setId(Long id) { this.id = id; }
}
