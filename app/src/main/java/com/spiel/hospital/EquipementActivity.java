package com.spiel.hospital;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.SearchView;
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
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class EquipementActivity extends AppCompatActivity {

     String str_productname,str_producttype,str_rate,str_id,str_names,str_desc;
    TextView text_back_equp;
    AlertDialog alertDialog_Box;
    TextView textview_doctlist_eqp;
    RecyclerView recyclerView;
    MyListAdapter adapter;
    JSONArray array_doctorlist;
   ProgressDialog progressDialog;
    AlertDialog alert11;
    SearchView searchview;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipement);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        text_back_equp =(TextView)findViewById(R.id.text_back_equp);
         recyclerView = (RecyclerView) findViewById(R.id.recyclerView_equp_list);
        searchview = (SearchView) findViewById(R.id.serch_equp_searchView);
        array_doctorlist = new JSONArray();

         adapter = new MyListAdapter(array_doctorlist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            public boolean onQueryTextSubmit(String query) {
                Log.d("seach_query", query);
                // do something on text submit
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.listdata = new JSONArray();
                if (array_doctorlist != null)                {
                    if (TextUtils.isEmpty(newText.toString())) {
                        //str_search_txt = "";
                        adapter.listdata = array_doctorlist;
                    } else {
                        for (int i = 0; i < array_doctorlist.length(); i++) {

                            try {
                                String string = array_doctorlist.getJSONObject(i).getString("productname");
                                String str_category = array_doctorlist.getJSONObject(i).getString("producttype");
                                // str_search_txt = newText;
                                if ((string.toLowerCase()).contains(newText.toLowerCase()) || (str_category.toLowerCase()).contains(newText.toLowerCase()) ) {

                                    adapter.listdata.put(array_doctorlist.getJSONObject(i));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }  }}

                    if (recyclerView.getAdapter() != null)
                    {
                        adapter.notifyDataSetChanged();
                    }

                }
                else
                {
                    if (recyclerView.getAdapter() != null)
                    {
                        adapter.notifyDataSetChanged();
                    }
                }
                return false;
            }
        });
        new Equipement_communication().execute();

        text_back_equp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{
        private JSONArray listdata;

        // RecyclerView recyclerView;
        public MyListAdapter(JSONArray listdata) {
            this.listdata = listdata;
        }
        @Override
        public MyListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem= layoutInflater.inflate(R.layout.doctorlist_item, parent, false);
            MyListAdapter.ViewHolder viewHolder = new MyListAdapter.ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyListAdapter.ViewHolder holder, int position) {

            holder.textView_equp_inq.setTag(position);

            try {
                JSONObject obj = new JSONObject(String.valueOf(listdata.getJSONObject(position)));
                holder.textView_doctname.setText("Product Name: "+obj.getString("productname"));
                // holder.imageView_profilelogo.setImageResource(listdata[position].getImgId());
                holder.textView_doctordegree.setText("Product Type: "+obj.getString("producttype"));
                holder.textView_doctor_exp.setText(Html.fromHtml( " <font color=#414141>  "+"Price: "+ " </font> <font color=#00b33c> <b> "+"Rs"+obj.getString("rate")+ " </b> </font>"));

                String str_imageurl = obj.getString("imageurl");

                if (str_imageurl.length() ==0)
                {
                    str_imageurl ="http://www.sachinmokashi";
                }
                Picasso.with(EquipementActivity.this)
                        .load(str_imageurl)
                        .placeholder(R.drawable.defaultdoctor)
                        .into( holder.imageView_equp_logo, new Callback() {
                            @Override
                            public void onSuccess() {


                            }

                            @Override
                            public void onError() {

                            }
                        });

                holder.textView_equp_inq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            JSONObject obj_Val = new JSONObject(String.valueOf(listdata.getJSONObject((Integer) holder.textView_equp_inq.getTag())));
                            str_productname = obj_Val.getString("productname");
                            str_producttype= obj_Val.getString("producttype");
                            str_rate= obj_Val.getString("rate");
                            str_id= obj_Val.getString("id");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        LayoutInflater inflater =EquipementActivity.this.getLayoutInflater();
                        View alertLayout = inflater.inflate(R.layout.custom_enquiry_popup, null);

                        AlertDialog.Builder alert = new AlertDialog.Builder(EquipementActivity.this);
                        alert.setView(alertLayout);
                        alert.setCancelable(false);
                        final AlertDialog dialog1 = alert.create();
                        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog1.show();
                        TextView edittext_cs_enq_name= alertLayout.findViewById(R.id.edittext_cs_enq_name);
                        TextView edittext_cs_desc= alertLayout.findViewById(R.id.edittext_cs_desc);
                        TextView text_cs_enqu_cancel= alertLayout.findViewById(R.id.text_cs_enqu_cancel);
                        TextView text_cs_enq_ok= alertLayout.findViewById(R.id.text_cs_enq_ok);


                        text_cs_enqu_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                str_productname = "";
                                str_producttype= "";
                                str_rate="";
                                str_id= "";
                                str_desc ="";
                                str_id="";
                                edittext_cs_desc.setText("");
                                edittext_cs_enq_name.setText("");
                                 dialog1.dismiss();
                            }
                        });

                        text_cs_enq_ok.setOnTouchListener(new View.OnTouchListener()
                        {
                            View v;
                            private GestureDetector gestureDetector = new GestureDetector(EquipementActivity.this, new GestureDetector.SimpleOnGestureListener() {

                                @Override
                                public boolean onDoubleTap(MotionEvent e)
                                {

                                    Toast.makeText(EquipementActivity.this, "Not allow to double tapped.",Toast.LENGTH_LONG).show();
                                    return super.onDoubleTap(e);
                                }

                                @Override
                                public boolean onSingleTapConfirmed(MotionEvent e) {


                                    ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                                    NetworkInfo netInfo = cm.getActiveNetworkInfo();
                                    if (netInfo != null && netInfo.isConnectedOrConnecting()) {

                                        dialog1.dismiss();
                                        progressDialog = new ProgressDialog(EquipementActivity.this);
                                        progressDialog.setMessage("Loading..."); // Setting Message
                                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                                        progressDialog.show(); // Display Progress Dialog
                                        progressDialog.setCancelable(false);
                                        str_desc = String.valueOf(edittext_cs_desc.getText());
                                        str_names= String.valueOf(edittext_cs_enq_name.getText());
                                        new SendInquieryDetails_Comm().execute();



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
             public ImageView imageView_equp_logo;
            public TextView textView_doctordegree;
            public TextView textView_doctor_exp,textView_doctname,textView_equp_inq;

            public ViewHolder(View itemView) {
                super(itemView);
                this.imageView_equp_logo = (ImageView) itemView.findViewById(R.id.imageView_equp_logo);
                this.textView_doctor_exp = (TextView) itemView.findViewById(R.id.textView_doctor_exp);
                this.textView_doctordegree = (TextView) itemView.findViewById(R.id.textView_doctordegree);
                this.textView_doctname = (TextView) itemView.findViewById(R.id.textView_doctname);
                this.textView_equp_inq = (TextView) itemView.findViewById(R.id.textView_equp_inq);

            }
        }
    }

    public class Equipement_communication extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {


            try {

                URL url = new URL(Urlclass.getequipement);
                JSONObject postDataParams = new JSONObject();


//                postDataParams.put("email",OTPActivity.Stremail);
                postDataParams.put("mobile",pref.getString("userid",""));
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
                        recyclerView.setLayoutManager(new LinearLayoutManager(EquipementActivity.this));
                        recyclerView.setAdapter(adapter);

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            else
            {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(EquipementActivity.this);
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

    public class SendInquieryDetails_Comm extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {


            try {

                URL url = new URL(Urlclass.inquerydetails);
                JSONObject postDataParams = new JSONObject();


                postDataParams.put("mobile","8850519524");
                postDataParams.put("productname",str_productname);
                postDataParams.put("producttype",str_producttype);
                postDataParams.put("rate",str_rate);
                postDataParams.put("id",str_id);
                postDataParams.put("name",str_names);
                postDataParams.put("desc",str_desc);

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


                            AlertDialog.Builder builder1 = new AlertDialog.Builder(EquipementActivity.this);
                            builder1.setTitle("Sucessfull!");
                            builder1.setMessage(obj_values.getString("errormessage"));
                            builder1.setCancelable(false);
                            builder1.setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            alertDialog_Box = builder1.create();
                            alertDialog_Box.show();


                        } else if (obj_values.getString("status").equalsIgnoreCase("0")) {


                            AlertDialog.Builder builder1 = new AlertDialog.Builder(EquipementActivity.this);
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

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(EquipementActivity.this);
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


                        AlertDialog.Builder builder1 = new AlertDialog.Builder(EquipementActivity.this);
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
                AlertDialog.Builder builder1 = new AlertDialog.Builder(EquipementActivity.this);
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
        AlertDialog.Builder builder4 = new AlertDialog.Builder(EquipementActivity.this);
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