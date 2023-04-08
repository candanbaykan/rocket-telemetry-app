package com.candanbaykan.restclient;

import com.candanbaykan.model.Rocket;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

import java.util.List;

public interface RocketRestClient {
    @Headers("X-API-KEY: API_KEY_1")
    @GET("rockets")
    Call<List<Rocket>> getAll();
}
