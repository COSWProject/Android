package com.eci.cosw.easyaccess.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.eci.cosw.easyaccess.R;
import com.eci.cosw.easyaccess.model.User;
import com.eci.cosw.easyaccess.service.UserService;
import com.eci.cosw.easyaccess.util.RetrofitHttp;
import com.eci.cosw.easyaccess.util.SharedPreference;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity {

    private RetrofitHttp retrofitHttp;
    private SharedPreference sharedPreference;

    private String file;
    private String userLogged;

    private User user;

    private UserService userService;

    private final ExecutorService executorService =
            Executors.newFixedThreadPool(1);

    private TextView profileName;
    private TextView profileEmail;
    private TextView profileMobile;
    private TextView profileCity;
    private TextView profileRol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        file = getString(R.string.preference_file_key);
        sharedPreference = new SharedPreference(this, file);

        userLogged = sharedPreference.getValue("USER_LOGGED");

        retrofitHttp = new RetrofitHttp(sharedPreference.getValue("TOKEN_KEY"));

        userService = retrofitHttp.getRetrofit().create(UserService.class);

        profileName = (TextView) findViewById(R.id.profileName);
        profileEmail = (TextView) findViewById(R.id.profileEmail);
        profileMobile = (TextView) findViewById(R.id.profileMobile);
        profileCity = (TextView) findViewById(R.id.profileCity);
        profileRol = (TextView) findViewById(R.id.profileRol);

        obtainUser();
    }

    private void obtainUser() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<User> userResponse = userService.getUserByEmail(userLogged).execute();

                    if (userResponse.isSuccessful()) {
                        user = userResponse.body();
                        updateUserInfo(findViewById(R.id.drawer_layout), user);
                    }
                    System.out.println(user);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateUserInfo(final View view, final User user) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                profileName.setText("Name: " + user.getName());
                profileEmail.setText("Email: " + user.getEmail());
                profileMobile.setText("Phone: " + String.valueOf(user.getMobilePhone()));
                profileCity.setText("City: " + user.getCity());
                profileRol.setText("Rol: " + user.getRol());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, MainUserActivity.class);
        startActivity(intent);
        return true;
    }
}
