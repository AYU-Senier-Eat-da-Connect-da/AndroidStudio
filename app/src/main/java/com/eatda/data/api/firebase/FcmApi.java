package com.eatda.data.api.firebase;

import com.eatda.data.form.firbase.FCMNotificationRequestDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FcmApi {
    @POST("/api/v1/notification")
    Call<String> sendNotification(
            @Query("userId") Long userId,
            @Query("userType") String userType,
            @Body FCMNotificationRequestDTO requestDto
    );
}
