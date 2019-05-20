package com.eci.cosw.easyaccess.service;

import com.eci.cosw.easyaccess.model.Access;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AccessService {
    @GET("/api/access/meetingsOwner/{owner}")
    Call<List<Access>> getAccessByOwner(@Path("owner") String id);

    @GET("/api/access/meetingsInvitedBy/{company}")
    Call<List<Access>> getAccessByCompany(@Path("company") String company);

    @GET("/api/access/all")
    Call<List<Access>> getAccesses();

    @POST("/api/access")
    Call<ResponseBody> createAccess(@Body Access access);

}
