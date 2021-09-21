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
import android.os.StrictMode;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;



public class DoctorListActivity extends AppCompatActivity {

    AlertDialog alertDialog_Box;
    TextView textview_doctlist_eqp,textview_doctlist_menu,text_close_free,text_buy_free;
    RecyclerView recyclerView;
    MyListAdapter adapter;
    JSONArray array_doctorlist;
    SearchView searchview;
    RelativeLayout image_move;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    private  int xDelta,yDelta;
    private  ViewGroup mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_list);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        textview_doctlist_eqp = (TextView) findViewById(R.id.textview_doctlist_eqp);
        textview_doctlist_menu = (TextView) findViewById(R.id.textview_doctlist_menu);
        searchview = (SearchView) findViewById(R.id.searchView_doctlist);
        image_move= (RelativeLayout) findViewById(R.id.image_move);
        text_buy_free = (TextView) findViewById(R.id.text_buy_free);
                text_close_free = (TextView) findViewById(R.id.text_close_free);
        image_move.setOnTouchListener(OnTouchListener());






        mainLayout = (RelativeLayout)findViewById(R.id.relative_main);
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }

        array_doctorlist = new JSONArray();
        textview_doctlist_eqp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DoctorListActivity.this,EquipementActivity.class);
                startActivity(intent);
            }
        });

        text_buy_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorListActivity.this,PackageActivity.class);
                startActivity(intent);

            }
        });

        text_close_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               image_move.setVisibility(View.INVISIBLE);
            }
        });

        textview_doctlist_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context wrapper = new ContextThemeWrapper(DoctorListActivity.this, R.style.popupMenuStyle);
                PopupMenu menu = new PopupMenu(wrapper, v);
                menu.getMenu().add("Buy Pakages");
                menu.getMenu().add("Paid Pakages");
                menu.getMenu().add("Transctions");// menus items
                menu.getMenu().add("Logout");
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {


                        if (String.valueOf(item).equalsIgnoreCase("Buy Pakages"))
                        {
                            Intent intent = new Intent(DoctorListActivity.this,PackageActivity.class);
                            startActivity(intent);

                        }
                        else  if (String.valueOf(item).equalsIgnoreCase("Paid Pakages"))
                        {
                            Intent intet = new Intent(DoctorListActivity.this,PaidPackageActivity.class);
                            startActivity(intet);
                        }
                        else  if (String.valueOf(item).equalsIgnoreCase("Transctions"))
                        {

                        }
                        else  if (String.valueOf(item).equalsIgnoreCase("Logout"))
                        {
                            editor.putString("userid","");
                            editor.putString("login","no");
                            editor.commit();
                            Intent intent = new Intent(DoctorListActivity.this,MainViewActivity.class);
                            startActivity(intent);
                        }
                        else {}


                        return false;
                    }
                });

                menu.show();
            }
        });

         recyclerView = (RecyclerView) findViewById(R.id.recyclerView_doctorlist);
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


    }

    private View.OnTouchListener OnTouchListener() {
        return new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final int x = (int) event.getRawX();
                final int y = (int) event.getRawY();
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        RelativeLayout.LayoutParams laayoutparam = (RelativeLayout.LayoutParams) v.getLayoutParams();
                        xDelta = x - laayoutparam.leftMargin;
                        yDelta = y - laayoutparam.topMargin;
                        break;

                    case MotionEvent.ACTION_MOVE:

                        RelativeLayout.LayoutParams laayoutparam1 = (RelativeLayout.LayoutParams) v.getLayoutParams();
                        laayoutparam1.leftMargin = x - xDelta;
                        laayoutparam1.topMargin = y - yDelta;
                        laayoutparam1.rightMargin = 0;
                        laayoutparam1.bottomMargin = 0;
                        v.setLayoutParams(laayoutparam1);
                        break;


                }
                mainLayout.invalidate();
                return true;
            }
        };

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
                holder.textView_doctor_chat.setTag(position);
                holder.textView_doctor_message.setTag(position);

                holder.textView_doctname.setText("Name: "+obj.getString("name"));
               // holder.imageView_profilelogo.setImageResource(listdata[position].getImgId());
                holder.textView_doctordegree.setText("Degree: "+obj.getString("degree"));
                holder.textView_specilistdc.setText("Specialist: "+obj.getString("specialist"));
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
                Picasso.with(DoctorListActivity.this).invalidate(str_imageurl);
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

                holder.textView_doctor_chat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                       Integer int_index = (Integer)view.getTag();
                        JSONObject objectval = null;
                        try {

                            objectval = new JSONObject(String.valueOf(listdata.getJSONObject(int_index)));
                            Intent intent = new Intent(DoctorListActivity.this,ChatActivity.class);
                            ChatActivity.str_reciverid = objectval.getString("id");;
                            ChatActivity.str_chatname = objectval.getString("name");;
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                });

                holder.textView_doctor_message.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        SendMessage();
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

            TextView textView_doctor_chat,textView_doctor_message;
            public ViewHolder(View itemView) {
                super(itemView);
                this.imageView_profilelogo = (ImageView) itemView.findViewById(R.id.imageView_profilelogo);
                this.textView_doctname = (TextView) itemView.findViewById(R.id.textView_doctname);
                this.textView_doctordegree = (TextView) itemView.findViewById(R.id.textView_doctordegree);
                this.textView_doctor_exp = (TextView) itemView.findViewById(R.id.textView_doctor_exp);
                this.textView_doctor_status = (TextView) itemView.findViewById(R.id.textView_doctor_status);
                this.textView_doctor_chat = (TextView) itemView.findViewById(R.id.textView_doctor_chat);
                this.textView_doctor_message = (TextView) itemView.findViewById(R.id.textView_doctor_message);
                this.textView_specilistdc= (TextView) itemView.findViewById(R.id.textView_specilistdc);

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

    public  void  SendMessage()
    {
        String authkey = "166733AbkGNaxvhDTl60f24e49P1";
//Multiple mobiles numbers separated by comma
        String mobiles = "8850519524";
//Sender ID,While using route4 sender id should be 6 characters long.
        String senderId = "BMHOSP";
//Your message to send, Add URL encoding here.
        String message = "Hi sachin mokashi message";
//define route
        String route="default";

        URLConnection myURLConnection=null;
        URL myURL=null;
        BufferedReader reader=null;

//encoding message
        String encoded_message=URLEncoder.encode(message);

//Send SMS API
        String mainUrl="http://api.msg91.com/api/sendhttp.php?";

//Prepare parameter string
        StringBuilder sbPostData= new StringBuilder(mainUrl);
        sbPostData.append("authkey="+authkey);
        sbPostData.append("&mobiles="+mobiles);
        sbPostData.append("&message="+encoded_message);
        sbPostData.append("&route="+route);
        sbPostData.append("&sender="+senderId);

//final string
        mainUrl = sbPostData.toString();
        try
        {
            //prepare connection
            myURL = new URL(mainUrl);
            try {
                myURLConnection = myURL.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            myURLConnection.connect();
            reader= new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));

            //reading response
            String response;
            while ((response = reader.readLine()) != null)
                //print response
                Log.d("RESPONSE", ""+response);

            //finally close connection
            reader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetDoctorList_communication().execute();
    }

    public void onBackPressed() {

    }
}
