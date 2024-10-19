package com.eatda.sponsor.ChildManagement;

import okhttp3.OkHttpClient;
import java.util.concurrent.TimeUnit;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SponsorChildManagementRetrofitClient {

    //private static final String BASE_URL = "http://10.0.2.2:8080"; // 서버 URL을 여기에 설정
    private static final String BASE_URL = "http://myip:8080";
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            // OkHttpClient 설정
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)  // 연결 실패 시 재시도
                    .connectTimeout(3, TimeUnit.SECONDS)  // 연결 타임아웃 30초
                    .readTimeout(3, TimeUnit.SECONDS)     // 읽기 타임아웃 30초
                    .build();

            // Retrofit 인스턴스 생성
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)  // OkHttpClient 설정 적용
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}