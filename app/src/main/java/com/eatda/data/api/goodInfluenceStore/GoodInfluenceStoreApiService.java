package com.eatda.data.api.goodInfluenceStore;

import com.eatda.data.form.goodInfluenceStore.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoodInfluenceStoreApiService {
    String Key = "선한영향력가게Key";

    @GET("GGGOODINFLSTOREST")
    Call<ApiResponse> getGoodInfluenceStores(
            @Query("Key") String Key,
            @Query("Type") String Type,
            @Query("pIndex") int pIndex,
            @Query("pSize") int pSize
    );
}
