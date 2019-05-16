package com.eci.cosw.easyaccess.network.access;

public class NetworkAccessImpl implements NetworkAccess {

    private static final String BASE_URL = "http://127.0.0.1:8080/";
    private NetworkAccess nse;

    public NetworkAccessImpl(){

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        nse = retrofit.create(NetworkServiceEvent.class);
    }

    @Override
    public void createAccess() {



    }

    @Override
    public void getAccessById() {


    }
}
