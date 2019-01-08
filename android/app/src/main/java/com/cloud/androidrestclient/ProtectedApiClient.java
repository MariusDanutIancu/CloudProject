package com.cloud.androidrestclient;

import android.util.Log;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProtectedApiClient {

    private static String connection = "http://192.168.2.104:8002";
    private static String connection_2 = "http://192.168.17.47:8002";

    // Logging
    private static String RETROFITTAG="RETROFITTAG";
    private static String retrofitClientLoggingMessageTemplate = "Building client for: %s";

    static Retrofit getClient() {

        Retrofit retrofit;

        Log.v(RETROFITTAG, String.format(retrofitClientLoggingMessageTemplate, connection));
        retrofit =  buildClient(connection);

        if (retrofit == null)
        {
            Log.v(RETROFITTAG, String.format(retrofitClientLoggingMessageTemplate, connection));
            retrofit =  buildClient(connection_2);
        }

        return retrofit;
    }

    static private Retrofit buildClient(String connection)
    {
        Retrofit retrofit;
        OkHttpClient client = new OkHttpClient.Builder().build();
        retrofit = new Retrofit.Builder()
                .baseUrl(connection)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }
}
