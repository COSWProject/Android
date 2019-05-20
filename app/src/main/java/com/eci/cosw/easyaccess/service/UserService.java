package com.eci.cosw.easyaccess.service;

import com.eci.cosw.easyaccess.model.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {

    @POST("token/newUser")
    Call<ResponseBody> createUser(@Body User user);

    @GET("api/user/email/{email}")
    Call<User> getUserByEmail(@Path("email") String email);

    @PUT("api/user/update")
    Call<ResponseBody> updateUser(@Body User user);
}
