package com.spiel.hospital;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    public  static  String str_mobilenumber;
    TextView text_loginbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        text_loginbtn = (TextView)findViewById(R.id.text_loginbtn);
        text_loginbtn = (TextView)findViewById(R.id.text_loginbtn);


        str_mobilenumber="";



        text_loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(LoginActivity.this,OtpmsgActivity.class);

                intent.putExtra("mobile", "8850519524");
                OtpmsgActivity.str_pagetype = "login";
                startActivity(intent);
                startActivity(intent);

            }
        });
    }
}