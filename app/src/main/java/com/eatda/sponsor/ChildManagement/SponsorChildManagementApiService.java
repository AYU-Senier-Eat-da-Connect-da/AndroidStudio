package com.eatda.sponsor.ChildManagement;

import com.eatda.sponsor.form.ChildResponse;
import com.eatda.sponsor.form.SponsorDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SponsorChildManagementApiService {
    // 후원자에게 모든 아동 리스트를 보여주는 API
    @GET("/api/sponsor/all")
    Call<List<ChildResponse>> getAllChildren();

    // 특정 아동의 상세 정보를 가져오는 API
    @GET("/api/child/{childId}")
    Call<ChildResponse> getChild(@Path("childId") Long childId);

    // 아동이 후원자의 정보를 확인하는 API
    @GET("/api/child/{childId}/sponsor")
    Call<SponsorDTO> getSponsorForChild(@Path("childId") Long childId);

    @GET("/api/sponsor/{sponsorId}/children")
    Call<List<ChildResponse>> getChildren(@Path("sponsorId") Long sponsorId);

    @POST("/api/sponsor/{sponsorId}/add/{childId}")
    Call<SponsorDTO> addChildToSponsor(@Path("sponsorId") Long sponsorId, @Path("childId") Long childId);

    @DELETE("/api/sponsor/{sponsorId}/delete/{childId}")
    Call<SponsorDTO> deleteChildFromSponsor(@Path("sponsorId") Long sponsorId, @Path("childId") Long childId);
}
