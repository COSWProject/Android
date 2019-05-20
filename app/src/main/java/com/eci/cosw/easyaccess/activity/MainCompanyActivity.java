package com.eci.cosw.easyaccess.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eci.cosw.easyaccess.R;
import com.eci.cosw.easyaccess.adapter.RVAdapter;
import com.eci.cosw.easyaccess.model.Access;
import com.eci.cosw.easyaccess.model.User;
import com.eci.cosw.easyaccess.service.AccessService;
import com.eci.cosw.easyaccess.service.UserService;
import com.eci.cosw.easyaccess.util.RetrofitHttp;
import com.eci.cosw.easyaccess.util.SharedPreference;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class MainCompanyActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String file;
    private SharedPreference sharedPreference;
    private String USER_LOGGED;
    private String TOKEN_KEY;

    private RVAdapter rvAdapter;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    private RetrofitHttp retrofitHttp;
    private AccessService accessService;
    private UserService userService;

    private final ExecutorService executorService =
            Executors.newFixedThreadPool(1);

    private List<Access> accesses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_company);
        Toolbar toolbar = findViewById(R.id.toolbar);


        recyclerView = (RecyclerView) findViewById(R.id.access);
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createMeeting();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        file = getString(R.string.preference_file_key);
        sharedPreference = new SharedPreference(this, file);

        TOKEN_KEY = getString(R.string.token_key);
        USER_LOGGED = getString(R.string.user_logged);

        retrofitHttp = new RetrofitHttp(sharedPreference.getValue(TOKEN_KEY));

        getAccesses();

        // specify an adapter (see also next example)
        rvAdapter = new RVAdapter(this);
    }

    private void createMeeting() {
        Intent intent = new Intent(this, CreateMeetingActivity.class);
        startActivity(intent);
        finish();
    }

    public void udpateAccesses() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(rvAdapter);
                rvAdapter.updateAccesses(accesses,"Company");
            }
        });
    }

    private void getAccesses() {
        accessService = retrofitHttp.getRetrofit().create(AccessService.class);
        userService = retrofitHttp.getRetrofit().create(UserService.class);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<User> userResponse = userService.getUserByEmail(sharedPreference.getValue(USER_LOGGED)).execute();
                    String name = userResponse.body().getName();
                    Log.d("STATE",name);
                    Response<List<Access>> accessesResponse = accessService.getAccessByCompany("24230").execute();

                    if (accessesResponse.isSuccessful()) {
                        accesses = accessesResponse.body();

                        accesses.add(new Access("Oscar Alba", "Oscar Alba-ECI-1015489564-21/05/2019-8:00",
                                "Oswaldo", "ECI", "8:00 am", "21/05/2019", "55"));
                        accesses.add(new Access("Oscar Alba", "Oscar Alba-ECI-1015489564-21/05/2019-8:00",
                                "Oswaldo", "ECI", "8:00 am", "21/05/2019", "55"));
                        accesses.add(new Access("Oscar Alba", "Oscar Alba-ECI-1015489564-21/05/2019-8:00",
                                "Oswaldo", "ECI", "8:00 am", "21/05/2019", "55"));

                        udpateAccesses();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
        getMenuInflater().inflate(R.menu.main, menu);
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

        if (id == R.id.company_logout) {
            logOut();
        } else if (id == R.id.company_reader) {
            qrReader();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logOut() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void qrReader() {
        Intent intent = new Intent(this, CodeScanner.class);
        startActivity(intent);
    }


}