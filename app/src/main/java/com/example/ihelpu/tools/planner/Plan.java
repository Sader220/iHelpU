package com.example.ihelpu.tools.planner;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

//Niedokończone!
public class Plan {
    //Obsługiwanie wartości planów, do wyświetalnia

    public String titleField, descriptionField, dateField, timeField;
    public Date mTimestamp;

    public Plan(){}
    public Plan(String title, String content, String date, String time){
        this.titleField = title;
        this.descriptionField = content;
        this.dateField = date;
        this.timeField = time;
    }

    public String getTitle() {
        return titleField;
    }

    public void setTitle(String title) {
        this.titleField = title;
    }

    public String getContent() {
        return descriptionField;
    }

    public void setContent(String content) {
        this.descriptionField = content;
    }

    public String getDate() {
        return dateField;
    }

    public void setDate(String date) {
        this.dateField = date;
    }

    public String getTimeField() {
        return timeField;
    }

    public void setTimeField(String timeField) {
        this.timeField = timeField;
    }

    @ServerTimestamp
    public Date getTimestamp() { return mTimestamp; }

    public void setTimestamp(Date timestamp) { mTimestamp = timestamp; }
}


