package com.eatda.data.api.order;

import com.eatda.data.form.order.OrderRequest;
import com.eatda.data.form.order.OrderResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OrderApiService {
    @POST("/api/orders")
    Call<OrderResponse> createOrder(@Body OrderRequest orderRequest);

    @GET("/api/orders/child/{childId}")
    Call<List<OrderResponse>> getOrderByChildId(@Path("childId") Long childId);
}
