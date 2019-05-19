package com.eci.cosw.easyaccess.util;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHttp {
    private Retrofit retrofit;
    private String token;
    private final String BASE_URL = "http://easyaccessbackend.herokuapp.com/";

    public RetrofitHttp() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public RetrofitHttp(final String token) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain)
                    throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder().header("Accept",
                        "application/json").header("Authorization", "Bearer " +
                        token).method(original.method(), original.body()).build();

                return chain.proceed(request);
            }
        });

        this.retrofit =
                new Retrofit.Builder().baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(httpClient.build()).build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
