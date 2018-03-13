package com.boun.volkanyilmaz.relica;

import android.content.Context;

import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by volkanyilmaz on 13/03/18.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>  {

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
    public void onBindViewHolder(ViewHolder holder, int position) {


        MemoryModel memory = modelList.get(position);
        holder.fullname.setText(memory.getFullname());
        holder.textTv.setText(memory.getMemoryText());
        holder.username.setText(memory.getUsername());

        if (!memory.getProfilePath().equals(""))
            Picasso.with(context).load(memory.getProfilePath()).into(holder.circleImageView);

        if (!memory.getImagePath().equals("")) {
            //Picasso
            Picasso.with(context).load(memory.getImagePath()).into(holder.imImageView);
            Log.d("Path cannot be null::",String.valueOf(position));

            //Volley
            ImageLoader.ImageCache imageCache = new BitmapLruCache ();
            ImageLoader imageLoader = new ImageLoader (Volley.newRequestQueue(context),imageCache);
            holder.imageView.setImageUrl (memory.getImagePath(),imageLoader);
            holder.imageView.setDefaultImageResId (R.drawable.icon);
            holder.imageView.setErrorImageResId (R.drawable.icon);
        }





        Log.d("pozisyon:::::::: ",String.valueOf(position));


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
        public NetworkImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            fullname = itemView.findViewById(R.id.textView7);
            username = itemView.findViewById(R.id.textView6);
            dateTv = itemView.findViewById(R.id.textView5);
            textTv = itemView.findViewById(R.id.text);
            circleImageView = itemView.findViewById(R.id.profile_image_memory);
            //imImageView = itemView.findViewById(R.id.imageView);
            //imageView = itemView.findViewById(R.id.img);
        }
    }
}
