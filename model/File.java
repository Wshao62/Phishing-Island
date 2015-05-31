package com.ponggan.phishing;

import java.sql.Blob;

public class File {

    //private variables
    int _id;
    String name;
    String figure;
    Blob photo;
    double avg_score;

    // Empty constructor
    public File(){

    }
    // constructor
    public File(int id, String name, String figure, Blob photo, int avg_score){
        this._id = id;
        this.name = name;
        this.figure = figure;
        this.photo = photo;
        this.avg_score = avg_score;
    }

    // getting ID
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }

    // getting content
    public String getContent(){
        return this.name;
    }

    // setting content
    public void setContent(String content){
        this.name = content;
    }

    // getting figure
    public String getFigure(){
        return this.figure;
    }

    // setting figure
    public void setFigure(String figure){
        this.figure = figure;
    }

    // getting Photo
    public Blob getPhoto(){
        return this.photo;
    }

    // setting Photo
    public void setPhoto(Blob photo){
        this.photo = photo;
    }

    // getting avg
    public double getAvg(){
        return this.avg_score;
    }

    // setting avg
    public void setAvg(double avg_score){
        this.avg_score = avg_score;
    }
}