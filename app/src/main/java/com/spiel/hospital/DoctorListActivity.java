package com.spiel.hospital;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DoctorListActivity extends AppCompatActivity {

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

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView_doctorlist);
        MyListAdapter adapter = new MyListAdapter(myListData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }


    public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{
        private MyListData[] listdata;

        // RecyclerView recyclerView;
        public MyListAdapter(MyListData[] listdata) {
            this.listdata = listdata;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem= layoutInflater.inflate(R.layout.doctorlist_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final MyListData myListData = listdata[position];
            holder.textView_doctname.setText(listdata[position].getDescription());
            holder.imageView_profilelogo.setImageResource(listdata[position].getImgId());
            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(),"click on item: "+myListData.getDescription(),Toast.LENGTH_LONG).show();
                }
            });
        }


        @Override
        public int getItemCount() {
            return listdata.length;
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
}
