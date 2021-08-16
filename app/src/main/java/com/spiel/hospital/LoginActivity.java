package com.spiel.hospital;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    RelativeLayout card_login2,card_reg2;
    TextView text_login_singup,text_reg_login,text_loginbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        card_login2 = (RelativeLayout)findViewById(R.id.card_login2);
        card_reg2 = (RelativeLayout)findViewById(R.id.card_reg2);
        text_login_singup = (TextView)findViewById(R.id.text_login_singup);
        text_reg_login = (TextView)findViewById(R.id.text_reg_login);
        text_loginbtn = (TextView)findViewById(R.id.text_reg_login);


        text_login_singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                card_login2.setVisibility(View.INVISIBLE);
                card_reg2.setVisibility(View.VISIBLE);

            }
        });

        text_reg_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card_login2.setVisibility(View.VISIBLE);
                card_reg2.setVisibility(View.INVISIBLE);

            }
        });

        text_loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(LoginActivity.this,OtpmsgActivity.class);

                intent.putExtra("mobile", "8850519524");
                OtpmsgActivity.str_pagetype = "reg";
                startActivity(intent);
                startActivity(intent);

            }
        });
    }
}