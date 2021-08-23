package com.spiel.hospital;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DoctorActivity extends AppCompatActivity {
    TextView textv_doctorup,textv_doctor_logout;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
         textv_doctorup = (TextView) findViewById(R.id.textv_doctorup);
        textv_doctor_logout = (TextView) findViewById(R.id.textv_doctor_logout);


        textv_doctorup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DoctorActivity.this,DoctorProfileActivity.class);
                startActivity(intent);

            }
        });

        textv_doctor_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.putString("logindoctor","no");
                editor.commit();
                Intent intent = new Intent(DoctorActivity.this,MainViewActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onBackPressed() {

    }

}