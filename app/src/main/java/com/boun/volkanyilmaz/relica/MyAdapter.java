package com.boun.volkanyilmaz.relica;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by volkanyilmaz on 13/03/18.
 */
public class MyAdapter extends RecyclerView.Adapter {

    private List<MemoryModel> modelList;
    private Context context;

    public MyAdapter(List<MemoryModel> modelList,Context c) {
        this.modelList = modelList;
        this.context=c;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Transform xml
        LinearLayout rootView = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.memory_list_item, parent, false);

        ViewHolder vh = new ViewHolder(rootView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        // Number of items in list
        if (modelList == null)
            return 0;
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView fullname, username, dateTv, textTv;
        public CircleImageView circleImageView;
        public ImageView imImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            fullname = itemView.findViewById(R.id.textView7);
            username = itemView.findViewById(R.id.textView6);
            dateTv = itemView.findViewById(R.id.textView5);
            textTv = itemView.findViewById(R.id.text);
            circleImageView = itemView.findViewById(R.id.profile_image_memory);
            imImageView = itemView.findViewById(R.id.imageView);
            //imageView = itemView.findViewById(R.id.img);
        }
    }
}
