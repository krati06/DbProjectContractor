package com.example.divyansh.dbprojectandroid;

/**
 * Created by Divyansh on 11/4/2017.
 */

public class Hostel {
    private String hostel_id;
    private String waitList;

    public Hostel(String uid, String waitList){
        this.hostel_id = uid;
        this.waitList = waitList;
    }

    public String getHostel_id(){
        return hostel_id;
    }

    public String getWaitList(){
        return waitList;
    }
}
