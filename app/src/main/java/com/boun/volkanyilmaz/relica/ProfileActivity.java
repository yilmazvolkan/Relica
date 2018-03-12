package com.boun.volkanyilmaz.relica;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;

public class ProfileActivity extends AppCompatActivity {
    private String id;
    private static final int PICK_IMAGE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        id = preferences.getString("id", "-1");


        findViewById(R.id.imageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu;
                popupMenu = new PopupMenu(ProfileActivity.this, findViewById(R.id.imageView));
                popupMenu.getMenu().add("Camera");
                popupMenu.getMenu().add("Gallery");
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getTitle() == "Gallery") {
                            resimSecmeIstegi();
                        }

                        if (item.getTitle() == "Camera") {

                        }
                        return true;
                    }
                });
            }
        });
    }

    private void resimSecmeIstegi() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choose an image."), PICK_IMAGE_REQUEST);
    }
}
