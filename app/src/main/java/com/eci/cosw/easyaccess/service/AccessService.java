package com.eci.cosw.easyaccess.service;

import com.eci.cosw.easyaccess.model.Access;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AccessService {
    @GET("/api/access/meetingOwner/{owner}")
    Call<Access> getAccessByOwner(@Path("owner") String id);

    @GET("/api/access/meetingCompany/{company}")
    Call<Access> getAccessByCompany(@Path("company") String company);

    @GET("/api/access/all")
    Call<List<Access>> getAccesses();
}
