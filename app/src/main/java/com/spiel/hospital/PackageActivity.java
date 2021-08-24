package com.spiel.hospital;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class PackageActivity extends AppCompatActivity {
 TextView text_submitpckg;

    String str_id,str_selectpkg,str_selectprice,str_dept;
    AlertDialog alertDialog_Box;
    RecyclerView recyclerView;
    MyListAdapter adapter;
    JSONArray array_doctorlist;
    ProgressDialog progressDialog;
    AlertDialog alert11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package);



         recyclerView = (RecyclerView) findViewById(R.id.recyclerView_pckage_list);
        array_doctorlist = new JSONArray();

        adapter = new MyListAdapter(array_doctorlist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        text_submitpckg = (TextView)findViewById(R.id.text_submitpckg);

        new GetPackages_communication().execute();

        text_submitpckg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(PackageActivity.this,DoctorListActivity.class);
                startActivity(intent);

            }
        });
    }


    public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{
        private JSONArray listdata;
        //{"id":"2","dept":"Orthopedic","monthly":"100","price":"300","halfyearly":"600","yearly":"1200"}
        // RecyclerView recyclerView;
        public MyListAdapter(JSONArray listdata) {
            this.listdata = listdata;
        }
        @Override
        public MyListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem= layoutInflater.inflate(R.layout.list_packages, parent, false);
            MyListAdapter.ViewHolder viewHolder = new MyListAdapter.ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyListAdapter.ViewHolder holder, int position) {


                holder.relative_slects_custveg1.setTag(position);
            holder.text_add_pck.setTag(position);

                try {
                    JSONObject obj = new JSONObject(String.valueOf(listdata.getJSONObject(position)));

                    //{"id":"2","dept":"Cardiology","Monthly":"610","Quarterly":"4000","Halfyearly":"4500","Yearly":"6000","selectpkg":"","selectprice":"610"}

                    holder.text_setprice_custveg .setText(obj.getString("selectpkg"));
                    holder.text_spc_namae_pkg .setText(obj.getString("dept"));
                    holder.textview_pck_price.setText(Html.fromHtml( " <font color=#414141>  "+"Price: "+ " </font> <font color=#00b33c> <b> "+"Rs"+obj.getString("selectprice")+ " </b> </font>"));



                    holder.text_add_pck.setOnTouchListener(new View.OnTouchListener()
                    {
                        View v;
                        private GestureDetector gestureDetector = new GestureDetector(PackageActivity.this, new GestureDetector.SimpleOnGestureListener() {
                            final Integer tagid = 0;//(Integer)v.getTag();
                            @Override
                            public boolean onDoubleTap(MotionEvent e)
                            {

                                Toast.makeText(PackageActivity.this, "Not allow to double tapped.",Toast.LENGTH_LONG).show();
                                return super.onDoubleTap(e);
                            }

                            @Override
                            public boolean onSingleTapConfirmed(MotionEvent e) {


                                ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                                if (netInfo != null && netInfo.isConnectedOrConnecting()) {

                                    try {
                                        JSONObject objectval = new JSONObject(String.valueOf(listdata.getJSONObject(tagid)));


                                        str_id = objectval.getString("id");
                                        str_selectpkg = objectval.getString("selectpkg");
                                        str_selectprice = objectval.getString("selectprice");
                                        str_dept  = objectval.getString("dept");

                                        progressDialog = new ProgressDialog(PackageActivity.this);
                                        progressDialog.setMessage("Loading..."); // Setting Message
                                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                                        progressDialog.show(); // Display Progress Dialog
                                        progressDialog.setCancelable(false);
                                        new Addpackages_Comm().execute();

                                    }
                                    catch (Exception ee)
                                    {

                                    }
                                }
                                else
                                {
                                    Connections();
                                }

                                return super.onSingleTapConfirmed(e);
                            }



                        });

                        @Override
                        public boolean onTouch(View v1, MotionEvent event) {

                            v = v1;

                            gestureDetector.onTouchEvent(event);
                            return true;
                        }
                    });


                    holder.relative_slects_custveg1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            final Integer tagid = (Integer)view.getTag();


                          //  JSONObject objectval = null;
                            try {
                                JSONObject objectval = new JSONObject(String.valueOf(listdata.getJSONObject(tagid)));


                                str_id = objectval.getString("id");
                                str_selectpkg = objectval.getString("selectpkg");
                                str_selectprice = objectval.getString("selectprice");



                                PopupMenu menu = new PopupMenu(PackageActivity.this, view);

                                menu.getMenu().add("Monthly");  // menus items
                                menu.getMenu().add("Quarterly");  // menus items
                                menu.getMenu().add("Halfyearly");
                                menu.getMenu().add("Yearly");
                                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {

                                       try {
                                           str_selectpkg = String.valueOf(item);
                                            str_selectprice = objectval.getString(String.valueOf(item));
                                            holder.text_setprice_custveg.setText(String.valueOf(item));
                                            holder.textview_pck_price.setText(Html.fromHtml( " <font color=#414141>  "+"Price: "+ " </font> <font color=#00b33c> <b> "+"Rs"+objectval.getString(String.valueOf(item))+ " </b> </font>"));

                                           objectval.put("selectpkg",str_selectpkg);
                                           objectval.put("selectprice",str_selectprice);
                                          listdata.put(tagid,objectval);
                                          adapter.notifyDataSetChanged();

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                        return false;
                                    }
                                });

                                menu.show();


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }

        }


        @Override
        public int getItemCount() {
            return listdata.length();
        }

        public  class ViewHolder extends RecyclerView.ViewHolder {

            public TextView text_setprice_custveg;
            public TextView text_spc_namae_pkg,textview_pck_price,text_add_pck;
            public RelativeLayout relative_slects_custveg1;
            public ViewHolder(View itemView) {
                super(itemView);
                this.text_setprice_custveg = (TextView) itemView.findViewById(R.id.text_setprice_custveg);
                this.text_spc_namae_pkg = (TextView) itemView.findViewById(R.id.text_spc_namae_pkg);
                this.textview_pck_price = (TextView) itemView.findViewById(R.id.textview_pck_price);
                this.text_add_pck = (TextView) itemView.findViewById(R.id.text_add_pck);
                this.relative_slects_custveg1 = (RelativeLayout)itemView.findViewById(R.id.relative_slects_custveg1);
            }
        }
    }
    //

    public class Addpackages_Comm extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {


            try {

                URL url = new URL(Urlclass.addpackage);
                JSONObject postDataParams = new JSONObject();


//                postDataParams.put("email",OTPActivity.Stremail);
                postDataParams.put("mobile","8850519524");
                postDataParams.put("id",str_id);
                postDataParams.put("dept",str_dept);
                postDataParams.put("selectpkg",str_dept);
                postDataParams.put("selectprice",str_id);

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
                                Intent intent = new Intent(PackageActivity.this,OtpmsgActivity.class);
                                startActivity(intent);
                            }
                            else if (obj_values.getString("result").equalsIgnoreCase("register"))
                            {

                                AlertDialog.Builder builder1 = new AlertDialog.Builder(PackageActivity.this);
                                builder1.setTitle("Sucessfull!");
                                builder1.setMessage(obj_values.getString("errormessage"));
                                builder1.setCancelable(false);
                                builder1.setPositiveButton("Ok",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();


                                                Intent intent = new Intent(PackageActivity.this,DoctorListActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                                alertDialog_Box = builder1.create();
                                alertDialog_Box.show();

                            }
                            else
                            {

                                AlertDialog.Builder builder1 = new AlertDialog.Builder(PackageActivity.this);
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


                            AlertDialog.Builder builder1 = new AlertDialog.Builder(PackageActivity.this);
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


                            AlertDialog.Builder builder1 = new AlertDialog.Builder(PackageActivity.this);
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
                AlertDialog.Builder builder1 = new AlertDialog.Builder(PackageActivity.this);
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

    public class GetPackages_communication extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {


            try {

                URL url = new URL(Urlclass.getpackage);
                JSONObject postDataParams = new JSONObject();


//                postDataParams.put("email",OTPActivity.Stremail);
                postDataParams.put("mobile","");
                postDataParams.put("id","");

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




            if (!result.equalsIgnoreCase(""))
            {
                //nodata
                try {
                    array_doctorlist = new JSONArray(result);




                    if (array_doctorlist != null)
                    {
                        adapter = new MyListAdapter(array_doctorlist);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(PackageActivity.this));
                        recyclerView.setAdapter(adapter);

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            else
            {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(PackageActivity.this);
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

    public  void Connections()
    {
        AlertDialog.Builder builder4 = new AlertDialog.Builder(PackageActivity.this);
        builder4.setTitle("No Internet");
        builder4.setMessage("Your internet connection not available");
        builder4.setCancelable(false);
        builder4.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });
        alert11 = builder4.create();
        alert11.show();
    }

}