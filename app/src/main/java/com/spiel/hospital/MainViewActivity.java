package com.spiel.hospital;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainViewActivity extends AppCompatActivity {

    TextView textv_hospital_login,textv_doctor_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);
        textv_hospital_login = (TextView) findViewById(R.id.textv_hospital_login);
                textv_doctor_login = (TextView) findViewById(R.id.textv_doctor_login);



        textv_hospital_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainViewActivity.this,LoginActivity.class);
                startActivity(intent);

            }
        });

        textv_doctor_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainViewActivity.this,LoginDoctorActivity.class);
                startActivity(intent);
            }
        });
    }
}