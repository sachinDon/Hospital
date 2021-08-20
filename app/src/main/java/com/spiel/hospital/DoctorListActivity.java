package com.spiel.hospital;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;



public class DoctorListActivity extends AppCompatActivity {

    AlertDialog alertDialog_Box;
      TextView textview_doctlist_eqp;
    RecyclerView recyclerView;
    MyListAdapter adapter;
    JSONArray array_doctorlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_list);

        MyListData[] myListData = new MyListData[] {
                new MyListData("Email", android.R.drawable.ic_dialog_email),
                new MyListData("Info", android.R.drawable.ic_dialog_info),
                new MyListData("Delete", android.R.drawable.ic_delete),
                new MyListData("Dialer", android.R.drawable.ic_dialog_dialer),
                new MyListData("Alert", android.R.drawable.ic_dialog_alert),
                new MyListData("Map", android.R.drawable.ic_dialog_map),
                new MyListData("Email", android.R.drawable.ic_dialog_email),
                new MyListData("Info", android.R.drawable.ic_dialog_info),
                new MyListData("Delete", android.R.drawable.ic_delete),
                new MyListData("Dialer", android.R.drawable.ic_dialog_dialer),
                new MyListData("Alert", android.R.drawable.ic_dialog_alert),
                new MyListData("Map", android.R.drawable.ic_dialog_map),
        };
        textview_doctlist_eqp = (TextView) findViewById(R.id.textview_doctlist_eqp);
        array_doctorlist = new JSONArray();
        textview_doctlist_eqp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DoctorListActivity.this,EquipementActivity.class);
                startActivity(intent);
            }
        });

         recyclerView = (RecyclerView) findViewById(R.id.recyclerView_doctorlist);
         adapter = new MyListAdapter(array_doctorlist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        new GetDoctorList_communication().execute();
    }


    public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{
        private JSONArray listdata;

        // RecyclerView recyclerView;
        public MyListAdapter(JSONArray listdata) {
            this.listdata = listdata;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem= layoutInflater.inflate(R.layout.doctor_itemlist1, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            try {
                JSONObject obj = new JSONObject(String.valueOf(listdata.getJSONObject(position)));

               //  [{"id":null,"name":"sachin","specialist":"Cediology","degree":"MBBS","exp":"10","regno":"457896","address":"panvel","pincode":"142102","ftime":"10","ttime":"21","status":"availabel","imageurl":"http:\/\/wwww.google.com\/ipl.png","phone":"8850519524","phone2":"9850017872"},{"id":"4","name":"Dr.Navin ","specialist":"Jadhav","degree":"Msc","exp":"2030","regno":"123","address":"Nerul","pincode":"123654","ftime":"","ttime":"","status":"Available","imageurl":"undefined","phone":null,"phone2":null},{"id":"5","name":"sdfsdf","specialist":"fsfsdfsdfs","degree":"sdfsdf","exp":"35000","regno":"323432","address":"234234","pincode":"324234234","ftime":"1","ttime":"2","status":"Available","imageurl":"undefined","phone":null,"phone2":null},{"id":"6","name":"sss","specialist":"sss","degree":"sssddd","exp":"12","regno":"123","address":"sss","pincode":"234","ftime":"10","ttime":"2","status":"Not Available","imageurl":"undefined","phone":"112","phone2":"12"}]

                holder.textView_doctname.setText("Name: "+obj.getString("name"));
               // holder.imageView_profilelogo.setImageResource(listdata[position].getImgId());
                holder.textView_doctordegree.setText("Degree: "+obj.getString("degree"));
                holder.textView_doctor_exp.setText("Experience: "+obj.getString("exp") + " yrs.");
                holder.textView_doctor_status.setText("Status: "+obj.getString("status"));

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
                Picasso.with(DoctorListActivity.this)
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
                        Toast.makeText(view.getContext(),"click on item: ",Toast.LENGTH_LONG).show();
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
            public TextView textView_doctname,textView_doctor_exp,textView_doctordegree,textView_doctor_status;
            public RelativeLayout relativeLayout;
            public ViewHolder(View itemView) {
                super(itemView);
                this.imageView_profilelogo = (ImageView) itemView.findViewById(R.id.imageView_profilelogo);
                this.textView_doctname = (TextView) itemView.findViewById(R.id.textView_doctname);
                this.textView_doctordegree = (TextView) itemView.findViewById(R.id.textView_doctordegree);
                this.textView_doctor_exp = (TextView) itemView.findViewById(R.id.textView_doctor_exp);
                this.textView_doctor_status = (TextView) itemView.findViewById(R.id.textView_doctor_status);
                relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
            }
        }
    }

    public class GetDoctorList_communication extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {


            try {

                URL url = new URL(Urlclass.getdoctorlist);
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
                        recyclerView.setLayoutManager(new LinearLayoutManager(DoctorListActivity.this));
                        recyclerView.setAdapter(adapter);

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            else
            {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(DoctorListActivity.this);
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
