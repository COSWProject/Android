package com.eci.cosw.easyaccess.network.service;

import com.eci.cosw.easyaccess.model.Login;
import com.eci.cosw.easyaccess.model.Token;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {

    @POST("token/loginUser")
    Call<Token> loginUser(@Body Login loginWrapper);

    @POST("token/login")
    Call<Token> loginCompany(@Body Login login);
}
