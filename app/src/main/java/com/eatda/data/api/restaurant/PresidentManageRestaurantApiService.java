package com.eatda.data.api.restaurant;

import com.eatda.data.form.restaurant.RestaurantDetailResponse;
import com.eatda.data.form.restaurant.RestaurantRequest;
import com.eatda.data.form.restaurant.RestaurantResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PresidentManageRestaurantApiService {

    @GET("/api/restaurant/{restaurantId}")
    Call<RestaurantDetailResponse> getRestaurantDetail(@Path("restaurantId") Long restaurantId);

    @GET("/api/president/findMyRestaurant/{presidentId}")
    Call<List<RestaurantResponse>> getRestaurantByPresidentId(@Path("presidentId") Long presidentId);

    @POST("/api/restaurant/create")
    Call<RestaurantRequest> addRestaurant(@Body RestaurantRequest request);

    @PUT("/api/restaurant/{restaurantId}/update")
    Call<RestaurantRequest> updateRestaurant(@Path("restaurantId") Long restaurantId, @Body RestaurantRequest request);

    @DELETE("/api/restaurant/{restaurantId}")
    Call<String> deleteRestaurant(@Path("restaurantId") Long restaurantId);
}
