package com.boun.volkanyilmaz.relica.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.boun.volkanyilmaz.relica.MemoryModel;
import com.boun.volkanyilmaz.relica.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    private String id;
    private Context context;
    private List<MemoryModel> modelList;
    private RecyclerView recyclerView;
    private TextView textView;


    private String url = "http://10.0.2.2/Relica/getMemories.php";


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = ((AppCompatActivity) context).findViewById(R.id.listTweet);
        textView = ((AppCompatActivity) context).findViewById(R.id.textView3);
        modelList = new ArrayList<>();
        id = getArguments().getString("id");

        sendRequest();


        Log.d("Volley test", ".............................................");


    }

    private void sendRequest() {
        final ProgressDialog loading = ProgressDialog.show(context, "Memories are loading...", "Please wait...", false, false);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                Log.d("Json data Memories: ", response);

                String durum = null, mesaj = null;

                Log.d("Volley işlemleri testi", "request işlemler tamamlandı.........................");


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
                params.put("id", id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);


    }
}
