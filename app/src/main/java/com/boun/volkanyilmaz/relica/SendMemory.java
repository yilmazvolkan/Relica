package com.boun.volkanyilmaz.relica;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SendMemory extends AppCompatActivity {

    private TextView counter;
    private EditText text;
    private ImageButton imButton;
    private ImageView picture;
    private FloatingActionButton fab;
    private Bitmap bitmap;
    private SharedPreferences preferences;
    private RequestQueue requestQueue;
    private String id;
    private static final int ONLY_PICTURE = 1;
    private static final int ONLY_TEXT = 2;
    private static final int TEXT_VE_PICTURE = 3;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String url_memories = "http://10.0.2.2/Relica/memories.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_memory);

        counter = findViewById(R.id.counter);
        text = findViewById(R.id.editText);
        picture = findViewById(R.id.imageView2);
        imButton = findViewById(R.id.imageButton);
        fab = findViewById(R.id.floatingActionButtonTweetle);

        preferences = PreferenceManager.getDefaultSharedPreferences(SendMemory.this);
        id = preferences.getString("id", "-1");
        requestQueue = Volley.newRequestQueue(SendMemory.this);

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                int i = 280 - start;
                counter.setText(String.valueOf(i));

                if (i < 0) {
                    counter.setTextColor(Color.RED);
                    fab.hide();
                } else {
                    counter.setTextColor(Color.parseColor("#444444"));
                    //counter.setTextColor(Color.parseColor("#444")); -->hatalı kullanım
                    fab.show();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        imButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Request of choose an image
                sendRequestImage();
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //tweet gönderme isteği yapılacak

                if (text.getText().toString().trim().length() != 0 && bitmap != null) {
                    // hem text hem resim
                    sendRequest(TEXT_VE_PICTURE);
                }

                if (text.getText().toString().trim().length() != 0 && bitmap == null) {
                    // sadece text
                    sendRequest(ONLY_TEXT);
                }

                if (text.getText().toString().trim().length() == 0 && bitmap != null) {
                    // sadece resim
                    sendRequest(ONLY_PICTURE);
                }

                if (text.getText().toString().trim().length() == 0 && bitmap == null) {
                    // ikiside boş
                    Snackbar.make(findViewById(R.id.floatingActionButtonTweetle), "Memory cannot be empty!", Snackbar.LENGTH_LONG).show();
                }


            }
        });
    }

    private void sendRequest(final int requestType) {
        final ProgressDialog loading = ProgressDialog.show(SendMemory.this, "Memory is being posted...", "Please wait..", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_memories, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Json data: ", response);
                loading.dismiss();

                String status = null, message = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (status.equals("200")) {
                    Snackbar.make(findViewById(R.id.floatingActionButtonTweetle), "Memory has been posted successfully!", Snackbar.LENGTH_LONG).show();
                    text.setText("");
                    picture.setImageBitmap(null);

                } else {
                    Snackbar.make(findViewById(R.id.floatingActionButtonTweetle), message, Snackbar.LENGTH_LONG).show();
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
                String uuid = UUID.randomUUID().toString();

                switch (requestType) {

                    case ONLY_PICTURE:
                        //Convert String from Bitmap
                        String image = getStringImage(bitmap);

                        // Add parameters
                        params.put("id", id);
                        params.put("uuid", uuid); // Universal unique identifier consists of 32 chars
                        params.put("picture", image);
                        params.put("request_type", String.valueOf(ONLY_PICTURE));
                        break;

                    case ONLY_TEXT:
                        // Add parameters
                        params.put("id", id);
                        params.put("uuid", uuid);
                        params.put("text", text.getText().toString());
                        params.put("request_type", String.valueOf(ONLY_TEXT));
                        break;

                    case TEXT_VE_PICTURE:
                        //Convert String from Bitmap
                        String image2 = getStringImage(bitmap);

                        // Add parameters
                        params.put("id", id);
                        params.put("uuid", uuid);
                        params.put("picture", image2);
                        params.put("text", text.getText().toString());
                        params.put("request_type", String.valueOf(TEXT_VE_PICTURE));
                        break;
                }
                return params;
            }
        };


        requestQueue.add(stringRequest);


    }


    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void sendRequestImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choose an image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            picture.setImageBitmap(bitmap);
        }
    }

}
