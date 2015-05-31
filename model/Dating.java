package com.ponggan.phishing;

public class Dating {

    //private variables
    int _id;
    String _date;
    String _content;
    int _scoreSelf;
    int _scoreTarget;
    int _target;

    // Empty constructor
    public Dating(){

    }
    // constructor
    public Dating(int id, String date, String _content, int _scoreSelf, int _scoreTarget, int _target){
        this._id = id;
        this._date = date;
        this._content = _content;
        this._scoreSelf = _scoreSelf;
        this._scoreTarget = _scoreTarget;
        this._target = _target;
    }

    // constructor
    public Dating(String date, String _content, int _scoreSelf, int _scoreTarget, int _target){
        this._date = date;
        this._content = _content;
        this._scoreSelf = _scoreSelf;
        this._scoreTarget = _scoreTarget;
        this._target = _target;
    }
    // getting ID
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }

    // getting date
    public String getDate(){
        return this._date;
    }

    // setting date
    public void setDate(String date){
        this._date = date;
    }

    // getting content
    public String getContent(){
        return this._content;
    }

    // setting content
    public void setContent(String content){
        this._content = content;
    }

    // getting scoreSelf
    public int getScoreSelf(){
        return this._scoreSelf;
    }

    // setting scoreSelf
    public void setScoreSelf(int scoreSelf){
        this._scoreSelf = scoreSelf;
    }

    // getting scoreTarget
    public int getScoreTarget(){
        return this._scoreTarget;
    }

    // setting scoreTarget
    public void setScoreTarget(int scoreTarget){
        this._scoreTarget = scoreTarget;
    }

    // getting target id
    public int getTarget(){
        return this._target;
    }

    // setting target id
    public void setTarget(int target){
        this._target = target;
    }
}