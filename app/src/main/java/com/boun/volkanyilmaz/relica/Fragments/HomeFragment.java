package com.boun.volkanyilmaz.relica.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.boun.volkanyilmaz.relica.Adapter;
import com.boun.volkanyilmaz.relica.MemoryModel;
import com.boun.volkanyilmaz.relica.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    private String id;
    private Context context;
    private List<MemoryModel> modelList;
    private ListView listView;
    private TextView textView;
    private SwipeRefreshLayout refreshLayout;


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

        listView = ((AppCompatActivity) context).findViewById(R.id.listview);
        textView = ((AppCompatActivity) context).findViewById(R.id.textView3);
        refreshLayout = ((AppCompatActivity) context).findViewById(R.id.refresh);
        modelList = new ArrayList<>();
        id = getArguments().getString("id");

        sendRequest();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                modelList.clear();
                sendRequestRefresh();
            }
        });


        Log.d("Volley test", ".............................................");


    }

    private void sendRequest() {
        final ProgressDialog loading = ProgressDialog.show(context, "Memories are loading...", "Please wait...", false, false);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                Log.d("Json data Memories: ", response);

                String status = null, message = null;
                JSONArray memories = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                    memories = jsonObject.getJSONArray("memories");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //success
                if (status.equals("200")) {

                    for (int i = 0; i < memories.length(); i++) {
                        JSONObject tweet;
                        MemoryModel model = new MemoryModel();
                        try {
                            tweet = memories.getJSONObject(i);
                            model.setFullname(tweet.getString("fullname"));
                            model.setUsername(tweet.getString("username"));
                            model.setProfilePath(tweet.getString("avatar"));
                            model.setImagePath(tweet.getString("path"));
                            model.setMemoryText(tweet.getString("text"));
                            model.setDate(tweet.getString("date"));
                        } catch (JSONException e) {
                            Log.e("json parse error", e.getLocalizedMessage());
                        }

                        modelList.add(model);

                    }

                } else {
                    //request failed
                    Snackbar.make(listView, message, Snackbar.LENGTH_LONG).show();
                }
                Log.d("Volley operations test", "request operations completed.........................");
                if (modelList == null) {
                    textView.setText("No memories can be found...");
                } else {
                    setAdapter();
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
                params.put("id", id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);

    }

    private void setAdapter() {

        Adapter adapter=new Adapter(context,modelList);
        listView.setAdapter(adapter);
    }


    private void sendRequestRefresh() {

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                refreshLayout.setRefreshing(false);
                Log.d("Json data Memories: ", response);

                String status = null, message = null;
                JSONArray memories = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                    memories = jsonObject.getJSONArray("memories");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Success
                if (status.equals("200")) {

                    for (int i = 0; i < memories.length(); i++) {
                        JSONObject memory;
                        MemoryModel model = new MemoryModel();
                        try {
                            memory = memories.getJSONObject(i);
                            model.setFullname(memory.getString("fullname"));
                            model.setUsername(memory.getString("username"));
                            model.setProfilePath(memory.getString("avatar"));
                            model.setImagePath(memory.getString("path"));
                            model.setMemoryText(memory.getString("text"));
                            model.setDate(memory.getString("date"));
                        } catch (JSONException e) {
                            Log.e("json parse error", e.getLocalizedMessage());
                        }

                        modelList.add(model);

                    }

                } else {
                    //request failed
                    Snackbar.make(listView, message, Snackbar.LENGTH_LONG).show();
                }
                Log.d("Volley operations test", "request operations completed.........................");
                if (modelList == null) {
                    textView.setText("No memories can be found...");
                    setAdapter();
                } else {
                    setAdapter();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                refreshLayout.setRefreshing(false);
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
        requestQueue.add(request);
    }

}
