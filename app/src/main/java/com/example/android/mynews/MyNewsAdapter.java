package com.example.android.mynews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyNewsAdapter extends ArrayAdapter<MyNews> {
    public MyNewsAdapter(@NonNull Context context, ArrayList<MyNews> news_list) {super(context,0,news_list);}
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list, parent, false);
        }

        MyNews currentNews = getItem(position);

        TextView Name = (TextView) convertView.findViewById(R.id.name);
        Name.setText(currentNews.getName());

        TextView headline = (TextView)convertView.findViewById(R.id.headline);
        headline.setText(currentNews.getheadline());

        TextView fristName = (TextView)convertView.findViewById(R.id.fistname);
        fristName.setText(currentNews.getFirstName());

        TextView lastName = (TextView)convertView.findViewById(R.id.lastname);
        lastName.setText(currentNews.getLastName());

        String fullDate  = currentNews.getDate();

        String mdate;
        String time;

        String[] parts = fullDate.split("T");
        mdate = parts[0] ;

        String[] parts2 = parts[1].split(("Z"));
        time = parts2[0];

        TextView date = (TextView) convertView.findViewById(R.id.date);
        date.setText(mdate);
        TextView mtime = (TextView) convertView.findViewById(R.id.time);
        mtime.setText(time);

        return convertView;
    }
}
