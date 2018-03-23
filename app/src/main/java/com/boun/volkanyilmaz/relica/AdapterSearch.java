package com.boun.volkanyilmaz.relica;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by volkanyilmaz on 23/03/18.
 */

public class AdapterSearch extends BaseAdapter {

    private List<MemoryModel> modelList;
    private Context context;

    public AdapterSearch(List<MemoryModel> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (modelList.size() == 0)
            return 0;

        return modelList.size();
    }

    @Override
    public Object getItem(int position) {
        if (modelList.size() == 0)
            return null;
        return modelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (modelList.size() == 0)
            return null;

        LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.user_list_item, parent, false);
        final TextView fullname = layout.findViewById(R.id.searchFullname);
        final TextView username = layout.findViewById(R.id.searchUsername);
        TextView mail = layout.findViewById(R.id.searchMail);
        final CircleImageView profileImage = layout.findViewById(R.id.profile_image_search);

        final MemoryModel user = modelList.get(position);

        fullname.setText(user.getFullname());
        username.setText(user.getUsername());
        mail.setText(user.getMail());

        if (!user.getProfilePath().equals("")) {
            Picasso.with(context).load(user.getProfilePath()).into(profileImage);

        }

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, UserMemoryActivity.class);
                intent.putExtra("id", user.getId());
                intent.putExtra("path", user.getProfilePath());
                intent.putExtra("username", user.getUsername());
                intent.putExtra("fullname", user.getFullname());
                intent.putExtra("mail", user.getMail());

                // Check android version for animation
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    View profile = profileImage;
                    View fullName = fullname;
                    View userName = username;
                    Pair<View, String> pairProfileFoto = Pair.create(profile, "profileImage");
                    Pair<View, String> pairFullname = Pair.create(fullName, "fullname");
                    Pair<View, String> pairUsername = Pair.create(userName, "username");

                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((AppCompatActivity) context, pairProfileFoto, pairFullname, pairUsername);
                    context.startActivity(intent, optionsCompat.toBundle());

                } else {
                    context.startActivity(intent);
                }

            }
        });

        return layout;
    }
}