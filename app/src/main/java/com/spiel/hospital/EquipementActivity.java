package com.spiel.hospital;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EquipementActivity extends AppCompatActivity {

    TextView text_back_equp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipement);

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
        text_back_equp =(TextView)findViewById(R.id.text_back_equp);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView_equp_list);
        MyListAdapter adapter = new MyListAdapter(myListData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        text_back_equp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
            View listItem= layoutInflater.inflate(R.layout.doctorlist_item, parent, false);
            MyListAdapter.ViewHolder viewHolder = new MyListAdapter.ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyListAdapter.ViewHolder holder, int position) {
            final MyListData myListData = listdata[position];
            holder.textView_doctname.setText(listdata[position].getDescription());


        }


        @Override
        public int getItemCount() {
            return listdata.length;
        }

        public  class ViewHolder extends RecyclerView.ViewHolder {
             public ImageView imageView_equp_logo;
            public TextView textView_doctordegree;
            public TextView textView_doctor_exp,textView_doctname,textView_equp_inq;

            public ViewHolder(View itemView) {
                super(itemView);
                this.textView_doctor_exp = (TextView) itemView.findViewById(R.id.textView_doctor_exp);
                this.textView_doctordegree = (TextView) itemView.findViewById(R.id.textView_doctordegree);
                this.textView_doctname = (TextView) itemView.findViewById(R.id.textView_doctname);
                this.textView_equp_inq = (TextView) itemView.findViewById(R.id.textView_equp_inq);

            }
        }
    }


}