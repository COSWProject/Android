package com.eci.cosw.easyaccess.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.eci.cosw.easyaccess.activity.R;
import com.eci.cosw.easyaccess.service.AccessService;
import com.eci.cosw.easyaccess.service.UserService;
import com.eci.cosw.easyaccess.util.RetrofitHttp;
import com.eci.cosw.easyaccess.util.SharedPreference;

public class CreateMeetingActivity extends AppCompatActivity {

    private SharedPreference sharedPreference;
    private RetrofitHttp retrofitHttp;

    private String file;

    private UserService userService;
    private AccessService accessService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        file = getString(R.string.preference_file_key);

        sharedPreference = new SharedPreference(this, file);

        retrofitHttp = new RetrofitHttp(sharedPreference.getValue("TOKEN_KEY"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, MainCompanyActivity.class);
        startActivity(intent);
        return true;
    }

}
