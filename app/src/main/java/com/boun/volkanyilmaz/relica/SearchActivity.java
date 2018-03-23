package com.boun.volkanyilmaz.relica;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by volkanyilmaz on 23/03/18.
 */

public class SearchActivity extends AppCompatActivity {
    private MaterialSearchView searchView;
    private ListView listView;
    private AdapterSearch adapter;
    private String id;
    private static final String url = "http://10.0.2.2/TwitterClone/selectusers.php";
    private RequestQueue requestQueue;
    private List<String> recommendation;
    private List<MemoryModel> modelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = findViewById(R.id.toolbarSearch);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        listView = findViewById(R.id.listViewSearch);
        searchView = findViewById(R.id.search_view);
        searchView.setHint("Search on users");


        recommendation = new ArrayList<>();
        modelList=new ArrayList<>();

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        // Get ID via SharedPreferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SearchActivity.this);
        id = preferences.getString("id", "-1");

        // Send empty string when activity opens to get user list
        sendRequest("");


        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Snackbar.make(findViewById(R.id.container), "onQueryTextSubmit: " + query, Snackbar.LENGTH_LONG)
                        .show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Snackbar.make(findViewById(R.id.container), "onQueryTextChange: " + newText, Snackbar.LENGTH_LONG)
                        .show();
                sendRequest(newText);
                return false;
            }
        });



        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView.setMenuItem(searchItem);
        return true;
    }


    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }


    private void sendRequest(final String newText) {

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Json data search: ", response);
                recommendation.clear();
                modelList.clear();

                String status = null, message = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (status.equals("200")) {
                    JSONArray users = null;
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        users = jsonObject.getJSONArray("users");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    for (int i = 0; i < users.length(); i++) {

                        try {
                            JSONObject user = users.getJSONObject(i);
                            MemoryModel model = new MemoryModel();

                            // Add recommendation list
                            recommendation.add(user.getString("fullname"));
                            recommendation.add(user.getString("username"));

                            // Add user to model list
                            model.setFullname(user.getString("fullname"));
                            model.setUsername(user.getString("username"));
                            model.setProfilePath(user.getString("avatar"));
                            model.setMail(user.getString("mail"));
                            model.setId(user.getString("id"));
                            modelList.add(model);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }else {
                    // If request is failed or empty
                    Snackbar.make(findViewById(R.id.container), message, Snackbar.LENGTH_LONG)
                            .show();
                }
                // Transform recommendation array into list
                String[] recommendationArray = new String[recommendation.size()];;
                for (int i=0; i<recommendation.size(); i++) {
                    String s=recommendation.get(i);
                    recommendationArray[i]=s;
                }

                //searchView.setSuggestions(tavsiyelerArray);

                // Set to adapter
                adapter=new AdapterSearch(modelList,SearchActivity.this);
                listView.setAdapter(adapter);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("word", newText);
                return params;
            }
        };
        requestQueue.add(request);
    }
}