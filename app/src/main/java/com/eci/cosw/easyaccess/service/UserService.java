package com.eci.cosw.easyaccess.service;

import com.eci.cosw.easyaccess.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {

    @POST("token/newUser")
    Call<User> createUser(@Body User user);
}
