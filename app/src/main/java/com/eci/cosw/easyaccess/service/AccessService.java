package com.eci.cosw.easyaccess.service;

import com.eci.cosw.easyaccess.model.Access;
import com.eci.cosw.easyaccess.model.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AccessService {

    @POST("/api/access")
    Call<ResponseBody> createAccess(@Body Access access);

}
