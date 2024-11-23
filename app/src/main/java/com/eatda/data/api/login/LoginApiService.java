package com.eatda.data.api.login;

import com.eatda.data.form.login.ChildJoinRequest;
import com.eatda.data.form.login.LoginRequest;
import com.eatda.data.form.login.PresidentJoinRequest;
import com.eatda.data.form.login.SponsorJoinRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface LoginApiService {

    @POST("/api/jwt-login/join/president")
    Call<String> joinPresident(@Body PresidentJoinRequest presidentJoinRequest);

    @POST("/api/jwt-login/join/sponsor")
    Call<String> joinSponsor(@Body SponsorJoinRequest sponsorJoinRequest);

    @POST("/api/jwt-login/join/child")
    Call<String> joinChild(@Body ChildJoinRequest childJoinRequest);

    @POST("/api/jwt-login/login")
    Call<String> loginAll(@Body LoginRequest loginRequest);

    @POST("/api/jwt-logout/")
    Call<Void> logout(@Header("Authorization") String token);
}
