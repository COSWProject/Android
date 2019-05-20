package com.eci.cosw.easyaccess.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.eci.cosw.easyaccess.R;
import com.eci.cosw.easyaccess.model.User;
import com.eci.cosw.easyaccess.service.UserService;
import com.eci.cosw.easyaccess.util.RetrofitHttp;
import com.eci.cosw.easyaccess.util.SharedPreference;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class UpdateUserProfileActivity extends AppCompatActivity {

    private EditText passwordText;
    private EditText emailText;
    private EditText phoneText;
    private EditText cityText;

    private String passwordStr;
    private String emailStr;
    private String phoneStr;
    private String cityStr;

    private String file;

    private SharedPreference sharedPreference;
    private RetrofitHttp retrofitHttp;

    private UserService userService;

    private User user;

    private final ExecutorService executorService =
            Executors.newFixedThreadPool(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_profile);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        file = getString(R.string.preference_file_key);

        sharedPreference = new SharedPreference(this, file);

        retrofitHttp = new RetrofitHttp(sharedPreference.getValue("TOKEN_KEY"));

        userService = retrofitHttp.getRetrofit().create(UserService.class);

        obtainUser();
    }

    private void obtainUser() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<User> userResponse = userService.getUserByEmail(sharedPreference.
                            getValue("USER_LOGGED")).execute();

                    if (userResponse.isSuccessful()) {
                        user = userResponse.body();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void updateProfile(final View view) {
        view.setEnabled(false);

        passwordText = (EditText) findViewById(R.id.udpateUserPassword);
        emailText = (EditText) findViewById(R.id.updateUserEmail);
        phoneText = (EditText) findViewById(R.id.udpateUserMobile);
        cityText = (EditText) findViewById(R.id.udpateUserCity);

        passwordStr = passwordText.getText().toString();
        emailStr = emailText.getText().toString();
        phoneStr = phoneText.getText().toString();
        cityStr = cityText.getText().toString();

        updatePassword();
        updateEmail();
        updatePhone();
        updateCity();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<ResponseBody> response = userService.updateUser(user).execute();

                    if (response.isSuccessful()) {
                        startProfileActivity();
                    } else {
                        showErrorMessage(view, "Error updating the user");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void startProfileActivity() {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
        finish();
    }

    private void updatePassword() {
        if (!passwordStr.matches("")) {
            user.setPassword(passwordStr);
        }
    }

    private void updateEmail() {
        if (!emailStr.matches("")) {
            user.setEmail(emailStr);
        }
    }

    private void updatePhone() {
        if (phoneStr.matches("")) {
            user.setMobilePhone(Integer.parseInt(phoneStr));
        }
    }

    private void updateCity() {
        if (cityStr.matches("")) {
            user.setCity(cityStr);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

    public void showErrorMessage(final View view, final String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
                Snackbar.make(view, error,
                        Snackbar.LENGTH_LONG)
                        .show();

            }
        });
    }
}
