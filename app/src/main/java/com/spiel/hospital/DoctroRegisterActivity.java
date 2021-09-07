package com.spiel.hospital;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class DoctroRegisterActivity extends AppCompatActivity {

    public  static Uri uriImage=null;
    private Bitmap bitmap;
    private Uri file;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    String str_encodedImg;


    AlertDialog alertDialog_Box;
    ProgressDialog progressDialog;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    String str_status,timeFrom,totime,str_contact;
    TextView textview_doct_back,text_doct_Fromtime1,text_doct_totime1,text_doct_submit_data;
    RelativeLayout relative_doc_uploadpic;
    ImageView imageview_profile_doc;
    RadioButton radio_doctor_unavialbel,radio_doctor_avialbel;
    EditText edittext_doctusername,edittext_doct_degree,edittext_doct_subjects,edittext_doct_exps;
    EditText edittext_doct_registraionnimber,edittext_doct_address,edittext_pincode,edittext_otherno;
    public static  String str_mobileno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctro_register);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        textview_doct_back = (TextView) findViewById(R.id.textview_doct_back11);
        relative_doc_uploadpic = (RelativeLayout) findViewById(R.id.relative_doc_uploadpic11);
        imageview_profile_doc = (ImageView) findViewById(R.id.imageview_profile_doc11);
        edittext_doctusername = (EditText) findViewById(R.id.edittext_doctusername11);
        edittext_doct_degree = (EditText) findViewById(R.id.edittext_doct_degree11);
        edittext_doct_subjects = (EditText) findViewById(R.id.edittext_doct_subjects11);
        edittext_doct_exps = (EditText) findViewById(R.id.edittext_doct_exps11);
        edittext_doct_registraionnimber = (EditText) findViewById(R.id.edittext_doct_registraionnimber11);
        edittext_doct_address = (EditText) findViewById(R.id.edittext_doct_address11);
        edittext_pincode = (EditText) findViewById(R.id.edittext_pincode11);
        edittext_otherno = (EditText) findViewById(R.id.edittext_otherno11);
        radio_doctor_unavialbel = (RadioButton) findViewById(R.id.radio_doctor_unavialbel11);
        radio_doctor_avialbel = (RadioButton) findViewById(R.id.radio_doctor_avialbel11);
        text_doct_Fromtime1 = (TextView) findViewById(R.id.text_doct_Fromtime111);
        text_doct_totime1 = (TextView) findViewById(R.id.text_doct_totime111);
        text_doct_submit_data = (TextView) findViewById(R.id.text_doct_submit_data11);


        relative_doc_uploadpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(DoctroRegisterActivity.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                    selectImage();
                }
                else
                    ActivityCompat.requestPermissions(DoctroRegisterActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

            }
        });

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
                TimePickerDialog timePickerDialog = new TimePickerDialog(DoctroRegisterActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, false);
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(DoctroRegisterActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, false);
                timePickerDialog.setTitle("Choose hour:");
                timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                timePickerDialog.show();


            }
        });

        text_doct_submit_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(DoctroRegisterActivity.this);
                progressDialog.setMessage("Sending..."); // Setting Message
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show(); // Display Progress Dialog
                progressDialog.setCancelable(false);
                progressDialog.show();
                new Doctorprofile_Communication().execute();

            }
        });

        radio_doctor_unavialbel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((RadioButton) v).isChecked();
                // Check which radiobutton was pressed

                str_status = "NotAvailable";
                radio_doctor_avialbel.setChecked(false);
                radio_doctor_unavialbel.setChecked(true);

            }
        });

        radio_doctor_avialbel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((RadioButton) v).isChecked();
                // Check which radiobutton was pressed

                str_status = "Available";
                radio_doctor_avialbel.setChecked(true);
                radio_doctor_unavialbel.setChecked(false);

            }
        });


    }


    public class Doctorprofile_Communication extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {


            try {

                URL url = new URL(Urlclass.doctorregister1);
                JSONObject postDataParams = new JSONObject();

                postDataParams.put("mobile",str_mobileno);
                postDataParams.put("name", edittext_doctusername.getText());
                postDataParams.put("specialist", edittext_doct_subjects.getText());
                postDataParams.put("degree", edittext_doct_degree.getText());
                postDataParams.put("exp", edittext_doct_exps.getText());
                postDataParams.put("regno", edittext_doct_registraionnimber.getText());
                postDataParams.put("address", edittext_doct_address.getText());
                postDataParams.put("pincode", edittext_pincode.getText());
                postDataParams.put("fromtime", text_doct_Fromtime1.getText());
                postDataParams.put("totime", text_doct_totime1.getText());
                postDataParams.put("status", str_status);
                postDataParams.put("mobile1", edittext_otherno.getText());
                postDataParams.put("image",str_encodedImg);

//Photo

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

                            JSONArray array_result = new JSONArray(obj_values.getString("result"));
                            JSONObject obj_newResult = new JSONObject(String.valueOf(array_result.get(0)));


                            AlertDialog.Builder builder1 = new AlertDialog.Builder(DoctroRegisterActivity.this);
                            builder1.setTitle("Sucessfull!");
                            builder1.setMessage(obj_values.getString("errormessage"));
                            builder1.setCancelable(false);
                            builder1.setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();

                                            editor.putString("logindoctordetails", String.valueOf(obj_newResult));
                                            editor.putString("logindoctor","yes");
                                            editor.commit();
                                            Intent intent = new Intent(DoctroRegisterActivity.this,DoctorActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                            alertDialog_Box = builder1.create();
                            alertDialog_Box.show();


                        } else if (obj_values.getString("status").equalsIgnoreCase("0")) {


                            AlertDialog.Builder builder1 = new AlertDialog.Builder(DoctroRegisterActivity.this);
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

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(DoctroRegisterActivity.this);
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


                        AlertDialog.Builder builder1 = new AlertDialog.Builder(DoctroRegisterActivity.this);
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
                AlertDialog.Builder builder1 = new AlertDialog.Builder(DoctroRegisterActivity.this);
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
    private void selectImage() {



        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };



        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(DoctroRegisterActivity.this);

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo"))

                {


                    dialog.dismiss();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    file = Uri.fromFile(getOutputMediaFile());
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

                    startActivityForResult(intent, 100);
                }

                else if (options[item].equals("Choose from Gallery"))

                {

                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(intent, 200);



                }

                else if (options[item].equals("Cancel")) {

                    str_encodedImg ="";
                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                Log.d("CameraDemo", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                //ProfileImg.setImageBitmap(loadBitmap(String.valueOf(file)));

                if (file != null) {

                    bitmap = loadBitmap(String.valueOf(file));

                    Handler refresh = new Handler(Looper.getMainLooper());
                    refresh.post(new Runnable() {
                        public void run() {


                            if (bitmap.getHeight() >= 500 && bitmap.getWidth() >= 500) {
                                bitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false);

                            }
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, bytes);


                            imageview_profile_doc.setImageBitmap(bitmap);

                            byte[] imageBytes = bytes.toByteArray();
                            str_encodedImg = Base64.encodeToString(imageBytes, Base64.DEFAULT);


                        }
                    });


                }
            }
        } else if (requestCode == 200) {


            if (data != null) {

                Uri selectedImage = data.getData();
                try {

                    editor.putString("select_photo", "no");
                    editor.commit();

                    bitmap = MediaStore.Images.Media.getBitmap(DoctroRegisterActivity.this.getContentResolver(), selectedImage);

                    Handler refresh = new Handler(Looper.getMainLooper());
                    refresh.post(new Runnable() {
                        public void run() {

                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();

                            if (bitmap.getHeight() >= 500 && bitmap.getWidth() >= 500) {
                                bitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false);

                            }
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
                            imageview_profile_doc.setImageBitmap(bitmap);
                            byte[] imageBytes = bytes.toByteArray();

                            str_encodedImg = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }

    }
    public Bitmap loadBitmap(String url)
    {
        Bitmap bm = null;
        InputStream is = null;
        BufferedInputStream bis = null;
        try
        {
            URLConnection conn = new URL(url).openConnection();
            conn.connect();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is, 8192);
            bm = BitmapFactory.decodeStream(bis);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if (bis != null)
            {
                try
                {
                    bis.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (is != null)
            {
                try
                {
                    is.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        return bm;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {


            }
        }
    }


}