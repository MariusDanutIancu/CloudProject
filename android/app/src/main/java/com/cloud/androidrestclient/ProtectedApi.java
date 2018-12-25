package com.cloud.androidrestclient;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProtectedApi {

    @GET("/api/protected-service/data")
    Call<ProtectedModel> requestData();
}
