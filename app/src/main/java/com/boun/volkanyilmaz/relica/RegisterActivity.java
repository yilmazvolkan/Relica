package com.boun.volkanyilmaz.relica;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout tilFullname, tilMail, tilPass, tilUsername;
    private TextInputEditText fullname, mail, password, username;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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

                if (event.getAction()==MotionEvent.ACTION_DOWN)
                    ((TextView)v).setTextColor(Color.parseColor("#DD999999"));

                if (event.getAction()==MotionEvent.ACTION_UP)
                    ((TextView)v).setTextColor(Color.WHITE);

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
                        tilFullname.setError("Lütfen ad ve soyadınızı giriniz");
                    if (isPassword)
                        tilPass.setError("Lütfen şifrenizi giriniz");
                    if (isUsername)
                        tilUsername.setError("Lütfen kullanıcı adınızı giriniz");

                    if (isMail)
                        tilMail.setError("Lütfen mail adresinizi giriniz");
                    else if (!mail.getText().toString().contains("@"))
                        tilMail.setError("Lütfen geçerli bir mail adresi giriniz");

                } else {
                    // TODO send request to register
                    if (!connectionControl()){
                        Snackbar.make(findViewById(R.id.rootRegister), "Check your connection!", Snackbar.LENGTH_LONG).show();
                    }else{
                        sendRequest();
                    }

                }
            }
        });
    }

    public void androidTools(){
        tilFullname =  findViewById(R.id.tilfullnameReg);
        tilMail =  findViewById(R.id.tilmailReg);
        tilPass =  findViewById(R.id.tilpassReg);
        tilUsername =  findViewById(R.id.tilusernameReg);
        fullname =  findViewById(R.id.usernameReg);
        mail =  findViewById(R.id.mailReg);
        password =  findViewById(R.id.passReg);
        username =  findViewById(R.id.usernameReg);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

    }
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

    }
    boolean connectionControl() {
        return false;
    }
}
