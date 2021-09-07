package com.spiel.hospital;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class LoginDoctorActivity extends AppCompatActivity {

    String str_logintype;
    AlertDialog alertDialog_Box;
    ProgressDialog progressDialog;
    private TextView text_loginbtn;
    private CheckBox checkbox_hospital,checkbox_doctor,checkbox_update_regno,checkbox_update_mobile;
    private EditText edittext_login_userid,edittext_login_password,edittext__up_newmobile,edittext_up_regno;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_doctor);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        text_loginbtn = (TextView) findViewById(R.id.text_loginbtn);
        edittext_login_userid = (EditText) findViewById(R.id.edittext_login_userid);
        edittext_login_password = (EditText) findViewById(R.id.edittext_login_password);

        edittext__up_newmobile = (EditText) findViewById(R.id.edittext__up_newmobile);
        edittext_up_regno = (EditText) findViewById(R.id.edittext_up_regno);

        checkbox_update_regno = (CheckBox) findViewById(R.id.checkbox_update_regno);
                checkbox_update_mobile = (CheckBox) findViewById(R.id.checkbox_update_mobile);
        checkbox_doctor = (CheckBox)findViewById(R.id.checkbox_doctorf);
        checkbox_hospital = (CheckBox)findViewById(R.id.checkbox_hospitalf);

        checkbox_doctor.setChecked(false);
        checkbox_hospital.setChecked(true);
        str_logintype="hospital";


        checkbox_update_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkbox_update_mobile.isChecked() == true)
                {
                    edittext__up_newmobile.setText("");
                    edittext__up_newmobile.setVisibility(View.VISIBLE);
                }
                else
                {
                    edittext__up_newmobile.setVisibility(View.GONE);
                }


            }
        });
        checkbox_update_regno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (checkbox_update_regno.isChecked() == true)
                {
                    edittext_up_regno.setVisibility(View.VISIBLE);
                }
                else
                {
                    edittext_up_regno.setText("");
                    edittext_up_regno.setVisibility(View.GONE);

                }
            }
        });



        checkbox_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_logintype="doctor";
                checkbox_doctor.setChecked(true);
                checkbox_hospital.setChecked(false);
            }
        });
        checkbox_hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_logintype="hospital";
                checkbox_doctor.setChecked(false);
                checkbox_hospital.setChecked(true);


            }
        });

        text_loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (edittext_login_userid.getText().length() != 0 && edittext_login_password.getText().length() !=0 )
                {
                 if (checkbox_update_mobile.isChecked() == true && checkbox_update_regno.isChecked() == true)
                {
                    if (edittext__up_newmobile.getText().length() != 0 && edittext_up_regno.getText().length()!= 0)
                    {
                       UpdateData();
                    }
                    else if (edittext__up_newmobile.getText().length() == 0 && edittext_up_regno.getText().length()== 0)
                    {
                        ErrorPopup("Please enter new mobile number and registration number");
                    }
                    else if (edittext__up_newmobile.getText().length() != 0 && edittext_up_regno.getText().length()== 0)
                    {
                        ErrorPopup("Please enter new registration number");
                    }
                    else if (edittext__up_newmobile.getText().length() == 0 && edittext_up_regno.getText().length() != 0)
                    {
                        ErrorPopup("Please enter new mobile number");
                    }
                    else
                    {
                        ErrorPopup("Please select updated records");
                    }
                }
                else if (checkbox_update_mobile.isChecked() == true && checkbox_update_regno.isChecked() == false)
                {
                    if (edittext__up_newmobile.getText().length() == 0)
                    {
                        ErrorPopup("Please enter new mobile number");
                    }
                    else
                    {
                        UpdateData();
                    }
                }
                else if (checkbox_update_mobile.isChecked() == false && checkbox_update_regno.isChecked() == true )
                {
                    if (edittext_up_regno.getText().length()==0)
                    {
                        ErrorPopup("Please enter new registration number");
                    }
                    else
                    {
                        UpdateData();
                    }
                }
                else
                 {
                     ErrorPopup("Please select updated records");
                 }
                }

                else {

                    ErrorPopup("Please enter both  old mobile number and old registration number ");
                }


            }
        });

    }
    public  void UpdateData()
    {
        new DoctorLoginCommunication().execute();
        progressDialog = new ProgressDialog(LoginDoctorActivity.this);
        progressDialog.setMessage("Sending..."); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    public void ErrorPopup(String str_msg)
    {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginDoctorActivity.this);
        builder1.setTitle("Oopps!");
        builder1.setMessage(str_msg);
        builder1.setCancelable(false);
        builder1.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();


                    }
                });
        alertDialog_Box = builder1.create();
        alertDialog_Box.show();
    }
    public class DoctorLoginCommunication extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {


            try {

                URL url = new URL(Urlclass.doctorloginupdate);
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("mobile", edittext_login_userid.getText());
                postDataParams.put("regno", edittext_login_password.getText());
                postDataParams.put("mobile1", edittext__up_newmobile.getText());
                postDataParams.put("regno1", edittext_up_regno.getText());
                postDataParams.put("type", str_logintype);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(60000 /* milliseconds */);
                conn.setConnectTimeout(60000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {


            if (!result.equalsIgnoreCase("")) {

                JSONArray jsonarray = null;
                JSONObject obj_values = null;
                try {
                    jsonarray = new JSONArray(result);
                    obj_values = new JSONObject(jsonarray.getString(0));

                    if (jsonarray != null) {

                        if (obj_values.getString("status").equalsIgnoreCase("1"))
                        {

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginDoctorActivity.this);
                            builder1.setTitle("Sucessfull!");
                            builder1.setMessage(obj_values.getString("errormessage"));
                            builder1.setCancelable(false);
                            builder1.setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();

                                            finish();
                                        }
                                    });
                            alertDialog_Box = builder1.create();
                            alertDialog_Box.show();


                     } else if (obj_values.getString("status").equalsIgnoreCase("0")) {


                            AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginDoctorActivity.this);
                            builder1.setTitle("Oops");
                            builder1.setMessage(obj_values.getString("errormessage"));
                            builder1.setCancelable(false);
                            builder1.setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            // finish();
                                        }
                                    });
                            alertDialog_Box = builder1.create();
                            alertDialog_Box.show();
                        } else {

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginDoctorActivity.this);
                            builder1.setTitle("Oops");
                            builder1.setMessage("Server encountered an error . Please try again later.");
                            builder1.setCancelable(false);
                            builder1.setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            // finish();
                                        }
                                    });
                            alertDialog_Box = builder1.create();
                            alertDialog_Box.show();

                        }

                    } else {


                        AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginDoctorActivity.this);
                        builder1.setTitle("Oops");
                        builder1.setMessage("Server encountered an error. Please try again later.");
                        builder1.setCancelable(false);
                        builder1.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        // finish();
                                    }
                                });
                        alertDialog_Box = builder1.create();
                        alertDialog_Box.show();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


            else
            {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginDoctorActivity.this);
                builder1.setTitle("Oops");
                builder1.setMessage("Server encountered an error in verifying your mobile number. Please try again later.");
                builder1.setCancelable(false);
                builder1.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                // finish();
                            }
                        });
                alertDialog_Box = builder1.create();
                alertDialog_Box.show();
            }

            progressDialog.dismiss();

//                else
//                {
//                    android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(OtpVerifyActivity.this);
//                    builder1.setTitle("Server timeout");
//                    builder1.setMessage("Server timeout");
//                    builder1.setCancelable(false);
//                    builder1.setPositiveButton("Ok",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                    // finish();
//                                }
//                            });
//                    alertDialog_Box = builder1.create();
//                    alertDialog_Box.show();





        }
    }
    public static String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

   
}