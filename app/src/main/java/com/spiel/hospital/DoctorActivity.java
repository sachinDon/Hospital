package com.spiel.hospital;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import javax.security.auth.callback.CallbackHandler;

public class DoctorActivity extends AppCompatActivity {
    LinearLayout lineat_tab1,lineat_tab2,lineat_tab3,lineat_tab4;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();


        lineat_tab1 = (LinearLayout) findViewById(R.id.lineat_tab1);
        lineat_tab2 = (LinearLayout) findViewById(R.id.lineat_tab2);
        lineat_tab3 = (LinearLayout) findViewById(R.id.lineat_tab3);
        lineat_tab4 = (LinearLayout) findViewById(R.id.lineat_tab4);


        lineat_tab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DoctorActivity.this,DoctorProfileActivity.class);
                startActivity(intent);

            }
        });
        lineat_tab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    JSONObject obj_val = new JSONObject(pref.getString("logindoctordetails",""));
                    ChatViewListActivity.str_id=obj_val.getString("phone");

                    Intent intent = new Intent(DoctorActivity.this, ChatViewListActivity.class);
                    ChatViewListActivity.str_mobile=obj_val.getString("phone");
                    ChatViewListActivity.str_id=obj_val.getString("id");
                    startActivity(intent);

                }catch (Exception e)
                {
                    Log.d("Exceptionee", String.valueOf(e));
                }



            }
        });

        lineat_tab4.setOnClickListener(new View.OnClickListener() {
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