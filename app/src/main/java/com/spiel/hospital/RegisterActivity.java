package com.spiel.hospital;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
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

public class RegisterActivity extends AppCompatActivity {

    AlertDialog alertDialog_Box;
    public  static  String str_mobileno;
    ProgressDialog progressDialog1;
    EditText edittext_reg_hospitalname,edittext_reg_hospitalregno,edittext_reg_mobile;
    TextView text_regbtn;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        edittext_reg_hospitalname = (EditText) findViewById(R.id.edittext_reg_hospitalname);
        edittext_reg_hospitalregno = (EditText) findViewById(R.id.edittext_reg_hospitalregno);
        edittext_reg_mobile = (EditText) findViewById(R.id.edittext_reg_mobile);
        edittext_reg_mobile.setText(str_mobileno);
        text_regbtn = (TextView) findViewById(R.id.text_regbtn);

        text_regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                progressDialog1 = new ProgressDialog(RegisterActivity.this);
                progressDialog1.setMessage("Register..."); // Setting Message
                progressDialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog1.show(); // Display Progress Dialog
                progressDialog1.setCancelable(false);
                progressDialog1.show();

                new SendPostRequest().execute();
            }
        });
    }

    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {


            try {

                URL url = new URL(Urlclass.registration1);
                JSONObject postDataParams = new JSONObject();


//                postDataParams.put("email",OTPActivity.Stremail);
                postDataParams.put("mobile",str_mobileno);
                postDataParams.put("hname",edittext_reg_hospitalname.getText());
                postDataParams.put("hregno",edittext_reg_hospitalregno.getText());

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
                try {
                    JSONArray jsonarray = new JSONArray(result);

                    JSONObject obj_values = new JSONObject(String.valueOf(jsonarray.getString(0)));



                    if (jsonarray != null) {

                        if (obj_values.getString("status").equalsIgnoreCase("1")) {

                            if (obj_values.getString("result").equalsIgnoreCase("noregister"))
                            {
                                Intent intent = new Intent(RegisterActivity.this,OtpmsgActivity.class);
                                startActivity(intent);
                            }
                            else if (obj_values.getString("result").equalsIgnoreCase("register"))
                            {

                                AlertDialog.Builder builder1 = new AlertDialog.Builder(RegisterActivity.this);
                                builder1.setTitle("Sucessfull!");
                                builder1.setMessage(obj_values.getString("errormessage"));
                                builder1.setCancelable(false);
                                builder1.setPositiveButton("Ok",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();

                                                editor.putString("login", "yes");
                                                editor.commit();

                                                Intent intent = new Intent(RegisterActivity.this,DoctorListActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                                alertDialog_Box = builder1.create();
                                alertDialog_Box.show();

                            }
                            else
                            {

                                AlertDialog.Builder builder1 = new AlertDialog.Builder(RegisterActivity.this);
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

                        }
                        else if (obj_values.getString("result").equalsIgnoreCase("0"))
                        {


                            AlertDialog.Builder builder1 = new AlertDialog.Builder(RegisterActivity.this);
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
                        }
                        else

                        {


                            AlertDialog.Builder builder1 = new AlertDialog.Builder(RegisterActivity.this);
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
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            else
            {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(RegisterActivity.this);
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

            progressDialog1.dismiss();

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