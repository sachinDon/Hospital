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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;

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

public class ChatActivity extends AppCompatActivity {

    AlertDialog alertDialog_Box;
    TextView text_chat_send,textview_doctlist_chatname;
    EditText edittext_chat_text;
    RecyclerView recyclerView;
    MyListAdapter adapter;
    JSONArray array_doctorlist;
    ProgressDialog progressDialog;
    public static String str_reciverid,str_chatname;
    Runnable mToastRunnable;
    Handler mHandler = new Handler();
    Integer int_arraycount;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    String str_scroll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        text_chat_send = (TextView) findViewById(R.id.text_chat_send);
        textview_doctlist_chatname = (TextView) findViewById(R.id.textview_doctlist_chatname);
        edittext_chat_text = (EditText) findViewById(R.id.edittext_chat_text);

        str_scroll ="no";
        int_arraycount=0;
        array_doctorlist = new JSONArray();
        textview_doctlist_chatname.setText(str_chatname);

        text_chat_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(ChatActivity.this);
                progressDialog.setMessage("Sending..."); // Setting Message
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                progressDialog.show(); // Display Progress Dialog
                progressDialog.setCancelable(false);
                new Sendchat_communication().execute();
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_doctorlist_chat);
        adapter = new MyListAdapter(array_doctorlist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


    }

    @Override
    protected void onResume() {
        super.onResume();

        //create runnable for delay
        mToastRunnable = new Runnable() {
            @Override
            public void run() {
                mHandler.postDelayed(this, 10000);
                new Getchat_communication().execute();
            }
        };
//start
        mToastRunnable.run();

//stop

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mToastRunnable);
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
            View listItem= layoutInflater.inflate(R.layout.list_chat, parent, false);
            MyListAdapter.ViewHolder viewHolder = new MyListAdapter.ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyListAdapter.ViewHolder holder, int position) {

            try {
                JSONObject obj = new JSONObject(String.valueOf(listdata.getJSONObject(position)));

                if (obj.getString("reciverid").equalsIgnoreCase(str_reciverid))
                {
                    holder.text_sender_chat.setVisibility(View.VISIBLE);
                    holder.text_reciver_chat.setVisibility(View.GONE);
                    holder.text_sender_chat.setText(obj.getString("message"));

                }
                else
                {
                    holder.text_sender_chat.setVisibility(View.GONE);
                    holder.text_reciver_chat.setVisibility(View.VISIBLE);
                    holder.text_reciver_chat.setText(obj.getString("message"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }



        }


        @Override
        public int getItemCount() {
            return listdata.length();
        }

        public  class ViewHolder extends RecyclerView.ViewHolder {


            TextView text_sender_chat,text_reciver_chat;
            public ViewHolder(View itemView) {
                super(itemView);
                this.text_sender_chat = (TextView) itemView.findViewById(R.id.text_sender_chat);
                this.text_reciver_chat = (TextView) itemView.findViewById(R.id.text_reciver_chat);

            }
        }
    }

    public class Getchat_communication extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {


            try {

                URL url = new URL(Urlclass.getchat);
                JSONObject postDataParams = new JSONObject();

                if (pref.getString("login","").equalsIgnoreCase("yes"))
                {
                   postDataParams.put("senderid",pref.getString("userid",""));
                    //postDataParams.put("senderid",ChatViewListActivity.str_id);
                    postDataParams.put("reciverid",str_reciverid);
                }
                else if(pref.getString("logindoctor","").equalsIgnoreCase("yes"))
                {
                    // postDataParams.put("senderid",pref.getString("userid",""));
                    postDataParams.put("senderid",ChatViewListActivity.str_id);
                    postDataParams.put("reciverid",str_reciverid);
                }


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

                        if (int_arraycount != array_doctorlist.length()) {

                            int_arraycount= array_doctorlist.length();
                            adapter = new MyListAdapter(array_doctorlist);
                            //recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
                            recyclerView.setAdapter(adapter);
                            recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount()-1);

                        }


                       //

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            else
            {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ChatActivity.this);
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
    public class Sendchat_communication extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {


            try {

                URL url = new URL(Urlclass.chat);
                JSONObject postDataParams = new JSONObject();


                if (pref.getString("login","").equalsIgnoreCase("yes"))
                {
                    postDataParams.put("senderid",pref.getString("userid",""));
                    postDataParams.put("reciverid",str_reciverid);
                }
                else if(pref.getString("logindoctor","").equalsIgnoreCase("yes"))
                {
                    postDataParams.put("senderid",ChatViewListActivity.str_id);
                    postDataParams.put("reciverid",str_reciverid);
                }
                postDataParams.put("message",edittext_chat_text.getText());
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

                        if (obj_values.getString("status").equalsIgnoreCase("1"))
                        {
                            edittext_chat_text.setText("");
                            new Getchat_communication().execute();
                        }
                        else if (obj_values.getString("result").equalsIgnoreCase("0"))
                        {


                            AlertDialog.Builder builder1 = new AlertDialog.Builder(ChatActivity.this);
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


                            AlertDialog.Builder builder1 = new AlertDialog.Builder(ChatActivity.this);
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
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ChatActivity.this);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mHandler.removeCallbacks(mToastRunnable);

    }
}