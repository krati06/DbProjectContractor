package com.example.divyansh.dbprojectandroid;

/**
 * Created by Divyansh on 11/24/2017.
 */

public class feedback {
    private String Timestamp;
    private String Text;

    public feedback(String Timestamp, String Text){
        this.Timestamp = Timestamp;
        this.Text = Text;
    }

    public String getTimestamp(){
        return Timestamp;
    }

    public String getText(){
        return Text;
    }
}