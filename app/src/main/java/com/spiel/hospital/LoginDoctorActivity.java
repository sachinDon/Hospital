package com.spiel.hospital;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LoginDoctorActivity extends AppCompatActivity {

    TextView text_loginbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_doctor);

        text_loginbtn = (TextView) findViewById(R.id.text_loginbtn);



        text_loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginDoctorActivity.this,DoctorActivity.class);
                startActivity(intent);

            }
        });

    }
}