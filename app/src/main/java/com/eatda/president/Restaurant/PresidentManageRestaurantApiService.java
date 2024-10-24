package com.eatda.president.Restaurant;

import com.eatda.president.Restaurant.form.RestaurantRequest;
import com.eatda.president.Restaurant.form.RestaurantResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PresidentManageRestaurantApiService {
    @GET("/api/president/{presidentId}")
    Call<List<RestaurantResponse>> getRestaurantByPresidentId(@Path("presidentId") Long presidentId);

    @POST("/api/restaurant/create")
    Call<RestaurantRequest> addRestaurant(@Body RestaurantRequest request);
}
