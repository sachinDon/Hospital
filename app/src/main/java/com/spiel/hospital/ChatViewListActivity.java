package com.spiel.hospital;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class ChatViewListActivity extends AppCompatActivity {

    public  static  String str_id,str_mobile;
    AlertDialog alertDialog_Box;
    RecyclerView recyclerView;
    MyListAdapter adapter;
    JSONArray array_doctorlist;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    SearchView searchview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_view_list);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        array_doctorlist = new JSONArray();
        searchview = (SearchView) findViewById(R.id.searchView_doctlist_chatlist);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_doctorlist_chat);


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
                                String string = array_doctorlist.getJSONObject(i).getString("hname");
                                //String str_category = array_doctorlist.getJSONObject(i).getString("degree");
                                // str_search_txt = newText;
                                if ((string.toLowerCase()).contains(newText.toLowerCase()) ) {

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


        new GetDoctorListchat_communication().execute();
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
            View listItem= layoutInflater.inflate(R.layout.list_doctor_chat, parent, false);
            MyListAdapter.ViewHolder viewHolder = new MyListAdapter.ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyListAdapter.ViewHolder holder, int position) {

            try {
                JSONObject obj = new JSONObject(String.valueOf(listdata.getJSONObject(position)));

                holder.relativeLayout.setTag(position);
                holder.text_hcname_chat.setText(obj.getString("hname"));
                holder.text_hregno_chat.setText(obj.getString("hregno"));

                if (obj.getString("read").equalsIgnoreCase("yes"))
                {
                    holder.text__readmsg.setVisibility(View.INVISIBLE);
                }
                else
                {
                    holder.text__readmsg.setVisibility(View.VISIBLE);
                }


                holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Integer int_index = (Integer)view.getTag();
                        JSONObject objectval = null;
                        try {

                            objectval = new JSONObject(String.valueOf(listdata.getJSONObject(int_index)));
                            Intent intent = new Intent(ChatViewListActivity.this,ChatActivity.class);
                            ChatActivity.str_reciverid = objectval.getString("mobile");;
                            ChatActivity.str_chatname = objectval.getString("hname");;
                            startActivity(intent);

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

            public RelativeLayout relativeLayout;
            TextView  text_hregno_chat,text_hcname_chat,text__readmsg;
            public ViewHolder(View itemView) {
                super(itemView);

                this.text__readmsg = (TextView) itemView.findViewById(R.id.text__readmsg);
                this.text_hregno_chat = (TextView) itemView.findViewById(R.id.text_hregno_chat);
                this.text_hcname_chat = (TextView) itemView.findViewById(R.id.text_hcname_chat);
                this.relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relative_chatlistview);
            }
        }
    }

    public class GetDoctorListchat_communication extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {


            try {

                URL url = new URL(Urlclass.getdoctorchatlist);
                JSONObject postDataParams = new JSONObject();


//                postDataParams.put("email",OTPActivity.Stremail);
                postDataParams.put("mobile",str_mobile);
                postDataParams.put("id",str_id);

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
                        recyclerView.setLayoutManager(new LinearLayoutManager(ChatViewListActivity.this));
                        recyclerView.setAdapter(adapter);

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            else
            {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ChatViewListActivity.this);
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