package com.boun.volkanyilmaz.relica;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by volkanyilmaz on 13/03/18.
 */

public class Adapter extends BaseAdapter {

    private Context context;
    private List<MemoryModel> modelList;

    public Adapter(Context context, List<MemoryModel> modelList) {
        this.modelList=modelList;
        this.context=context;
    }

    @Override
    public int getCount() {
        if (modelList==null)
            return 0;

        return modelList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (modelList==null)
            return null;

        LinearLayout layout= (LinearLayout) LayoutInflater.from(context).inflate(R.layout.memory_list_item,parent,false);

        TextView fullname = layout.findViewById(R.id.textView7);
        TextView username = layout.findViewById(R.id.textView6);
        TextView dateTv = layout.findViewById(R.id.textView5);
        TextView textTv = layout.findViewById(R.id.text);
        CircleImageView circleImageView = layout.findViewById(R.id.profile_image_memory;
        ImageView imImageView = layout.findViewById(R.id.imageView);

        MemoryModel memory = modelList.get(position);

        fullname.setText(memory.getFullname());
        textTv.setText(memory.getMemoryText());
        username.setText(memory.getUsername());

        if (!memory.getProfilePath().equals(""))
            Picasso.with(context).load(memory.getProfilePath()).into(circleImageView);

        if (!memory.getImagePath().equals(""))
            Picasso.with(context).load(memory.getImagePath()).into(imImageView);


        Date present=new Date();

        DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=null;
        try {
            date=df.parse(memory.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int differ= (int) (present.getTime()-date.getTime());

        int day = differ/(1000*60*60*24);
        int hour = differ/(1000*60*60);
        int minute = differ/(1000*60);
        int second = differ/(1000);

        if (second==0)
            dateTv.setText("present");

        if (second>0 && minute==0)
            dateTv.setText(second+"s");

        if (minute>0 && hour==0)
            dateTv.setText(minute+"min");

        if (hour>0 && day==0)
            dateTv.setText(hour+"h");

        if (day>0)
            dateTv.setText(day+"day");


        return layout;
    }
}
