package com.boun.volkanyilmaz.relica;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
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

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout tilFullname, tilMail, tilPass, tilUsername;
    private TextInputEditText fullname, mail, password, username;
    private SharedPreferences preferences;
    private RequestQueue requestQueue;
    private static final String url_register = "http://10.0.2.2/Relica/register.php"; // Local
    private boolean requestSent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        androidTools();
        animations();
        if (!connectionControl()) {
            Snackbar.make(findViewById(R.id.rootRegister), "Check your connection!", Snackbar.LENGTH_LONG).show();
        }


        findViewById(R.id.alreadyAccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                NavUtils.navigateUpTo(RegisterActivity.this, intent);
            }
        });

        findViewById(R.id.alreadyAccount).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    ((TextView) v).setTextColor(Color.parseColor("#DD999999"));

                if (event.getAction() == MotionEvent.ACTION_UP)
                    ((TextView) v).setTextColor(Color.WHITE);

                return false;
            }
        });

        findViewById(R.id.fabRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isFullname = TextUtils.isEmpty(fullname.getText());
                boolean isMail = TextUtils.isEmpty(mail.getText());
                boolean isPassword = TextUtils.isEmpty(password.getText());
                boolean isUsername = TextUtils.isEmpty(username.getText());

                tilUsername.setError(null);
                tilPass.setError(null);
                tilFullname.setError(null);
                tilMail.setError(null);
                if (isFullname || isMail || isPassword || isUsername || !mail.getText().toString().contains("@")) {

                    if (isFullname)
                        tilFullname.setError("Please write your fullname.");
                    if (isPassword)
                        tilPass.setError("Please write your password.");
                    if (isUsername)
                        tilUsername.setError("Please write your username.");

                    if (isMail)
                        tilMail.setError("Please write your mail.");
                    else if (!mail.getText().toString().contains("@"))
                        tilMail.setError("Mail adress is not valid!");

                } else {
                    if (!connectionControl()) {
                        Snackbar.make(findViewById(R.id.rootRegister), "Check your connection!", Snackbar.LENGTH_LONG).show();
                    } else {
                        if (!requestSent) {
                            requestSent = true;
                            sendRequest();
                        }
                    }

                }
            }
        });
    }

    public void androidTools() {
        tilFullname = findViewById(R.id.tilfullnameReg);
        tilMail = findViewById(R.id.tilmailReg);
        tilPass = findViewById(R.id.tilpassReg);
        tilUsername = findViewById(R.id.tilusernameReg);
        fullname = findViewById(R.id.usernameReg);
        mail = findViewById(R.id.mailReg);
        password = findViewById(R.id.passReg);
        username = findViewById(R.id.usernameReg);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

    }

    // Create animated image
    public void animations() {
        setContentView(R.layout.activity_register);
        ImageView image = findViewById(R.id.imageRegister);
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

    private void sendRequest() {
        StringRequest request = new StringRequest(Request.Method.POST, url_register, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Json data", response); // Check json data in log

                try {
                    JSONObject json = new JSONObject(response);
                    //String id = json.getString("id");
                    String status = json.getString("status");

                    if (status.equals("404")) {
                        Snackbar.make(findViewById(R.id.rootRegister), "Connection failed.", Snackbar.LENGTH_LONG).show();
                        requestSent = false;
                    } else if (status.equals("400")) {
                        Snackbar.make(findViewById(R.id.rootRegister), "Registration unsuccessful. Username is already exist.", Snackbar.LENGTH_LONG).show();
                        requestSent = false;
                    } else if (status.equals("200")) {
                        //preferences.edit().putString("id", id).commit(); we need to verification of email
                        new AlertDialog.Builder(RegisterActivity.this)
                                .setMessage("Registration successful! Check your mailbox.")
                                .setPositiveButton("Success", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                }).show();


                    }

                } catch (JSONException e) {
                    Log.e("Parse error.", e.getLocalizedMessage());
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
                values.put("fullname", fullname.getText().toString());
                values.put("mail", mail.getText().toString());
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
