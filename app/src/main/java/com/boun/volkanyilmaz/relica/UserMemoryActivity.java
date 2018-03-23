package com.boun.volkanyilmaz.relica;

import android.app.ProgressDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserMemoryActivity extends AppCompatActivity {


    private TextView fullname,username,mail;
    private ListView listView;
    private String id;
    private RequestQueue requestQueue;
    private CircleImageView profileFoto;
    private List<MemoryModel> modelList;
    private String url = "http://10.0.2.2/TwitterClone/getTweetler.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_memory);


        this.fullname = findViewById(R.id.userFullname);
        this.username = findViewById(R.id.userUsername);
        this.mail = findViewById(R.id.userMail);
        this.profileFoto = findViewById(R.id.profile_image_user);
        this.listView = findViewById(R.id.userMemoriesList);
        this.requestQueue = Volley.newRequestQueue(getApplicationContext());
        modelList=new ArrayList<>();

        Bundle arguments = getIntent().getExtras();
        String path=arguments.getString("path","");
        String fullname = arguments.getString("fullname","");
        String username = arguments.getString("username","");
        String mail = arguments.getString("mail","");
        this.id = arguments.getString("id","-1");

        this.fullname.setText(fullname);
        this.username.setText(username);
        this.mail.setText(mail);
        this.fullname.setText(fullname);

        if (!path.equals(""))
            Picasso.with(getApplicationContext()).load(path).into(profileFoto);
        sendRequest();

    }

    private void sendRequest() {
        final ProgressDialog loading = ProgressDialog.show(UserMemoryActivity.this, "Memories are loading...", "Please wait...", false, false);
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


                //herşey yolundaysa
                if (status.equals("200")) {

                    if (memories.length()==0) {
                        Snackbar.make(listView,"No memory is found..." , Snackbar.LENGTH_LONG).show();
                    }else {

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
                                model.setUuid(memory.getString("uuid"));
                            } catch (JSONException e) {
                                Log.e("json parse error", e.getLocalizedMessage());
                            }

                            modelList.add(model);

                        }

                        Adapter adapter=new Adapter(UserMemoryActivity.this, modelList);
                        listView.setAdapter(adapter);


                    }



                }

                else {
                    //request başarısız ise
                    Snackbar.make(listView, message, Snackbar.LENGTH_LONG).show();
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


        requestQueue.add(request);
    }//istekGonder metodu sonu
}
