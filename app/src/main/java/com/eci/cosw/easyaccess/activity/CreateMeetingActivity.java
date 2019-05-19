package com.eci.cosw.easyaccess.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.eci.cosw.easyaccess.R;
import com.eci.cosw.easyaccess.service.AccessService;
import com.eci.cosw.easyaccess.service.UserService;
import com.eci.cosw.easyaccess.util.DatePickerFragment;
import com.eci.cosw.easyaccess.util.RetrofitHttp;
import com.eci.cosw.easyaccess.util.SharedPreference;
import com.eci.cosw.easyaccess.util.TimePickerFragment;

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

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
}
