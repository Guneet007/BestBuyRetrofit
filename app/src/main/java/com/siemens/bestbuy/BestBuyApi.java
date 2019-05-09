package com.siemens.bestbuy;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BestBuyApi {

    @GET("products?format=json&apiKey=YQdX4YGQXWpMxkGpZKvl3fXR")
    Call<JsonData>getProducts();
}
