package com.example.divyansh.dbprojectandroid;

/**
 * Created by Divyansh on 11/4/2017.
 */

public class Hostel {
    private String uid;
    private String waitList;

    public Hostel(String uid, String waitList){
        this.uid = uid;
        this.waitList = waitList;
    }

    public String getUid(){
        return uid;
    }

    public String getWaitList(){
        return waitList;
    }
}
