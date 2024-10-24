package com.eatda.login;

import com.eatda.login.form.ChildJoinRequest;
import com.eatda.login.form.LoginRequest;
import com.eatda.login.form.PresidentJoinRequest;
import com.eatda.login.form.SponsorJoinRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginApiService {

    @POST("/api/jwt-login/join/president")
    Call<String> joinPresident(@Body PresidentJoinRequest presidentJoinRequest);

    @POST("/api/jwt-login/join/sponsor")
    Call<String> joinSponsor(@Body SponsorJoinRequest sponsorJoinRequest);

    @POST("/api/jwt-login/join/child")
    Call<String> joinChild(@Body ChildJoinRequest childJoinRequest);

    @POST("/api/jwt-login/login/president")
    Call<String> loginPresident(@Body LoginRequest loginRequest);

    @POST("/api/jwt-login/login/child")
    Call<String> loginChild(@Body LoginRequest loginRequest);

    @POST("/api/jwt-login/login/sponsor")
    Call<String> loginSponsor(@Body LoginRequest loginRequest);
}
