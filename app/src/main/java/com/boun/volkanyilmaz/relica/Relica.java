package com.boun.volkanyilmaz.relica;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.boun.volkanyilmaz.relica.Fragments.HomeFragment;
import com.boun.volkanyilmaz.relica.Fragments.MessagesFragment;
import com.boun.volkanyilmaz.relica.Fragments.NotificationsFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Relica extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private TextView fullname, mail;
    private CircleImageView profileFoto;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String id;
    private int[] tabIcons = {
            R.drawable.ic_home_24dp,
            R.drawable.ic_notifications_24dp,
            R.drawable.ic_forum_24dp
    };
    private SharedPreferences preferences;
    private static final String url_profile_info = "http://10.0.2.2/Relica/profileInfo.php";

    @Override
    protected void onResume() {  // To control navigator menu profile changes
        super.onResume();
        if (preferences.getBoolean("ProfileChanged", false)) {
            setProfileInfo(preferences.getString("id", "-1"));
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("ProfileChanged", false);
            editor.commit();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relica);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);


        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        LinearLayout layout = (LinearLayout) navigationView.getHeaderView(0);
        fullname = layout.findViewById(R.id.fullname);
        mail = layout.findViewById(R.id.mail);
        profileFoto = layout.findViewById(R.id.profile_image);
        preferences = PreferenceManager.getDefaultSharedPreferences(Relica.this);
        id = preferences.getString("id", "-1");
        setProfileInfo(id);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                toolbar.setTitle(tab.getText());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.relica, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profil_menu) {
            startActivity(new Intent(Relica.this, ProfileActivity.class));
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.signOut) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Relica.this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("rememberMe", false);
            editor.putString("id", "-1");
            editor.commit();
            startActivity(new Intent(Relica.this, LoginActivity.class));
            this.finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new HomeFragment(), "Home");
        adapter.addFrag(new NotificationsFragment(), "Notifications");
        adapter.addFrag(new MessagesFragment(), "Messages");
        viewPager.setAdapter(adapter);
    }

    private void setProfileInfo(final String id) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_profile_info, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Json data", response);

                String status = "", message = "", fullname = "", avatar = "", mail = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                    avatar = jsonObject.getString("avatar");
                    fullname = jsonObject.getString("fullname");
                    mail = jsonObject.getString("mail");

                } catch (JSONException e) {
                    Log.e("Json parse error", e.getLocalizedMessage());
                }

                if (status.equals("200")) {
                    setProfile(fullname, mail, avatar);
                } else {
                    Snackbar.make(findViewById(R.id.fab), message, Snackbar.LENGTH_LONG).show();
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
                values.put("id", id);
                return values;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    private void setProfile(String fullname, String mail, String avatar) {
        Log.d("AVATAR", avatar);

        Picasso.Builder builder = new Picasso.Builder(getApplicationContext());
        builder.listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                Log.e("Picasso error.", exception.getLocalizedMessage());
            }
        });
        Picasso pic = builder.build();

        if (!avatar.equals("")){
            pic.load(avatar).into(profileFoto);
        }


        this.fullname.setText(fullname);
        this.mail.setText(mail);

    }




    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            if (position==0){
                Fragment fragment=mFragmentList.get(position);
                Bundle bundle=new Bundle();
                bundle.putString("id",id);
                fragment.setArguments(bundle);
                return fragment;
            }
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
