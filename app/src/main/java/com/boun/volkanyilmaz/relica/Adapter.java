package com.boun.volkanyilmaz.relica;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (modelList==null)
            return null;

        final LinearLayout layout= (LinearLayout) LayoutInflater.from(context).inflate(R.layout.memory_list_item,parent,false);

        TextView fullname = layout.findViewById(R.id.textView7);
        TextView username = layout.findViewById(R.id.textView6);
        TextView dateTv = layout.findViewById(R.id.textView5);
        TextView textTv = layout.findViewById(R.id.text);
        CircleImageView circleImageView = layout.findViewById(R.id.profile_image_memory);
        ImageView imImageView = layout.findViewById(R.id.imageView);

        final MemoryModel memory = modelList.get(position);

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

        layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                layout.setAlpha(.5f);
                new AlertDialog.Builder(context)
                        .setTitle("Delete memory")
                        .setMessage("Are you sure to delete your memory?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendRequestDelete(position, memory.getUuid(), layout);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                layout.setAlpha(1);
                            }
                        }).show();
                return false;
            }
        });
        return layout;
    }

    private void sendRequestDelete(final int position, final String uuid, final View layout){
        final ProgressDialog loading = ProgressDialog.show(context, "Memories are loading...", "Please wait...", false, false);
        StringRequest request = new StringRequest(Request.Method.POST, "http://10.0.2.2/Relica/deleteMemory.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                Log.d("Json data: ", response);

                String status = null, message = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //success
                if (status.equals("200")) {
                    Snackbar.make(((AppCompatActivity) context).findViewById(R.id.listview), message, Snackbar.LENGTH_LONG).show();
                    modelList.remove(position); // Remove
                    notifyDataSetChanged();
                } else {
                    //request failed
                    Snackbar.make(((AppCompatActivity) context).findViewById(R.id.listview), message, Snackbar.LENGTH_LONG).show();
                    layout.setAlpha(1);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("uuid", uuid);

                if (!modelList.get(position).getImagePath().equals("")){
                    params.put("path",modelList.get(position).getImagePath());
                }
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }
}
