package com.eatda.data.api.goodInfluenceStore;

import com.eatda.Local;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoodInfluenceStoreRetrofitClient {
    private static final String BASE_URL = "https://openapi.gg.go.kr/";
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
