package com.zybooks.projecttwo;

// This class was created as an alternative to the Log class in order to present it on screen properly due to the variables being String only. It
// might not be necessary to use since toString() is used on the original Log objects but will keep it just in case.
public class StringedLog {
    private String date;
    private String currentWeight;
    private String comment;

    public StringedLog(String date, String currentWeight, String comment ) {
        this.date = date;
        this.currentWeight = currentWeight;
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }
    public String getCurrentWeight() {
        return currentWeight;
    }
    public String getComment() {
        return comment;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public void setCurrentWeight(String currentWeight) {
        this.currentWeight = currentWeight;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
}