package com.eci.cosw.easyaccess.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.eci.cosw.easyaccess.R;
import com.eci.cosw.easyaccess.model.Login;
import com.eci.cosw.easyaccess.model.Token;
import com.eci.cosw.easyaccess.service.AuthService;
import com.eci.cosw.easyaccess.util.RetrofitHttp;
import com.eci.cosw.easyaccess.util.SharedPreference;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static AuthService authService;
    private final ExecutorService executorService =
            Executors.newFixedThreadPool(1);
    private SharedPreference sharedPreference;
    private RetrofitHttp retrofitHttp;
    private String file;
    private String TOKEN_KEY;
    private String USER_LOGGED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        file = getString(R.string.preference_file_key);
        TOKEN_KEY = getString(R.string.token_key);
        USER_LOGGED = getString(R.string.user_logged);

        sharedPreference = new SharedPreference(this, file);
        retrofitHttp = new RetrofitHttp();

        authService = retrofitHttp.getRetrofit().create(AuthService.class);
    }

    public void login(final View view) {
        view.setEnabled(false);

        EditText emailEditText = (EditText) findViewById(R.id.email);
        EditText passEditText = (EditText) findViewById(R.id.password);

        String userEmail = emailEditText.getText().toString();
        String userPass = passEditText.getText().toString();

        if (userEmail.matches("") || userPass.matches("")) {
            showErrorMessage(view, getString(R.string.login_empty));
        } else {
            loginRequest(view, userEmail, userPass);
        }
    }

    public void loginRequest(final View view, final String email, final String password) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Login login = new Login(email, password);
                try {
                    Response<Token> tokenResponse = authService.login(login).execute();

                    if (tokenResponse.isSuccessful()) {
                        Token token = tokenResponse.body();

                        sharedPreference.save(TOKEN_KEY, token.getAccessToken());
                        sharedPreference.save(USER_LOGGED, email);

                        startMainActivity();

                        finish();
                    } else {
                        showErrorMessage(view, getString(R.string.bad_login));
                    }
                } catch (IOException e) {
                    showErrorMessage(view, getString(R.string.net_error));
                }
            }
        });
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

    public void startMainActivity() {
        Intent intent = new Intent(this, MainUserActivity.class);
        startActivity(intent);
    }

    public void register(final View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
