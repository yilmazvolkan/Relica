package com.boun.volkanyilmaz.relica;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.CheckBox;
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

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout tilPass, tilUsername;
    private TextInputEditText password, username;
    private CheckBox rememberMe;
    private RequestQueue requestQueue;
    private static final String url_login = "http://10.0.2.2/Relica/login.php";
    private SharedPreferences preferences;
    private boolean requestSent = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        androidTools();
        animations();
        //If remember me is active, pass to new activity
        if (preferences.getBoolean("rememberMe", false)) {
            startActivity(new Intent(LoginActivity.this, Relica.class));
            LoginActivity.this.finish();
        }
        if (!connectionControl()) {
            Snackbar.make(findViewById(R.id.rootLogin), "Check your connection!", Snackbar.LENGTH_LONG).show();
        }
        findViewById(R.id.textView).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    ((TextView) v).setTextColor(Color.parseColor("#DD999999"));

                if (event.getAction() == MotionEvent.ACTION_UP)
                    ((TextView) v).setTextColor(Color.WHITE);

                return true;
            }
        });
    }
    public void androidTools(){
        tilPass =  findViewById(R.id.tilpass);
        tilUsername = findViewById(R.id.tiluserName);
        password =  findViewById(R.id.pass);
        username =  findViewById(R.id.userName);
        rememberMe = findViewById(R.id.rememberMe);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }
    public void animations() {
        setContentView(R.layout.activity_login);
        ImageView image = findViewById(R.id.imageLogin);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        WindowManager wm = window.getWindowManager();
        Display screen = wm.getDefaultDisplay();
        Point point = new Point();
        screen.getSize(point);
        int genis = point.x;
        int yuksek = point.y;

        image.getLayoutParams().width = (int) (yuksek * 2.752);
        image.getLayoutParams().height = yuksek;

        ObjectAnimator animator = ObjectAnimator.ofFloat(image, "x", 0, -(yuksek * 2.752f - genis), 0, -(yuksek * 2.752f - genis));
        animator.setDuration(210000);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;

            case R.id.fabLogin:

                boolean isPass = TextUtils.isEmpty(password.getText());
                boolean isUsername = TextUtils.isEmpty(username.getText());

                tilUsername.setError(null);
                tilPass.setError(null);
                if (isPass || isUsername) {

                    if (isPass)
                        tilPass.setError("Please, write your password.");
                    if (isUsername)
                        tilUsername.setError("Please, write your username.");


                } else {
                    if (!connectionControl()) {
                        Snackbar.make(findViewById(R.id.rootLogin), "Check your connection!", Snackbar.LENGTH_LONG).show();
                    } else {
                        if (!requestSent) {
                            requestSent = true;
                            sendRequest();
                        }

                    }

                    break;
                }
        }
    }
    private void sendRequest() {
        StringRequest request = new StringRequest(Request.Method.POST, url_login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Json data", response); // Check json data in log
                requestSent = false;

                try {
                    JSONObject json = new JSONObject(response);
                    String status = json.getString("status");
                    String message = json.getString("mesaj");
                    if (status.equals("200")) {

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("id", json.getString("id"));
                        editor.putBoolean("rememberMe", rememberMe.isChecked());
                        editor.commit();
                        //ana ekrana geçiş
                        startActivity(new Intent(LoginActivity.this, Relica.class));
                        LoginActivity.this.finish();

                    } else {
                        Snackbar.make(findViewById(R.id.rootLogin), message, Snackbar.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> values = new HashMap<>();
                values.put("username", username.getText().toString());
                values.put("password", password.getText().toString());
                return values;
            }
        };

        requestQueue.add(request);

    }
    boolean connectionControl() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager.getActiveNetworkInfo().isAvailable() &&
                connectivityManager.getActiveNetworkInfo().isConnected() &&
                connectivityManager.getActiveNetworkInfo() != null)
        {
            return true;
        }
        else {
            return false;
        }
    }
}
