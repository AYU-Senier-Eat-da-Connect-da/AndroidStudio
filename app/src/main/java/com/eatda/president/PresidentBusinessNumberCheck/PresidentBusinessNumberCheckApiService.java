package com.eatda.president.PresidentBusinessNumberCheck;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface PresidentBusinessNumberCheckApiService {
    @Headers({"Content-Type: application/json"})
    @POST("사업자번호")
    Call<PresidentBusinessResponse> checkBusinessNumber(@Body PresidentBusinessRequest body);
}
