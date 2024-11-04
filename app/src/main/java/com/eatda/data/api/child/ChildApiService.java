package com.eatda.data.api.child;

import com.eatda.data.form.childManagement.ChildResponse;
import com.eatda.data.form.sponsor.SponsorDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ChildApiService {
    @GET("/api/child/get/{childId}")
    Call<ChildResponse> getChildInfo(@Path("childId") Long childId);

    @GET("/api/child/update-amount/{childId}")
    Call<String> addPoint(@Path("childId") Long childId, @Query("amount") int amount);
}
