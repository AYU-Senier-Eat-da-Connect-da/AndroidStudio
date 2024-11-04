package com.eatda.data.api.president;

import com.eatda.data.form.president.PresidentDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PresidentApiService {

    @GET("/api/president/get/{presidentId}")
    Call<PresidentDTO> getPresidentInfo(@Path("presidentId") Long presidentId);
}
