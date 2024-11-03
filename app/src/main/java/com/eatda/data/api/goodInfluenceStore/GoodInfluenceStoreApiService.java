package com.eatda.data.api.goodInfluenceStore;

import com.eatda.data.form.goodInfluenceStore.GoodInfluenceStore;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GoodInfluenceStoreApiService {
    // 위도 경도 정보 가져오기
    @GET("/api/goodInfluenceStore")
    Call<List<GoodInfluenceStore>> callStoreApi();
}
