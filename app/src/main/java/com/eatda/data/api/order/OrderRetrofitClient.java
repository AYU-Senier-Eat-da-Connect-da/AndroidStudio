package com.eatda.data.api.order;

import android.content.Context;
import android.content.SharedPreferences;

import com.eatda.Local;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class OrderRetrofitClient {
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance(Context context) {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, jsonContext) ->
                        LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_DATE_TIME))
                .create();
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            // SharedPreferences에서 JWT 토큰 가져오기
                            SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                            String token = sharedPreferences.getString("jwt_token", null);

                            // 요청에 Authorization 헤더 추가
                            Request request = chain.request();
                            if (token != null) {
                                Request.Builder builder = request.newBuilder()
                                        .addHeader("Authorization", "Bearer " + token);
                                request = builder.build();
                            }

                            return chain.proceed(request);
                        }
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(Local.ip)  // API의 base URL
                    .client(client)  // OkHttpClient 사용
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
