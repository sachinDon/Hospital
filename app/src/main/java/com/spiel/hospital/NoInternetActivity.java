package com.spiel.hospital;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class NoInternetActivity extends AppCompatActivity {

    private Button button_retry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);

        button_retry = findViewById(R.id.button_retry);
        button_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isNetworkAwailabel(NoInternetActivity.this)) {
                    finish();
                } else {
                    Toast.makeText(NoInternetActivity.this, "not connect to internet", Toast.LENGTH_SHORT);
                }
            }
        });
    }
        @Override
        public void onBackPressed()
        {
            finish();
        }
}