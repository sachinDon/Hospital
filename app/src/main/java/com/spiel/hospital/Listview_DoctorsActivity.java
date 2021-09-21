package com.spiel.hospital;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class Listview_DoctorsActivity extends AppCompatActivity {

    AlertDialog alertDialog_Box;
    RecyclerView recyclerView;
    MyListAdapter adapter;
    JSONArray array_doctorlist;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    ArrayList <String> array_id;
    public static ArrayList <String> array_dept ;
    TextView text_submitpckg2;
    SearchView searchview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview__doctors);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        array_doctorlist = new JSONArray();
        array_id = new ArrayList<String>();

        searchview = (SearchView) findViewById(R.id.searchView_doctlist2);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_doctorlist2);
        text_submitpckg2 = (TextView) findViewById(R.id.text_submitpckg2);

        adapter = new MyListAdapter(array_doctorlist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        text_submitpckg2.setVisibility(View.GONE);

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
                                String string = array_doctorlist.getJSONObject(i).getString("name");
                                String str_category = array_doctorlist.getJSONObject(i).getString("degree");
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
        text_submitpckg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SubmitDoctorList_communication().execute();

            }
        });

        new GetDoctorList_communication().execute();

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
            View listItem= layoutInflater.inflate(R.layout.doctor_itemlist1, parent, false);
            MyListAdapter.ViewHolder viewHolder = new MyListAdapter.ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyListAdapter.ViewHolder holder, int position) {

            try {
                JSONObject obj = new JSONObject(String.valueOf(listdata.getJSONObject(position)));

                //  [{"id":null,"name":"sachin","specialist":"Cediology","degree":"MBBS","exp":"10","regno":"457896","address":"panvel","pincode":"142102","ftime":"10","ttime":"21","status":"availabel","imageurl":"http:\/\/wwww.google.com\/ipl.png","phone":"8850519524","phone2":"9850017872"},{"id":"4","name":"Dr.Navin ","specialist":"Jadhav","degree":"Msc","exp":"2030","regno":"123","address":"Nerul","pincode":"123654","ftime":"","ttime":"","status":"Available","imageurl":"undefined","phone":null,"phone2":null},{"id":"5","name":"sdfsdf","specialist":"fsfsdfsdfs","degree":"sdfsdf","exp":"35000","regno":"323432","address":"234234","pincode":"324234234","ftime":"1","ttime":"2","status":"Available","imageurl":"undefined","phone":null,"phone2":null},{"id":"6","name":"sss","specialist":"sss","degree":"sssddd","exp":"12","regno":"123","address":"sss","pincode":"234","ftime":"10","ttime":"2","status":"Not Available","imageurl":"undefined","phone":"112","phone2":"12"}]
                holder.relativeLayout.setTag(obj.getString("id"));

                if (array_id.contains(String.valueOf(obj.getString("id"))))
                {

                    holder.relativeLayout.setBackgroundResource(R.drawable.border_bottom12);
                }
                else
                {

                    holder.relativeLayout.setBackgroundResource(R.drawable.bottom_border11);
                }


                holder.textView_doctname.setText("Name: "+obj.getString("name"));
                holder.textView_specilistdc.setText("Specialist: "+obj.getString("specialist"));
                holder.textView_doctordegree.setText("Degree: "+obj.getString("degree"));
                holder.textView_doctor_exp.setText("Experience: "+obj.getString("exp") + " yrs.");
                holder.textView_doctor_status.setText("Status: "+obj.getString("status"));
                holder.textView_doctor_chat.setVisibility(View.INVISIBLE);
                holder.textView_doctor_message.setVisibility(View.INVISIBLE);
                if (obj.getString("status").equalsIgnoreCase("Available"))
                {
                    holder.textView_doctor_status.setText(Html.fromHtml( " <font color=#414141>  "+"Status: "+ " </font> <font color=#00b33c> <b> "+obj.getString("status")+ " </b> </font>"));

                }
                else
                {
                    holder.textView_doctor_status.setText(Html.fromHtml( " <font color=#414141>  "+"Status: "+ " </font> <font color=#ff0000> <b> "+obj.getString("status")+ " </b> </font>"));
                }

                String str_imageurl = obj.getString("imageurl");

                if (str_imageurl.length() ==0)
                {
                    str_imageurl ="http://www.sachinmokashi";
                }
//                Picasso.with(DoctorListActivity.this)
//                        .load(str_imageurl)
//                        .resize(100, 100)
//                        .transform(new CropCircleTransformation())
//                        .into(holder.imageView_profilelogo);
                Picasso.with(Listview_DoctorsActivity.this).invalidate(str_imageurl);
                Picasso.with(Listview_DoctorsActivity.this)
                        .load(str_imageurl)
                        .placeholder(R.drawable.defaultdoctor)
                        .into( holder.imageView_profilelogo, new Callback() {
                            @Override
                            public void onSuccess() {


                            }

                            @Override
                            public void onError() {

                            }
                        });

                holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (array_id.contains(String.valueOf(view.getTag())))
                        {
                            array_id.remove(String.valueOf(view.getTag()));
                            holder.relativeLayout.setBackgroundResource(R.drawable.bottom_border11);
                        }
                        else
                        {
                            array_id.add(String.valueOf(view.getTag()));
                            holder.relativeLayout.setBackgroundResource(R.drawable.border_bottom12);
                        }
                        if (array_id.size() == 0)
                        {
                            text_submitpckg2.setText("Submit");
                            text_submitpckg2.setVisibility(View.GONE);

                        }
                        else
                        {
                            text_submitpckg2.setText("Submit ("+array_id.size()+")");
                            text_submitpckg2.setVisibility(View.VISIBLE);
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
            public ImageView imageView_profilelogo;
            public TextView textView_specilistdc,textView_doctname,textView_doctor_exp,textView_doctordegree,textView_doctor_status;
            public RelativeLayout relativeLayout;
            TextView  textView_doctor_chat,textView_doctor_message;
            public ViewHolder(View itemView) {
                super(itemView);
                this.imageView_profilelogo = (ImageView) itemView.findViewById(R.id.imageView_profilelogo);
                this.textView_doctname = (TextView) itemView.findViewById(R.id.textView_doctname);
                this.textView_doctordegree = (TextView) itemView.findViewById(R.id.textView_doctordegree);
                this.textView_doctor_exp = (TextView) itemView.findViewById(R.id.textView_doctor_exp);
                this.textView_doctor_status = (TextView) itemView.findViewById(R.id.textView_doctor_status);
                this.textView_doctor_chat = (TextView) itemView.findViewById(R.id.textView_doctor_chat);
                this.textView_doctor_message = (TextView) itemView.findViewById(R.id.textView_doctor_message);
                this.relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
                this.textView_specilistdc= (TextView) itemView.findViewById(R.id.textView_specilistdc);
            }
        }
    }


    public class SubmitDoctorList_communication extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {


            try {

                URL url = new URL(Urlclass.submitdoctorlist);
                JSONObject postDataParams = new JSONObject();


                postDataParams.put("mobile",pref.getString("userid",""));
                postDataParams.put("id",pref.getString("userid",""));
                String str_ids = array_id.toString();
                str_ids = str_ids.replace("[","");
                str_ids = str_ids.replace("]","");
                postDataParams.put("ids",str_ids);

                String str_dept = array_dept.toString();
                str_dept = str_dept.replace("[","");
                str_dept = str_dept.replace("]","");
                str_dept = str_dept.replace(" ","");
                postDataParams.put("dept",str_dept);



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
                JSONArray jsonarray = null;
                JSONObject obj_values = null;
                try {
                    jsonarray = new JSONArray(result);
                    obj_values = new JSONObject(jsonarray.getString(0));
                    if (jsonarray != null)
                    {
                        if (obj_values.getString("status").equalsIgnoreCase("1"))
                        {
                            AlertDialogView alertview = new AlertDialogView();
                            alertview.showAlert(Listview_DoctorsActivity.this, getString(R.string.alert_sucess), obj_values.getString("errormessage"), "close");
                        }
                        else if (obj_values.getString("status").equalsIgnoreCase("0"))
                        {
                        AlertDialogView alertview = new AlertDialogView();
                        alertview.showAlert(Listview_DoctorsActivity.this, getString(R.string.alert_opps), obj_values.getString("errormessage"), "no");
                        }
                        else {
                            AlertDialogView alertview = new AlertDialogView();
                            alertview.showAlert(Listview_DoctorsActivity.this, getString(R.string.alert_opps), getString(R.string.alert_serverexit), "no");
                        }
                    } else {
                        AlertDialogView alertview = new AlertDialogView();
                        alertview.showAlert(Listview_DoctorsActivity.this, getString(R.string.alert_opps), getString(R.string.alert_serverexit), "no");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            else
            {
                AlertDialogView alertview = new AlertDialogView();
                alertview.showAlert(Listview_DoctorsActivity.this, getString(R.string.alert_opps), getString(R.string.alert_server), "no");

            }

        }
    }

    public class GetDoctorList_communication extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {


            try {

                URL url = new URL(Urlclass.getdoctorselect);
                JSONObject postDataParams = new JSONObject();


//                postDataParams.put("email",OTPActivity.Stremail);
                postDataParams.put("mobile",pref.getString("userid",""));
                postDataParams.put("id",pref.getString("userid",""));

                String str_dept = array_dept.toString();
                str_dept = str_dept.replace("[","");
                str_dept = str_dept.replace("]","");
                str_dept = str_dept.replace(" ","");
                postDataParams.put("dept",str_dept);



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
                        recyclerView.setLayoutManager(new LinearLayoutManager(Listview_DoctorsActivity.this));
                        recyclerView.setAdapter(adapter);

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            else {

                AlertDialogView alertview = new AlertDialogView();
                alertview.showAlert(Listview_DoctorsActivity.this, getString(R.string.alert_opps), getString(R.string.alert_server), "no");


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




}