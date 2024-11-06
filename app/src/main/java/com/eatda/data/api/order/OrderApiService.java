package com.eatda.data.api.order;

import com.eatda.data.form.order.OrderRequest;
import com.eatda.data.form.order.OrderResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OrderApiService {
    @POST("/api/orders")
    Call<OrderResponse> createOrder(@Body OrderRequest orderRequest);
}
