package com.cloud.androidrestclient;

import android.util.Log;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProtectedApiClient {

    private static Retrofit retrofit = null;
    private static String RETROFITTAG="RETROFITTAG";

    static Retrofit getClient() {

        OkHttpClient client = new OkHttpClient.Builder().build();

        Log.v(RETROFITTAG, "BUILDING client on http://192.168.2.104:8002" );
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.2.104:8002")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        if (retrofit == null)
        {
            Log.v(RETROFITTAG, "Client is null");
        }
        else {
            Log.v(RETROFITTAG, "Client BUILT");
        }

        return retrofit;
    }

}
