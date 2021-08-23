package com.spiel.hospital;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainViewActivity extends AppCompatActivity {

    public SharedPreferences pref;
    SharedPreferences.Editor editor;

    TextView textv_hospital_login,textv_doctor_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);


        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        textv_hospital_login = (TextView) findViewById(R.id.textv_hospital_login);
                textv_doctor_login = (TextView) findViewById(R.id.textv_doctor_login);

            if (pref.getString("login","").equalsIgnoreCase("yes"))
            {
                Intent intent = new Intent(MainViewActivity.this,DoctorListActivity.class);
                startActivity(intent);
            }
            else if(pref.getString("logindoctor","").equalsIgnoreCase("yes"))
            {
                Intent intent = new Intent(MainViewActivity.this,DoctorActivity.class);
                startActivity(intent);
            }

        textv_hospital_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainViewActivity.this,OtpmsgActivity.class);
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
    public void onBackPressed() {

    }

}