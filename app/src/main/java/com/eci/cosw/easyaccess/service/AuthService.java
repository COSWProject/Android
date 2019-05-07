package com.eci.cosw.easyaccess.service;

import com.eci.cosw.easyaccess.model.Login;
import com.eci.cosw.easyaccess.model.Token;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {

    @POST("token/login")
    Call<Token> login(@Body Login loginWrapper);
}
