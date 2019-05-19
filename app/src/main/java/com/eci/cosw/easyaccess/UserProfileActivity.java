package com.eci.cosw.easyaccess;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.eci.cosw.easyaccess.activity.MainUserActivity;
import com.eci.cosw.easyaccess.util.RetrofitHttp;
import com.eci.cosw.easyaccess.util.SharedPreference;

public class UserProfileActivity extends AppCompatActivity {

    private RetrofitHttp retrofitHttp;
    private SharedPreference sharedPreference;

    private String file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        file = getString(R.string.preference_file_key);
        sharedPreference = new SharedPreference(this, file);

        retrofitHttp = new RetrofitHttp(sharedPreference.getValue("TOKEN_KEY"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, MainUserActivity.class);
        startActivity(intent);
        return true;
    }
}
