package com.boun.volkanyilmaz.relica;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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

public class ForgetPasswordActivity extends AppCompatActivity {

    private TextInputLayout textInputLayout;
    private TextInputEditText email;
    private static final String url = "http://10.0.2.2/Relica/resetPassword.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        textInputLayout = findViewById(R.id.textInputLayout);
        email = findViewById(R.id.emailforget);

        findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textInputLayout.setError(null);

                if (email.getText().toString().trim().length() == 0) {
                    textInputLayout.setError("Please write your mail.");
                } else if (!email.getText().toString().contains("@")) {
                    textInputLayout.setError("Mail address is not valid!");
                } else {
                    sendRequest(email.getText().toString());
                }

            }
        });
    }

    private void sendRequest(final String mail) {

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("Json data: ", response);

                JSONObject jsonObject = null;
                String status = null;
                String message = null;
                try {
                    jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");

                } catch (JSONException e) {
                    Log.e("Json parse error", e.getLocalizedMessage());
                }

                if (status.equals("200")) {
                    new AlertDialog.Builder(ForgetPasswordActivity.this)
                            .setMessage("We sent you an e-mail to reset your password. Please check your e-mail.")
                            .show();

                } else {
                    Snackbar.make(findViewById(R.id.rootForgetPassword), message, Snackbar.LENGTH_LONG).show();
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
                values.put("mail", mail);
                return values;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ForgetPasswordActivity.this);
        requestQueue.add(request);

    }
}
