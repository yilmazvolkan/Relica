package com.boun.volkanyilmaz.relica.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NotificationsFragment extends Fragment{


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        TextView view=new TextView(getContext());
        view.setText("Notifications Fragment");
        view.setTextSize(30);
        view.setGravity(Gravity.CENTER);

        return view;
    }
}
