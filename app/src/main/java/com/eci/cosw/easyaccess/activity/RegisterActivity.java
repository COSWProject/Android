package com.eci.cosw.easyaccess.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.eci.cosw.easyaccess.R;
import com.eci.cosw.easyaccess.model.Login;
import com.eci.cosw.easyaccess.service.UserService;
import com.eci.cosw.easyaccess.util.RetrofitHttp;

public class RegisterActivity extends AppCompatActivity {

    private RetrofitHttp retrofitHttp;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        retrofitHttp = new RetrofitHttp();
        userService = retrofitHttp.getRetrofit().create(UserService.class);
    }

    public void register(final View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }


    public void companyRegister(final View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}
