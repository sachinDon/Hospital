package com.spiel.hospital;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Locale;

public class DoctorProfileActivity extends AppCompatActivity {

    String str_status,timeFrom,totime;
    TextView textview_doct_back,text_doct_Fromtime1,text_doct_totime1,text_doct_submit_data;
    RelativeLayout relative_doc_uploadpic;
    ImageView imageview_profile_doc;
    RadioButton radio_doctor_unavialbel,radio_doctor_avialbel;
    EditText edittext_doctusername,edittext_doct_degree,edittext_doct_subjects,edittext_doct_exps;
    EditText edittext_doct_registraionnimber,edittext_doct_address,edittext_pincode,edittext_otherno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);

        textview_doct_back = (TextView) findViewById(R.id.textview_doct_back);
        relative_doc_uploadpic = (RelativeLayout) findViewById(R.id.relative_doc_uploadpic);
        imageview_profile_doc = (ImageView) findViewById(R.id.imageview_profile_doc);
        edittext_doctusername = (EditText) findViewById(R.id.edittext_doctusername);
        edittext_doct_degree = (EditText) findViewById(R.id.edittext_doct_degree);
        edittext_doct_subjects = (EditText) findViewById(R.id.edittext_doct_subjects);
        edittext_doct_exps = (EditText) findViewById(R.id.edittext_doct_exps);
        edittext_doct_registraionnimber = (EditText) findViewById(R.id.edittext_doct_registraionnimber);
        edittext_doct_address = (EditText) findViewById(R.id.edittext_doct_address);
        edittext_pincode = (EditText) findViewById(R.id.edittext_pincode);
        edittext_otherno = (EditText) findViewById(R.id.edittext_otherno);
        radio_doctor_unavialbel = (RadioButton) findViewById(R.id.radio_doctor_unavialbel);
        radio_doctor_avialbel = (RadioButton) findViewById(R.id.radio_doctor_avialbel);
        text_doct_Fromtime1 = (TextView) findViewById(R.id.text_doct_Fromtime1);
        text_doct_totime1 = (TextView) findViewById(R.id.text_doct_totime1);
        text_doct_submit_data = (TextView) findViewById(R.id.text_doct_submit_data);


        textview_doct_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        text_doct_Fromtime1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar myCalender = Calendar.getInstance();
                int hour = myCalender.get(Calendar.HOUR_OF_DAY);
                int minute = myCalender.get(Calendar.MINUTE);


                TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (view.isShown()) {
                            myCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            myCalender.set(Calendar.MINUTE, minute);
                            //

                            int hour = hourOfDay % 12;
                            if (hour == 0) hour = 12;
                            String _AM_PM = (hourOfDay > 12) ? "PM" : "AM";
                            System.out.println(String.format(Locale.getDefault(), "%02d:%02d %s", hour, minute, _AM_PM));
                            text_doct_Fromtime1.setText( hour+":"+minute +" "+_AM_PM);
                        }
                    }
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(DoctorProfileActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, false);
                timePickerDialog.setTitle("Choose hour:");
                timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                timePickerDialog.show();

            }
        });

        text_doct_totime1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar myCalender = Calendar.getInstance();
                int hour = myCalender.get(Calendar.HOUR_OF_DAY);
                int minute = myCalender.get(Calendar.MINUTE);


                TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (view.isShown()) {
                            myCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            myCalender.set(Calendar.MINUTE, minute);
                           //

                            int hour = hourOfDay % 12;
                            if (hour == 0) hour = 12;
                            String _AM_PM = (hourOfDay > 12) ? "PM" : "AM";
                            System.out.println(String.format(Locale.getDefault(), "%02d:%02d %s", hour, minute, _AM_PM));
                            text_doct_totime1.setText( hour+":"+minute +" "+_AM_PM);
                        }
                    }
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(DoctorProfileActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, false);
                timePickerDialog.setTitle("Choose hour:");
                timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                timePickerDialog.show();


            }
        });

        text_doct_submit_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        radio_doctor_unavialbel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((RadioButton) v).isChecked();
                // Check which radiobutton was pressed

                    str_status = "UnAvailble";
                    radio_doctor_avialbel.setChecked(false);
                radio_doctor_unavialbel.setChecked(true);

            }
        });

        radio_doctor_avialbel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((RadioButton) v).isChecked();
                // Check which radiobutton was pressed

                    str_status = "Availble";
                radio_doctor_avialbel.setChecked(true);
                radio_doctor_unavialbel.setChecked(false);

            }
        });






    }
}