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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class PaidPackageActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MyListAdapter adapter;
    JSONArray array_doctorlist;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid_package);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        array_doctorlist = new JSONArray();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_doctorlist2);
        adapter = new MyListAdapter(array_doctorlist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);




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
            View listItem= layoutInflater.inflate(R.layout.list_paidlist, parent, false);
            MyListAdapter.ViewHolder viewHolder = new MyListAdapter.ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyListAdapter.ViewHolder holder, int position) {

            holder.linear_paidlist.setTag(position);
            holder.text_paidlist_date.setTag(position);
            try {
                JSONObject obj = new JSONObject(String.valueOf(listdata.getJSONObject(position)));
                holder.text_paidlist_dept.setText(obj.getString("dept"));
                holder.text_paidlist_type.setText(obj.getString("selectpkg"));
                holder.text_paidlist_price.setText(obj.getString("selectprice"));
                holder.text_paidlist_date.setText(obj.getString("date"));

                holder.linear_paidlist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    String str_dept = ((TextView) recyclerView.findViewHolderForAdapterPosition((Integer) v.getTag()).itemView.findViewById(R.id.text_paidlist_dept)).getText().toString();
                    Listview_DoctorsActivity.array_dept = new ArrayList<String>();
                    Listview_DoctorsActivity.array_dept.add(str_dept);
                    Intent intet = new Intent(PaidPackageActivity.this,Listview_DoctorsActivity.class);
                     startActivity(intet);
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

            public TextView text_paidlist_dept,text_paidlist_type,text_paidlist_price,text_paidlist_date;
            public LinearLayout linear_paidlist;
            public ViewHolder(View itemView) {
                super(itemView);
                this.linear_paidlist = (LinearLayout) itemView.findViewById(R.id.linear_paidlist);
                this.text_paidlist_dept = (TextView) itemView.findViewById(R.id.text_paidlist_dept);
                this.text_paidlist_type = (TextView) itemView.findViewById(R.id.text_paidlist_type);
                this.text_paidlist_price = (TextView) itemView.findViewById(R.id.text_paidlist_price);
                this.text_paidlist_date = (TextView) itemView.findViewById(R.id.text_paidlist_date);


            }
        }
    }

    public class Getpaidlist_communication extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {


            try {

                URL url = new URL(Urlclass.paidpackagelist);
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
                if (result.equalsIgnoreCase("nullerror"))
                {
                    AlertDialogView alertview = new AlertDialogView();
                    alertview.showAlert(PaidPackageActivity.this,getString(R.string.alert_opps),getString(R.string.alert_nullerror),"no");
                }
                else  if (result.equalsIgnoreCase("nodata"))
                {
                    array_doctorlist = new JSONArray();
                    recyclerView.setAdapter(null);
                }
                else {
                    //nodata
                    try {
                        array_doctorlist = new JSONArray(result);
                        if (array_doctorlist != null)
                        {
                            adapter = new MyListAdapter(array_doctorlist);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(PaidPackageActivity.this));
                            recyclerView.setAdapter(adapter);

                        }
                        else
                        {  AlertDialogView alertview = new AlertDialogView();
                            alertview.showAlert(PaidPackageActivity.this,getString(R.string.alert_opps),getString(R.string.alert_server),"no");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            else
            {
                AlertDialogView alertview = new AlertDialogView();
                alertview.showAlert(PaidPackageActivity.this,getString(R.string.alert_opps),getString(R.string.alert_server),"no");
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

    @Override
    protected void onResume() {
        super.onResume();

        new Getpaidlist_communication().execute();
    }
}