package com.eatda.data.api.sponsor;

import com.eatda.data.form.president.PresidentDTO;
import com.eatda.data.form.sponsor.SponsorDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SponsorApiService {

    @GET("/api/president/get/{sponsorId}")
    Call<SponsorDTO> getSponsorInfo(@Path("sponsorId") Long sponsorId);
}
