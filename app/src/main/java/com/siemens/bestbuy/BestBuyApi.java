package com.siemens.bestbuy;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BestBuyApi {

    @GET("products")
    Call<JsonData>getProducts(
            @Query("format")String format,
            @Query("apiKey")String apiKey
    );
}
