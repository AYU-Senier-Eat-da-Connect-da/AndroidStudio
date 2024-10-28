package com.eatda.data.api.home;

import com.eatda.data.form.restaurant.RestaurantResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface HomeApiService {
    @GET("/api/restaurant/search/{searchText}")
    Call<List<RestaurantResponse>> searchByText(@Path("searchText") String searchText);

    @GET("/api/restaurant/search/category/{restaurantCategory}")
    Call<List<RestaurantResponse>> getRestaurantByCategory(@Path("restaurantCategory") String category);
}
