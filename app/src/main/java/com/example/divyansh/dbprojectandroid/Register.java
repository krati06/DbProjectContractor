package com.example.divyansh.dbprojectandroid;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
        if (actionBar != null) {
            actionBar.setTitle("Register");
        }
    }
}
