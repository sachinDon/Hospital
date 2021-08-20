package com.spiel.hospital;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.msg91.sendotpandroid.library.internal.SendOTP;
import com.msg91.sendotpandroid.library.listners.VerificationListener;
import com.msg91.sendotpandroid.library.roots.RetryType;
import com.msg91.sendotpandroid.library.roots.SendOTPConfigBuilder;
import com.msg91.sendotpandroid.library.roots.SendOTPResponseCode;

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


public class VerificationActivity extends AppCompatActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback, VerificationListener {
    private static final String TAG = "VerificationActivity";
    private static final int OTP_LNGTH = 4;
    TextView resend_timer,smsVerificationButton,text_exitpage;
    private OtpEditText mOtpEditText;
    ProgressDialog progressDialog,progressDialog1;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    String str_mobileno;
    AlertDialog alertDialog_Box;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        resend_timer = (TextView) findViewById(R.id.resend_timer);

        text_exitpage= (TextView) findViewById(R.id.text_exitpage);
        smsVerificationButton = (TextView) findViewById(R.id.smsVerificationButton);
        smsVerificationButton.setBackgroundColor(getResources().getColor(R.color.colorred));
        resend_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResendCode();
            }
        });
        startTimer();
        mOtpEditText = findViewById(R.id.inputCode);
        mOtpEditText.setMaxLength(OTP_LNGTH);
        text_exitpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });
        mOtpEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

                if (mOtpEditText.getText().length() >=4)
                {
                    smsVerificationButton.setBackgroundColor(getResources().getColor(R.color.green_1));
                }
                else
                {
                    smsVerificationButton.setBackgroundColor(getResources().getColor(R.color.colorred));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        enableInputField(true);
        initiateVerification();
    }

    void createVerification(String phoneNumber, int countryCode) {
        new SendOTPConfigBuilder()
                .setCountryCode(countryCode)
                .setMobileNumber(phoneNumber)
                //////////////////direct verification while connect with mobile network/////////////////////////
                .setVerifyWithoutOtp(true)
                //////////////////Auto read otp from Sms And Verify///////////////////////////
                .setAutoVerification(VerificationActivity.this)
                //  .setOtpExpireInMinute(5)//default value is one day
                //  .setOtpHits(3) //number of otp request per number
                //  .setOtpHitsTimeOut(0L)//number of otp request time out reset in milliseconds default is 24 hours
                .setSenderId("ABCDEF")
                .setMessage("##OTP## is Your Pritama Medicals verification code.")
                .setOtpLength(OTP_LNGTH)
                .setVerificationCallBack(this).build();

        SendOTP.getInstance().getTrigger().initiate();


    }


    void initiateVerification() {
        Intent intent = getIntent();
        if (intent != null) {
//            DataManager.getInstance().showProgressMessage(this, "");
            str_mobileno = intent.getStringExtra(OtpmsgActivity.INTENT_PHONENUMBER);

            int countryCode = intent.getIntExtra(OtpmsgActivity.INTENT_COUNTRY_CODE, 0);
            TextView phoneText = (TextView) findViewById(R.id.numberText);
            phoneText.setText("+" + countryCode + str_mobileno);
            createVerification(str_mobileno, countryCode);
        }
    }

    public void ResendCode() {
        startTimer();
        SendOTP.getInstance().getTrigger().resend(RetryType.TEXT);
    }

    public void onSubmitClicked(View view) {
        String code = mOtpEditText.getText().toString();
        if (!code.isEmpty()) {
            hideKeypad();
            verifyOtp(code);
            // DataManager.getInstance().showProgressMessage(this, "");
            progressDialog1 = new ProgressDialog(VerificationActivity.this);
            progressDialog1.setMessage("Verifying..."); // Setting Message
            progressDialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog1.show(); // Display Progress Dialog
            progressDialog1.setCancelable(false);
            progressDialog1.show();

            TextView messageText = (TextView) findViewById(R.id.textView);
            messageText.setText("Verification in progress");
            // enableInputField(false);

        }

    }


    void enableInputField(final boolean enable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View container = findViewById(R.id.inputContainer);
                if (enable) {
                    container.setVisibility(View.VISIBLE);
                    mOtpEditText.requestFocus();
                } else {
                    container.setVisibility(View.GONE);
                }
                TextView resend_timer = (TextView) findViewById(R.id.resend_timer);
                resend_timer.setClickable(false);
                resend_timer.setBackgroundColor(getResources().getColor(R.color.colorred));
                resend_timer.setTextColor(getResources().getColor(R.color.white));
            }
        });

    }

    void hideProgressBarAndShowMessage(int message) {
        hideProgressBar();
        TextView messageText = (TextView) findViewById(R.id.textView);
        messageText.setText(message);
    }

    void hideProgressBar() {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressIndicator);
        progressBar.setVisibility(View.INVISIBLE);
        TextView progressText = (TextView) findViewById(R.id.progressText);
        progressText.setVisibility(View.INVISIBLE);
    }

    void showProgress() {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressIndicator);
        progressBar.setVisibility(View.VISIBLE);
    }

    void showCompleted(boolean isDirect) {
        ImageView checkMark = (ImageView) findViewById(R.id.checkmarkImage);
        if (isDirect) {
            checkMark.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_magic));
        } else {
            checkMark.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_checkmark));
        }
        checkMark.setVisibility(View.VISIBLE);
    }

    public void verifyOtp(String otp) {
        SendOTP.getInstance().getTrigger().verify(otp);
    }


    @Override
    public void onSendOtpResponse(final SendOTPResponseCode responseCode, final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "onSendOtpResponse: " + responseCode.getCode() + "=======" + message);
                Log.e(TAG, "onSendOtpResponse: " + responseCode.SERVER_ERROR_OTP_NOT_VERIFIED + "=======" + message);
                if (responseCode == SendOTPResponseCode.DIRECT_VERIFICATION_SUCCESSFUL_FOR_NUMBER || responseCode == SendOTPResponseCode.OTP_VERIFIED) {

                    if (progressDialog1 !=null)
                    {
                        progressDialog1.dismiss();
                    }
                    enableInputField(false);
                    hideKeypad();
                    TextView textView2 = (TextView) findViewById(R.id.textView2);
                    TextView textView1 = (TextView) findViewById(R.id.textView1);
                    TextView messageText = (TextView) findViewById(R.id.textView);
                    ImageView topImg = (ImageView) findViewById(R.id.topImg);
                    TextView phoneText = (TextView) findViewById(R.id.numberText);
                    RelativeLayout topLayout = findViewById(R.id.topLayout);
                    if (android.os.Build.VERSION.SDK_INT > 16)
                        topLayout.setBackgroundDrawable(ContextCompat.getDrawable(VerificationActivity.this, R.drawable.gradient_bg_white));
                    else
                        topLayout.setBackgroundResource(R.drawable.gradient_bg_white);
                    messageText.setVisibility(View.GONE);
                    phoneText.setVisibility(View.GONE);
                    topImg.setVisibility(View.INVISIBLE);
                    textView1.setVisibility(View.VISIBLE);
                    textView2.setVisibility(View.VISIBLE);
                    if (responseCode == SendOTPResponseCode.DIRECT_VERIFICATION_SUCCESSFUL_FOR_NUMBER)
                        textView2.setText("Mobile verified using Invisible OTP.");
                    else textView2.setText("Your Mobile number has been successfully verified.");

                    hideProgressBarAndShowMessage(R.string.verified);
                    showCompleted(responseCode == SendOTPResponseCode.DIRECT_VERIFICATION_SUCCESSFUL_FOR_NUMBER);
                    progressDialog = new ProgressDialog(VerificationActivity.this);
                    progressDialog.setMessage("Sending..."); // Setting Message
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.show(); // Display Progress Dialog
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    if (OtpmsgActivity.str_pagetype.equalsIgnoreCase("for"))
                    {
                        new communication_forgotpassword().execute();
                    }
                    else
                    {
                        new SendPostRequest().execute();
                    }
                } else if (responseCode == SendOTPResponseCode.READ_OTP_SUCCESS)
                {
                    if (progressDialog1 !=null)
                    {
                        progressDialog1.dismiss();
                    }
                    mOtpEditText.setText(message);

                }
                else if (responseCode == SendOTPResponseCode.SERVER_ERROR_ALREADY_VERIFIED)
                {
                    mOtpEditText.setText(message);
                    if (progressDialog1 !=null)
                    {
                        progressDialog1.dismiss();
                    }


                }
                else if (responseCode == SendOTPResponseCode.SMS_SUCCESSFUL_SEND_TO_NUMBER || responseCode == SendOTPResponseCode.DIRECT_VERIFICATION_FAILED_SMS_SUCCESSFUL_SEND_TO_NUMBER)
                {
                    if (progressDialog1 !=null)
                    {
                        progressDialog1.dismiss();
                    }
                } else if (responseCode == SendOTPResponseCode.NO_INTERNET_CONNECTED)
                {

                    AlertDialog.Builder alert = new AlertDialog.Builder(VerificationActivity.this);
                    alert.setTitle("Opps!");
                    alert.setMessage("Internet connection not availabel, Please check internet connection");

                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alertDialog1 = alert.create();
                    alertDialog1.show();
                    if (progressDialog1 !=null)
                    {
                        progressDialog1.dismiss();
                    }
                }
                else if (responseCode == SendOTPResponseCode.SERVER_ERROR_OTP_NOT_VERIFIED)
                {

                    AlertDialog.Builder alert = new AlertDialog.Builder(VerificationActivity.this);
                    alert.setTitle("Opps!");
                    alert.setMessage("Server could not verified code, Please try again");

                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alertDialog1 = alert.create();
                    alertDialog1.show();
                    if (progressDialog1 !=null)
                    {
                        progressDialog1.dismiss();
                    }
                }
                else
                {

                    hideKeypad();
                    //   hideProgressBarAndShowMessage(R.string.failed);

                    AlertDialog.Builder alert = new AlertDialog.Builder(VerificationActivity.this);
                    alert.setTitle("!Opps");
                    alert.setMessage("Verification Failed!");

                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alertDialog1 = alert.create();
                    alertDialog1.show();
                    enableInputField(true);
                    if (progressDialog1 !=null)
                    {
                        progressDialog1.dismiss();
                    }
                }
            }
        });
    }

    private void startTimer() {
        resend_timer.setClickable(false);
        resend_timer.setBackgroundColor(getResources().getColor(R.color.colorred));
        resend_timer.setTextColor(ContextCompat.getColor(VerificationActivity.this, R.color.white));
        new CountDownTimer(15000, 1000) {
            int secondsLeft = 0;

            public void onTick(long ms) {
                if (Math.round((float) ms / 1000.0f) != secondsLeft) {
                    secondsLeft = Math.round((float) ms / 1000.0f);
                    resend_timer.setText("Resend ( " + secondsLeft + " )");
                }
            }

            public void onFinish() {
                resend_timer.setClickable(true);
                resend_timer.setBackgroundColor(getResources().getColor(R.color.green_1));
                resend_timer.setText("Resend");
                resend_timer.setTextColor(ContextCompat.getColor(VerificationActivity.this, R.color.white));
            }
        }.start();
    }

    private void hideKeypad() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {


            try {

                URL url = new URL(Urlclass.registration);
                JSONObject postDataParams = new JSONObject();


//                postDataParams.put("email",OTPActivity.Stremail);
                postDataParams.put("mobile",str_mobileno);

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
                                Intent intent = new Intent(VerificationActivity.this,RegisterActivity.class);
                                RegisterActivity.str_mobileno= str_mobileno;
                                startActivity(intent);
                            }
                           else if (obj_values.getString("result").equalsIgnoreCase("register"))
                            {
                                editor.putString("login", "yes");
                                editor.commit();
                            Intent intent = new Intent(VerificationActivity.this,DoctorListActivity.class);
                            startActivity(intent);
                            }
                         else
                            {

                                AlertDialog.Builder builder1 = new AlertDialog.Builder(VerificationActivity.this);
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


                            AlertDialog.Builder builder1 = new AlertDialog.Builder(VerificationActivity.this);
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


                                AlertDialog.Builder builder1 = new AlertDialog.Builder(VerificationActivity.this);
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
                AlertDialog.Builder builder1 = new AlertDialog.Builder(VerificationActivity.this);
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
    public class communication_forgotpassword extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... args0) {

            try {
                URL url = new URL(Urlclass.forgotpassword);
                JSONObject postDataParams = new JSONObject();


                postDataParams.put("mobile",str_mobileno);
                postDataParams.put("password",mOtpEditText.getText().toString());
                Log.e("params", postDataParams.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
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

            Log.d("resuult", result);

            if (result !=null)
            {
                if (!result.equalsIgnoreCase(""))
                {

                    if (result.equalsIgnoreCase("noemail"))
                    { AlertDialog.Builder builder2 = new AlertDialog.Builder(VerificationActivity.this);
                        builder2.setTitle("Oops");
                        builder2.setMessage("The email address you have entered is not registered in our system or your account has been deactivated. Please try again.");
                        builder2.setCancelable(false);
                        builder2.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        finish();
                                    }
                                });
                        alertDialog_Box = builder2.create();
                        alertDialog_Box.show();


                    }
                    else if (result.equalsIgnoreCase("sent"))
                    {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(VerificationActivity.this);
                        builder2.setTitle("sucessful");
                        builder2.setMessage("Password has been updated your register mobile number. your password is "+mOtpEditText.getText().toString());
                        builder2.setCancelable(false);
                        builder2.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();

//                                        Intent intent = new Intent(VerificationActivity.this,HomeActivity.class);
//                                        startActivity(intent);
                                    }
                                });
                        alertDialog_Box = builder2.create();
                        alertDialog_Box.show();


                    }
                    else if (result.equalsIgnoreCase("updateerror"))
                    {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(VerificationActivity.this);
                        builder2.setTitle("Oops");
                        builder2.setMessage("Connection time out. Please try again.");
                        builder2.setCancelable(false);
                        builder2.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        finish();
                                    }
                                });
                        alertDialog_Box = builder2.create();
                        alertDialog_Box.show();


                    }
                    else
                    {
                        if (!result.equalsIgnoreCase("") && result.length()>=20)
                        {
                            try {
                                JSONArray jsonarray = new JSONArray(result);

                                JSONObject obj_values=new JSONObject(String.valueOf(jsonarray.getString(0)));


                                if (jsonarray !=null) {

                                    editor.putString("custid", obj_values.getString("custid"));
                                    editor.putString("custname", obj_values.getString("custname"));
                                    editor.putString("userid", obj_values.getString("mobile"));
                                    editor.putString("address1", obj_values.getString("address1"));
                                    // editor.putString("address2",obj_values.getString("address2"));
                                    editor.putString("pincode", obj_values.getString("pincode"));
                                    //pnacard//adarcard//votercard


                                    editor.putString("custname1", obj_values.getString("custname"));
                                    editor.putString("custname2", "");
                                    editor.putString("mobile1", obj_values.getString("mobile"));
                                    editor.putString("mobile2", "");
                                    editor.putString("address1", obj_values.getString("address1"));
                                    editor.putString("address2", "");
                                    editor.putString("pincode1", obj_values.getString("pincode"));
                                    editor.putString("regtype", obj_values.getString("regtype"));
                                    editor.putString("pincode2", "");
                                    editor.commit();


                                    if (obj_values.getString("regtype").equalsIgnoreCase("admin"))
                                    {
                                        editor.putString("login", "yes");
                                        editor.commit();
//                                        Intent intent = new Intent(VerificationActivity.this, AdminPanelActivity.class);
//                                        startActivity(intent);
                                    }
                                    else
                                    {

                                        if (obj_values.getString("otp").equalsIgnoreCase("yes")) {
                                            editor.putString("login", "yes");
                                            editor.commit();
//                                            Intent intent = new Intent(VerificationActivity.this, HomeActivity.class);
//                                            startActivity(intent);
                                        } else {
                                            editor.putString("login", "no");
                                            editor.commit();
//                                  Intent intent = new Intent(LoginActivity.this, OTPActivity.class);
//                                   OTPActivity.Strmobileno = String.valueOf(obj_values.getString("mobile"));
//                                    startActivity(intent);

                                            Intent intent = new Intent(VerificationActivity.this, OtpmsgActivity.class);
                                            intent.putExtra("mobile", obj_values.getString("mobile"));
                                            OtpmsgActivity.str_pagetype = "reg";
                                            startActivity(intent);

                                        }
                                    }


                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            AlertDialog.Builder builder2 = new AlertDialog.Builder(VerificationActivity.this);
                            builder2.setTitle("Oops");
                            builder2.setMessage("Server could not found.");
                            builder2.setCancelable(false);
                            builder2.setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            // finish();
                                        }
                                    });
                            alertDialog_Box = builder2.create();
                            alertDialog_Box.show();

                        }
                    }

                }
                else
                {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(VerificationActivity.this);
                    builder2.setTitle("Oops");
                    builder2.setMessage("Server could not found.");
                    builder2.setCancelable(false);
                    builder2.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    finish();
                                }
                            });
                    alertDialog_Box = builder2.create();
                    alertDialog_Box.show();
                }
            }
            else
            {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(VerificationActivity.this);
                builder2.setTitle("Oops");
                builder2.setMessage("Server could not found.");
                builder2.setCancelable(false);
                builder2.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                finish();
                            }
                        });
                alertDialog_Box = builder2.create();
                alertDialog_Box.show();

            }
            progressDialog.dismiss();
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

    public void onBackPressed() {

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SendOTP.getInstance().getTrigger().stop();
    }
}
