package com.spiel.hospital;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PackageActivity extends AppCompatActivity {
    GridView coursesGV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package);

        coursesGV = findViewById(R.id.grid_package);

        ArrayList<CourseModel> courseModelArrayList = new ArrayList<CourseModel>();
        courseModelArrayList.add(new CourseModel("Rs 250", R.drawable.blue1));
        courseModelArrayList.add(new CourseModel("600", R.drawable.blue2));
        courseModelArrayList.add(new CourseModel("400", R.drawable.blue3));
        courseModelArrayList.add(new CourseModel("500", R.drawable.blue4));
        courseModelArrayList.add(new CourseModel("900", R.drawable.button1));
        courseModelArrayList.add(new CourseModel("1000", R.drawable.blue1));

        CourseGVAdapter adapter = new CourseGVAdapter(PackageActivity.this, courseModelArrayList);
        coursesGV.setAdapter(adapter);
    }

    public class CourseGVAdapter extends ArrayAdapter<CourseModel> {
        public CourseGVAdapter(@NonNull Context context, ArrayList<CourseModel> courseModelArrayList) {
            super(context, 0, courseModelArrayList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View listitemView = convertView;
            if (listitemView == null) {
                // Layout Inflater inflates each item to be displayed in GridView.
                listitemView = LayoutInflater.from(getContext()).inflate(R.layout.card_item, parent, false);
            }
            CourseModel courseModel = getItem(position);
            TextView courseTV = listitemView.findViewById(R.id.idTVCourse);
            ImageView courseIV = listitemView.findViewById(R.id.idIVcourse);
            courseTV.setText(courseModel.getCourse_name());
            courseIV.setImageResource(courseModel.getImgid());
            return listitemView;
        }
    }
}