package com.eatda.data.api.goodInfluenceStore;

import com.eatda.data.form.goodInfluenceStore.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoodInfluenceStoreApiService {
    String Key = "c1d303fc6d9b40bca412e31828f322f5";

    @GET("GGGOODINFLSTOREST")
    Call<ApiResponse> getGoodInfluenceStores(
            @Query("Key") String Key,
            @Query("Type") String Type,
            @Query("pIndex") int pIndex,
            @Query("pSize") int pSize
    );
}
