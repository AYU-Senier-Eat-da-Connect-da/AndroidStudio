package com.eatda.president.PresidentBusinessNumberCheck;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface PresidentBusinessNumberCheckApiService {
    @Headers({"Content-Type: application/json"})
    @POST("status?serviceKey=5oo4ebgn0jBbxYrhR%2Fd9vcXsS%2F0W4vK%2BYUrB%2FQPrAbwXFzZm29JKcmsM4JuKoZ6rhwvstYmmXLAot7PGxaloDw%3D%3D")
    Call<PresidentBusinessResponse> checkBusinessNumber(@Body PresidentBusinessRequest body);
}
