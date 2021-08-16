package com.spiel.hospital;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PackageActivity extends AppCompatActivity {
 TextView text_submitpckg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package);


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

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView_pckage_list);
        MyListAdapter adapter = new MyListAdapter(myListData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        text_submitpckg = (TextView)findViewById(R.id.text_submitpckg);
        text_submitpckg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(PackageActivity.this,DoctorListActivity.class);
                startActivity(intent);

            }
        });
    }


    public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{
        private MyListData[] listdata;

        // RecyclerView recyclerView;
        public MyListAdapter(MyListData[] listdata) {
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
            final MyListData myListData = listdata[position];
            holder.text_spc_namae_pkg.setText(listdata[position].getDescription());

            holder.relative_slects_custveg1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {





                    PopupMenu menu = new PopupMenu(PackageActivity.this, view);


                        menu.getMenu().add("Monthly");  // menus items
                        menu.getMenu().add("Quterly");  // menus items
                        menu.getMenu().add("Halfyearly");
                        menu.getMenu().add("Yearly");
                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            holder.text_spc_namae_pkg.setText(String.valueOf(item));

                            return false;
                        }
                    });

                    menu.show();
                }
            });
        }


        @Override
        public int getItemCount() {
            return listdata.length;
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


}