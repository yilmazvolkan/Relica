package com.boun.volkanyilmaz.relica;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.CheckBox;
import android.widget.ImageView;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout tilPass, tilUsername;
    private TextInputEditText password, username;
    private CheckBox rememberMe;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        androidTools();
        animations();
        //If remember me is active pass to new activity
        if (preferences.getBoolean("rememberMe", false)) {
            //startActivity(new Intent(LoginActivity.this, ??));
            LoginActivity.this.finish();
        }
        if (!connectionControl()) {
            Snackbar.make(findViewById(R.id.rootLogin), "Check your connection!", Snackbar.LENGTH_LONG).show();
        }
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
                        findViewById(R.id.fabLogin).setEnabled(false);
                        sendRequest();
                    }

                    break;
                }
        }
    }
    private void sendRequest() {

    }
    boolean connectionControl() {
        return false;
    }
}
