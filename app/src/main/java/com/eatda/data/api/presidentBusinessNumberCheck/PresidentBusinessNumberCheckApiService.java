package com.eatda.data.api.presidentBusinessNumberCheck;

import com.eatda.data.form.presidentBusinessNumberCheck.PresidentBusinessRequest;
import com.eatda.data.form.presidentBusinessNumberCheck.PresidentBusinessResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface PresidentBusinessNumberCheckApiService {
    @Headers({"Content-Type: application/json"})
    @POST("status?serviceKey=서비스키")
    Call<PresidentBusinessResponse> checkBusinessNumber(@Body PresidentBusinessRequest body);
}
