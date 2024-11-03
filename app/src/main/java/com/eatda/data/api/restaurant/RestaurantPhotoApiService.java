package com.eatda.data.api.Restaurant;

import com.eatda.data.form.restaurant.RestaurantPhotoResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface RestaurantPhotoApiService {
    /*
    @GET("/api/restaurant/{restaurantId}/photo")
    Call<RestaurantPhotoResponse> getRestaurantPhoto(@Path("restaurantId") Long restaurantId);

    @Multipart
    @POST("restaurants/{restaurantId}/photo")
    Call<String> uploadOrUpdateRestaurantPhoto(
            @Path("restaurantId") Long restaurantId,
            @Part MultipartBody.Part photo
    );

     */
}
